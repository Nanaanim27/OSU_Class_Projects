#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <pthread.h>

#define NUMTHREADS 4;

int	NowYear;		// 2014 - 2019
int	NowMonth;		// 0 - 11

float	NowPrecip;		// inches of rain per month
float	NowTemp;		// temperature this month
float	NowHeight;		// grain height in inches
int		NowNumDeer;
int  	NowNumDead;

const float GRAIN_GROWS_PER_MONTH =		8.0; // inches
const float ONE_DEER_EATS_PER_MONTH =		0.5;

const float AVG_PRECIP_PER_MONTH =		6.0; // inches
const float AMP_PRECIP_PER_MONTH =		6.0;
const float RANDOM_PRECIP =			2.0;

const float AVG_TEMP =				50.0; // Fahrenheit
const float AMP_TEMP =				20.0;
const float RANDOM_TEMP =			10.0;

const float MIDTEMP =				40.0;
const float MIDPRECIP =				10.0;


pthread_barrier_t doneComputing;
pthread_barrier_t doneAssigning;
pthread_barrier_t donePrinting;
pthread_mutex_t tempAndPrec;
pthread_attr_t attr;

float	Ranf( float, float );
int		Ranf( int, int );

float Ranf( float low, float high )
{
	float r = (float) rand();		// 0 - RAND_MAX
	return(   low  +  r * ( high - low ) / (float)RAND_MAX   );
}


int Ranf( int ilow, int ihigh )
{
	float low = (float)ilow;
	float high = (float)ihigh + 0.9999f;

	return (int)(  Ranf(low,high) );
}


void calcTempAndPrecip(void *t){
	//Must atomize so we don't have race conditions between months.
	pthread_mutex_lock(&tempAndPrec);
	float ang = (  30.*(float)NowMonth + 15.  ) * ( M_PI / 180. );

	float temp = AVG_TEMP - AMP_TEMP * cos( ang );
	NowTemp = temp + Ranf( -RANDOM_TEMP, RANDOM_TEMP );

	float precip = AVG_PRECIP_PER_MONTH + AMP_PRECIP_PER_MONTH * sin( ang );
	NowPrecip = precip + Ranf( -RANDOM_PRECIP, RANDOM_PRECIP );
	if( NowPrecip < 0. ){
		NowPrecip = 0.;
	}
	pthread_mutex_unlock(&tempAndPrec);
}

void *grainGrowth(void *t){
	int bar;
	float tempFactor;
	float precFactor;
	
	//loop infinitely until thread is cancelled in MAIN
	while(1){
		tempFactor = exp(-1* pow(((float)(NowTemp-MIDTEMP)/10),2));
		precFactor = exp(-1* pow(((float)(NowPrecip-MIDPRECIP)/10),2));
		
		//Wait for other threads to compute
		bar = pthread_barrier_wait(&doneComputing);
	
		float tempNowHeight = tempFactor * precFactor * GRAIN_GROWS_PER_MONTH;
		tempNowHeight -= (float)NowNumDeer * ONE_DEER_EATS_PER_MONTH; 
	
		NowHeight = tempNowHeight;
		if(NowHeight < 0){
			NowHeight = 0;
		}
		
		//Wait for other threads to Assign
		bar = pthread_barrier_wait(&doneAssigning);
		
		//Wait for other threads to Print
		bar = pthread_barrier_wait(&donePrinting);
	
	}
		
}

void *grainDeer(void *t){
	int bar;
	int tempNowNumDeer;
	
	//loop infinitely until thread is cancelled in MAIN
	while(1){
		tempNowNumDeer = NowNumDeer;
		
		if(NowHeight > tempNowNumDeer){
			tempNowNumDeer++;
		}else if(NowHeight < tempNowNumDeer){
			tempNowNumDeer--;
		}	
		
		//Wait for other threads to compute
		bar = pthread_barrier_wait(&doneComputing);
		
		//re-assign global to computed temp value
		NowNumDeer = tempNowNumDeer;
		
		//Wait for other threads to Assign
		bar = pthread_barrier_wait(&doneAssigning);
		
		//Wait for other threads to Print
		bar = pthread_barrier_wait(&donePrinting);
	}

}

void *watcher(void *t){
	int bar;
	int curMonth = 0;
	float celcius;
	float centi;
	void *temp;
	
	while(NowYear < 2020){
		
		bar = pthread_barrier_wait(&doneComputing);
		//Wait for other threads to Assign before printing starts
		bar = pthread_barrier_wait(&doneAssigning);
		
		celcius = (5./9.) * (NowTemp - 32);
		centi = NowHeight * 2.54;
		
		printf("Current Month: %d\n", curMonth);
		printf("Current Year: %d\n", NowYear);
		printf("Current Precipitation: %f\n", NowPrecip);
		printf("Current Temperature (C): %f\n", celcius);
		printf("Current Height (cm): %f\n", centi);
		printf("Current Deer Population: %d\n", NowNumDeer);
		printf("Current Dead Deer: %d\n", NowNumDead);
		printf("-----------------------------------\n");
		
		curMonth++;
		if(NowMonth < 11){
			NowMonth++;
		}else{
			NowMonth = 0;
			NowYear++;
		}
		
		calcTempAndPrecip(temp);
		
		bar = pthread_barrier_wait(&donePrinting);
	}	
	pthread_exit((void*) t);
}
/*
 *Open Season: In the town of Grainville, the human population generally remains happy eating grain
 *However sometimes they require meat.  What better meat than venison?  We will use a random chance
 *that the humans of grainVille crave meet (1 in 10), in which case they will open a hunting season.
 *during hunting season, up to 5 Graindeer can be killed. 
 */
void *huntingSeason(void *t){
	int bar;
	int seasonRandom;
	int deathRandom;
	
	//loop infinitely until thread is cancelled in MAIN
	while(1){
	
		
		bar = pthread_barrier_wait(&doneComputing);

		seasonRandom = rand() % 10 + 1;
		
		if(seasonRandom == 10){
			deathRandom = rand() % 5+1;
			if(deathRandom > NowNumDeer){
				deathRandom = NowNumDeer;
			}
			NowNumDeer -= deathRandom;
			NowNumDead = deathRandom;
			if(NowNumDeer < 0){
				NowNumDeer = 0;
			}
		}else{
			NowNumDead = 0;
		}
		
		bar = pthread_barrier_wait(&doneAssigning);
		
		bar = pthread_barrier_wait(&donePrinting);
	}
}

int main() {
	NowNumDeer = 1;
    NowHeight = 1.;
    NowMonth = 0;
    NowYear  = 2014;
	NowNumDead = 0;
	void *watcherJoin;
	void *temp;
	/* Array of 4 threads:
	 * idx 0: Grain Growth
	 * idx 1: Grain Deer
	 * idx 2: Hunting season (added case)
	 * idx 3: watcher
	 */
	pthread_t threads[4];
	
	//Populate an array with thread ID's
	int tids[4];
	for(int i = 0; i < 4; ++i) {
        tids[i] = i;
	}
	
	//Initialize barriers, mutex, attr....
	pthread_mutex_init(&tempAndPrec, NULL);
    pthread_barrier_init(&doneComputing, NULL, 4-1);
    pthread_barrier_init(&doneAssigning, NULL, 4-1);
    pthread_barrier_init(&donePrinting, NULL, 4-1);
    pthread_attr_init(&attr);
	
	//calulate temperature and precipitation from initial month
	calcTempAndPrecip(temp);
	
	//Spawn Threads
	pthread_create(&threads[0], &attr, grainGrowth, (void *) tids[0]);
    pthread_create(&threads[1], &attr, grainDeer, (void *) tids[1]);
    pthread_create(&threads[2], &attr, huntingSeason, (void *) tids[2]);
    pthread_create(&threads[3], &attr, watcher, (void *) tids[3]);
	
	//Join watcher
	pthread_join(threads[3], &watcherJoin);
	
	//Cancel all others
	pthread_cancel(threads[0]);
    pthread_cancel(threads[1]);
    pthread_cancel(threads[2]);
	
	pthread_attr_destroy(&attr);
	
	return 0;
	
}








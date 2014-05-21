#include <omp.h>
#include <stdio.h>

#define XMIN	 0.
#define XMAX	 3.
#define YMIN	 0.
#define YMAX	 3.

#define Z00	0.
#define Z10	1.
#define Z20	0.
#define Z30	0.

#define Z01	1.
#define Z11	6.
#define Z21	1.
#define Z31	0.

#define Z02	0.
#define Z12	1.
#define Z22	0.
#define Z32	4.

#define Z03	3.
#define Z13	2.
#define Z23	3.
#define Z33	3.

float Height( int iu, int iv, int NUMS )	// iu,iv = 0 .. NUMS-1
{
	float u = (float)iu / (float)(NUMS-1);
	float v = (float)iv / (float)(NUMS-1);

	// the basis functions:

	float bu0 = (1.-u) * (1.-u) * (1.-u);
	float bu1 = 3. * u * (1.-u) * (1.-u);
	float bu2 = 3. * u * u * (1.-u);
	float bu3 = u * u * u;

	float bv0 = (1.-v) * (1.-v) * (1.-v);
	float bv1 = 3. * v * (1.-v) * (1.-v);
	float bv2 = 3. * v * v * (1.-v);
	float bv3 = v * v * v;

	// finally, we get to compute something:

	float height = 	  bu0 * ( bv0*Z00 + bv1*Z01 + bv2*Z02 + bv3*Z03 )
			+ bu1 * ( bv0*Z10 + bv1*Z11 + bv2*Z12 + bv3*Z13 )
			+ bu2 * ( bv0*Z20 + bv1*Z21 + bv2*Z22 + bv3*Z23 )
			+ bu3 * ( bv0*Z30 + bv1*Z31 + bv2*Z32 + bv3*Z33 );

	return height;
}

float Height( int, int );

int main( int argc, char *argv[ ])
{
	#ifndef _OPENMP
	fprintf(stderr, "OpenMP is not supported. \n");
	#endif

	
	int NUMT;
	int NUMS;
	double startTime;
	double endTime;
	double totalTime;
	float volume;

	// the area of a single full-sized tile:

	float fullTileArea = (  ( (XMAX-XMIN)/(float)(NUMS-1) )  *  ( ( YMAX - YMIN )/(float)(NUMS-1) )  );

	// sum up the weighted heights into the variable "volume"
	
	for(int i = 1; i <= 16; i = i*2)
	{
		NUMT = i;
		omp_set_num_threads(NUMT);

		for(int j = 2; j <= 4096; j = j*2)
		{
			NUMS = j;
			startTime = omp_get_wtime();		

			#pragma omp parallel for default(none) reduction(+:volume) shared(NUMS) 
			for( int k = 0; k < NUMS*NUMS; k++ )
			{
				int iu = k % NUMS;
				int iv = k / NUMS;
				float temp = Height(iu, iv, NUMS);
				
				if((iu == 0 && iv == 0) || (iu == XMAX && iv == YMAX))//corner case
				{
					temp = temp*0.25;
				}

				else if(iu == 0 || iv == 0 || iu == XMAX || iv == YMAX)//edge case
				{
					temp = temp*0.5;
		
				}

				float test = temp * (  ( (XMAX-XMIN)/(float)(NUMS-1) )  *  ( ( YMAX - YMIN )/(float)(NUMS-1) )  );
				//printf("test: %f\n", test);
				volume = volume + test;
				
			}
			endTime = omp_get_wtime();
			//Compute total wall time
			totalTime = endTime - startTime;
			printf("Number of Threads: %d\n", NUMT);
			printf("Number of Subdivisions: %d\n", NUMS);
			printf("Total Time Required: %f\n", totalTime);
			printf("Total Volume Computed with %d subdivisions: %f\n", NUMS, volume);
			//Reset volume for next round of subdivisions
			volume = 0;
			printf("\n==========================================\n");
		}
	}
	
	
	

}

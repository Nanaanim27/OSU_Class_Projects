#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <omp.h>


struct s
{
	float value;
	int pad[NUM];

}Array[4];

int main( int argc, char *argv[ ] )
{

	omp_set_num_threads(NUMT);
	int someBigNumber = 1000000000;
	
	double startTime = omp_get_wtime();
	float temp;
	#pragma omp parallel default(none), private(Array), shared(someBigNumber) 
	for(int i = 0; i < NUMT; i++)
	{	
		float temp = Array[i].value;
		for(int j = 0; j < someBigNumber; j++)
		{
			temp = temp + 2;
			//Array[i].value = Array[i].value + 2;
		}
	}

	double endTime = omp_get_wtime();
	double totalTime = endTime-startTime;
	
	float fnumt = (float) NUMT;
	float fsbn = 1000000000.;
	float op1 = fnumt*fsbn;

	float mflops = op1/(totalTime)/1000000.;		
	
//	float mflops = ((float) ((NUMT*someBigNumber) / (totalTime))/1000000.);
	
	printf("op1: %f\n", op1);
	printf("Number of Threads: %d\n", NUMT);
	printf("Pad Number: %d\n", NUM);
	printf("MFlops per Second: %f\n", mflops);
	printf("total time: %f\n", totalTime);
	
	printf("==========================================\n");

	return 0;
}

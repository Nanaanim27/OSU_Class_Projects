#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "Program1.h"



int main(int argc, char* argv[])
{
	int n = 10000000;	/*upper limit*/
	int *allNums;                           /*to be filled with all numbers 1 - N*/
	int *primeNums;                         /*to be filled with all prime numbers*/
	int totalPrimeNums;                     /*counter variable for number of primes*/

	allNums = malloc(n*sizeof(int));
	primeNums = malloc(n*sizeof(int));
	
	fillArray(allNums, n);
	totalPrimeNums = sieveEras(allNums, primeNums, n);

	//UNCOMMENT TO TEST 
	printArray(primeNums, totalPrimeNums);
	printf("the number of primes between 1 and %d are: ", n);
	printf("%d\n", totalPrimeNums);
        
	return 0;	
}

/*
 * fillArray takes an array pointer, and an 
 * integer values and fills the array with 
 * numbers 1 through n.
 */ 

void fillArray(int *arrayA, int n)
{
int i;	
for(i=0; i<n; i++){
	arrayA[i] = i+1;
	}
}  
/*
 *sieveEras takes a full array of all numbers
 *an an empty array to be filled with prime numbers
 *and returns the number of primes between 1 and n. 
 */   
int sieveEras(int *arrayA, int *arrayP, int n)
{
	int k = 0;			/*first element of array (value:1)*/ 
	int j;                          /*loop counter*/
	int isPrime = 2;		/*first prime number*/						
	int primeIndex = 0;             /*beginning of prime number array to be filled*/

	while(k < n){
		
		if(arrayA[k]!=1 && arrayA[k]!=-1){	   /*accounts for number 1 as a special case*/
  			arrayP[primeIndex] = arrayA[k];    /*num at index k is prime; place in primeNums array*/
			isPrime = arrayA[k];		   /*set isPrime to the next prime number*/     			 
			primeIndex++;
		}
	for(j=isPrime; j <= n; j+=isPrime){                /*each multiple of the current prime number to -1 (indicating composite)*/ 
		if(arrayA[j-1] != 2){
			 arrayA[j-1] = -1;										
		}	
	}
	k++;		                                   /*go to next index*/
	}
	return primeIndex;
}
/* This function prints the elements of the array.
 * used for testing
 * UNCOMMENT TO TEST!!
 *
 */
void printArray (int *arrayP, int size)
{
	int i;
	for(i = 0; i<size; i++){	
		printf("%d\n", arrayP[i]);
       	 }


}


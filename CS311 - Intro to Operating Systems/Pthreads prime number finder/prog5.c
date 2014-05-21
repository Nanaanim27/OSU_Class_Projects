#define _BSD_SOURCE

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <signal.h>
#include <fcntl.h>
#include <ctype.h>
#include <limits.h>
#include <sys/mman.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <errno.h>
#include <unistd.h>
#include <pthread.h>
#include <stdint.h>
#include <time.h>
#include "primeFunctions.h"

/*arguments for seive function (starter)*/
struct arg_struct{
	uint32_t *allNums;
	uint32_t *endPrimes;
	uint32_t total;
	uint32_t startIndex;
	uint32_t iterations;
};

/*global variables to share*/	
int totalPrimes;
uint32_t *nums;
uint32_t *primes;
uint32_t iterCounter;
int primeIndex;
int isPrime;

/*mutex for threading*/
pthread_mutex_t sieveMutex;
int main(int argc, char *argv[])

{
	/*initialize all necessary vars and get assign command line arguments*/
	iterCounter = 0;        
	int num_threads = atoi(argv[1]);
	int num_procs = atoi(argv[1]);
	/*unsigned int*/ uint32_t totalNums = atoi(argv[2]);
	uint32_t i;
	int choice = atoi(argv[3]);
	clock_t start, end;
	double timeSpent;
	char *print;
	
	
	/*allocate memory for array of numbers, and primes*/
	primes = (uint32_t *)malloc(totalNums * sizeof(uint32_t));
	nums = (uint32_t *)malloc(totalNums * sizeof(uint32_t));
	pthread_mutex_init(&sieveMutex, NULL);

	/*populate array with numbers 1 thru totalNums*/
	for(i = 0; i < totalNums; i++){
		nums[i] = i+1;
	}
	
	/*call the parallelization function according to command line arg*/
	if(choice == 0){
		start = clock();
		threadPar(num_threads, nums, totalNums);
		end = clock();
	}else{	
		start = clock();
		procPar(num_procs, nums, totalNums);
		end = clock();
	}
	timeSpent = (double) (end-start)/CLOCKS_PER_SEC;
	if(choice == 0){
		printf("Time Spent for %d threads to calculate prime numbers between 0 and %d was: %f seconds\n", num_threads, totalNums, timeSpent);
	}else{
	printf("Time Spent for %d processes to calculate prime numbers between 0 and %d was: %f seconds\n", num_procs, totalNums, timeSpent);
	}
	
	return 0;
}

/*
 *threadPar creates the specified number of threads,
 *each of which call a function that finds prime numbers.
 *Each thread searched through an even portion of the array
 *of all numbers.
 */

void threadPar(int num_threads, uint32_t *numArray, uint32_t totalNums)
{
	     
	pthread_t *threads;  
	int *tfd;            
	int threadInputSize = totalNums/num_threads;  
	int unBal = totalNums % num_threads;
	int result;
	int i;

	/*allocate memory for threads and their FDs*/
	threads = (pthread_t *)malloc(num_threads * sizeof(pthread_t));
	tfd = (int *)malloc(num_threads * sizeof(int));

	if(threads == NULL || tfd == NULL){
		perror("problem creating threads");
	}

	/*create threads that call the sieveEras function with different input of size total/numThreads*/
	for(i = 0; i < num_threads; i++){
		
		/*decleration of arguments to be used by each thread.*/  
		struct arg_struct args;
		args.allNums = numArray;                   
		args.endPrimes = primes;                  
		args.total = totalNums;  
                   

		/*check to ensure that no numbers get excluded due to integer division*/
		if(unBal != 0 && iterCounter == 0){			
			args.iterations = threadInputSize + unBal;          
			args.startIndex = (i)*(threadInputSize);
			
		}else{
			args.iterations = threadInputSize;
			args.startIndex = ((i)*(threadInputSize)) + unBal;
		}
		/*call starter function*/
		tfd[i] = pthread_create(&threads[i], NULL, sieveEras, (void *) &args);
		if(tfd[i] != 0){
			perror("error creating thread");
		}
		
		/*join the thread, and retrieve variable returned by sieveEras*/
		
		pthread_join(threads[i], &result);
		totalPrimes += (int) result;
		
	}
	pthread_mutex_destroy(&sieveMutex);
	printf("total number of primes is: %d\n", totalPrimes);
}

void procPar(int num_procs, uint32_t *numArray, uint32_t totalNums)
{
	int procInputSize = totalNums/num_procs;
	int unBal = totalNums % num_procs;
	int result;
	int i;
	int status;
	
	/*create processes equal to num_procs*/
	for(i = 0; i < num_procs; i++){
		struct arg_struct args;
		args.allNums = numArray;                    
		args.endPrimes = primes;                    
		args.total = totalNums;                  
		if(unBal != 0 && iterCounter == 0){
			
			args.iterations = procInputSize + unBal;          
			args.startIndex = (i)*(procInputSize);
			
		}else{
			args.iterations = procInputSize;
			args.startIndex = ((i)*(procInputSize)) + unBal;
		}

		switch(fork()){
		case -1:
			//error
		case 0: 
			//child process
			result = sieveEras(&args);
			totalPrimes += (int) result;
		default:
			//parent
			wait(&status);
			
		}
		       		
	}
	printf("The total number of primes is %d\n", totalPrimes);
	
}




void *sieveEras(void *args){
	/*create local variables to make the code more readable*/
	uint32_t start;
	uint32_t totalNums;
	uint32_t *arrayP;
	uint32_t *arrayA;
	uint32_t iter;
	/*initialize local variables with the values passed to sieveEras*/
	struct arg_struct *params = args;
	start = params->startIndex;
	totalNums = params->total;
	arrayA = params->allNums;
	arrayP = params->endPrimes;
	iter = params->iterations;

	uint32_t i;
	uint32_t j;
	int tempPrimeIndex = primeIndex;

	
	pthread_mutex_lock(&sieveMutex);
 	
	if(iterCounter == 0){
		primeIndex = 0;
		isPrime = 2;
	}
	
	for(i = 0; i < iter; i++){
		
		if(arrayA[i+start] != 1 && arrayA[i+start] != -1){
		       	arrayP[primeIndex] = arrayA[i+start];
			isPrime = arrayA[i+start];
			primeIndex++;
		}
		
		for(j = isPrime; j <= totalNums; j += isPrime){
			if(arrayA[(j-1)+start] != 2){
				arrayA[(j-1)] = -1;
			}
		}	
		
	}       
	primeIndex = primeIndex - tempPrimeIndex;
	iterCounter++;
	pthread_mutex_unlock(&sieveMutex);
	return (void *) primeIndex;
}

void printArray(uint32_t *array, int size)
{
	int i;
	for(i = 0; i < size; i++){
		printf("%d\n", array[i]);
	}
}

/*
void *init_shmem(char *path, int obj_size){
	int shmemFD;
	void *addr;

	//create shared memory object and resize it
	shmemFD = shm_open(path, O_CREAT | O_RDWR, S_IRUSR | S_IWUSR);
	if(shmemFD == -1){
		perror("failed to create shared mem object");
		
	}

	//resize to something reasonable
	if(ftruncate(shmemFD, object_size)){
		perror("could not resize to requested object size");
	}

	//create map to virtual address space for shared memory object
	addr = mmap(NULL, object_size, PROT_READ | PROT_WRITE, MAP_SHARED, 0);
	if(addr == MAP_FAILED){
		perror("failed to map shared mem object");
	}
	//return shared memory FD (mapped VA space)
	return addr;
} 
*/


/*	stackapp.c: Stack application. */
#include <stdio.h>
#include <stdlib.h>
#include "dynArray.h"
#include <string.h>


/* ***************************************************************
Using stack to check for unbalanced parentheses.
***************************************************************** */

/* Returns the next character of the string, once reaches end return '0' (zero)
	param: 	s pointer to a string 	
	pre: s is not null		
*/
char nextChar(char* s)
{
	static int i = -1;	
	char c;
	++i;	
	c = *(s+i);			
	if ( c == '\0' )
		return '0';	
	else 
		return c;
}

/* Checks whether the (), {}, and [] are balanced or not
	param: 	s pointer to a string 	
	pre: s is not null	
	post:	
*/
int isBalanced(char* s)
{
	
	int curlyCount = 0;
	int squareCount = 0;
	int perenCount = 0;
        int i;
	/* FIXME: You will write this function */
       /*for(j = 0; s[j]; j++){
                size++;
		
	}*/     
        	
	                         
        DynArr stack = *newDynArr(strlen(s)+1);
        stack.size = strlen(s);
	   	
	for(i=0; i < strlen(s); i++){
		
		stack.size--;	
	
	}
        
        for(i=0; i < strlen(s); i++){
       		  pushDynArr(&stack, s[i]);        	
	   
			
  		switch(topDynArr(&stack)){
			case '{':
                        curlyCount++;
			popDynArr(&stack);
			break;
	
			case '}':
			curlyCount--;
			popDynArr(&stack);
			break;

			case '[':
			squareCount++;
			popDynArr(&stack);
			break;

			case ']':
			squareCount--;
			popDynArr(&stack);
			break;

			case '(':
			perenCount++;
			popDynArr(&stack);
			break;
			
			case ')':
			perenCount--;
			popDynArr(&stack);
			break;

			default:
		        popDynArr(&stack);
			break;
 	       }
      
        
        }
        if((curlyCount==0)&&(squareCount == 0)&&(perenCount==0)){		
       	        return 1; /*It is balanced*/
     	}   
	return 0; /*Is not balanced*/
}

int main(int argc, char* argv[]){

        printf("Assignment2\n");

	char* s=argv[1]; 
	int res;
	
	res = isBalanced(s);

	if (res)
		printf("The string %s is balanced\n",s);
	else 
		printf("The string %s is not balanced\n",s);
	
	return 0;	
}


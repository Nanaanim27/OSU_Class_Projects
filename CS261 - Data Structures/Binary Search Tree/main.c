#include<stdio.h>
#include<stdlib.h>
#include "bst.h"
#include "type.h"
#include "structs.h"

/* Example main file to begin exercising your tree */

int main(int argc, char *argv[])
{	


	struct BSTree *tree	= newBSTree(); /* do not change */
	
	/* Change the command lines below to generate new test examples */ 
	

	/*Create value of the type of data that you want to store*/
	struct data myData1;
	struct data myData2;
	struct data myData3;
	struct data myData4;
	struct data myData5;
	struct data myData6;
	struct data myData7;
	struct data myData8;
	
	myData1.number = 5;
	myData1.name = "rooty";
	myData2.number = 1;
	myData2.name = "lefty";
	myData3.number = 10;
	myData3.name = "righty";
	myData4.number = 3;
	myData4.name = "righty";
	myData5.number = 50;
	myData5.name = "Spartacus";
	myData6.number = 100;
	myData6.name = "Krixus";
	myData7.number = 75;
        myData7.name = "Ganicus";
	myData8.number = 4;    
        myData8.name = "Batteatus";

	/*add the values to BST*/
	addBSTree(tree, &myData1);
	addBSTree(tree, &myData2);
	addBSTree(tree, &myData3);
	addBSTree(tree, &myData4);
	addBSTree(tree, &myData5);
	addBSTree(tree, &myData6);
        addBSTree(tree, &myData7);
        addBSTree(tree, &myData8);
	
	/*Print the entire tree*/	
	printTree(tree);	
	/*(( 1 ( 3 ) ) 5 ( 10 ))*/
	printf("\n");
	printf("%d", containsBSTree(tree,&myData1));
	printf("\n");
	removeBSTree(tree,&myData6);
	return 1;
}


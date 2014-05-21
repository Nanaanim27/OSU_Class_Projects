/*
 File: bst.c
 Implementation of the binary search tree data structure.
 
 */
#include <stdlib.h>
#include <stdio.h>
#include "assert.h"
#include "type.h"
#include "bst.h"


struct Node {
	TYPE         val;
	struct Node *left;
	struct Node *right;
};

struct BSTree {
	struct Node *root;
	int          cnt;
};

/*----------------------------------------------------------------------------*/
/*
 function to initialize the binary search tree.
 param tree
 pre: tree is not null
 post:	tree size is 0
		root is null
 */

void initBSTree(struct BSTree *tree)
{	
	tree->cnt  = 0; 
	tree->root = 0;	
}

/*
 function to create a binary search tree.
 param: none
 pre: none
 post: tree->count = 0
		tree->root = 0;
 */
 
struct BSTree*  newBSTree()
{
	struct BSTree *tree = (struct BSTree *)malloc(sizeof(struct BSTree));
	assert(tree != 0);
	
	initBSTree(tree);
	return tree;
}

/*----------------------------------------------------------------------------*/
/*
function to free the nodes of a binary search tree
param: node  the root node of the tree to be freed
 pre: none
 post: node and all descendants are deallocated
*/
 
void _freeBST(struct Node *node)
{
	if (node != 0) {
		_freeBST(node->left);
		_freeBST(node->right);		
		free(node);
	}
}

/* 
 function to clear the nodes of a binary search tree 
 param: tree    a binary search tree
 pre: tree ! = null
 post: the nodes of the tree are deallocated
		tree->root = 0;
		tree->cnt = 0
 */
void clearBSTree(struct BSTree *tree)
{
	_freeBST(tree->root);
	tree->root = 0;
	tree->cnt  = 0;
}

/*
 function to deallocate a dynamically allocated binary search tree
 param: tree   the binary search tree
 pre: tree != null;
 post: all nodes and the tree structure itself are deallocated.
 */
void deleteBSTree(struct BSTree *tree)
{
	clearBSTree(tree);
	free(tree);
}

/*----------------------------------------------------------------------------*/
/*
 function to determine if  a binary search tree is empty.
 
 param: tree    the binary search tree
 pre:  tree is not null
 */
int isEmptyBSTree(struct BSTree *tree) { return (tree->cnt == 0); }

/*
 function to determine the size of a binary search tree

param: tree    the binary search tree
pre:  tree is not null
*/
int sizeBSTree(struct BSTree *tree) { return tree->cnt; }

/*----------------------------------------------------------------------------*/
/*
 recursive helper function to add a node to the binary search tree.
 param:  cur	the current root node
		 val	the value to be added to the binary search tree
 pre:	cur is not null
		val is not null
 */
struct Node *_addNode(struct Node *cur, TYPE val)
{

	        

	/*write this*/
	struct Node *temp = (struct Node *)malloc(sizeof(struct Node));
        if(cur == NULL){
		cur = temp;
		temp->val = val;
		temp->left = 0;
		temp->right = 0;
		return cur;
	}
       	if(compare(cur->val, val) == 1){
		cur->left = _addNode(cur->left, val);
	}
	if(compare(cur->val, val) == 0){
        	printf("%s", "Cannot add duplicate elements.");
	}
	if(compare(cur->val, val) == -1){
      		cur->right = _addNode(cur->right, val);
	}   

	return cur; 
}

/*
 function to add a value to the binary search tree
 param: tree   the binary search tree
		val		the value to be added to the tree
 
 pre:	tree is not null
		val is not null
 pose:  tree size increased by 1
		tree now contains the value, val
 */
void addBSTree(struct BSTree *tree, TYPE val)
{
	tree->root = _addNode(tree->root, val);	
	tree->cnt++;
}


/*
function to determine if the binary search tree contains a particular element
 param:	tree	the binary search tree
		val		the value to search for in the tree
 pre:	tree is not null
		val is not null
 post:	none
 */
int _containsHelper(struct Node *cur, TYPE val)
{

	        

	if(compare(cur->val, val) == 0){
		return 1;
	}
/*	if(cur == NULL){
		return 0;
	}  */
	if(compare(cur->val, val) == -1){
		return(_containsHelper(cur->left, val));
	}
	if(compare(cur->val, val) == 1){
		return(_containsHelper(cur->right, val));
	}
	else
	return 0;
}
	
/*----------------------------------------------------------------------------*/
int containsBSTree(struct BSTree *tree, TYPE val)
{
	       
        
	/*write this*/
	if(compare(tree->root->val, val)==0){
 	
		return 1; 
	}else{
		
		return _containsHelper(tree->root, val);
	}
}

/*
 helper function to find the left most child of a node
 param: cur		the current node
 pre:	cur is not null
 post: none
 */

/*----------------------------------------------------------------------------*/
TYPE _leftMost(struct Node *cur)
{
                

	printf("%s", "leftmost");
	/*write this*/
	struct Node *temp = cur;
	while(temp->left != NULL){
		return _leftMost(temp->left);
	}	
	return temp->val;	
}
/*
 helper function to remove the left most child of a node
 param: cur	the current node
 pre:	cur is not null
 post:	the left most node of cur is not in the tree
 */
/*----------------------------------------------------------------------------*/
struct Node *_removeLeftMost(struct Node *cur)
{
	        

        printf("%s", "removeleftmost");
	/*write this*/
	struct Node *temp2;
	while(cur->left->left!=NULL){
        	return _removeLeftMost(cur->left);
	}
	temp2 = cur->left;
	cur->left = 0;
	
	return temp2;	
}
/*
 recursive helper function to remove a node from the tree
 param:	c:ur	the current node
		val	the value to be removed from the tree
 pre:	val is in the tree
		cur is not null
		val is not null
 */
/*----------------------------------------------------------------------------*/
struct Node *_removeNode(struct Node *cur, TYPE val)
{
	/*write this*/
	        
        printf("%s", "removenode");
	if(cur->val == val){
		if(cur->right == NULL){
			return cur->left;

	       } else{
	       		cur->val = (_leftMost(cur->right));
       			cur->right = _removeLeftMost(cur->right);
	}
}
	else if (cur->val > val)
		cur->left = _removeNode(cur->left, val);
	else
		cur->right = _removeNode(cur->right, val);

	return cur; 
	
}
/*
 function to remove a value from the binary search tree
 param: tree   the binary search tree
		val		the value to be removed from the tree
 pre:	tree is not null
		val is not null
		val is in the tree
 pose:	tree size is reduced by 1
 */
void removeBSTree(struct BSTree *tree, TYPE val)
{
	printf("%s", "removeBSTree");
	if (containsBSTree(tree, val)) {
		tree->root = _removeNode(tree->root, val);
		tree->cnt--;
	}
}

/*----------------------------------------------------------------------------*/

/* The following is used only for debugging, set to "#if 0" when used 
  in other applications */
#if 1
#include <stdio.h>

/*----------------------------------------------------------------------------*/
void printNode(struct Node *cur) {
	 if (cur == 0) return;
	 printf("(");
	 printNode(cur->left);	 
	 /*print_type prints the value of the TYPE*/
	 print_type(cur->val);
	 printNode(cur->right);
	 printf(")");
}

void printTree(struct BSTree *tree) {
	 if (tree == 0) return;	 
	 printNode(tree->root);	 
}
/*----------------------------------------------------------------------------*/

#endif

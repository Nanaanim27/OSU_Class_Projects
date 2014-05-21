#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include "cirListDeque.h"
#include "type.h"
/* internal functions interface */
struct DLink* _createLink (TYPE val);
void _addLinkAfter(struct cirListDeque *q, struct DLink *lnk, struct DLink *newLnk);
void _removeLink(struct cirListDeque *q, struct DLink *lnk);


/* ************************************************************************
	Deque Functions
************************************************************************ */

/* Initialize deque.

	param: 	q		pointer to the deque
	pre:	q is not null
	post:	q->Sentinel is allocated and q->size equals zero
*/




void initCirListDeque (struct cirListDeque *q) 
{
	/* FIX ME*/
	assert(q!=NULL);

	struct DLink *sentinel = (struct DLink *)malloc(sizeof(struct DLink));
	assert(q->Sentinel != 0);
        q->Sentinel = sentinel;


	q->Sentinel->next = q->Sentinel;
	q->Sentinel->prev = q->Sentinel;;
	 
}

/* Create a link for a value.

	param: 	val		the value to create a link for
	pre:	none
	post:	a link to store the value
*/
struct DLink * _createLink (TYPE val)
{
	/* FIX ME*/
	struct DLink *newLink = (struct DLink *)malloc(sizeof(struct DLink));
	assert(newLink!=0);
	newLink->value = val;
	newLink->next = 0;
        newLink->prev = 0;
	return newLink;
}
/* Adds a link after another link

	param: 	q		pointer to the deque
	param: 	lnk		pointer to the existing link in the deque
	param: 	newLnk	pointer to the new link to add after the existing link
	pre:	q is not null
	pre: 	lnk and newLnk are not null
	pre:	lnk is in the deque 
	post:	the new link is added into the deque after the existing link
*/
void _addLinkAfter(struct cirListDeque *q, struct DLink *lnk, struct DLink *newLnk)
{
	/* FIX ME*/
	assert((q!=NULL)&&(lnk!=NULL)&&(newLnk!=NULL));	

	lnk->next->prev = newLnk;
	newLnk->next = lnk->next;
	lnk->next = newLnk;
	newLnk->prev=lnk;
	q->size++;
	        
}

/* Adds a link to the back of the deque

	param: 	q		pointer to the deque
	param: 	val		value for the link to be added
	pre:	q is not null
	post:	a link storing val is added to the back of the deque
*/
void addBackCirListDeque (struct cirListDeque *q, TYPE val) 
{
	/* FIX ME*/
	assert(q!=NULL);
       struct DLink *new = _createLink(val);
       _addLinkAfter(q, q->Sentinel->next, new); 
	
	
	
}

/* Adds a link to the front of the deque

	param: 	q		pointer to the deque
	param: 	val		value for the link to be added
	pre:	q is not null
	post:	a link storing val is added to the front of the deque
*/
void addFrontCirListDeque(struct cirListDeque *q, TYPE val)
{
	/* FIX ME*/
	assert(q!=NULL);
	struct DLink *new = _createLink(val);
	_addLinkAfter(q,q->Sentinel->prev, new);
}

/* Get the value of the front of the deque

	param: 	q		pointer to the deque
	pre:	q is not null and q is not empty
	post:	none
	ret: 	value of the front of the deque
*/
TYPE frontCirListDeque(struct cirListDeque *q) 
{
	/* FIX ME*/
	assert((q!=NULL)&&(isEmptyCirListDeque(q) == 0));
	return q->Sentinel->prev->value;

}

/* Get the value of the back of the deque

	param: 	q		pointer to the deque
	pre:	q is not null and q is not empty
	post:	none
	ret: 	value of the back of the deque
*/
TYPE backCirListDeque(struct cirListDeque *q)
{
	/* FIX ME*/
	assert((q!=NULL)&&(isEmptyCirListDeque(q) == 0));

	return q->Sentinel->next->value;
}

/* Remove a link from the deque

	param: 	q		pointer to the deque
	param: 	lnk		pointer to the link to be removed
	pre:	q is not null and q is not empty
	pre:	lnk is in the deque 
	post:	the link is removed from the deque
*/
void _removeLink(struct cirListDeque *q, struct DLink *lnk)
{
	/* FIX ME*/
	assert((q!=NULL)&&(isEmptyCirListDeque(q) == 0));

	lnk->prev->next = lnk->next;
	lnk->next->prev =  lnk->prev;
	free(lnk);
	q->size--;
}

/* Remove the front of the deque

	param: 	q		pointer to the deque
	pre:	q is not null and q is not empty
	post:	the front is removed from the deque
*/
void removeFrontCirListDeque (struct cirListDeque *q) {
	/* FIX ME*/
        assert((q!=NULL)&&(isEmptyCirListDeque(q) == 0));

	_removeLink(q, q->Sentinel->prev);
}


/* Remove the back of the deque

	param: 	q		pointer to the deque
	pre:	q is not null and q is not empty
	post:	the back is removed from the deque
*/
void removeBackCirListDeque(struct cirListDeque *q)
{
	/* FIX ME*/
        assert((q!=NULL)&&(isEmptyCirListDeque(q) == 0));

	_removeLink(q, q->Sentinel->next);
}

/* De-allocate all links of the deque

	param: 	q		pointer to the deque
	pre:	none
	post:	All links (including backSentinel) are de-allocated
*/
void freeCirListDeque(struct cirListDeque *q)
{
	/* FIX ME*/
	free(q);
	
}

/* Check whether the deque is empty

	param: 	q		pointer to the deque
	pre:	q is not null
	ret: 	1 if the deque is empty. Otherwise, 0.
*/
int isEmptyCirListDeque(struct cirListDeque *q) {
	/* FIX ME*/
       if(q->Sentinel->next == q->Sentinel->prev){
        	return 1;
	}
       return 0; 
}

/* Print the links in the deque from front to back

	param: 	q		pointer to the deque
	pre:	q is not null and q is not empty
	post: 	the links in the deque are printed from front to back
*/
void printCirListDeque(struct cirListDeque *q)
{
	/* FIX ME*/
        assert((q!=NULL)&&(isEmptyCirListDeque(q) == 0));

	struct DLink *current = q->Sentinel->next;
	while(current!=q->Sentinel){
	printf("%d\n", current->value);
	current = current->next;
	}

}

/* Reverse the deque

	param: 	q		pointer to the deque
	pre:	q is not null and q is not empty
	post: 	the deque is reversed
*/
void reverseCirListDeque(struct cirListDeque *q)
{
	/* FIX ME*/
        assert((q!=NULL)&&(isEmptyCirListDeque(q) == 0));

        struct DLink *temp = q->Sentinel->next;
	struct DLink *temp2;
	while(temp!=q->Sentinel){
		temp2 = temp->prev;
		temp->prev = temp->next;
		temp->next = temp2;
		temp = temp->next;
	 }       
	temp2 = q->Sentinel->prev;
	q->Sentinel->prev = q->Sentinel->next;
	q->Sentinel->next = temp2;
		

       /* struct CirListDeque *reversed;
	struct CirListTemp
	initCirListDeque(reversed);
	int oldSize = q->size;
	struct DLink current = q->Sentinel->prev;
	while(current!=q->Sentinel){
  		reversed->Sentinel->next = current;
		current = current->prev*/      	
	

	
	


	
}      

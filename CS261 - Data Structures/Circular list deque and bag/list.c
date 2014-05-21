#include "listbag.h"
#include "type.h"
#include <assert.h>
#include <stdlib.h>
#include <stdio.h>


       /* function to initialize the list, set the sentinels and set the size
	param lst the list
	pre: lst is not null
	post: lst size is 0*/




void initList (struct list *lst) {
	/* FIX ME*/
	assert(lst!=NULL);
	struct DLink *head = (struct DLink*)malloc(sizeof(struct DLink));
	struct DLink *tail = (struct DLink*)malloc(sizeof(struct DLink));

	lst->head = head;
	lst->tail = tail;
	lst->head->next = tail;
	lst->tail->prev = head;
	lst->head->prev = 0;
	lst->tail->next = 0;
	lst->size = 0;

}


/*
	_addLink
	Funtion to add a value v to the list before the link l
	param: lst the list
	param: lnk the  link to add before
	param: v the value to add
	pre: lst is not null
	pre: lnk is not null
	post: lst is not empty
*/

void _addLink(struct list *lst, struct DLink *lnk, TYPE v)
{
	/* FIX ME*/
	 assert((lst!=NULL)&&(lnk!=NULL));

	struct DLink *l = (struct DLink *)malloc(sizeof(struct DLink));
        l->value = v;

        l->prev = lnk->prev;
	l->next = lnk;;
	lnk->prev->next = l;
	lnk->prev = l;

	lst->size++; 
	
}


/*
	addFrontList
	Function to add a value to the front of the list, can use _addLink()
	param: lst the list
	param: e the element to be added
	pre: lst is not null
	post: lst is not empty, increased size by 1
*/

void addFrontList(struct list *lst, TYPE e)
{
	/* FIX ME*/
	 assert(lst!=NULL);
        

	_addLink(lst, lst->head->next, e);
}

/*
	addBackList
	Function to add a value to the back of the list, can use _addlink()
	param: lst the list
	pre: lst is not null
	post: lst is not empty
*/

void addBackList(struct list *lst, TYPE e) {
	/* FIX ME*/
	 assert(lst!=NULL);
	 


	_addLink(lst, lst->tail->prev, e);
}

/*
	frontList
	function to return the element in the front of the  list
	param: lst the list
	pre: lst is not null
	pre: lst is not empty
	post: none
*/

TYPE frontList (struct list *lst) {
	/* FIX ME*/
	 assert((lst!=NULL)&&(isEmptyList(lst) == 0));

	return (lst->head->next->value);
}

/*
	backList
	function to return the element in the back of the  list
	param: lst the list
	pre: lst is not null
	pre: lst is not empty
	post: lst is not empty
*/

TYPE backList(struct list *lst)
{
	/* FIX ME*/
	assert((lst!=NULL)&&(isEmptyList(lst) == 0));

	return (lst->tail->prev->value);
}

/*
	_removeLink
	Function to remove a given link
	param: lst the list
	param: lnk the linke to be removed
	pre: lst is not null
	pre: lnk is not null
	post: lst size is reduced by 1
*/

void _removeLink(struct list *lst, struct DLink *lnk)
{
	/* FIX ME*/
	assert((lst!=NULL)&&(lnk!=NULL));

	lnk->prev->next = lnk->next;
	lnk->next->prev = lnk->prev;
	free(lnk);
	lst->size--;
}

/*
	removeFrontList
	Function to remove element from front of list, can use _removelink()
	param: lst the list
	pre:lst is not null
	pre: lst is not empty
	post: size is reduced by 1
*/

void removeFrontList(struct list *lst) {
	/* FIX ME*/
	assert((lst!=NULL)&&(isEmptyList(lst) == 0));

	lst->head->next = lst->head->next->next;
	lst->head->next->next->prev = lst->head;
	free(lst->head->next);
	lst->size--;
}

/*
	removeBackList
	Function to remove element from back of list, can use _removelink()
	param: lst the list
	pre: lst is not null
	pre:lst is not empty
	post: size reduced by 1
*/

void removeBackList(struct list *lst)
{
	/* FIX ME*/
        assert((lst!=NULL)&&(isEmptyList(lst) == 0));

	lst->tail->prev->prev->next = lst->tail;
	lst->tail->prev = lst->tail->prev->prev;
	free(lst->tail->prev);
	lst->size--;
}

/*
	isEmptyList
	param: q the list
	pre: q is not null
	post: none
*/

int isEmptyList(struct list *lst) {
	/* FIX ME*/
        assert(lst!=NULL);

	if((lst->head->next == lst->tail)&&(lst->tail->prev == lst->head)){
		return 1; /*for true*/
	}
	return 0; /*for false*/
	
}



/* Recursive implementation of contains()
 Pre: current is not null
 Post: 1 if found, 0 otherwise
 */
int _contains_recursive(struct list *lst, struct DLink* current, TYPE e){
	/* FIX ME*/
	 assert(current!=NULL);

	if(current->next == 0){
		return 0;
	}
	if(current->value == e){
		return 1;
	}
	return _contains_recursive(lst,current->next, e);

}

/* Wrapper function for contains
 Pre: lst is not null
 Post: 1 if found, 0 otherwise
 */
int listContains (struct list *lst, TYPE e) {
	assert(lst!=NULL);
	return _contains_recursive(lst,lst->head->next, e);
}

/* Recursive implementation of remove()*/
void  _remove_recursive(struct list *lst, struct DLink* current, TYPE e){
	/* FIX ME*/
	if((current->value)== e){
        	current->prev->next = current->next;
		current->next->prev = current->prev;
		free(current);
		lst->size--;
	}else{
	_remove_recursive(lst, current->next, e);
	}
}
/* Wrapper for remove()*/
void listRemove (struct list *lst, TYPE e) {
	/* FIX ME*/
	assert(isEmptyList(lst)==0);
	_remove_recursive(lst, lst->head->next, e);
}




                   

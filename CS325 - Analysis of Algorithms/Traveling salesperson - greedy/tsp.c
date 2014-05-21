#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <limits.h>

typedef struct simp_point {
    int number;
    int x;
    int y;
    struct simp_point *next;
} simple_point;

//city 
typedef struct node{
    int number;
    int x;
    int y;
    int visited;
    struct link **neighbors;
    int n_size;
} node;

typedef struct link{
    struct node *node;
    int distance;
} link;

typedef struct tour_link{
    struct node *node;
    int distance;
    struct tour_link *next;
	struct tour_link *prev;
} tour_link;


//Globals
node **points;
int size = 0;
long sum;

int dist(node *a, node *b){
    return (int) (sqrt((double)((b->x - a->x) * (b->x - a->x) + (b->y - a->y) * (b->y - a->y))) + 0.5);
}


void init_node_list(){
    points = (node **) malloc(size * sizeof(node *));
            
    for (int i = 0; i < size; i++){
        points[i] = (node *) malloc(sizeof(node));
        points[i]->n_size = size - 1;
        points[i]->visited = 0;
        points[i]->neighbors = (link **) malloc((size - 1) * sizeof(link));  
    }
}
/* function to optimize greedy algorithm
 * takes old solution as a paramater
 */
tour_link *generate_opt2(tour_link *tour)
{
	
	//store the old solution;
	//S
	tour_link *greedyResult = (tour_link *) malloc(sizeof(tour_link));
	greedyResult = tour;

	//S*
	tour_link *newResult = (tour_link *) malloc(sizeof(tour_link));
	newResult = greedyResult;
	
	tour_link *tempLink = (tour_link *) malloc(sizeof(tour_link));
	tempLink = greedyResult;
	int i,j,k;
	//set the best tour length to the global sum found in greedy
	int current_best_length = sum;
	int new_tour_length = 0;
	for(i = 0; i < size;   i++){
	       
		for(j = 1; j <=size; j++){
			//swap the current and next cities to check for optimized path
			
			swap_nodes(points[i], points[j]);
			//compute distance of new path
			for(k = 0; k < size; k++){
				new_tour_length += tempLink->distance;
				tempLink = tempLink->next;
			}
			//check if better path was found by swapping
			if(new_tour_length < current_best_length){
				//better path found, store in temp var
				current_best_length = new_tour_length;
				newResult = tempLink;
			}else{
				//reset and continue
				tempLink = greedyResult;
			}
			
		}
		
	}

	return newResult;
}

/* Function used to switch two nodes
 * to be used by the 2-opt algorithm
 */
void swap_nodes(node *A, node *B)
{

	node *tempNode = (node *) malloc(sizeof(node));
	tempNode = A;

	//A->next = B->next;
	//B->next = tempNode->next;
	
	A->neighbors = B->neighbors;
	B->neighbors = tempNode->neighbors;

}

tour_link *generate_greedy(){
    tour_link *head_point = (tour_link *) malloc(sizeof(tour_link));
    head_point->node = points[0];
    points[0]->visited = 1;
    
    tour_link *current_point = head_point;

    int current = 0;
    for (int i = 0; i < size - 1; i++){
        //printf("i = %d, current = %d\n", i, current);
        int min_dist = INT_MAX;
        int min_point = -1;
        for (int j = 1; j < size; j++){
            //find best not taken
            if (current == j || points[j]->visited == 1){
                continue;
            }
            
            int len = dist(points[current], points[j]);
    
            //printf("j = %d, len = %d\n", j, len);
    
            points[current]->neighbors[j]->distance = len;
            points[j]->neighbors[current]->distance = len;

            if (len < min_dist){
                min_dist = len;
                min_point = j;
            }
        }
        //printf("min_point = %d, min dist = %d\n", min_point, min_dist);
        tour_link *new_link = (tour_link *) malloc(sizeof(tour_link));
        new_link->node = points[min_point];
        points[min_point]->visited = 1;
		new_link->prev = current_point;
        current_point->next = new_link;
        current_point->distance = min_dist;
        current_point = new_link;
        current = min_point;
    }
    current_point->next = head_point;
	head_point->prev = current_point;
    current_point->distance = dist(current_point->node, head_point->node);

    return head_point;
}

void print_tour(tour_link *tour){
    tour_link *current = tour;    
    //initialize global sum variable -NICK
    sum = 0;
    for (int i = 0; i < size; i++){
        sum += current->distance;
        printf("Point %d - Dist %d - Sum %ld\n", current->node->number, current->distance, sum); 
        current = current->next;
    }
}

void init_links(){
    for (int i = 0; i < size; i++){
        for (int k = i; k < size; k++){
            points[i]->neighbors[k] = (link *) malloc(sizeof(link));
            points[k]->neighbors[i] = (link *) malloc(sizeof(link));

            points[i]->neighbors[k]->node = points[k];
            points[k]->neighbors[i]->node = points[i];
        }
    }
}

int make_points_from_file(char *filename){
    char line[50];
    FILE *file = fopen (filename, "rt");
	
	if (file == NULL){
		fprintf(stderr, "Failed to open file: %s\n", filename);
		return -1;
	}

    simple_point *point = (simple_point *) malloc(sizeof(simple_point));
    simple_point *point_list = point;
    while(fgets(line, 50, file) != NULL){
        int num;
        int x;
        int y;
                                                                
        sscanf (line, "%d %d %d", &num, &x, &y);

        size++;
        point->number = num;
        point->x = x;
        point->y = y;

        simple_point *next_point = (simple_point *) malloc(sizeof(simple_point));
        point->next = next_point;
        point = next_point;
    }
                                
    free(point);
    fclose(file);  /* close the file prior to exiting the routine */

    printf("%d\n", size);
    init_node_list();

    for (int i = 0; i < size; i++){
        points[i]->number = point_list->number;
        points[i]->x = point_list->x;
        points[i]->y = point_list->y;
        point_list = point_list->next;
    }

    init_links();
	return 0;
}




void usage(){
	fprintf(stderr, "Usage: tsp [filename]\n");
	exit(-1);
}

int main(int argc, char **argv){

	if (argc != 2){
		usage();
	}

    if (make_points_from_file(argv[1]) < 0){
		usage();
	}
    tour_link* tour = generate_greedy();
    fprintf(stderr, "printing\n");
    tour_link *optTour = generate_opt2(tour);
    print_tour(tour);
    print_tour(optTour);
}




#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <errno.h>
#include <unistd.h>
#include <string.h>
#include "prog3functions.h"
#include "ar.h"

int global_num_records = 0;

int main (int argc, char **argv)
{

      
       
       	/*
       	* The value for the key was -q  
       	* quickly append named files to archive
       	*/  
       	if(strcmp(argv[1],"-q") == 0){  
	  quickappend(argc,  argv);
       	}
       	/*
       	* The value for the key was -x
       	* extract named files
       	*/
       	if(strcmp(argv[1], "-x") == 0){
       		extract(argc, argv); 
       	}
       	/*
       	* The value for the key was -t
       	* print a concise table of contents of the archive
       	*/
       	if(strcmp(argv[1], "-t")== 0){
	  shortCont(argc, argv);
       
       	}
        /*
	 * The value for the key was -v
         * print a verbose table of contents of the archive
         */
       	if(strcmp(argv[1],"-v") == 0){
      	       	printf("%s\n", argv[1]);
       	}
       	/* The value of the key was -d
       	* delete named files from the archive
       	*/
       	if(strcmp(argv[1], "-d") == 0){
       		printf("%s\n", argv[1]);
       	}
       	/*
       	* The value of the key was -A
        * quickly append all "regular files in the current directory
       	*/

       	if(strcmp(argv[1], "-A") == 0 ){
       		printf("%s\n", argv[1]);
       	}
		
       	return 0;
}

void quickappend(int numArgs, char **args)
{
        //NOT CORRECT
       	int  MAX_READ = 1024;
       	int counter = 0;
	int fd1, fd2, numRead, written, i;
       	char buffer[MAX_READ + 1];
       	char temp[SARMAG];
       	struct ar_hdr arch;
	//Open the file and write the magic string
       	fd1 = open(args[2], O_RDWR | O_CREAT | O_APPEND, S_IRWXU);
       	if(fd1 == -1){
       		perror("open1");
       	}

       	strcpy(temp,ARMAG);
       	written = write(fd1,temp,SARMAG);
       	if(written == -1){
       		perror("Written");

       	}

	/*for each file, open and create a state struct.
	 *convert the integer values into char[] values
	 *and temporarily store that in the ar_hdr struct
	 *write each header to the archive file as well 
	 *as each files' contents.
	 */
       	for(i = 3; i < numArgs; i++){
	  
       		fd2 = open(args[i], O_RDWR);
       	       	if(fd2 == -1){
	       		perror("open2");      
              	}
	       	struct stat fileStat;
	       	if(fstat(fd2, &fileStat) == -1){
		       	perror("fstat");
	      	}
   
		strcpy(arch.ar_name, args[i]);
		sprintf(arch.ar_date, "%d",fileStat.st_ctime);
       	       	sprintf(arch.ar_uid, "%d",fileStat.st_uid);
	       	sprintf(arch.ar_gid, "%d",fileStat.st_gid);
	       	sprintf(arch.ar_mode, "%d",fileStat.st_mode);
	       	sprintf(arch.ar_size, "%d",fileStat.st_size);
	       	sprintf(arch.ar_fmag, "%s", ARFMAG);
    
		
       		numRead = read(fd2, buffer, MAX_READ);
       		if (numRead == -1){
		        perror("read");
       		}
      	      	buffer[numRead] = '\0';			   
       		written = write(fd1,arch.ar_name,sizeof(arch.ar_name));
       		written = write(fd1,arch.ar_date,sizeof(arch.ar_date));
       		written = write(fd1,arch.ar_uid,sizeof(arch.ar_uid));
       		written = write(fd1,arch.ar_gid,sizeof(arch.ar_gid));
       		written = write(fd1,arch.ar_mode,sizeof(arch.ar_mode));
       		written = write(fd1,arch.ar_size,sizeof(arch.ar_size));
       		written = write(fd1,arch.ar_fmag,sizeof(arch.ar_fmag));

       		if(written == -1){
       			perror("write");
       		}
		
       		written = write(fd1, buffer, numRead);
		
       		if(written == -1){
       			perror("write");
       		}
		
       	}	    
	
}



 
void shortCont(int numArgs, char **args)
{

	struct ar_hdr new;
	int hdrSize = sizeof(new);
       	int nameSize = sizeof(new.ar_name);
       	int toSize = 32;
       	int toEnd = 2;
       	int input, name, name2, seeker, numRead;
       	char buffer[1024];
       	off_t offset;
       	int x;
       	int numFiles = 10;
	
	

       	input = open(args[2], O_RDWR);
       	if(input == -1){
       		perror("open");
       	}
  
       	numRead = read(input, buffer, SARMAG);
       	if(numRead == -1){    
       		perror("lseek1");
       	}
 
       	buffer[numRead] = '\0';

       	if(strcmp(ARMAG, buffer) != 0){
       		printf("Not a valid archive file\n");
       	}
 

       	for(x = 1; x <= numFiles; x++){
  
       		name = read(input, buffer, nameSize);
       		if(name == -1){
       			perror("read");
       		}
       		buffer[name] = '\0';
       		printf("%s\n", buffer);
    
       		seeker = lseek(input, toSize, SEEK_CUR);
       		if(offset == -1){
       			perror("Offset");
       		}
	
       		name = read(input, buffer, sizeof(new.ar_size));
       		if(name2 == -1){
       			perror("lseek");
       		
       		}
       		int i = atoi(buffer);
       		seeker = lseek(input, i+sizeof(new.ar_fmag), SEEK_CUR);
       		if(seeker == -1){
       			perror("lseek");
		}
       	}
}

void extract(int numArgs, char **args)
{
       	char buffer[1024];
       	int i, input, numRead, newFile, sizeSeek, endSeek, written;
       	int numFiles = 3;
       	struct ar_hdr new;
       	int offset;

  //open the archive file
 
       	input = open(args[2], O_RDWR);
       	if(input == -1){
       		perror("open");
       	}

  //validate the archive file by checking the magic string

       	numRead = read(input, buffer, SARMAG);
       	if(numRead == -1){    
       	       	perror("lseek1");
       	}
       	buffer[numRead] = '\0';
  
       	if(strcmp(ARMAG, buffer) != 0){
       	printf("Not a valid archive file\n");
       	}
  
  /*read the header to get the name of each file in the archive.
   *then use each name read to create a new file in the directory
   *then seeks to the size in hdr and stores it can be used to write
   *that many bytes to the new file.
   */
       	for(i = 1; i <= numFiles; i++){
       		numRead = read(input, buffer, sizeof(new.ar_name));
      
       		if(numRead == -1){
       			perror("numRead");
       		}
       		buffer[numRead] = '\0';
         
	       	newFile = open(buffer, O_CREAT | O_RDWR, S_IRWXU | S_IRWXO);
	       	if(newFile == -1){
	       		perror("NewFile");
	       	}
	       	//seek to size field.
	       	sizeSeek = lseek(input, 32 , SEEK_CUR);
	       	if(sizeSeek == -1){
	       		perror("sizeSeek");
	       	}
      
       		numRead = read(input, buffer, sizeof(new.ar_size));
       		if(numRead == -1){
       			perror("numRead2");
       		}      
      
       		int fsize = atoi(buffer);
	
       		endSeek = lseek(input, sizeof(new.ar_fmag), SEEK_CUR);
       		if(endSeek == -1){
       			perror("endSeek");
       		}
       		numRead = read(input, buffer, fsize);
       		if(numRead == -1){
       			perror("numRead3");
       		}
       		printf("%d\n",numRead);
       		written = write(newFile,buffer,numRead);
		if(written == -1){
		       	perror("written");
      	      	}
       	}
}

int getNumRecs(int num)
{
  return num;
}

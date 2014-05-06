#include <mpi.h>

#ifndef MPIDEF
#define MPIDEF
#include"mpiDef.h"
#endif
#ifndef VECTOR
#define VECTOR
#include<vector>
#endif
#ifndef IOSTREAM
#define IOSTREAM
#include<iostream>
#endif
#ifndef CSTDIO
#define CSTDIO
#include<cstdio>
#endif
#include <omp.h>
#define FOR(I,T) for(int I=0;I<T;I++)
using namespace std;
void master_function(){
  	char processor_name[MPI_MAX_PROCESSOR_NAME];
	int name_len;
	MPI_Get_processor_name(processor_name, &name_len);
  	input();
  	//printf("Input from processor %s, rank %d out of %d processors\n",processor_name, world_rank, world_size);
  	convert();
  	//printf("convert world from processor %s, rank %d out of %d processors\n",processor_name, world_rank, world_size);
  }
int main(int argc, char** argv) 
{
  
  MPI_Init(NULL, NULL);
  int world_rank;
  int num_elements_per_proc;
  num_elements_per_proc=(DIV+2)*N;
  MPI_Comm_rank(MPI_COMM_WORLD, &world_rank);
  int world_size;
  MPI_Comm_size(MPI_COMM_WORLD, &world_size);
  if(world_rank==0){
	//cout<<endl<<"send"<<num_elements_per_proc*P<<"Rec"<<num_elements_per_proc<<endl;
  	master_function();//populatesAdvAdjList from input 
    }
  MPI_Scatter(AdvAdjList, num_elements_per_proc, MPI_INT, subAdvAdjList,num_elements_per_proc, MPI_INT, 0, MPI_COMM_WORLD);
 
do{
	initSubDest();
	#pragma omp parallel
	{
	#pragma omp for
 	FOR(src,N)
	{
	  	int n=subAdvAdjList[src][0];
	  	int k=subAdvAdjList[src][1];  	
		#pragma omp critical	  	
		FOR(i,k)
		{
			int div;
			if(N%P==0) div = N/P;
        		else div = N/P + 1;
			int index = subAdvAdjList[src][i+2];
	  		subDest[index%div]+=source[src]/n;
	  	}
  	}
	#pragma omp for
  	FOR(d,DIV)
	{
		#pragma omp critical
		{
	  		subDest[d] = CONV*subDest[d]+(1-CONV)/N;
		}		
		//cout<<"Process "<<world_rank<<":"<<subDest[d]<<"subDest["<<d<<"]"<<endl;
  	}
	}
	MPI_Barrier( MPI_COMM_WORLD);
	MPI_Allgather(&subDest, DIV, MPI_INT,dest, DIV, MPI_INT,MPI_COMM_WORLD);
	
        residual = calResidual(source,dest); 
  	copySourceDestination();
  	MPI_Barrier( MPI_COMM_WORLD); 
  }while(residual > E);
  
  MPI_Finalize();

}  


Module 0: sequential
Execution:
compile- 
g++ random.cpp
g++ -fopenmp openmp.cpp
Run the program with following command: time ./a.out

Module 1: openmp
Execution:
compile- 
g++ random.cpp
g++ -fopenmp openmp.cpp
Run the program with following command: time ./a.out
Number of threads can be changed by changing the line 5 from graphDef.cpp
#define P N 
where N is the Number of threads.

Module 2: mpi
Execution: 
Edit number of processors "N" in mpiDef.h
Compile- mpic++ mpi.cpp
Run the program with following command: time mprun -np N -host {,} ./a.out
For example: mpirun -np 3 -host 172.18.4.103,172.18.4.119,172.18.4.46 ./a.out

Module 3: hybrid
Execution: 
Edit number of processors "N" in mpiDef.h
Compile- mpic++ -fopenmp hybrid.cpp
Run the program with following command: time mprun -np N -host {,}./a.out
For example: mpirun -np 3 -host 172.18.4.103,172.18.4.119,172.18.4.46 ./a.out



Input format-
Input.txt
Each line corresponds to each source node, same representation of adjacency matrix as given in the paper. Input.txt is randomly generated.

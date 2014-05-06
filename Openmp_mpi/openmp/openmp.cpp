#include<cstdlib>
#include"graphDef.h"
#include<iostream>
#include<cstdio>
#include<cmath>
#include<time.h>
#include<omp.h>
#define FOR(I,T) for(int I=0;I<T;I++)
using namespace std;
clock_t start, end;
double time_used;


void input()
{
    FILE* fp = fopen("input.txt","r");
    FOR(s,N)
    {
        FOR(i,N)
        {
            int b;
            fscanf(fp,"%d",&b);
            if(b==1) AdjList[s].push_back(i);
        }
    }
}

double calResidual(vector<double> one, vector<double> two)
{
//    #pragma omp parallel
    
    double sum = 0;
        FOR(i,N)
        { 
            sum += (one[i]-two[i])*(one[i]-two[i]);
        }
//        printf("%lf\n",sqrt(sum));
    return sqrt(sum);
}
    
int main()
{
   // P = atoi(argv[1]);
    omp_set_num_threads(P);
    start = clock();
    input(); 
    residual = calResidual(source,dest);
    int iter = 0;
    while(residual > E)
    {
        iter++;
        
        #pragma omp parallel
        {
            #pragma omp for
                FOR(i,N)
                {
                    dest[i]=0;
                }
     //       #pragma omp barrier 
            #pragma omp for
            FOR(src,N)
            {
                int n = AdjList[src].size();
                #pragma omp critical
                FOR(j,n)
                {
                    dest[AdjList[src][j]] = dest[AdjList[src][j]] + source[src]/n;
                }
            }

       //     #pragma omp barrier 
            #pragma omp for
            FOR(d,N)
            {
                #pragma omp critical
                dest[d] = CONV*dest[d]+(1-CONV)/N;
            }
           // #pragma omp barrier 
        }        
            
            residual = calResidual(source,dest);
         
        #pragma omp parallel
        { 
            #pragma omp for
                FOR(i,N)
                {
              //      #pragma omp critical
                    source[i] = dest[i];
                }
        }

        
    }

    return 0;
}

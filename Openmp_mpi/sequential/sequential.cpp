#include<stdlib.h>
#include"graphDef.h"
#include<iostream>
#include<cstdio>
#include<cmath>
#include<time.h>
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
    double sum = 0;
        FOR(i,N)
        { 
            sum += (one[i]-two[i])*(one[i]-two[i]);
        }
        printf("%lf\n",sqrt(sum));
    return sqrt(sum);
}

int main()
{
    start = clock();
    input();
    residual = calResidual(source,dest);
    int iter = 0;

    while(residual > E)
    {
        
        FOR(i,N) dest[i]=0;
        FOR(src,N)
        {
            int n = AdjList[src].size();
            FOR(j,n)
            {
                dest[AdjList[src][j]] = dest[AdjList[src][j]] + source[src]/n;
            }
        }
        
        FOR(d,N) dest[d] = CONV*dest[d]+(1-CONV)/N;

        residual = calResidual(source,dest);
        
        FOR(i,N) source[i] = dest[i];
        
        iter ++;
    }
    end = clock();
    time_used = ((double)(end-start));
    return 0;
}

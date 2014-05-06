#include<iostream>
#include<time.h>
#include<stdlib.h>
#include<stdio.h>
#include"graphDef.h"
using namespace std;

int main()
{
    FILE* fp = fopen("input.txt","w");
    srand ( time(NULL) );
    for(int i=0;i<N;i++)
    {
        for (int t=0;t<N;t++)
        {
            int temp;
            temp = rand() % 100;
            if(temp%2==0) fprintf(fp,"%d ",1);
            else fprintf(fp,"%d ",0);
        }
        fprintf(fp,"\n");
    }
}

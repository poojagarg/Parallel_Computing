#ifndef M
#define M
#include<vector>
#define N 1000 
#define P 9
#define E 0.000001
#define CONV 0.85
#define DIV N/P
#define FOR(I,T) for(int I=0;I<T;I++)

using namespace std;

double residual;

vector<double> source(N,1.0/N);
vector<double> dest(N);
vector<vector<int> > AdjList(N);

int AdvAdjList[DIV+2][N*P];
//vector<pair<int,vector<int> > > AdvAdjList[P+1];
#endif

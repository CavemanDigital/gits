
//David Saraydar
//41485111
//1079

#include <iostream>
#include <fstream>
#include <iomanip>
#include "hw1prob1.h"

using namespace std;

int main()
{
   int **a;
   int rowLength;
   ifstream File;
   ofstream fOut;
   fOut.open("matOut.txt");
   File.open("matInp.txt");
	if(!File){
		exit(1);
	}
   File>>rowLength;
   int rowSize[rowLength];
   for(int i=0; i<rowLength; i++){
	File>>rowSize[i];
   }
   makeGen2dArray(a,rowLength,rowSize);

   // assign values to all elements of the array
  for(int x=0; x<rowLength; x++){
	for(int y=0; y<rowSize[x]; y++){
		File>>a[x][y];
		fOut<<a[x][y]+5<<' ';
	}
	fOut<<'\n';
  }

  freeGen2dArray(a);
  File.close();
  fOut.close();
   return 0;
}

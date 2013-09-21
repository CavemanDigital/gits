//David Saraydar
//41485111
//1079

#ifndef HW1PROB1_H
#define HW1PROB1_H

#include <exception>

using namespace std;

template <class T>
void makeGen2dArray(T ** &x, int numberOfRows, int *rowSize)
{
          // create pointers for the rows
          x = new T * [numberOfRows];
          // get memory for each row
          for (int i = 0; i < numberOfRows; i++){
              x[i] = new T[rowSize[i]];
		  }
}

template <class T>
void freeGen2dArray(T ** &x){
	delete x;
}

#endif

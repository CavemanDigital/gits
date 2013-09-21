#include <iostream>
#include <fstream>
#include <vector>
#include "hw1prob2.h"

using namespace std;

int main()
{
	ifstream File;
	ofstream fOut;
	fOut.open("prob2Out.txt");
	File.open("prob2Input.txt");
	if(!File){
		exit(1);
	}
	int x;
	vector<int> vect;
	while(File>>x){
		vect.push_back(x);
	}
	vector<int> envect = addEven2OddLines(vect);
	for(int z=0; z<envect.size(); z++){
		fOut<<envect.at(z)<<'\n';
	}
	File.close();
	fOut.close();
	return 0;
}

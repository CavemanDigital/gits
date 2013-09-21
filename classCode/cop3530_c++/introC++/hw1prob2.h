//David Saraydar
//41485111
//1079

#ifndef HW1PROB2_H
#define HW1PROB2_H

#include <exception>

using namespace std;

vector<int> addEven2OddLines(vector<int> & vect){
	vector<int> envect;
	for(int x=0; x<vect.size(); x+=2){
		envect.push_back(vect.at(x)+vect.at(x+1));
	}
	return envect;
}

#endif

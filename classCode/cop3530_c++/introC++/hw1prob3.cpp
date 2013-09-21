//David Saraydar
//41485111
//1079

#include <string> 
#include <iostream>
#include <fstream>
#include <list>
#include <iomanip>

using namespace std;

int main()
{
	FILE * File;
	File = fopen("Dictionary.txt", "r");
	ofstream fOut;
	fOut.open("sortedDictionary.txt");
	if(!File){
		exit(1);
	}
	char line[50];
	list<string> myList;
	list<string>::iterator it;
	while(fgets(line, 100, File)!=NULL){
		myList.push_back(line);
	}
	myList.sort();
	for(it=myList.begin(); it!=myList.end(); ++it){
		fOut<<*it;
	}

	fOut.close();
	fclose(File);
	return 0;
}

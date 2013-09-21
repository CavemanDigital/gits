#include<iostream>

using namespace std;

int knapsack(){
			//initialize
			int capacity=0;
			cout<<"Capacity:";
			cin>>capacity;
			
			int item=0;
			cout<<"Number of Items available: ";
			cin>>item;
			
			int val[item+1];
			int size[item+1];
			char name[item+1];
			name[0]=' ';
			for(int l=1; l<item+1; l++){
				name[l]=(char)l+64;
			}
			for(int r=0; r<=item; r++){
				val[r]=0;
				size[r]=0;
			}
			for(int s=1; s<=item; s++){
				cout<<"Item "<<(char)(s+64)<<"'s size: ";
				cin>>size[s];
				cout<<"Item "<<(char)(s+64)<<"'s value: ";
				cin>>val[s];
			}
			int sack[item+1][capacity+1];
			for(int j=0; j<=item; j++){
				for(int k=0; k<=capacity; k++){
					sack[j][k]=0;
				}
			}
			//sort items by size
/* 				for (int p=1; p <=item; p++) {
					for (int l=0; l <=item-p; l++) {
						if (size[l] > size[l+1]) {
							int temps = size[l];  //swap size array
							size[l] = size[l+1];  
							size[l+1] = temps;
							int tempv = val[l];  //swap value array
							val[l] = val[l+1];  
							val[l+1] = tempv;
							int tempn = name[l];  //swap name array
							name[l] = name[l+1];  
							name[l+1] = tempn;
						}
					}
				} */
			cout<<"-----------------"<<endl;
			
			//calculation table
			for(int k=1; k<=item; k++){
				for(int i=1; i<=capacity; i++){
					if(size[k]<=i){ //becomes possible item fit
						if((val[k]+sack[k-1][i-size[k]])>=sack[k-1][i]){  //is it improvement?
							sack[k][i] = val[k]+sack[k-1][i-size[k]];  //item k added to bag of value sack[k-1][i-size[k]]
								// item at 
						}
						else  //its not, keep it as it was (no new item)
							sack[k][i] = sack[k-1][i];
					}
					else //not possible fit (no new item)
						sack[k][i] = sack[k-1][i];
				}
			}
			
			//print table
/*  			for(int j=1; j<=item; j++){
				cout<<name[j]<<"-";
				for(int k=1; k<=capacity; k++){
					cout<<sack[j][k];
					cout<<" ";
				}
				cout<<endl;
			} */

			
			//print options
			for(int q=1; q<=item; q++){
				cout<<"Item "<<name[q]<<"--> "<<"size: "<<size[q]<<" - value: "<<val[q]<<endl;
			}
			cout<<endl;
			
			//print items used
			char used[item];
			for(int w=0; w<item; w++){
				used[w]=' ';
			}
			int x=0;
			int m=item;
			int n=capacity;
			while(m>0){
				if(sack[m][n] > sack[m-1][n]){
					used[x++]=(char)m+64;
					n=n-size[m];
					m--;
				}
				else if(sack[m][n] == sack[m-1][n]){
					m--;
				}
			}
			cout<<"Items used:";
			for(int i=item; i>=0; i--){
				if(used[i] != ' '){
					cout<<" "<<used[i];
				}
			}
			cout<<endl;
			return sack[item][capacity];
}

int main(){
	int k = knapsack();
	cout<<"The Maximum value of the sack is: "<<k;
	return 0;
}
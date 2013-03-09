int rec(int x){
	if(x == 0)
		return 42;
		
	return rec(x - 1);
}

int main(){
	int x, y;
	x = 5;
	y = rec(x);
}
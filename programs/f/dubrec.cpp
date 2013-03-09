int wat(int x){
	return x- 1;
}

int rec(int x){
	if(x == 0)
		return 42;
		
	return rec(wat(x));
}

int main(){
	int i,j;
	i = 5;
	j = rec(i);
}
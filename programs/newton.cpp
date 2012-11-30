int main() {
	float a, x, result;
	a = 4.0;
	x = 1.0;
	while (x*x > a+0.0001 || x*x < a-0.0001 )
		x = (x + a/x)/2.0;
	result = x;
}

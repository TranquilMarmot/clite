int main ( ) {
    int n, i;
    float f;
    n = 3;
    i = 0;
    f = 1.0;
    while (i < n) {
        i = i + 1;
        f = f * i;  // implicit (float)i
    } 
}

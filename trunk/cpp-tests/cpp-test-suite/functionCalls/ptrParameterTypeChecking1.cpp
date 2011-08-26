//! Compile. Expect error matches ""
//                                TBD Error message pattern.

void f( double **p ) {
}

int main() {
        double a[10][10] ;
        f(a) ;
        return 0 ;
}


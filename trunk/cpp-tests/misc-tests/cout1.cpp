class myostream {
    private: int x ;
    public: class myostream& operator<<( long i) ;
} ;

myostream myout ;

int main(){
    myout << 42 ;
}

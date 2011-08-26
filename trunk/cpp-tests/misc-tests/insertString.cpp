
class istream {
  private: int status ;
  public: class istream& get( char& ) ;
} ;

// The following should be declared within the class
istream& operator>>( istream&, char* );

istream cin ;
int main() {

    char buf[100] ;
    char *str ;
    str = &buf[0] ;
    cin >> str ;
}
    
class istream {
  private: int status ;
  public: class istream& get( char& ) ;
  public: bool eof( ) ;
  public: bool fail( ) ;
} ;

// The following should be declared within the class
istream& operator>>( istream&, char&);
istream& operator>>( istream&, unsigned char&);
istream& operator>>( istream&, signed char&);
istream& operator>>( istream&, short& );
istream& operator>>( istream&, unsigned short& );
istream& operator>>( istream&, int& );
istream& operator>>( istream&, unsigned int& );
istream& operator>>( istream&, long& );
istream& operator>>( istream&, unsigned long& );
istream& operator>>( istream&, float& );
istream& operator>>( istream&, double& );
istream& operator>>( istream&, long double& );
istream& operator>>( istream&, char* );
istream& operator>>( istream&, bool& );

istream cin ;

class ostream {
   private: int status ;
   public: class ostream& put( char ) ;
} ;
// The following should be declared within the class
ostream& operator<<( ostream&, char ) ;
ostream& operator<<( ostream&, signed char ) ;
ostream& operator<<( ostream&, unsigned char ) ;
ostream& operator<<( ostream&, bool ) ;
ostream& operator<<( ostream&, short ) ;
ostream& operator<<( ostream&, unsigned short ) ;
ostream& operator<<( ostream&, int ) ;
ostream& operator<<( ostream&, unsigned int ) ;
ostream& operator<<( ostream&, long ) ;
ostream& operator<<( ostream&, unsigned long int) ;
ostream& operator<<( ostream&, float ) ;
ostream& operator<<( ostream&, double ) ;
ostream& operator<<( ostream&, long double ) ;
ostream& operator<<( ostream&, char* ) ;

ostream cout ;
static const char endl = '\n' ;
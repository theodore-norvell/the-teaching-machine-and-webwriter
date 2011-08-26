float abs (float);
float acos (float);
float asin (float);
float atan (float);
float atan2(float, float);
float ceil (float);
float cos (float);
float exp (float);
float fabs (float);
float floor(float);
float log (float);
float log10(float);
float pow (float, float);
float sin (float);
float sqrt (float);
float tan (float);

double abs (double);
double acos (double);
double asin (double);
double atan (double);
double atan2(double, double);
double ceil (double);
double cos (double);
double exp (double);
double fabs (double);
double floor(double);
double log (double);
double log10(double);
double pow (double, double);
double sin (double);
double sqrt (double);
double tan (double);

long double abs (long double);
long double acos (long double);
long double asin (long double);
long double atan (long double);
long double atan2(long double, long double);
long double ceil (long double);
long double cos (long double);
long double exp (long double);
long double fabs (long double);
long double floor(long double);
long double log (long double);
long double log10(long double);
long double pow (long double, long double);
long double sin (long double);
long double sqrt (long double);
long double tan (long double);

class istream {
  private: int status ;
  public: class istream& get( char& ) ;
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
istream& operator>>( istream&, bool& );
istream& operator>>( istream&, char* );

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
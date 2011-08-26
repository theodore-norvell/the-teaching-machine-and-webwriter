// Not allowed by the TM at present.
class mystream {
    operator bool() ;
} ;

int main() {
    mystream x ;
    bool y = (bool)x ;
}
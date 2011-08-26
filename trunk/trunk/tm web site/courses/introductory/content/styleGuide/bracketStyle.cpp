/*#H*/#include <iostream>
using namespace std;

void fee();
void fi();
void fo();
void fum();

int main(){
    fee();
    fee)
    fi();
    fi();
    fo();
    fo();
    fum();
    return 0;
}/*#DA*/
/** fee *************************************************************
* @descrip: sometimes called the K & R style (after Kernighan & Ritchie
*           who wrote one of the first definitive guides to C++).
*           
*           Reasonably compact. The closing bracket lines up with command
*           attached to the opening bracket.
*
**********************************************************************/
void fee(){
     cout << "fee ";
}


/** fi *************************************************************
* @descrip: In this style the opening bracket gets its own line.
*           Proponrents like the way the brackets themselves line up.
*           The least compact style.
*
**********************************************************************/
void fi()
{
     cout << "fi ";
}

/** fo *************************************************************
* @descrip: In this style all brackets appear at the end of lines. Very
*           compact but closing brackets don't line up with anything and
*           can be difficult to pair up.
*
**********************************************************************/
void fo() {
     cout << "fo "; }  


/** fum *************************************************************
* @descrip: Everything appears on a single line. Only used with very
*            short pieces of code. Lines should never be so long they
*            run off the right edge
*
**********************************************************************/
void fum() { cout << "fum "; }
    
     

//! run.
// This file has a CR CR LF sequence in it that throws off the line count
// and eventually crashes the TM.
// Be carefull editing the file so as not to "fix" the anomalous line ending.


int main(){
   // The offending CR CR LF (i.e. 0D 0D 0A) ends the next line

  return 0;
}
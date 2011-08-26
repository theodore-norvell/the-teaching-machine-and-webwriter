//! Compile; expect no error.
package parsertest;

/**
 * <p>Title: </p>
 * <p>Description: Test expression names and primary expressions</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

// The following 2 classes are from JLS (8.8.5.1)
class Outer {
    class Inner{}
}
class ChildOfInner extends Outer.Inner {
    ChildOfInner(){(new Outer()).super();}
}

class Enclosing extends Parent {
    class PrimaryTests extends Parent {

        void test() {
            // ExpressionNames
            n.j ++ ;
            n.j = n.j ++ ;
        
            // Array Creation Expressions
            a = new int[10][] ;
            a = new int[10][5] ;
            a = new int[][] {new int[0], new int[1]} ;
            b = new PrimaryTests.Nested[5] ;
            b = new Nested[] { new Nested(), new PrimaryTests.Nested() } ;
        
            // Literal
            i = 1 ;
            s = "hello" ;
            // .class
            c = void.class ;
            c = PrimaryTests.Nested.class ;
            c = PrimaryTests.class ;
            c = int[][].class ;
            c = Nested[][][].class ;
        
            // this
            k = this ;
            k = PrimaryTests.this ;
        
            // Parens
            i = (1) ;
            
            // Instance creation
            n = new Nested() ;
            n = new Nested() { int k ; } ;
            n = k.new Nested() ;
            n = (k).new Nested() ;
            n = k.new Nested() { int k ; } ;
            n = (k).new Nested() { int k ; } ;
            
            // Field Access
            k.i = n.j ;
            i = super.i ;
            i = Enclosing.super.i ;
            (k).i = (n).j ;
            
            // Method Invocation
            Enclosing.staticMethod() ;     // MethodName()
            k.test() ;                     // MethodName()
            (k).test() ;                   // Primary.Id()
            super.test() ;
            Enclosing.super.test() ;
        
            // ArrayAccess
            i = a[i][i] ;
            n = b[0] ;
            i = z()[0] ;
            // i = (a)[i][j] ; // Missinterpreted by JDK.
        }
        
        int a[][] ;
        Nested b[] ;
        int i ;
        String s ;
        Class c ;
        PrimaryTests k ;
        Nested n ;
        class Nested{
            int j ;
            void nestedMeth() {}
        }
        int [] z() { return a[0]; }
    }
    
    int i ;
    static void staticMethod() {} 
}

class Parent {
    int i ;   // Hidden in subclass
    void test() { } // Overridden in subclass
}

// Following is of questionable legality

class Outer3 {
    class Inner3{}
    static Outer3 outer ;
}
class ChildOfInner3 extends Outer3.Inner3 {
    ChildOfInner3(){Outer3.outer.super();}  // Syntax not allowed by JLS 2nd ed.
}
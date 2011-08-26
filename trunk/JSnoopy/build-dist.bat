if not exist "jsnoopy-dist" goto :afterrmdir
@ECHO OFF
ECHO *
ECHO * Answer Yes to the next question
ECHO *
ECHO ON

rmdir /S jsnoopy-dist

:afterrmdir

mkdir jsnoopy-dist

javac -g:lines -deprecation -d classes -classpath "D:\progfiles\junit3.7\junit.jar;D:\progfiles\JBuilder6\jdk1.3.1\jre\lib\rt.jar" src\jsnoopy\*.java src\jsnoopy\parser\*.java src\jsnoopy\swingui\*.java

cd classes
jar cf ..\jsnoopy-dist\jsnoopy.jar jsnoopy\*.class jsnoopy\parser\*.class jsnoopy\swingui\*.class
jar cf ..\\jsnoopy-dist\jsnoopy-minimal.jar jsnoopy\JSnoopy.class jsnoopy\Instrumentor.class jsnoopy\DummyInstrumentor.class jsnoopy\Assert.class jsnoopy\AssertException.class
cd ..

call jdoc.bat
xcopy doc\*.* jsnoopy-dist\doc\*.* /s/e

mkdir jsnoopy-dist\help
copy help\*.htm jsnoopy-dist\help


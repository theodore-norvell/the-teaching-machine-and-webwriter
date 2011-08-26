; files.nsi
; Various files needed by the TM

File "tm\tm-dist\tm.jar"
/* Really the initial.tmcfg file should be copied
   to the "Application Data" folder of each user.
   However, I have no idea how to do that.  So we just
   put it in the installation directory.
*/
File "tm\tm-dist\initial.tmcfg"
File /r "tm\tm-dist\help"
File /r "Examples-for-distribution"

/* PlugIn stuff */
File "tmEclipsePlugIn-2\plugins\*.jar"
File "tmEclipsePlugIn-2\Eclipse-plug-in-instructions.txt"

DetailPrint "Creating .bat file."

ClearErrors
FileOpen $0 tm.bat "w"
IfErrors 0 +3
    DetailPrint "Error on Open of .bat file"
    Abort

ClearErrors
FileWrite $0 '"$JREPath" -cp "$INSTDIR\tm.jar;%CLASSPATH%" tm.TMMainFrame -ic "$INSTDIR\initial.tmcfg" -id "$INSTDIR" %*'
IfErrors 0 +3
    DetailPrint "Error on Write to .bat file"
    Abort

ClearErrors
FileClose $0
IfErrors 0 +3
    DetailPrint "Error on Close of .bat file"
    Abort
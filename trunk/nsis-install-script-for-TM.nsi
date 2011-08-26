; basic script template for NSIS installers
;
; Written by Philip Chu, adapted for The Teaching Machine by Theodore Norvell
; Copyright (c) 2004-2005 Technicat, LLC
;
; This software is provided 'as-is', without any express or implied warranty.
; In no event will the authors be held liable for any damages arising from the use of this software.
 
; Permission is granted to anyone to use this software for any purpose,
; including commercial applications, and to alter it ; and redistribute
; it freely, subject to the following restrictions:
 
;    1. The origin of this software must not be misrepresented; you must not claim that
;       you wrote the original software. If you use this software in a product, an
;       acknowledgment in the product documentation would be appreciated but is not required.
 
;    2. Altered source versions must be plainly marked as such, and must not be
;       misrepresented as being the original software.
 
;    3. This notice may not be removed or altered from any source distribution.
 
!define setup "setup-tm.exe"
 
; change this to wherever the files to be packaged reside
!define srcdir "."
 
!define company "MUN"
 
!define prodname "The Teaching Machine"
!define exec "tm.bat"
!define website http://www.engr.mun.ca/~theo/TM/index.html
 
; optional stuff

!define helpfile "help\help.html"
!define manualfile "help\InstructorsManual.pdf"
 
; text file to open in notepad after installation
; !define notefile "README.txt"
 
; license text file
!define licensefile tm\src\LICENCE
 
; icons must be Microsoft .ICO files
!define icon tm-logo.ico
 
; installer background screen
; !define screenimage background.bmp
 
; file containing list of file-installation commands
!define files "files.nsi"
 
; file containing list of file-uninstall commands
; !define unfiles "unfiles.nsi"
 
; registry stuff
 
!define regkey "Software\${company}\${prodname}"
!define uninstkey "Software\Microsoft\Windows\CurrentVersion\Uninstall\${prodname}"
 
!define startmenu "$SMPROGRAMS\${prodname}"
!define uninstaller "uninstall.exe"
 
;--------------------------------
 
XPStyle on
ShowInstDetails hide
ShowUninstDetails hide
 
Name "${prodname}"
Caption "${prodname}"
 
!ifdef icon
Icon "${icon}"
!endif
 
OutFile "${setup}"
 
SetDateSave on
SetDatablockOptimize on
CRCCheck on
SilentInstall normal
 
InstallDir "$PROGRAMFILES\${company}\${prodname}"
InstallDirRegKey HKLM "${regkey}" ""
 
!ifdef licensefile
LicenseText "License"
LicenseData "${srcdir}\${licensefile}"
!endif
 
; pages
; we keep it simple - leave out selectable installation types
 
!ifdef licensefile
Page license
!endif
 
;;Page components
Page directory
Page instfiles
 
UninstPage uninstConfirm
UninstPage instfiles
 
;--------------------------------
 
AutoCloseWindow false
ShowInstDetails show
 
 
!ifdef screenimage
 
; set up background image
; uses BgImage plugin
 
Function .onGUIInit
	; extract background BMP into temp plugin directory
	InitPluginsDir
	File /oname=$PLUGINSDIR\1.bmp "${screenimage}"
 
	BgImage::SetBg /NOUNLOAD /FILLSCREEN $PLUGINSDIR\1.bmp
	BgImage::Redraw /NOUNLOAD
FunctionEnd
 
Function .onGUIEnd
	; Destroy must not have /NOUNLOAD so NSIS will be able to unload and delete BgImage before it exits
	BgImage::Destroy
FunctionEnd
 
!endif

;; Check the JRE

Var JREPath
Section ; Search for JRE Put the location of the java.exe file in $JREPath
    Push "1.2.0" ; Minimum JRE version required.
    Call DetectJRE
    Pop $0	  ; DetectJRE's return value
    
    StrCpy $1 "The Java Runtime environment could not be found."
    StrCmp $0 "0" ExitInstallJRE 0
    StrCpy $1 "The Java Runtime environment is too old."
    StrCmp $0 "-1" ExitInstallJRE 0
    
    StrCpy $1  "The file '$0' can not be found."
    IfFileExists $0 +1 ExitInstallJRE
 
    StrCpy $JREPath "$0"
    Goto End
  
  ExitInstallJRE:
    StrCpy $JREPath "java.exe"
    MessageBox MB_YESNO "$1$\nIt is suggested that you$\n\
                         * stop this installation,$\n\
                         * install the Java Runtime Envirnment from SUN Microsystems,$\n\
                         * and then install the Teaching Machine.$\n\
                      Would you like to proceed" IDYES End IDNO +1
    Abort
    
  End:
    ; MessageBox MB_OK "JREPath is $JREPath"
    
SectionEnd

Section 
 
  MessageBox MB_YESNO "Install for all users?" IDYES allUsers IDNO currentUsers
  allUsers:
    SetShellVarContext all
    Goto endAllUsers
  currentUsers:
    SetShellVarContext current
  endAllUsers:

  WriteRegStr SHELL_CONTEXT "${regkey}" "Install_Dir" "$INSTDIR"
  ; write uninstall strings
  WriteRegStr SHELL_CONTEXT "${uninstkey}" "DisplayName" "${prodname} (remove only)"
  WriteRegStr SHELL_CONTEXT "${uninstkey}" "UninstallString" '"$INSTDIR\${uninstaller}"'
 


!ifdef icon
  WriteRegStr HKCR "${prodname}\DefaultIcon" "" "$INSTDIR\${icon}"
!endif
 
  SetOutPath $INSTDIR
 
 
; package all files, recursively, preserving attributes
; assume files are in the correct places
; File /a "${srcdir}\${exec}"
 
!ifdef licensefile
File /a "${srcdir}\${licensefile}"
!endif
 
!ifdef notefile
File /a "${srcdir}\${notefile}"
!endif
 
!ifdef icon
File /a "${srcdir}\${icon}"
!endif
 
; any application-specific files
!ifdef files
!include "${files}"
!endif
 
  WriteUninstaller "${uninstaller}"
  
SectionEnd
 
; create shortcuts
Section 
  
  CreateDirectory "${startmenu}"
  SetOutPath $INSTDIR ; for working directory

!ifdef icon
  CreateShortCut "${startmenu}\${prodname}.lnk" "$INSTDIR\${exec}" "" "$INSTDIR\${icon}"
  CreateShortCut "$DESKTOP\${prodname}.lnk" "$INSTDIR\${exec}" "" "$INSTDIR\${icon}"
!else
  CreateShortCut "${startmenu}\${prodname}.lnk" "$INSTDIR\${exec}"
  CreateShortCut "$DESKTOP\${prodname}.lnk" "$INSTDIR\${exec}"
!endif
 
!ifdef notefile
  CreateShortCut "${startmenu}\Release Notes.lnk "$INSTDIR\${notefile}"
!endif
 
!ifdef helpfile
  CreateShortCut "${startmenu}\Help.lnk" "$INSTDIR\${helpfile}"
!endif 

!ifdef manualfile
  CreateShortCut "${startmenu}\Manual.lnk" "$INSTDIR\${manualfile}"
!endif
 
!ifdef website
  ; WriteINIStr "${startmenu}\web site.url" "InternetShortcut" "URL" ${website}
  CreateShortCut "${startmenu}\Web Site.lnk" "${website}" "URL"
!endif
 
!ifdef notefile
ExecShell "open" "$INSTDIR\${notefile}"
!endif
 
SectionEnd

;; Add Associations ;;
;;;;;;;;;;;;;;;;;;;;;;
Section

; Add association for .java files. ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;  Code adapted from http://nsis.sourceforge.net/File_Association
;;;;;;;;;TODO Refactor into a subroutine ;;;;;;;;;;;;;
;;;;;;;;;TODO Uninstall. ;;;;;;;;;;;;;
!define Index "Line${__LINE__}"
  ReadRegStr $1 HKCR ".java" ""
  DetailPrint ".java files have type $1"
  
  ; If the .java suffix is unassociated, associate it with tm.cpp
  StrCmp $1 "" 0 "${Index}-NoChange"
    StrCpy $1 "tm.java"
    DetailPrint "Writing to .java $1"
    WriteRegStr HKCR ".java" "" "$1"
"${Index}-NoChange:"

  ; assert $1 contains the association for .java
  
  ; If the association is new, add a description.
  ReadRegStr $0 HKCR "$1" ""
  DetailPrint "$1 has value $0"
  StrCmp $0 "" 0 "${Index}-Skip"
    DetailPrint "Writing to $1 'Java Source File'"
	WriteRegStr HKCR "$1" "" "Java Source File"
"${Index}-Skip:"
  
  ; Add a context menu command.
  DetailPrint "Writing to $1\shell\openTM Open with the Teaching Machine"
  WriteRegStr HKCR "$1\shell\openTM" "" "Open with the Teaching Machine"
  DetailPrint 'Writing to $1\shell\openTM\command $INSTDIR\${exec} "%1"'
  WriteRegStr HKCR "$1\shell\openTM\command" ""     '$INSTDIR\${exec} "%1"'
  

  ; Ask about default action.
  MessageBox MB_YESNO "Change default viewer for .java files to the Teaching Machine?" \
       IDYES "${Index}-Set-default-action" IDNO "${Index}-Skip-default-action"
"${Index}-Set-default-action:"
	WriteRegStr HKCR "$1\shell" "" "openTM"
	WriteRegStr HKCR "$1\DefaultIcon" "" "$INSTDIR\${icon},0"
"${Index}-Skip-default-action:"
!undef Index

; Add association for .cpp files.  ;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;  Code adapted from http://nsis.sourceforge.net/File_Association
;;;;;;;;;TODO Refactor into a subroutine ;;;;;;;;;;;;;
;;;;;;;;;TODO Uninstall. ;;;;;;;;;;;;;

!define Index "Line${__LINE__}"
  ReadRegStr $1 HKCR ".cpp" ""
  DetailPrint ".cpp files have type $1"
  
  ; If the .cpp suffix is unassociated, associate it with tm.cpp
  StrCmp $1 "" 0 "${Index}-NoChange"
    StrCpy $1 "tm.cpp"
    DetailPrint "Writing to .cpp $1"
    WriteRegStr HKCR ".cpp" "" "$1"
"${Index}-NoChange:"

  ; assert $1 contains the association for .cpp
  
  ; If the association is new, add a description.
  ReadRegStr $0 HKCR "$1" ""
  DetailPrint "$1 has value $0"
  StrCmp $0 "" 0 "${Index}-Skip"
    DetailPrint "Writing to $1 'C++ Source File'"
	WriteRegStr HKCR "$1" "" "C++ Source File"
"${Index}-Skip:"
  
  ; Add a context menu command.
  DetailPrint "Writing to $1\shell\openTM Open with the Teaching Machine"
  WriteRegStr HKCR "$1\shell\openTM" "" "Open with the Teaching Machine"
  DetailPrint 'Writing to $1\shell\openTM\command $INSTDIR\${exec} "%1"'
  WriteRegStr HKCR "$1\shell\openTM\command" ""     '$INSTDIR\${exec} "%1"'
  
  ; Ask about default action.
  MessageBox MB_YESNO "Change default viewer for .cpp files to the Teaching Machine?" IDYES "${Index}-Set-default-action" IDNO "${Index}-Skip-default-action"
"${Index}-Set-default-action:"
	WriteRegStr HKCR "$1\shell" "" "openTM"
	WriteRegStr HKCR "$1\DefaultIcon" "" "$INSTDIR\${icon},0"
"${Index}-Skip-default-action:"

!undef Index

; Notify the OS. ;
;;;;;;;;;;;;;;;;;;
  System::Call 'Shell32::SHChangeNotify(i 0x8000000, i 0, i 0, i 0)'
SectionEnd

Section

  MessageBox MB_OK "Do you use Eclipse?$\n\
             See the instructions in the installation directory$\n\
             ($INSTDIR)$\n\
             on installing the Teaching Machine Plug-In for Eclipse."
  
  StrCpy $0 ""
  StrCmp "$JREPath" "java.exe" 0 DoneWarning
      StrCpy $0 "$\n\ 
                 Because the Java Runtime Environment could not be found,$\n\
                 the Teaching Machine may not launch. Here are some$\n\
                 ways to resolve the problem$\n\
                 * Install the Java Runtime Environment and then reinstall$\n\
                   the Teaching Machine.$\n\
                 * Find a java.exe program on your system and alter the$\n\
                   PATH environment variable to include the directory that$\n\
                   program is contained in.$\n\
                 * Edit the tm.bat file in directory$\n\
                 '$INSTDIR'$\n\
                 so that it uses another java interpreter."
  DoneWarning:
  
  MessageBox MB_OK "Installation Complete $0"
  
SectionEnd
 
; Uninstaller
; All section names prefixed by "Un" will be in the uninstaller
 
UninstallText "This will uninstall ${prodname}."
 
!ifdef icon
UninstallIcon "${icon}"
!endif
 
Section "Uninstall"
 
  DeleteRegKey HKLM "${uninstkey}"
  DeleteRegKey HKLM "${regkey}"
  
  
  DeleteRegKey HKCR "*\shell\openTM\command"
  ;;
  ;; The following lines don't seem to work.
  Delete "${startmenu}\*.*"
  RmDir "${startmenu}"
 
!ifdef licensefile
Delete "$INSTDIR\${licensefile}"
!endif
 
!ifdef notefile
Delete "$INSTDIR\${notefile}"
!endif
 
!ifdef icon
Delete "$INSTDIR\${icon}"
!endif
 
Delete "$INSTDIR\${exec}"
 
!ifdef unfiles
!include "${unfiles}"
!endif

RmDir /r "$INSTDIR"
 
SectionEnd

;; DetectJRE  ;;
;;;;;;;;;;;;;;;;
;; From http://nsis.sourceforge.net/New_installer_with_JRE_check_%28includes_fixes_from_%27Simple_installer_with_JRE_check%27_and_missing_jre.ini%29

Function DetectJRE
    Exch $0	; Get version requested  
  		; Now the previous value of $0 is on the stack, and the asked for version of JDK is in $0
    Push $1	; $1 = Java version string (ie 1.5.0)
    Push $2	; $2 = Javahome
    Push $3	; $3 and $4 are used for checking the major/minor version of java
    Push $4
    ;MessageBox MB_OK "Detecting JRE"
    ReadRegStr $1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
    ;MessageBox MB_OK "Read : $1"
    StrCmp $1 "" DetectTry2
    ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$1" "JavaHome"
    ;MessageBox MB_OK "Read 3: $2"
    StrCmp $2 "" DetectTry2
    Goto GetJRE
   
  DetectTry2:
    ReadRegStr $1 HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion"
    ;MessageBox MB_OK "Detect Read : $1"
    StrCmp $1 "" NoFound
    ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$1" "JavaHome"
    ;MessageBox MB_OK "Detect Read 3: $2"
    StrCmp $2 "" NoFound
   
  GetJRE:
  ; $0 = version requested. $1 = version found. $2 = javaHome
    ;MessageBox MB_OK "Getting JRE"
    IfFileExists "$2\bin\java.exe" 0 NoFound
    StrCpy $3 $0 1			; Get major version. Example: $1 = 1.5.0, now $3 = 1
    StrCpy $4 $1 1			; $3 = major version requested, $4 = major version found
    ;MessageBox MB_OK "Want $3 , found $4"
    IntCmp $4 $3 0 FoundOld FoundNew
    StrCpy $3 $0 1 2
    StrCpy $4 $1 1 2			; Same as above. $3 is minor version requested, $4 is minor version installed
    ;MessageBox MB_OK "Want $3 , found $4" 
    IntCmp $4 $3 FoundNew FoundOld FoundNew
   
  NoFound:
    ;MessageBox MB_OK "JRE not found"
    Push "0"
    Goto DetectJREEnd
   
  FoundOld:
    ;MessageBox MB_OK "JRE too old: $3 is older than $4"
    Push "-1"
    Goto DetectJREEnd  
  FoundNew:
    ;MessageBox MB_OK "JRE is new: $3 is newer than $4"
   
     Push "$2\bin\java.exe"
     Goto DetectJREEnd
     
  DetectJREEnd:
  	; Top of stack is return value, then r4,r3,r2,r1
  	Exch	; => r4,rv,r3,r2,r1,r0
  	Pop $4	; => rv,r3,r2,r1r,r0
  	Exch	; => r3,rv,r2,r1,r0
  	Pop $3	; => rv,r2,r1,r0
  	Exch 	; => r2,rv,r1,r0
  	Pop $2	; => rv,r1,r0
  	Exch	; => r1,rv,r0
  	Pop $1	; => rv,r0
  	Exch	; => r0,rv
  	Pop $0	; => rv 
  FunctionEnd
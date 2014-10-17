# Microsoft Developer Studio Project File - Name="w1" - Package Owner=<4>
# Microsoft Developer Studio Generated Build File, Format Version 6.00
# ** DO NOT EDIT **

# TARGTYPE "Win32 (x86) Application" 0x0101

CFG=w1 - Win32 Debug
!MESSAGE This is not a valid makefile. To build this project using NMAKE,
!MESSAGE use the Export Makefile command and run
!MESSAGE 
!MESSAGE NMAKE /f "w1.mak".
!MESSAGE 
!MESSAGE You can specify a configuration when running NMAKE
!MESSAGE by defining the macro CFG on the command line. For example:
!MESSAGE 
!MESSAGE NMAKE /f "w1.mak" CFG="w1 - Win32 Debug"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "w1 - Win32 Release" (based on "Win32 (x86) Application")
!MESSAGE "w1 - Win32 Debug" (based on "Win32 (x86) Application")
!MESSAGE "w1 - Win32 release1" (based on "Win32 (x86) Application")
!MESSAGE 

# Begin Project
# PROP AllowPerConfigDependencies 0
# PROP Scc_ProjName ""
# PROP Scc_LocalPath ""
CPP=cl.exe
MTL=midl.exe
RSC=rc.exe

!IF  "$(CFG)" == "w1 - Win32 Release"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 0
# PROP BASE Output_Dir "Release"
# PROP BASE Intermediate_Dir "Release"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 0
# PROP Output_Dir "Release"
# PROP Intermediate_Dir "Release"
# PROP Target_Dir ""
# ADD BASE CPP /nologo /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MBCS" /YX /FD /c
# ADD CPP /nologo /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MBCS" /YX /FD /c
# ADD BASE MTL /nologo /D "NDEBUG" /mktyplib203 /win32
# ADD MTL /nologo /D "NDEBUG" /mktyplib203 /win32
# ADD BASE RSC /l 0x409 /d "NDEBUG"
# ADD RSC /l 0x409 /d "NDEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:windows /machine:I386
# ADD LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:windows /machine:I386

!ELSEIF  "$(CFG)" == "w1 - Win32 Debug"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 1
# PROP BASE Output_Dir "Debug"
# PROP BASE Intermediate_Dir "Debug"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 1
# PROP Output_Dir "Debug"
# PROP Intermediate_Dir "Debug"
# PROP Ignore_Export_Lib 0
# PROP Target_Dir ""
# ADD BASE CPP /nologo /W3 /Gm /GX /ZI /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /D "_MBCS" /YX /FD /GZ /c
# ADD CPP /nologo /W3 /Gm /GX /ZI /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /D "_MBCS" /YX /FD /GZ /c
# ADD BASE MTL /nologo /D "_DEBUG" /mktyplib203 /win32
# ADD MTL /nologo /D "_DEBUG" /mktyplib203 /win32
# ADD BASE RSC /l 0x409 /d "_DEBUG"
# ADD RSC /l 0x409 /d "_DEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:windows /debug /machine:I386 /pdbtype:sept
# ADD LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib comctl32.lib /nologo /subsystem:windows /debug /machine:I386 /pdbtype:sept

!ELSEIF  "$(CFG)" == "w1 - Win32 release1"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 0
# PROP BASE Output_Dir "w1___Win32_release1"
# PROP BASE Intermediate_Dir "w1___Win32_release1"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 0
# PROP Output_Dir "w1___Win32_release1"
# PROP Intermediate_Dir "w1___Win32_release1"
# PROP Target_Dir ""
# ADD BASE CPP /nologo /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MBCS" /YX /FD /c
# ADD CPP /nologo /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MBCS" /YX /FD /c
# ADD BASE MTL /nologo /D "NDEBUG" /mktyplib203 /win32
# ADD MTL /nologo /D "NDEBUG" /mktyplib203 /win32
# ADD BASE RSC /l 0x409 /d "NDEBUG"
# ADD RSC /l 0x409 /d "NDEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:windows /machine:I386
# ADD LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:windows /machine:I386

!ENDIF 

# Begin Target

# Name "w1 - Win32 Release"
# Name "w1 - Win32 Debug"
# Name "w1 - Win32 release1"
# Begin Group "Source Files"

# PROP Default_Filter "cpp;c;cxx;rc;def;r;odl;idl;hpj;bat"
# Begin Source File

SOURCE=.\a1.c
# End Source File
# Begin Source File

SOURCE=.\dlgfns.c
# End Source File
# Begin Source File

SOURCE=.\fns.c
# End Source File
# Begin Source File

SOURCE=.\game.c
# End Source File
# Begin Source File

SOURCE=.\view.c
# End Source File
# End Group
# Begin Group "Header Files"

# PROP Default_Filter "h;hpp;hxx;hm;inl"
# Begin Source File

SOURCE=.\defins.h
# End Source File
# Begin Source File

SOURCE=.\fns.h
# End Source File
# Begin Source File

SOURCE=.\resource.h
# End Source File
# Begin Source File

SOURCE=.\structs.h
# End Source File
# End Group
# Begin Group "Resource Files"

# PROP Default_Filter "ico;cur;bmp;dlg;rc2;rct;bin;rgs;gif;jpg;jpeg;jpe"
# Begin Source File

SOURCE=.\cards\010.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\011.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\012.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\013.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\020.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\021.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\022.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\023.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\030.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\031.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\032.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\033.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\040.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\041.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\042.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\043.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\050.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\052.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\053.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\060.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\061.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\062.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\063.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\070.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\071.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\072.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\073.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\080.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\081.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\082.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\083.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\090.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\091.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\092.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\093.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\10.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\100.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\101.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\102.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\103.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\110.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\111.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\112.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\113.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\120.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\121.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\122.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\123.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\130.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\131.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\132.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\133.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\20.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\30.bmp
# End Source File
# Begin Source File

SOURCE=.\cards\bitmap26.bmp
# End Source File
# Begin Source File

SOURCE=.\bitmap5.bmp
# End Source File
# Begin Source File

SOURCE=.\bitmap8.bmp
# End Source File
# Begin Source File

SOURCE=.\Clouds.bmp
# End Source File
# Begin Source File

SOURCE=.\Forest.bmp
# End Source File
# Begin Source File

SOURCE=.\icon1.ico
# End Source File
# Begin Source File

SOURCE=".\Metal Links.bmp"
# End Source File
# Begin Source File

SOURCE=.\player1.bmp
# End Source File
# Begin Source File

SOURCE=.\player2.bmp
# End Source File
# Begin Source File

SOURCE=.\R1.rc
# End Source File
# Begin Source File

SOURCE=.\untitled1.bmp
# End Source File
# End Group
# End Target
# End Project

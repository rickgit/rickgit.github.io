set fileFullName="%1"
javac %fileFullName%

set classFullName=%fileFullName:.java=.class%

dx --dump --debug %classFullName% > TempFile_dx.java
pause
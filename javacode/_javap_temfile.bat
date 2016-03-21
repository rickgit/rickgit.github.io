set fileFullName="%1"
javac %fileFullName%

set classFullName=%fileFullName:.java=.class%
javap -v -p %classFullName% > TempFile_javap.java
## pause
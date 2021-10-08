curl  https://services.gradle.org/distributions/ |\
    grep -E 'all.zip</span>$'| \
    grep -v rc |\
    head -10 |\
     sed 's/^.*"name">//g'|\
     sed 's/<\/span>$//g' |\
     awk '{printf  "curl --location https://services.gradle.org/distributions/%s >%s --progress \n",$0,$0}'
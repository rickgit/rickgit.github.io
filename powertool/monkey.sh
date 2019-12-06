#!/bin/bash
# -*- encoding: utf8 -*-
###############################################################
##
## Name:		Monkey Script
## Version:		1.0 2013.09.03
## Version:		1.1 2013.09.05
## Version:		1.2 2013.09.06
## Author:		mcxiaoke
##
## https://gist.github.com/mcxiaoke/8971659
###############################################################
function check_device()
{
	DEVICE=`adb devices | awk 'NR>1 {print $1}'`
	if [ -z ${DEVICE} ]; then
		#echo "No device connected, quit."
		#exit
		return 1
	else
		return 0
	fi
	return ${DEVICE}
}
function run_monkey()
{
	EVENTS=90000000
	if [ $DEBUG == true ];then
		EVENTS=20	
	fi
	THROTTLE=200
	SEED=`date "+%s"`
	DATE=`date "+%Y%m%d"`
	DIR=logs/${DATE}
	FILE=${DIR}.txt
	TIME=`date "+%H%M%S"`
	LOG_DIR=${DIR}/${TIME}

	mkdir -p ${LOG_DIR}
	echo "Seed:" ${SEED}
	echo "Events:" ${EVENTS}
	echo "Logs:" ${LOG_DIR}

	CMD="adb -s ${DEVICE} shell monkey -p ${PACKAGE} -s ${SEED} --hprof --throttle ${THROTTLE} -v -v -v ${EVENTS} -pct-touch 60% --pct-motion 20% --pct-anyevent 20% --ignore-security-exceptions --kill-process-after-error --monitor-native-crashes >${LOG_DIR}/monkey.txt"
	echo ${CMD}>${LOG_DIR}/cmd.txt
	echo "Monkey command: (\""${CMD}\"\)
	echo "Monkey running..."
	adb -s ${DEVICE} shell monkey -p ${PACKAGE} -s ${SEED} --hprof --throttle ${THROTTLE} -v -v -v ${LOOP} -pct-touch 50% --pct-motion 20% --pct-anyevent 30% --ignore-security-exceptions --kill-process-after-error --monitor-native-crashes >${LOG_DIR}/monkey.txt
	echo "Monkey finished."

	echo "Collecting df..."
	adb -s ${DEVICE} shell df>${LOG_DIR}/df.txt
	echo "Collecting getprop..."
    adb -s ${DEVICE} shell getprop>${LOG_DIR}/prop.txt
	echo "Collecting traces..."
	adb -s ${DEVICE} shell cat /data/anr/traces.txt>${LOG_DIR}/traces.txt
	echo "Collecting cpuinfo..."
	adb -s ${DEVICE} shell cat /proc/cpuinfo>${LOG_DIR}/cpuinfo.txt
	echo "Collecting meminfo..."
	adb -s ${DEVICE} shell cat /proc/meminfo>${LOG_DIR}/meminfo.txt

	echo "Collecting dumpheap..."
	adb -s ${DEVICE} shell "am dumpheap ${PACKAGE} /mnt/sdcard/dumpheap.hprof"
	#adb -s ${DEVICE} pull /mnt/sdcard/dumpheap.hprof ${LOG_DIR}/dumpheap.hprof #git bash需要转化转化
	echo "MSYS_NO_PATHCONV=1 adb -s ${DEVICE} pull /mnt/sdcard/dumpheap.hprof ${LOG_DIR}/dumpheap.hprof" #git bash需要转化转化
	echo "Collecting dumpsys info..."
	adb -s ${DEVICE} shell dumpsys meminfo ${PACKAGE} >${LOG_DIR}/dumpmeminfo.txt
	if [ ${DEBUG} == false ];then
		echo "Collecting bugreport..."
		adb -s ${DEVICE} bugreport>>${LOG_DIR}/bugreport.txt	
	fi
	echo "Collecting fatal exceptions..."
	echo "---------------------------" >> ${FILE}
	echo "${TIME} Crashes: " >> ${FILE}
	grep  -A 10 -B 3 -inr FATAL ${LOG_DIR} >> ${FILE}
	echo " " >> ${FILE} 

}
adb devices
DEBUG=false
PACKAGE=com.xp.browser
LOOP=10000
SLEEP=10
#echo "$# parameters"
#echo "Parameters:" "$@";
PROGNAME=`basename $0`
if [ -z "$1" ] || [ "$1" != "start" ]; then
	echo "Usage: ${PROGNAME} start"
	exit
fi
if [ -n "$2" ] && [ "$2" = "debug" ]; then
	DEBUG=true
	LOOP=3
	SLEEP=5
fi

check_device
if [ "$?" -eq 1 ]; then
    echo "No device connected, quit"
	break
fi

echo "====== Monkey Script ======"
echo "Debug:" ${DEBUG}
echo "Package:" ${PACKAGE}
echo "Loop:" ${LOOP}
cd ${PWD}

for((i=1;i<=${LOOP};i++));
do
	#check_device
	#if [ "$?" -eq 1 ]; then
    #	echo "No device connected, quit"
	#	break
	#else
    #	echo "Device:" ${DEVICE}
	#fi

	check_device
	while [ "$?" -eq 1 ]; do
    	echo "No device connected."
		echo "Will check 5s later, waiting..."
		sleep 5
		check_device
	done

	echo ""
	echo "Monkey Start, Loop $i"
	run_monkey
	echo "Sleep 10s for next loop..."
	sleep ${SLEEP}
	echo "Monkey End, Loop $i"
	echo ""
done


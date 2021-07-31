echo 'search ps fd count'
#   set `adb shell ps |grep pkgname |grep -v channel |grep -v Daemon`
#    set `adb shell ps |grep pkgname |grep -n 1| grep ^1|grep -v channel |grep -v Daemon` -v排除关键字 -n 排序 ^1选取第1行数据

set `adb shell ps |grep pkgname |grep -v EinkAd`
pidnum=$2
index=0
while true
do
index=$[index+1]
echo '##################'
echo ' '$index' time search PID  '$pidnum
echo 'all fd'
adb shell ls -l /proc/$pidnum/fd |grep "" -c

echo 'anon fd for handler/looper'
adb shell ls -l /proc/$pidnum/fd |grep anon -c

echo 'pipe count for handler/HandlerThread/looper'
adb shell ls -ls /proc/$pidnum/fd | grep pipe -c

echo '/dev/ashmen count for contentProvider'
adb shell ls -ls /proc/$pidnum/fd | grep ashmen -c

echo 'socket count for system server input channel'
adb shell ls -ls /proc/$pidnum/fd | grep socket -c

echo 'sof fd'
adb shell lsof -p $pidnum | grep "" -c
echo 'thread count'
adb shell ps -t -P  $pidnum | grep "" -c

echo '##################'
sleep 2
done
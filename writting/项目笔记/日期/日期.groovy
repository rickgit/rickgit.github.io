import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

Date localDate=new Date();
long serverTimeStamp=localDate.getTime()+TimeUnit.HOURS.toMillis(1);//东九区时间戳

long diff=serverTimeStamp-localDate.getTime()//东九区时间戳与本地相差

TimeZone.getDefault()

Date newServerDate=new Date(new Date().getTime()+diff)//本地时区，服务器时间戳的本地时钟
SimpleDateFormat serverFormat = new SimpleDateFormat("HH:mm");
//serverFormat.setTimeZone(TimeZone.getTimeZone("GMT+9:00"));
serverFormat.format(newServerDate)
/*
Date begin = new Date();
begin.setHours(20);
begin.setMinutes(53);
*/ 
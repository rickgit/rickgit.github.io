import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

Date localDate=new Date();
long serverTimeStamp=localDate.getTime()+TimeUnit.HOURS.toMillis(1);

long diff=serverTimeStamp-localDate.getTime()

TimeZone.getDefault()

Date newServerDate=new Date(new Date().getTime()+diff)//服务器时间戳的本地时间
SimpleDateFormat serverFormat = new SimpleDateFormat("HH:mm");
//serverFormat.setTimeZone(TimeZone.getTimeZone("GMT+9:00"));
serverFormat.format(newServerDate)

Date begin = new Date();
begin.setHours(20);
begin.setMinutes(53);

begin
newServerDate
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

Date localDate=new Date();
long serverTimeStamp=localDate.getTime()+TimeUnit.HOURS.toMillis(1);//������ʱ���

long diff=serverTimeStamp-localDate.getTime()//������ʱ����뱾�����

TimeZone.getDefault()

Date newServerDate=new Date(new Date().getTime()+diff)//����ʱ����������ʱ����ı���ʱ��
SimpleDateFormat serverFormat = new SimpleDateFormat("HH:mm");
//serverFormat.setTimeZone(TimeZone.getTimeZone("GMT+9:00"));
serverFormat.format(newServerDate)
/*
Date begin = new Date();
begin.setHours(20);
begin.setMinutes(53);
*/ 
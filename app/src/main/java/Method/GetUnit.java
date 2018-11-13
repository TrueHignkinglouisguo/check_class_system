package Method;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GetUnit{
    private boolean getunit(Date begin, Date end) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date now = df.parse(df.format(new Date()));
        Calendar nowTime = Calendar.getInstance();
        nowTime.setTime(now);
        Calendar beginTime = Calendar.getInstance();
        beginTime.setTime(begin);
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(end);
        if (nowTime.before(endTime) && nowTime.after(beginTime)) {
            return true;
        } else {
            return false;
        }
    }
    public String getthe_time() throws ParseException{
        String query_unit=null;
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date begin = df.parse("7:59");
        Date end = df.parse("9:51");
        if(getunit(begin,end)){
            query_unit="1-2节";
        }
        Date begin1 = df.parse("10:09");
        Date end1 = df.parse("12:01");
        if(getunit(begin1,end1)){
            query_unit="3-4节";
        }
        Date begin2 = df.parse("14:29");
        Date end2 = df.parse("16:21");
        if(getunit(begin2,end2)){
            query_unit="5-6节";
        }
        Date begin3 = df.parse("16:39");
        Date end3 = df.parse("18:31");
        if(getunit(begin3,end3)){
            query_unit="7-8节";
        }
        Date begin4 = df.parse("19:29");
        Date end4 = df.parse("21:31");
        if(getunit(begin4,end4)){
            query_unit="9-10节";
        }
        return query_unit;
    }
}

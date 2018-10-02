package companion.virtual.com.virtualcompanion.utils.date;


import java.util.Calendar;

public class DateTime {

    public DateTime(){}

    public String getCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        month = month + 1;
        String dayS = day + "";
        String monthS = month + "";
        String hourS = hour + "";
        String minuteS = minute + "";
        String secondS = second + "";
        if(day < 10){
            dayS = "0" + day;
        }
        if(month < 10){
            monthS = "0" + month;
        }
        if(hour < 10){
            hourS = "0" + hour;
        }
        if(minute < 10){
            minuteS = "0" + minute;
        }
        if(second < 10){
            secondS = "0" + second;
        }
        String dayString = dayS + "/" + monthS + "/" + year;
        String hourString = hourS + ":" + minuteS + ":" + secondS;
        return dayString + " " + hourString;
    }

}

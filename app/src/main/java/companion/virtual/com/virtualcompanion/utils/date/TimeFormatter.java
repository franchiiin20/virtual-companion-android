package companion.virtual.com.virtualcompanion.utils.date;



public class TimeFormatter {

    public TimeFormatter(){

    }

    public String getTime(String fullDate){
        String timeString = "";
        timeString += fullDate.charAt(11);
        timeString += fullDate.charAt(12);
        timeString += fullDate.charAt(13);
        timeString += fullDate.charAt(14);
        timeString += fullDate.charAt(15);
        return timeString;
    }

}

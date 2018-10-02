package companion.virtual.com.virtualcompanion.utils.date;


import android.app.Activity;

import companion.virtual.com.virtualcompanion.R;


public class DateConvertReadable {

    private Activity activity;

    public DateConvertReadable(Activity activity){
        this.activity = activity;
    }

    public String convertDate(String dateString){
        String day  = dateString.charAt(0) + "" + dateString.charAt(1);
        String month  = dateString.charAt(3) + "" + dateString.charAt(4);
        String year  = dateString.charAt(6) + "" + dateString.charAt(7) +
                "" + dateString.charAt(8) + "" + dateString.charAt(9);
        String hour  = dateString.charAt(11) + "" + dateString.charAt(12);
        String minute  = dateString.charAt(14) + "" + dateString.charAt(15);
        if(month.equalsIgnoreCase("01")){
            month = activity.getResources().getString(R.string.month_january);
        } else if(month.equalsIgnoreCase("02")){
            month = activity.getResources().getString(R.string.month_february);
        } else if(month.equalsIgnoreCase("03")){
            month = activity.getResources().getString(R.string.month_march);
        } else if(month.equalsIgnoreCase("04")){
            month = activity.getResources().getString(R.string.month_april);
        } else if(month.equalsIgnoreCase("05")){
            month = activity.getResources().getString(R.string.month_may);
        } else if(month.equalsIgnoreCase("06")){
            month = activity.getResources().getString(R.string.month_june);
        } else if(month.equalsIgnoreCase("07")){
            month = activity.getResources().getString(R.string.month_july);
        } else if(month.equalsIgnoreCase("08")){
            month = activity.getResources().getString(R.string.month_august);
        } else if(month.equalsIgnoreCase("09")){
            month = activity.getResources().getString(R.string.month_september);
        } else if(month.equalsIgnoreCase("10")){
            month = activity.getResources().getString(R.string.month_october);
        } else if(month.equalsIgnoreCase("11")){
            month = activity.getResources().getString(R.string.month_november);
        } else if(month.equalsIgnoreCase("12")){
            month = activity.getResources().getString(R.string.month_december);
        }
        dateString = month + " " + day + ", " + year + " - " + hour + ":" + minute;
        return dateString;
    }

}

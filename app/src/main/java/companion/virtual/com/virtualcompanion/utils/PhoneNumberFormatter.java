package companion.virtual.com.virtualcompanion.utils;


import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

public class PhoneNumberFormatter {

    private Activity activity;

    private char PLUS_SIGN_IN_BEGINNING;
    private char PLUS_SIGN_IN_FORMAT;

    public PhoneNumberFormatter(Activity activity){
        this.activity = activity;
        PLUS_SIGN_IN_BEGINNING = '+';
        PLUS_SIGN_IN_FORMAT = '0';
    }

    public String getCountryCode(){
        String countryCode = "33";
        try {
            TelephonyManager tm = (TelephonyManager) activity.getApplicationContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String country = tm.getSimCountryIso();
            CountryCodes getCountryCode = new CountryCodes();
            countryCode = getCountryCode.getCode(country);
        } catch (Exception ignore){}
        return countryCode;
    }

    public String getCompleteNumber(String code, String number){
        String entryNumber = number.trim().charAt(0)
                + "" + number.trim().charAt(1)
                + "" + number.trim().charAt(2)
                + "" + number.trim().charAt(3);
        String returnPhone = number;
        if(!entryNumber.contains(code)){
            if(returnPhone.trim().charAt(0) != PLUS_SIGN_IN_BEGINNING){
                if(returnPhone.trim().charAt(0) == PLUS_SIGN_IN_FORMAT){
                    returnPhone = returnPhone.substring(1);
                    returnPhone = code + returnPhone;
                } else {
                    returnPhone = code + returnPhone;
                }
                returnPhone = PLUS_SIGN_IN_BEGINNING + returnPhone;
            } else {
                returnPhone = returnPhone.substring(1);
                if(returnPhone.trim().charAt(0) == PLUS_SIGN_IN_FORMAT){
                    returnPhone = returnPhone.substring(1);
                    returnPhone = code + returnPhone;
                } else {
                    returnPhone = code + returnPhone;
                }
                returnPhone = PLUS_SIGN_IN_BEGINNING + returnPhone;
            }
        } else {
            if(returnPhone.trim().charAt(0) != PLUS_SIGN_IN_BEGINNING){
                returnPhone = PLUS_SIGN_IN_BEGINNING + returnPhone;
            }
        }
        return returnPhone;
    }

}

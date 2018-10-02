package companion.virtual.com.virtualcompanion.utils;


import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

public class CheckerUtils {

    public static boolean CheckVersion(){
        return (Build.VERSION.SDK_INT >= 23);
    }

    public static boolean CheckCamera(Activity activity){
        return (ActivityCompat.checkSelfPermission(activity,
                android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED);
    }

    public static boolean CheckStorage(Activity activity){
        return (ActivityCompat.checkSelfPermission(activity,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED);
    }

    public static boolean CheckLocation(Activity activity){
        return (ActivityCompat.checkSelfPermission(activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED);
    }

}

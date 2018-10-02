package companion.virtual.com.virtualcompanion.utils;

import android.app.Activity;

import companion.virtual.com.virtualcompanion.R;


public class PageAnimation {

    private Activity activity;

    public PageAnimation(Activity activity){
        this.activity = activity;
    }

    public void fadeAnimation(){
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void slideInAnimation(){
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public void slideOutAnimation(){
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

}

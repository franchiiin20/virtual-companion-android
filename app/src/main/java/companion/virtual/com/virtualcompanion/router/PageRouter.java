package companion.virtual.com.virtualcompanion.router;

import android.app.Activity;
import android.content.Intent;

import companion.virtual.com.virtualcompanion.EmergencyActivity;
import companion.virtual.com.virtualcompanion.LoginConfirmActivity;
import companion.virtual.com.virtualcompanion.LoginSeekerActivity;
import companion.virtual.com.virtualcompanion.MapActivity;
import companion.virtual.com.virtualcompanion.SetUpDescriptionActivity;
import companion.virtual.com.virtualcompanion.SetUpProfileActivity;
import companion.virtual.com.virtualcompanion.SettingsActivity;
import companion.virtual.com.virtualcompanion.SettingsNameActivity;
import companion.virtual.com.virtualcompanion.SignUpConfirmActivity;
import companion.virtual.com.virtualcompanion.SignUpDoneActivity;
import companion.virtual.com.virtualcompanion.SignUpNameActivity;
import companion.virtual.com.virtualcompanion.SignUpPhoneActivity;
import companion.virtual.com.virtualcompanion.SignUpStartActivity;
import companion.virtual.com.virtualcompanion.SplashActivity;
import companion.virtual.com.virtualcompanion.model.RegistrationModel;
import companion.virtual.com.virtualcompanion.model.UserModel;
import companion.virtual.com.virtualcompanion.utils.Constant;
import companion.virtual.com.virtualcompanion.utils.PageAnimation;


public class PageRouter {

    private Activity activity;
    private PageAnimation pageAnimation;

    public PageRouter(Activity activity){
        this.activity = activity;
        pageAnimation = new PageAnimation(this.activity);
    }

    public void openLoginSeekerPage(){
        Intent intent = new Intent(activity, LoginSeekerActivity.class);
        activity.startActivity(intent);
        activity.finish();
        pageAnimation.fadeAnimation();
    }

    public void openLoginConfirmPage(UserModel userModel){
        Intent intent = new Intent(activity, LoginConfirmActivity.class);
        intent.putExtra(Constant.IntentParams.INTENT_REGISTER, userModel);
        activity.startActivity(intent);
        activity.finish();
        pageAnimation.fadeAnimation();
    }

    public void openSetUpProfilePage(){
        Intent intent = new Intent(activity, SetUpProfileActivity.class);
        activity.startActivity(intent);
        activity.finish();
        pageAnimation.fadeAnimation();
    }

    public void openSetUpDescriptionPage(){
        Intent intent = new Intent(activity, SetUpDescriptionActivity.class);
        activity.startActivity(intent);
        activity.finish();
        pageAnimation.fadeAnimation();
    }

    public void openSettingsNamePage(){
        Intent intent = new Intent(activity, SettingsNameActivity.class);
        activity.startActivity(intent);
        pageAnimation.fadeAnimation();
    }

    public void openSettingsPage(){
        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivity(intent);
        pageAnimation.fadeAnimation();
    }

    public void openMainPage(){
        Intent intent = new Intent(activity, MapActivity.class);
        activity.startActivity(intent);
        activity.finish();
        pageAnimation.fadeAnimation();
    }

    public void openSignUpSeekerStartPage(){
        Intent intent = new Intent(activity, SignUpStartActivity.class);
        activity.startActivity(intent);
        activity.finish();
        pageAnimation.fadeAnimation();
    }

    public void openSignUpSeekerNamePage(){
        Intent intent = new Intent(activity, SignUpNameActivity.class);
        activity.startActivity(intent);
        activity.finish();
        pageAnimation.fadeAnimation();
    }

    public void openSignUpSeekerMobilePhonePage(RegistrationModel registrationModel){
        Intent intent = new Intent(activity, SignUpPhoneActivity.class);
        intent.putExtra(Constant.IntentParams.INTENT_REGISTER, registrationModel);
        activity.startActivity(intent);
        activity.finish();
        pageAnimation.fadeAnimation();
    }

    public void openSignUpSeekerConfirmPage(RegistrationModel registrationModel){
        Intent intent = new Intent(activity, SignUpConfirmActivity.class);
        intent.putExtra(Constant.IntentParams.INTENT_REGISTER, registrationModel);
        activity.startActivity(intent);
        activity.finish();
        pageAnimation.fadeAnimation();
    }

    public void openSignUpSeekerDonePage(){
        Intent intent = new Intent(activity, SignUpDoneActivity.class);
        activity.startActivity(intent);
        activity.finish();
        pageAnimation.fadeAnimation();
    }

    public void openSplashScreen(){
        Intent intent = new Intent(activity, SplashActivity.class);
        activity.startActivity(intent);
        activity.finish();
        pageAnimation.fadeAnimation();
    }

    public void openEmergencyScreen(){
        Intent intent = new Intent(activity, EmergencyActivity.class);
        activity.startActivity(intent);
        pageAnimation.fadeAnimation();
    }

}

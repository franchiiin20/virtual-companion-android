package companion.virtual.com.virtualcompanion;

import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import companion.virtual.com.virtualcompanion.router.PageRouter;

public class SplashActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        closeSplashScreen();
    }

    private void closeSplashScreen() {
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished){}
            @Override
            public void onFinish() {
                checkSession();
            }
        }.start();
    }

    private void checkSession(){
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception ignore){}
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            Log.d("SubscribeLogs", "getUid: " + currentUser.getUid());
            FirebaseMessaging.getInstance().subscribeToTopic(currentUser.getUid());
            openMainPage();
        } else {
            openLoginPage();
        }
    }
    private void openMainPage(){
        PageRouter pageRouter = new PageRouter(this);
        pageRouter.openMainPage();
    }

    private void openLoginPage(){
        PageRouter pageRouter = new PageRouter(this);
        pageRouter.openLoginSeekerPage();
    }

}

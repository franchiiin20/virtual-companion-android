package companion.virtual.com.virtualcompanion;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import companion.virtual.com.virtualcompanion.router.PageRouter;
import companion.virtual.com.virtualcompanion.utils.PageAnimation;


public class SignUpStartActivity extends FragmentActivity {

    private ImageView backImageView;
    private Button proceedButton;
    private PageRouter pageRouter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up_start);
        pageTransition();
        init();
        initValues();
        initClick();
    }

    private void backAction(){
        pageRouter.openLoginSeekerPage();
    }

    @Override
    public void onBackPressed() {
        backAction();
    }

    private void pageTransition(){
        PageAnimation pageAnimation = new PageAnimation(this);
        pageAnimation.fadeAnimation();
    }

    private void initClick(){
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backAction();
            }
        });
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageRouter.openSignUpSeekerNamePage();
            }
        });
    }

    private void initValues(){
        pageRouter = new PageRouter(this);
    }

    private void init(){
        backImageView = (ImageView)findViewById(R.id.icon_back);
        proceedButton = (Button)findViewById(R.id.register_main_button);
    }

}

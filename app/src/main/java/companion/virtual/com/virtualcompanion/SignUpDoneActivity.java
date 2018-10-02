package companion.virtual.com.virtualcompanion;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import companion.virtual.com.virtualcompanion.router.PageRouter;
import companion.virtual.com.virtualcompanion.utils.PageAnimation;

public class SignUpDoneActivity extends FragmentActivity {

    private PageRouter pageRouter;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up_done);
        pageTransition();
        init();
        initValues();
        initClick();
    }

    private void pageTransition(){
        PageAnimation pageAnimation = new PageAnimation(this);
        pageAnimation.fadeAnimation();
    }

    private void initClick(){
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageRouter.openSetUpProfilePage();
            }
        });
    }

    private void initValues(){
        pageRouter = new PageRouter(this);
    }

    private void init(){
        startButton = (Button)findViewById(R.id.register_setup);
    }

}

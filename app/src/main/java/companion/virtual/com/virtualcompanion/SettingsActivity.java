package companion.virtual.com.virtualcompanion;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import companion.virtual.com.virtualcompanion.dialog.SpinnerDialog;
import companion.virtual.com.virtualcompanion.model.UserModel;
import companion.virtual.com.virtualcompanion.router.PageRouter;
import companion.virtual.com.virtualcompanion.utils.Constant;
import companion.virtual.com.virtualcompanion.utils.PageAnimation;


public class SettingsActivity extends FragmentActivity {

    private UserModel userModel;
    private ImageView backImageView;
    private PageRouter pageRouter;
    private LinearLayout nameLinearLayout;
    private LinearLayout photoLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);
        pageTransition();
        init();
        initClick();
        initValues();
    }

    @Override
    public void onBackPressed() {
        try {
            finish();
            PageAnimation pageAnimation = new PageAnimation(this);
            pageAnimation.slideOutAnimation();
        } catch (Exception ignore){}
    }

    private void pageTransition(){
        PageAnimation pageAnimation = new PageAnimation(this);
        pageAnimation.slideInAnimation();
    }

    private void updatePrivacy(boolean isChecked){
        try {
            String value = Constant.FixedValues.DEACTIVATE;
            if(isChecked) value = Constant.FixedValues.ACTIVE;
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child(Constant.FireUser.TABLE)
                    .child(userModel.getUID())
                    .child(Constant.FireUser.PRIVACY)
                    .setValue(value);
        } catch (Exception ignore){}
    }

    private void initClick(){
        nameLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageRouter.openSettingsNamePage();
            }
        });

        photoLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageRouter.openSetUpProfilePage();
            }
        });
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initValues(){
        try {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            pageRouter = new PageRouter(this);
            userModel = new UserModel();
            if (user != null) {
                userModel.setUID(user.getUid());
            }
        } catch (Exception ignore){}
    }

    private void init(){
        backImageView = (ImageView)findViewById(R.id.icon_back);
        nameLinearLayout = (LinearLayout)findViewById(R.id.settings_name);
        photoLinearLayout = (LinearLayout)findViewById(R.id.settings_photo);
    }

}

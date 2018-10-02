package companion.virtual.com.virtualcompanion;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import companion.virtual.com.virtualcompanion.dialog.RegisterCancelDialog;
import companion.virtual.com.virtualcompanion.interfaces.RegisterCallBack;
import companion.virtual.com.virtualcompanion.model.RegistrationModel;
import companion.virtual.com.virtualcompanion.router.PageRouter;
import companion.virtual.com.virtualcompanion.utils.PageAnimation;

public class SignUpNameActivity extends FragmentActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private ImageView backImageView;
    private Button proceedButton;
    private PageRouter pageRouter;
    private RegistrationModel registrationModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up_name);
        pageTransition();
        init();
        initValues();
        initClick();
    }

    private void nameError(){
        Toast.makeText(this, getResources().getString(R.string.register_name_error),
                Toast.LENGTH_LONG).show();
    }

    private void saveName(){
        try {
            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            if(firstName.trim().length() > 0 && lastName.trim().length() > 0){
                registrationModel.setFirstName(firstName);
                registrationModel.setLastName(lastName);
                pageRouter.openSignUpSeekerMobilePhonePage(registrationModel);
            } else {
                nameError();
            }
        } catch (Exception ignore){
            nameError();
        }
    }

    private void backAction(){
        try {
            pageRouter.openLoginSeekerPage();
//            RegisterCancelDialog registerCancelDialog =
//                    new RegisterCancelDialog(this, new RegisterCallBack() {
//                @Override
//                public void onClick() {
//                    pageRouter.openLoginSeekerPage();
//                }
//            });
//            if(!registerCancelDialog.isShowing()){
//                registerCancelDialog.show();
//            }
        } catch (Exception ignore){}
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
                saveName();
            }
        });
    }

    private void initValues(){
        registrationModel = new RegistrationModel();
        pageRouter = new PageRouter(this);
    }

    private void init(){
        firstNameEditText = (EditText) findViewById(R.id.register_name_first);
        lastNameEditText = (EditText)findViewById(R.id.register_name_last);
        backImageView = (ImageView)findViewById(R.id.icon_back);
        proceedButton = (Button)findViewById(R.id.register_proceed);
    }

}

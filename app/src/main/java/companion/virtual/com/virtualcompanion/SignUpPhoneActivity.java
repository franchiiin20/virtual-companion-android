package companion.virtual.com.virtualcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import companion.virtual.com.virtualcompanion.dialog.RegisterCancelDialog;
import companion.virtual.com.virtualcompanion.interfaces.RegisterCallBack;
import companion.virtual.com.virtualcompanion.model.RegistrationModel;
import companion.virtual.com.virtualcompanion.router.PageRouter;
import companion.virtual.com.virtualcompanion.utils.Constant;
import companion.virtual.com.virtualcompanion.utils.PageAnimation;
import companion.virtual.com.virtualcompanion.utils.PhoneNumberFormatter;


public class SignUpPhoneActivity extends FragmentActivity {

    private CountryCodePicker countryCodePicker;
    private EditText mobileEditText;
    private ImageView backImageView;
    private Button proceedButton;
    private PageRouter pageRouter;
    private RegistrationModel registrationModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up_phone);
        pageTransition();
        init();
        initValues();
        initClick();
    }

    private void mobileError(){
        Toast.makeText(this, getResources().getString(R.string.register_mobile_error),
                Toast.LENGTH_LONG).show();
    }

    private void saveMobile(){
        try {
            PhoneNumberFormatter number = new PhoneNumberFormatter(this);
            String mobileNumber = number.getCompleteNumber(countryCodePicker.getSelectedCountryCode(),
                    mobileEditText.getText().toString());
            if(mobileNumber.trim().length() > 8){
                registrationModel.setMobileNumber(mobileNumber);
                pageRouter.openSignUpSeekerConfirmPage(registrationModel);
            } else {
                mobileError();
            }
        } catch (Exception ignore){
            mobileError();
        }
    }

    private void backAction(){
        try {
            pageRouter.openLoginSeekerPage();
//            RegisterCancelDialog registerCancelDialog =
//                    new RegisterCancelDialog(this, new RegisterCallBack() {
//                        @Override
//                        public void onClick() {
//                            pageRouter.openLoginSeekerPage();
//                        }
//                    });
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
                saveMobile();
            }
        });
    }

    private void initValues(){
        pageRouter = new PageRouter(this);
        try {
            Intent intent = getIntent();
            registrationModel = (RegistrationModel) intent.getSerializableExtra(Constant.IntentParams.INTENT_REGISTER);
            if(registrationModel == null)
                finish();
        } catch (Exception ignore){}
    }

    private void init(){
        countryCodePicker = (CountryCodePicker) findViewById(R.id.country_code_picker);
        mobileEditText = (EditText)findViewById(R.id.login_phone);
        backImageView = (ImageView)findViewById(R.id.icon_back);
        proceedButton = (Button)findViewById(R.id.register_proceed);
    }

}

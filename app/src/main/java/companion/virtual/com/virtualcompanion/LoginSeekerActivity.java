package companion.virtual.com.virtualcompanion;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import companion.virtual.com.virtualcompanion.model.UserModel;
import companion.virtual.com.virtualcompanion.router.PageRouter;
import companion.virtual.com.virtualcompanion.utils.PageAnimation;
import companion.virtual.com.virtualcompanion.utils.PhoneNumberFormatter;

public class LoginSeekerActivity extends FragmentActivity {

    private CountryCodePicker countryCodePicker;
    private EditText mobileEditText;
    private Button proceedButton;
    private PageRouter pageRouter;
    private TextView signUpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        // Animation page transition
        pageTransition();
        // This method initializes and connects the variables from the design XML page.
        init();
        // This method initializes all the variables needed from this page.
        initValues();
        // This method assigns the on click listeners.
        initOnClick();
    }

    // This method opens the login confirmation sms page.
    private void openLoginConfirmation(UserModel userModel){
        PageRouter pageRouter = new PageRouter(this);
        pageRouter.openLoginConfirmPage(userModel);
    }

    // This method prints a small pop-up error message.
    private void mobileError(){
        Toast.makeText(this, getResources().getString(R.string.register_mobile_error),
                Toast.LENGTH_LONG).show();
    }

    private void saveMobile(){
        try {
            // This code checks if the mobile number and country code combination is in the correct format.
            PhoneNumberFormatter number = new PhoneNumberFormatter(this);
            String mobileNumber = number.getCompleteNumber(countryCodePicker.getSelectedCountryCode(), mobileEditText.getText().toString());
            // Check if mobile number has more than 8 characters.
            if(mobileNumber.trim().length() > 8){
                // Saves the mobile number string to the user object.
                UserModel userModel = new UserModel();
                userModel.setMobile(mobileNumber);
                // This method opens the login confirmation sms page.
                // Passing the variable userModel to the login confirmation sms page.
                openLoginConfirmation(userModel);
            } else {
                // This method prints a small pop-up error message.
                mobileError();
            }
        } catch (Exception ignore){
            // This method prints a small pop-up error message.
            mobileError();
        }
    }

    // Default back method.
    @Override
    public void onBackPressed() {
        finish();
    }

    // Animation page transition
    private void pageTransition(){
        PageAnimation pageAnimation = new PageAnimation(this);
        pageAnimation.fadeAnimation();
    }

    // This method assigns the on click listeners.
    private void initOnClick(){
        // This listener is called if the login button is clicked.
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMobile();
            }
        });
        // This listener is called if the sign up text is clicked.
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageRouter.openSignUpSeekerStartPage();
            }
        });
    }

    // This method initializes all the variables needed from this page.
    private void initValues(){
        pageRouter = new PageRouter(this);
    }

    // This method initializes and connects the variables from the design XML page.
    private void init(){
        countryCodePicker = (CountryCodePicker) findViewById(R.id.country_code_picker);
        mobileEditText = (EditText)findViewById(R.id.login_phone);
        proceedButton = (Button)findViewById(R.id.login_button);
        signUpTextView = (TextView)findViewById(R.id.login_sign_up_seeker);
    }

}

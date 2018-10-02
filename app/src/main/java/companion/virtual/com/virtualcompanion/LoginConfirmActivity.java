package companion.virtual.com.virtualcompanion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import companion.virtual.com.virtualcompanion.model.UserModel;
import companion.virtual.com.virtualcompanion.router.PageRouter;
import companion.virtual.com.virtualcompanion.utils.Constant;
import companion.virtual.com.virtualcompanion.utils.PageAnimation;


public class LoginConfirmActivity extends FragmentActivity {

    private static final int RESEND_COUNTER_FOR_REFRESH = 30;
    private static final int SECOND_DIVISION = 1000;
    private Handler handler;
    private int counterMaxValue;
    private int delayCounter;

    private ImageView backImageView;
    private Button proceedButton;
    private PageRouter pageRouter;
    private UserModel userModel;
    private FirebaseAuth mAuth;

    private EditText oneEditText;
    private EditText twoEditText;
    private EditText threeEditText;
    private EditText fourEditText;
    private EditText fiveEditText;
    private EditText sixEditText;
    private ProgressBar progressBar;
    private TextView counterTextView;
    private TextView resendTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_confirm);
        // Animation page transition
        pageTransition();
        // This method initializes and connects the variables from the design XML page.
        init();
        // This method initializes all the variables needed from this page.
        initValues();
        // This method assigns the on click listeners.
        initClick();
    }

    private void loginComplete(){
        PageRouter pageRouter = new PageRouter(this);
        pageRouter.openMainPage();
    }

    @SuppressLint("SetTextI18n")
    private void processCounter(){
        processFireBasePhoneAuth();
        counterTextView.setText(getResources().getString(R.string.register_confirm_counter)
                + " " + RESEND_COUNTER_FOR_REFRESH);
        resendTextView.setVisibility(View.GONE);
        counterMaxValue = RESEND_COUNTER_FOR_REFRESH;
        delayCounter = SECOND_DIVISION;
        try {
            handler = new Handler();
            handler.postDelayed(new Runnable(){
                public void run(){
                    if(counterMaxValue > 0){
                        counterMaxValue--;
                        String counter = getResources().getString(R.string.register_confirm_counter)
                                + " " + counterMaxValue;
                        counterTextView.setText(counter);
                        if(counterMaxValue == 0){
                            counterTextView.setText(getResources().getString(R.string.register_confirm_resend_text));
                            resendTextView.setVisibility(View.VISIBLE);
                        }
                        handler.postDelayed(this, delayCounter);
                    }
                }
            }, delayCounter);
        } catch (Exception ignore){}
    }

    private String getConfirmationCode(){
        String code = "";
        code += oneEditText.getText().toString();
        code += twoEditText.getText().toString();
        code += threeEditText.getText().toString();
        code += fourEditText.getText().toString();
        code += fiveEditText.getText().toString();
        code += sixEditText.getText().toString();
        if(code.equalsIgnoreCase("")){
            code = "000000";
        }
        return code;
    }

    // This method is called if the user clicked on the confirm button.
    private void confirmCode(){
        try {
            processBarVisible();
            // Will then check if the code entered matches from the code sent from the device.
            PhoneAuthCredential credential = PhoneAuthProvider
                    .getCredential(userModel.getVerificationString(), getConfirmationCode());
            signInWithPhoneAuthCredential(credential);
        } catch (Exception ignore){}
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        loginComplete();
                    } else {
                        Log.w("Confirmation", "signInWithCredential:failure", task.getException());
                        processBarGone();
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) verificationFailed();
                        else systemError();
                    }
                }
            });
    }

    private void processFireBasePhoneAuth(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(userModel.getMobile(), 60, TimeUnit.SECONDS,
            this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential credential) {
                    Log.d("Confirmation", "onVerificationCompleted: " + credential);
                    try {
                        oneEditText.setText("0");
                        twoEditText.setText("0");
                        threeEditText.setText("0");
                        fourEditText.setText("0");
                        fiveEditText.setText("0");
                        sixEditText.setText("0");
                    } catch (Exception ignore){}
                    signInWithPhoneAuthCredential(credential);
                }
                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Log.w("Confirmation", "onVerificationFailed", e);
                    processBarGone();

                    // This method prints a small pop-up error message.
                    // This method prints a small pop-up error message.
                    if (e instanceof FirebaseAuthInvalidCredentialsException) verificationFailed();
                    else systemError();
                }
                @Override
                public void onCodeSent(String verificationId,
                                       PhoneAuthProvider.ForceResendingToken token) {
                    Log.d("Confirmation", "onCodeSent: " + verificationId);
                    userModel.setVerificationString(verificationId);
                }
            });
    }

    // This method prints a small pop-up error message.
    private void systemError(){
        Toast.makeText(this, getResources().getString(R.string.register_confirm_system_error),
                Toast.LENGTH_LONG).show();
    }

    // This method prints a small pop-up error message.
    private void verificationFailed(){
        Toast.makeText(this, getResources().getString(R.string.register_confirm_error),
                Toast.LENGTH_LONG).show();
    }

    // This method sets the spinner visibility to none.
    private void processBarGone(){
        progressBar.setVisibility(View.GONE);
    }

    // This method sets the spinner visibility to visible.
    private void processBarVisible(){
        progressBar.setVisibility(View.VISIBLE);
    }

    private void backAction(){
        try {
            pageRouter.openLoginSeekerPage();
        } catch (Exception ignore){}
    }

    // Default back method.
    @Override
    public void onBackPressed() {
        backAction();
    }

    // Animation page transition
    private void pageTransition(){
        PageAnimation pageAnimation = new PageAnimation(this);
        pageAnimation.fadeAnimation();
    }

    // This method assigns the on click listeners.
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
                confirmCode();
            }
        });
        resendTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processCounter();
            }
        });
    }

    // This method assigns all the listener whenever a user types in one of the
    // six edit text field, the number will auto transfer to the next field.
    private void initListener(){
        oneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) { }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
            @Override
            public void afterTextChanged(Editable arg0) {
                if(!oneEditText.getText().toString().equalsIgnoreCase("")){
                    twoEditText.setFocusableInTouchMode(true);
                    twoEditText.requestFocus();
                }
            }
        });
        twoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) { }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
            @Override
            public void afterTextChanged(Editable arg0) {
                if(!twoEditText.getText().toString().equalsIgnoreCase("")){
                    threeEditText.setFocusableInTouchMode(true);
                    threeEditText.requestFocus();
                }
            }
        });
        threeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) { }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
            @Override
            public void afterTextChanged(Editable arg0) {
                if(!threeEditText.getText().toString().equalsIgnoreCase("")){
                    fourEditText.setFocusableInTouchMode(true);
                    fourEditText.requestFocus();
                }
            }

        });
        fourEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) { }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
            @Override
            public void afterTextChanged(Editable arg0) {
                if(!fourEditText.getText().toString().equalsIgnoreCase("")){
                    fiveEditText.setFocusableInTouchMode(true);
                    fiveEditText.requestFocus();
                }
            }
        });
        fiveEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) { }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
            @Override
            public void afterTextChanged(Editable arg0) {
                if(!fiveEditText.getText().toString().equalsIgnoreCase("")){
                    sixEditText.setFocusableInTouchMode(true);
                    sixEditText.requestFocus();
                }
            }
        });
    }

    // This method initializes all the variables needed from this page.
    private void initValues(){
        initListener();
        mAuth = FirebaseAuth.getInstance();
        pageRouter = new PageRouter(this);
        try {
            Intent intent = getIntent();
            // This code checks if you are logged in or not.
            // If you are logged in, this page will close automatically because login
            // user are not allowed on this page.
            userModel = (UserModel) intent.getSerializableExtra(Constant.IntentParams.INTENT_REGISTER);
            if(userModel == null)
                finish();
            processCounter();
        } catch (Exception ignore){}
    }

    // This method initializes and connects the variables from the design XML page.
    private void init(){
        backImageView = (ImageView)findViewById(R.id.icon_back);
        proceedButton = (Button)findViewById(R.id.register_proceed);
        oneEditText = (EditText)findViewById(R.id.confirm_one);
        twoEditText = (EditText)findViewById(R.id.confirm_two);
        threeEditText = (EditText)findViewById(R.id.confirm_three);
        fourEditText = (EditText)findViewById(R.id.confirm_four);
        fiveEditText = (EditText)findViewById(R.id.confirm_five);
        sixEditText = (EditText)findViewById(R.id.confirm_six);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        counterTextView = (TextView)findViewById(R.id.register_confirm_resend_text);
        resendTextView = (TextView)findViewById(R.id.register_confirm_resend);
    }

}

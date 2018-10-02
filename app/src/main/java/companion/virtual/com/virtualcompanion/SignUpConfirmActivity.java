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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import companion.virtual.com.virtualcompanion.dialog.RegisterCancelDialog;
import companion.virtual.com.virtualcompanion.interfaces.RegisterCallBack;
import companion.virtual.com.virtualcompanion.model.RegistrationModel;
import companion.virtual.com.virtualcompanion.router.PageRouter;
import companion.virtual.com.virtualcompanion.utils.Constant;
import companion.virtual.com.virtualcompanion.utils.PageAnimation;

public class SignUpConfirmActivity extends FragmentActivity {

    private static final int RESEND_COUNTER_FOR_REFRESH = 30;
    private static final int SECOND_DIVISION = 1000;
    private Handler handler;
    private int counterMaxValue;
    private int delayCounter;

    private ImageView backImageView;
    private Button proceedButton;
    private PageRouter pageRouter;
    private RegistrationModel registrationModel;
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
        setContentView(R.layout.activity_sign_up_confirm);
        pageTransition();
        init();
        initValues();
        initClick();
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

    private void confirmCode(){
        try {
            processBarVisible();
            PhoneAuthCredential credential = PhoneAuthProvider
                    .getCredential(registrationModel.getVerificationString(), getConfirmationCode());
            signInWithPhoneAuthCredential(credential);
        } catch (Exception ignore){}
    }

    private void createAccount(@NonNull Task<AuthResult> task){
        try {
            if (task.isSuccessful()) {
                FirebaseUser user = task.getResult().getUser();
                user.sendEmailVerification();
                Log.d("Confirmation", "isEmailVerified: [" + user.isEmailVerified() + "]");
                final String stringUID = user.getUid();
                String tableString = Constant.FireUser.TABLE;
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child(tableString)
                        .child(stringUID)
                        .child(Constant.FireUser.FIRST_NAME)
                        .setValue(registrationModel.getFirstName());
                mDatabase.child(tableString)
                        .child(stringUID)
                        .child(Constant.FireUser.LAST_NAME)
                        .setValue(registrationModel.getLastName());
                mDatabase.child(tableString)
                        .child(stringUID)
                        .child(Constant.FireUser.EMAIL)
                        .setValue("");
                mDatabase.child(tableString)
                        .child(stringUID)
                        .child(Constant.FireUser.MOBILE)
                        .setValue(registrationModel.getMobileNumber());
                mDatabase.child(tableString)
                        .child(stringUID)
                        .child(Constant.FireUser.RESUME_URL)
                        .setValue("");
                mDatabase.child(tableString)
                        .child(stringUID)
                        .child(Constant.FireUser.PROFILE_URL)
                        .setValue("");
                mDatabase.child(tableString)
                        .child(stringUID)
                        .child(Constant.FireUser.DESCRIPTION)
                        .setValue("");
                mDatabase.child(tableString)
                        .child(stringUID)
                        .child(Constant.FireUser.TYPE)
                        .setValue(Constant.FixedValues.TYPE_SEEKER);
                mDatabase.child(tableString)
                        .child(stringUID)
                        .child(Constant.FireUser.UID)
                        .setValue(stringUID);
                mDatabase.child(tableString)
                        .child(stringUID)
                        .child(Constant.FireUser.PRIVACY)
                        .setValue(Constant.FixedValues.DEACTIVATE);
                mDatabase.child(tableString)
                        .child(stringUID)
                        .child(Constant.FireUser.LONGITUDE)
                        .setValue("");
                mDatabase.child(tableString)
                        .child(stringUID)
                        .child(Constant.FireUser.LATITUDE)
                        .setValue("");
                mDatabase.child(tableString)
                        .child(stringUID)
                        .child(Constant.FireUser.STATUS)
                        .setValue(Constant.FixedValues.ACTIVE)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                pageRouter.openSignUpSeekerDonePage();
                            }
                        });
            }
        } catch (Exception ignore){
            systemError();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            createAccount(task);
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
        PhoneAuthProvider.getInstance().verifyPhoneNumber(registrationModel.getMobileNumber(), 60, TimeUnit.SECONDS,
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
                        if (e instanceof FirebaseAuthInvalidCredentialsException) verificationFailed();
                        else systemError();
                    }
                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {
                        Log.d("Confirmation", "onCodeSent: " + verificationId);
                        registrationModel.setVerificationString(verificationId);
                    }
                });
    }

    private void systemError(){
        Toast.makeText(this, getResources().getString(R.string.register_confirm_system_error),
                Toast.LENGTH_LONG).show();
    }

    private void verificationFailed(){
        Toast.makeText(this, getResources().getString(R.string.register_confirm_error),
                Toast.LENGTH_LONG).show();
    }

    private void processBarGone(){
        progressBar.setVisibility(View.GONE);
    }

    private void processBarVisible(){
        progressBar.setVisibility(View.VISIBLE);
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
                confirmCode();
            }
        });
    }

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

    private void initValues(){
        initListener();
        mAuth = FirebaseAuth.getInstance();
        pageRouter = new PageRouter(this);
        try {
            Intent intent = getIntent();
            registrationModel = (RegistrationModel) intent.getSerializableExtra(Constant.IntentParams.INTENT_REGISTER);
            if(registrationModel == null)
                finish();
            processCounter();
        } catch (Exception ignore){}
    }

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

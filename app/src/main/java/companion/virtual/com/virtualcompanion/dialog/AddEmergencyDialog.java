package companion.virtual.com.virtualcompanion.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import companion.virtual.com.virtualcompanion.R;
import companion.virtual.com.virtualcompanion.utils.Constant;
import companion.virtual.com.virtualcompanion.utils.PhoneNumberFormatter;


public class AddEmergencyDialog extends Dialog {

    private String UID;
    private Activity activity;
    private Button yesBottom;
    private Button noBottom;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private CountryCodePicker countryCodePicker;

    public AddEmergencyDialog(Activity activity, String UID) {
        super(activity);
        this.UID = UID;
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_emergency);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setInit();
        setOnClick();
    }

    private void saveRecords(){
        try {
            String name = nameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String numberRaw = phoneEditText.getText().toString();
            if(name.length() > 0 && email.length() > 0 && numberRaw.length() > 0){

                PhoneNumberFormatter number = new PhoneNumberFormatter(activity);
                String mobileNumber = number.getCompleteNumber(countryCodePicker.getSelectedCountryCode(),
                        numberRaw);

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                String mainReferenceTable = Constant.FireEmergency.TABLE;
                String pushID = mDatabase.push().getKey();
                mDatabase.child(mainReferenceTable)
                        .child(UID)
                        .child(pushID)
                        .child(Constant.FireEmergency.NAME)
                        .setValue(name);
                mDatabase.child(mainReferenceTable)
                        .child(UID)
                        .child(pushID)
                        .child(Constant.FireEmergency.MOBILE)
                        .setValue(mobileNumber);
                mDatabase.child(mainReferenceTable)
                        .child(UID)
                        .child(pushID)
                        .child(Constant.FireEmergency.EMAIL)
                        .setValue(email);
                mDatabase.child(mainReferenceTable)
                        .child(UID)
                        .child(pushID)
                        .child(Constant.FireEmergency.STATUS)
                        .setValue(Constant.FixedValues.ACTIVE)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity.getBaseContext(),activity.getResources().getString(R.string.resume_error_internet),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(activity.getBaseContext(),activity.getResources().getString(R.string.resume_error),
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ignore){}
    }

    private void setOnClick(){
        yesBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRecords();
            }
        });
        noBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void setInit(){
        countryCodePicker = (CountryCodePicker) findViewById(R.id.country_code_picker);
        nameEditText = (EditText) findViewById(R.id.emergency_name);
        emailEditText = (EditText)findViewById(R.id.emergency_email);
        phoneEditText = (EditText)findViewById(R.id.emergency_mobile);
        yesBottom = (Button)findViewById(R.id.resume_save);
        noBottom = (Button)findViewById(R.id.resume_cancel);
    }
}

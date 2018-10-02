package companion.virtual.com.virtualcompanion;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import companion.virtual.com.virtualcompanion.model.UserModel;
import companion.virtual.com.virtualcompanion.utils.Constant;
import companion.virtual.com.virtualcompanion.utils.PageAnimation;


public class SettingsNameActivity extends FragmentActivity {

    private UserModel userModel;
    private ImageView backImageView;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private Button proceedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings_name);
        pageTransition();
        init();
        initClick();
        initValues();
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
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child(Constant.FireUser.TABLE)
                        .child(userModel.getUID())
                        .child(Constant.FireUser.FIRST_NAME)
                        .setValue(firstName);
                mDatabase.child(Constant.FireUser.TABLE)
                        .child(userModel.getUID())
                        .child(Constant.FireUser.LAST_NAME)
                        .setValue(lastName);
                onBackPressed();
            } else nameError();
        } catch (Exception ignore){
            nameError();
        }
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

    @SuppressLint("SetTextI18n")
    private void setProfile(DataSnapshot dataSnapshot){
        try {
            Map<String, String> dataMap = (Map<String, String>)dataSnapshot.getValue();
            String firstName = dataMap.get(Constant.FireUser.FIRST_NAME);
            String lastName = dataMap.get(Constant.FireUser.LAST_NAME);
            userModel.setFirstName(firstName);
            userModel.setLastName(lastName);
            firstNameEditText.setText(userModel.getFirstName());
            lastNameEditText.setText(userModel.getLastName());
        } catch (Exception ignore){}
    }

    private void getProfile(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constant.FireUser.TABLE).child(userModel.getUID());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setProfile(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError){}
        });
    }

    private void initClick(){
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
        userModel = new UserModel();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userModel.setUID(user.getUid());
            getProfile();
        }
    }

    private void init(){
        firstNameEditText = (EditText) findViewById(R.id.register_name_first);
        lastNameEditText = (EditText)findViewById(R.id.register_name_last);
        backImageView = (ImageView)findViewById(R.id.icon_back);
        proceedButton = (Button)findViewById(R.id.register_proceed);
    }

}

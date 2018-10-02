package companion.virtual.com.virtualcompanion;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

import companion.virtual.com.virtualcompanion.model.UserModel;
import companion.virtual.com.virtualcompanion.router.PageRouter;
import companion.virtual.com.virtualcompanion.utils.Constant;
import companion.virtual.com.virtualcompanion.utils.PageAnimation;
import companion.virtual.com.virtualcompanion.utils.date.DateConvertReadable;
import companion.virtual.com.virtualcompanion.utils.date.DateTime;


public class SetUpDescriptionActivity extends FragmentActivity {

    private TextView counterTextView;
    private EditText descriptionEditText;
    private Button doneButton;
    private UserModel userModel;
    private PageRouter pageRouter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_set_up_description);
        pageTransition();
        init();
        initValues();
        initClick();
    }

    private void setDescription(){
        try {
            String description = descriptionEditText.getText().toString();
            if(!description.equalsIgnoreCase("")){
                String tableString = Constant.FireUser.TABLE;
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child(tableString)
                        .child(userModel.getUID())
                        .child(Constant.FireUser.DESCRIPTION)
                        .setValue(description)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
//                                setNotification();
                                pageRouter.openMainPage();
                            }
                        });
            } else openNext();
        } catch (Exception ignore){}
    }

    private void openNext(){
        PageRouter pageRouter = new PageRouter(SetUpDescriptionActivity.this);
        pageRouter.openMainPage();
    }

    private void processCounter(){
        descriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
            @Override
            public void afterTextChanged(Editable editable){}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    showCharacters();
                } catch (Exception ignore){}
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showCharacters(){
        try {
            int DESCRIPTION_LIMIT = 100;
            counterTextView.setText(descriptionEditText.getText().toString().length()
                    + "/" + DESCRIPTION_LIMIT);
        } catch (Exception ignore){}
    }

    private void pageTransition(){
        PageAnimation pageAnimation = new PageAnimation(this);
        pageAnimation.fadeAnimation();
    }

    private void setProfile(DataSnapshot dataSnapshot){
        try {
            Map<String, String> dataMap = (Map<String, String>)dataSnapshot.getValue();
            String firstName = dataMap.get(Constant.FireUser.FIRST_NAME);
            String lastName = dataMap.get(Constant.FireUser.LAST_NAME);
            String profileURL = dataMap.get(Constant.FireUser.PROFILE_URL);
            userModel.setFirstName(firstName);
            userModel.setLastName(lastName);
            userModel.setProfileURL(profileURL);
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
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDescription();
            }
        });
    }

    private void initValues(){
        pageRouter = new PageRouter(this);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            finish();
        } else {
            userModel = new UserModel();
            userModel.setUID(currentUser.getUid());
            getProfile();
            processCounter();
            showCharacters();
        }
    }

    private void init(){
        counterTextView = (TextView)findViewById(R.id.setup_counter);
        descriptionEditText = (EditText)findViewById(R.id.setup_description_field);
        doneButton = (Button)findViewById(R.id.setup_done);
    }

}

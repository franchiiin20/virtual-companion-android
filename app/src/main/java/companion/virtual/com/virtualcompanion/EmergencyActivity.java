package companion.virtual.com.virtualcompanion;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import companion.virtual.com.virtualcompanion.adapter.EmergencyAdapter;
import companion.virtual.com.virtualcompanion.dialog.AddEmergencyDialog;
import companion.virtual.com.virtualcompanion.interfaces.CallBack;
import companion.virtual.com.virtualcompanion.model.EmergencyModel;
import companion.virtual.com.virtualcompanion.model.UserModel;
import companion.virtual.com.virtualcompanion.utils.Constant;
import companion.virtual.com.virtualcompanion.utils.PageAnimation;

public class EmergencyActivity extends FragmentActivity {

    private ImageView backImageView;
    private ImageView addUserImageView;
    private RelativeLayout emergencyRelativeLayout;
    private UserModel userModel;
    private RecyclerView emergencyRecyclerView;
    private List<EmergencyModel> emergencyModels;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_emergency);
        pageTransition();
        init();
        initValues();
        initClick();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.PermissionReference.LOCATION: {
                getLocationOfUser();
                break;
            } case Constant.PermissionReference.SMS: {
                getEmergencyList(false);
                break;
            }
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location){
            try {
                userModel.setLongitude(location.getLongitude() + "");
                userModel.setLatitude(location.getLatitude() + "");
                Log.d("LogsDialogs", "getLatitude: " + userModel.getLatitude());
                Log.d("LogsDialogs", "getLongitude: " + userModel.getLongitude());
            } catch (Exception ignore){
                Log.d("LogsDialogs", "Location: " + ignore);
            }
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){}
        @Override
        public void onProviderEnabled(String provider){}
        @Override
        public void onProviderDisabled(String provider){}
    };

    private void getLocationOfUser(){
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                assert locationManager != null;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locationListener);
            } else
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION}, Constant.PermissionReference.LOCATION);
        } catch (Exception ignore){
            Log.d("LogsDialogs", "getLocationOfUser: " + ignore);
        }
    }

    private void setEmergencyList(DataSnapshot dataSnapshot){
        try {
            Map<String, String> dataMap = (Map<String, String>)dataSnapshot.getValue();
            String name = dataMap.get(Constant.FireEmergency.NAME);
            String email = dataMap.get(Constant.FireEmergency.EMAIL);
            String mobile = dataMap.get(Constant.FireEmergency.MOBILE);
            String status = dataMap.get(Constant.FireEmergency.STATUS);
            if(status.equalsIgnoreCase(Constant.FixedValues.ACTIVE)){
                EmergencyModel emergencyModel = new EmergencyModel();
                emergencyModel.setId(dataSnapshot.getKey());
                emergencyModel.setName(name);
                emergencyModel.setEmail(email);
                emergencyModel.setMobile(mobile);
                emergencyModel.setUID(userModel.getUID());

                boolean flag = false;
                List<EmergencyModel> temp = new ArrayList<>();
                List<EmergencyModel> checker = emergencyModels;
                for(EmergencyModel row : checker){
                    if(row.getId().equalsIgnoreCase(emergencyModel.getId())){
                        flag = true;
                    } else {
                        temp.add(row);
                    }
                }
                if(flag)
                    emergencyModels = temp;
                emergencyModels.add(emergencyModel);

                EmergencyAdapter adapter = new EmergencyAdapter(this, emergencyModels, new CallBack() {
                    @Override
                    public void onClick() {
                        Toast.makeText(getApplicationContext(),
                                R.string.deleted, Toast.LENGTH_SHORT).show();
                        getEmergencyList(true);
                    }
                });
                emergencyRecyclerView.setAdapter(adapter);
                emergencyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                emergencyRecyclerView.setNestedScrollingEnabled(false);
            }
        } catch (Exception ignore){}
    }

    private void sendEmergencyContacts(DataSnapshot dataSnapshot){
        try {
            Map<String, String> dataMap = (Map<String, String>)dataSnapshot.getValue();
            String name = dataMap.get(Constant.FireEmergency.NAME);
            String mobile = dataMap.get(Constant.FireEmergency.MOBILE);
            String email = dataMap.get(Constant.FireEmergency.EMAIL);
            String status = dataMap.get(Constant.FireEmergency.STATUS);
            if(status.equalsIgnoreCase(Constant.FixedValues.ACTIVE)){
                String message = "Hi " + name + " ,\n" +
                        userModel.getFirstName() + " has sent an emergency SMS/Email to you. Do contact him/her with this number ASAP: " + mobile + ".";
                //if(!userModel.getLatitude().equalsIgnoreCase("") && !userModel.getLongitude().equalsIgnoreCase("")){
                //message = message.concat("Last known address: https://www.google.com.ph/maps/");
                //message = message.concat(userModel.getLatitude());
                //message = message.concat(",");
                //message = message.concat(userModel.getLongitude() + ",17z?hl=en");
                //}
                message = message.concat("\nMany thanks,\nVirtual Companion.");
                sendSMS(mobile, message);

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                String mainReferenceTable = Constant.FireEmergencyEmail.TABLE;
                String pushID = mDatabase.push().getKey();
                mDatabase.child(mainReferenceTable)
                        .child(pushID)
                        .child(Constant.FireEmergencyEmail.EMAIL)
                        .setValue(email);
                mDatabase.child(mainReferenceTable)
                        .child(pushID)
                        .child(Constant.FireEmergencyEmail.MOBILE)
                        .setValue(mobile);
                mDatabase.child(mainReferenceTable)
                        .child(pushID)
                        .child(Constant.FireEmergencyEmail.MESSAGE)
                        .setValue(message);
                mDatabase.child(mainReferenceTable)
                        .child(pushID)
                        .child(Constant.FireEmergencyEmail.STATUS)
                        .setValue(Constant.FixedValues.ACTIVE);
            }
        } catch (Exception ignore){
            Log.d("LogsDialogs", "sendEmergencyContacts: " + ignore);
        }
    }

    private void sendSMS(String phoneNumber, String message){
        try {
            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";
            PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                    new Intent(SENT), 0);
            PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                    new Intent(DELIVERED), 0);
            registerReceiver(new BroadcastReceiver(){
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()){
                        case Activity.RESULT_OK:
                            Toast.makeText(getBaseContext(), "SMS sent",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Toast.makeText(getBaseContext(), "Generic failure",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toast.makeText(getBaseContext(), "No service",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Toast.makeText(getBaseContext(), "Null PDU",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toast.makeText(getBaseContext(), "Radio off",
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter(SENT));
            registerReceiver(new BroadcastReceiver(){
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode()){
                        case Activity.RESULT_OK:
                            Toast.makeText(getBaseContext(), "SMS delivered",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(getBaseContext(), "SMS not delivered",
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter(DELIVERED));
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        } catch (Exception ignore){}
    }

    private void getEmergencyList(final boolean isList){
        try {
            Log.d("LogsDialogs", "getEmergencyList: ");
            boolean allowEmergency = false;
            if(isList){
                allowEmergency = true;
                emergencyModels = new ArrayList<>();
            }
            if(!isList){
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                    allowEmergency = true;
                } else
                    ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.SEND_SMS}, Constant.PermissionReference.SMS);
            }
            if(allowEmergency){
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(Constant.FireEmergency.TABLE)
                        .child(userModel.getUID());
                myRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if(isList) setEmergencyList(dataSnapshot);
                        else sendEmergencyContacts(dataSnapshot);
                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s){
                        if(isList) setEmergencyList(dataSnapshot);
                    }
                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot){}
                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s){}
                    @Override
                    public void onCancelled(DatabaseError databaseError){}
                });
            }
        } catch (Exception ignore){
            Log.d("LogsDialogs", "getEmergencyList-ignore: " + ignore);
        }
    }

    private void backAction(){
        try {
            finish();
            PageAnimation pageAnimation = new PageAnimation(this);
            pageAnimation.slideOutAnimation();
        } catch (Exception ignore){}
    }

    @Override
    public void onBackPressed() {
        backAction();
    }

    private void pageTransition(){
        PageAnimation pageAnimation = new PageAnimation(this);
        pageAnimation.slideInAnimation();
    }

    private void initClick(){
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backAction();
            }
        });
        addUserImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AddEmergencyDialog dialog = new AddEmergencyDialog(EmergencyActivity.this, userModel.getUID());
                    if(!dialog.isShowing()){
                        dialog.show();
                    }
                } catch (Exception ignore){}
            }
        });
        emergencyRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEmergencyList(false);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setProfile(DataSnapshot dataSnapshot){
        try {
            Map<String, String> dataMap = (Map<String, String>)dataSnapshot.getValue();
            String firstName = dataMap.get(Constant.FireUser.FIRST_NAME);
            String lastName = dataMap.get(Constant.FireUser.LAST_NAME);
            String email = dataMap.get(Constant.FireUser.EMAIL);
            String mobileNumber = dataMap.get(Constant.FireUser.MOBILE);
            userModel.setFirstName(firstName);
            userModel.setLastName(lastName);
            userModel.setEmail(email);
            userModel.setMobile(mobileNumber);
            getLocationOfUser();
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

    private void initValues(){
        try {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            userModel = new UserModel();
            if (user != null) {
                userModel.setUID(user.getUid());
                getProfile();
                getEmergencyList(true);
            } else {
                Toast.makeText(getApplicationContext(),
                        R.string.internet_error, Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (Exception ignore){}
    }

    private void init() {
        emergencyRecyclerView = (RecyclerView) findViewById(R.id.emergency_list);
        backImageView = (ImageView) findViewById(R.id.icon_back);
        addUserImageView = (ImageView) findViewById(R.id.add_contacts);
        emergencyRelativeLayout = (RelativeLayout) findViewById(R.id.emergency);
    }

}

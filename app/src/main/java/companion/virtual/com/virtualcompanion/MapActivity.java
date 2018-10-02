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
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import android.location.LocationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import companion.virtual.com.virtualcompanion.adapter.EmergencyAdapter;
import companion.virtual.com.virtualcompanion.adapter.UserAdapter;
import companion.virtual.com.virtualcompanion.adapter.UserEmAdapter;
import companion.virtual.com.virtualcompanion.interfaces.CallBack;
import companion.virtual.com.virtualcompanion.interfaces.LocationCallBack;
import companion.virtual.com.virtualcompanion.interfaces.LocationUserCallBack;
import companion.virtual.com.virtualcompanion.model.EmergencyModel;
import companion.virtual.com.virtualcompanion.model.UserModel;
import companion.virtual.com.virtualcompanion.router.PageRouter;
import companion.virtual.com.virtualcompanion.utils.Constant;
import companion.virtual.com.virtualcompanion.utils.ConstantUtils;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.android.gms.maps.CameraUpdateFactory.newCameraPosition;
import static java.lang.Float.parseFloat;

public class MapActivity extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    private static final double LATER_RADIUS_OF_SEARCH = 1000.00;

    private List<EmergencyModel> emergencyModels;
    private FirebaseAuth mAuth;
    private UserModel userModel;
    private GoogleMap googleMap;
    private MapView mapView;
    private LocationManager locationManager;
    private Bundle savedInstanceState;
    private PageRouter pageRouter;
    private RelativeLayout emergencyRelativeLayout;
    private RelativeLayout safeRelativeLayout;
    private RelativeLayout helperRelativeLayout;

    private CircleImageView profileCircleImageView;
    private ImageView logoutImageView;

    public CircleImageView profileRowCircleImageView;
    public TextView nameTextView;
    public TextView titleTextView;

    private LinearLayout emergencyCardLinearLayout;
    private Button emergencyCardButton;

    private LinearLayout addEmergencyLinearLayout;
    private RelativeLayout ringAlarmRelativeLayout;
    private RelativeLayout sendWarningRelativeLayout;

    private LinearLayout emergencyRequestListLinearLayout;
    private Button emergencyRequestListButton;
    private RecyclerView emergencyRequestRecyclerView;

//    private ArrayList<Marker> mMarkerArray;
    private Map<String, Marker> mMarkerArray = new HashMap<>();
    private List<UserModel> userModelList;
    private List<UserModel> userEmergencyModelList;
    private RecyclerView userListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map);
        init();
        initClicked();
        showMapsAndLocation();
    }

    private String calculateDistance(float lat1, float lon1, float lat2, float lon2)
    {
        float dLat = (float) Math.toRadians(lat2 - lat1);
        float dLon = (float) Math.toRadians(lon2 - lon1);
        float a =
                (float) (Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2));
        float c = (float) (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
        float d = 6371 * c;
        if(d < 1.0){
            return round((d * 1000), 2) + " meters";
        } else {
            return round(d, 2) + " kilometers";
        }
    }

    public double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    private void setListOfUsers(DataSnapshot dataSnapshot){
        try {
            Map<String, String> dataMap = (Map<String, String>)dataSnapshot.getValue();
            String firstName = dataMap.get(Constant.FireUser.FIRST_NAME);
            String lastName = dataMap.get(Constant.FireUser.LAST_NAME);
            String email = dataMap.get(Constant.FireUser.EMAIL);
            String mobileNumber = dataMap.get(Constant.FireUser.MOBILE);
            String profileURL = dataMap.get(Constant.FireUser.PROFILE_URL);
            String latitude = dataMap.get(Constant.FireUser.LATITUDE);
            String longitude = dataMap.get(Constant.FireUser.LONGITUDE);
            String description = dataMap.get(Constant.FireUser.DESCRIPTION);
            String privacy = dataMap.get(Constant.FireUser.PRIVACY);

            String distanceFloat = calculateDistance(Float.parseFloat(userModel.getLatitude()),Float.parseFloat(userModel.getLongitude()),
                    Float.parseFloat(latitude), Float.parseFloat(longitude));

            UserModel userModelTemp = new UserModel();
            userModelTemp.setUID(dataSnapshot.getKey());
            userModelTemp.setFirstName(firstName);
            userModelTemp.setLastName(lastName);
            userModelTemp.setEmail(email);
            userModelTemp.setMobile(mobileNumber);
            userModelTemp.setLatitude(latitude);
            userModelTemp.setLongitude(longitude);
            userModelTemp.setDescription("Distance: " + distanceFloat + ", " + description);
            userModelTemp.setProfileURL(profileURL);
            userModelTemp.setPrivacy(privacy);

            // Setting my current location.
            Location myCurrentLocation = new Location("point A");
            myCurrentLocation.setLatitude(Double.parseDouble(userModel.getLatitude()));
            myCurrentLocation.setLongitude(Double.parseDouble(userModel.getLongitude()));

            // Current location of a user.
            Location usersCurrentLocation = new Location("point B");
            usersCurrentLocation.setLatitude(Double.parseDouble(userModelTemp.getLatitude()));
            usersCurrentLocation.setLongitude(Double.parseDouble(userModelTemp.getLongitude()));

            float distance = myCurrentLocation.distanceTo(usersCurrentLocation);
            if (distance <= LATER_RADIUS_OF_SEARCH) {
                boolean flag = true;
                for (UserModel row : userModelList) {
                    if(row.getUID().equalsIgnoreCase(userModelTemp.getUID())){
                        flag = false;
                        break;
                    }
                }

                if(!userModelTemp.getUID().equalsIgnoreCase(userModel.getUID())){
                    if(flag){
                        Log.d("MapPager", "Not yet in the list.");
                        if(privacy.equalsIgnoreCase(Constant.FixedValues.ACTIVE)) {
                            userEmergencyModelList.add(userModelTemp);
                        }
                    } else {
                        Log.d("MapPager", "In list.");
                        List<UserModel> tempList1 = userEmergencyModelList;
                        userEmergencyModelList = new ArrayList<>();
                        for (UserModel row : tempList1) {
                            if(!row.getUID().equalsIgnoreCase(userModelTemp.getUID())){
                                if(row.getPrivacy().equalsIgnoreCase(Constant.FixedValues.ACTIVE)) {
                                    userEmergencyModelList.add(row);
                                }
                            }
                        }
                        if(privacy.equalsIgnoreCase(Constant.FixedValues.ACTIVE)) {
                            userEmergencyModelList.add(userModelTemp);
                        }
                    }
                }

                if(flag){
                    Log.d("MapPager", "Not yet in the list.");
                    userModelList.add(userModelTemp);
                } else {
                    Log.d("MapPager", "In list.");
                    List<UserModel> tempList = userModelList;
                    userModelList = new ArrayList<>();
                    for (UserModel row : tempList) {
                        if(!row.getUID().equalsIgnoreCase(userModelTemp.getUID())){
                            userModelList.add(row);
                        }
                    }
                    userModelList.add(userModelTemp);
                }

                UserAdapter userAdapter = new UserAdapter(userModelList, this, new LocationCallBack() {
                    @Override
                    public void onClick(String lat, String lon) {
                        try {
                            double latitude = Double.parseDouble(lat);
                            double longitude = Double.parseDouble(lon);
                            LatLng defaultLocation = new LatLng(latitude, longitude);
                            CameraPosition cameraPosition = new CameraPosition
                                    .Builder()
                                    .target(defaultLocation)
                                    .zoom(15)
                                    .build();
                            googleMap.moveCamera(newCameraPosition(cameraPosition));
                        } catch (Exception ignore){}
                    }
                });
                userListRecyclerView.setAdapter(userAdapter);
                userListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                userListRecyclerView.setNestedScrollingEnabled(false);

                if(privacy.equalsIgnoreCase(Constant.FixedValues.ACTIVE)){
                    UserEmAdapter userAdapterSecond = new UserEmAdapter(userEmergencyModelList, this, new LocationUserCallBack() {
                        @Override
                        public void onClick(String lat, String lon, UserModel temp) {
                            try {
                                double latitude = Double.parseDouble(lat);
                                double longitude = Double.parseDouble(lon);
                                LatLng defaultLocation = new LatLng(latitude, longitude);
                                CameraPosition cameraPosition = new CameraPosition
                                        .Builder()
                                        .target(defaultLocation)
                                        .zoom(15)
                                        .build();
                                googleMap.moveCamera(newCameraPosition(cameraPosition));
                                sendAcceptEmergency(temp);
                            } catch (Exception ignore){}
                        }
                    });
                    emergencyRequestRecyclerView.setAdapter(userAdapterSecond);
                    emergencyRequestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                    emergencyRequestRecyclerView.setNestedScrollingEnabled(false);
                    if(!userModelTemp.getUID().equalsIgnoreCase(userModel.getUID())) {
                        emergencyRequestListLinearLayout.setVisibility(View.VISIBLE);
                    }
                }

                if(!userModel.getUID().equalsIgnoreCase(userModelTemp.getUID())){
                    double latitudeF = Double.parseDouble(latitude);
                    double longitudeF = Double.parseDouble(longitude);

                    LatLng defaultLocation = new LatLng(latitudeF, longitudeF);
                    Marker previousMarker = mMarkerArray.get(userModelTemp.getUID());
                    if (previousMarker != null) {
                        previousMarker.setPosition(defaultLocation);
                    } else {
                        Marker marker = googleMap.addMarker(new MarkerOptions().position(defaultLocation)
                                .title(userModelTemp.getFirstName() + " " + userModelTemp.getLastName())
                                .snippet(userModelTemp.getDescription()));
                        marker.setTag(userModelTemp.getUID());
                        mMarkerArray.put(userModelTemp.getUID(), marker);
                    }
                }
            }
        } catch (Exception ignore){
            Log.d("MapPager", "ignore-setListOfUsers: ", ignore);
        }
    }

    private void sendAcceptEmergency(UserModel temp){
        try {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            String mainReferenceTable = Constant.FireUser.TABLE;
            mDatabase.child(mainReferenceTable)
                    .child(temp.getUID())
                    .child(Constant.FireUser.RESUME_URL)
                    .setValue(userModel.getUID());
            emergencyRequestListLinearLayout.setVisibility(View.GONE);
        } catch (Exception ignore){}
    }

    private void getListOfUsers(){
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(Constant.FireUser.TABLE);
            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    setListOfUsers(dataSnapshot);
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s){
                    setListOfUsers(dataSnapshot);
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot){}
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s){}
                @Override
                public void onCancelled(DatabaseError databaseError){}
            });
        } catch (Exception ignore){
            Log.d("MapPager", "ignore-getListOfUsers: ", ignore);
        }
    }

    private void setMapReady(GoogleMap mMap) {
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap = mMap;
                googleMap.getUiSettings().setMapToolbarEnabled(false);
                googleMap.getUiSettings().setZoomControlsEnabled(false);
                googleMap.getUiSettings().setCompassEnabled(false);
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                LatLng defaultLocation = new LatLng(location.getLatitude(), location.getLongitude());
                CameraPosition cameraPosition = new CameraPosition
                        .Builder()
                        .target(defaultLocation)
                        .zoom(15)
                        .build();
                googleMap.moveCamera(newCameraPosition(cameraPosition));
                getListOfUsers();
            }
        } catch (Exception ignore) {
            Log.d("MapPager", "ignore-setMapReady: " + ignore);
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location){
            try {
                // Set my current location locally.
                userModel.setLatitude(location.getLatitude() + "");
                userModel.setLongitude(location.getLongitude() + "");

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                String tableString = Constant.FireUser.TABLE;
                mDatabase.child(tableString)
                        .child(userModel.getUID())
                        .child(Constant.FireUser.LONGITUDE)
                        .setValue(location.getLongitude() + "");
                mDatabase.child(tableString)
                        .child(userModel.getUID())
                        .child(Constant.FireUser.LATITUDE)
                        .setValue(location.getLatitude() + "");

                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                Marker previousMarker = mMarkerArray.get(userModel.getUID());
                if (previousMarker != null) {
                    previousMarker.setPosition(currentLocation);
                } else {
                    Marker marker = googleMap.addMarker(new MarkerOptions().position(currentLocation)
                            .title(userModel.getFirstName() + " " + userModel.getLastName())
                            .snippet(userModel.getDescription()));
                    marker.setTag(userModel.getUID());
                    mMarkerArray.put(userModel.getUID(), marker);
                }
            } catch (Exception ignore){
                Log.d("MapPager", "ignore-onLocationChanged: " + ignore);
            }
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){}
        @Override
        public void onProviderEnabled(String provider){}
        @Override
        public void onProviderDisabled(String provider){}
    };

    private void showMapsAndLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mapView.onCreate(savedInstanceState);
                mapView.onResume();
                MapsInitializer.initialize(getApplicationContext());
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap mMap) {
                        setMapReady(mMap);
                    }
                });
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locationListener);
            } else
                ActivityCompat.requestPermissions(MapActivity.this, new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION}, ConstantUtils.PermissionReference.LOCATION);
        } catch (Exception ignore) {
            Log.d("MapPager", "ignore-showMapsAndLocation: " + ignore);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ConstantUtils.PermissionReference.LOCATION: {
                showMapsAndLocation();
                break;
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @SuppressLint("SetTextI18n")
    private void setSaviorsInformation(DataSnapshot dataSnapshot){
        try {
            Map<String, String> dataMap = (Map<String, String>)dataSnapshot.getValue();
            String firstName = dataMap.get(Constant.FireUser.FIRST_NAME);
            String lastName = dataMap.get(Constant.FireUser.LAST_NAME);
            String email = dataMap.get(Constant.FireUser.EMAIL);
            String mobileNumber = dataMap.get(Constant.FireUser.MOBILE);
            String profileURL = dataMap.get(Constant.FireUser.PROFILE_URL);
            String latitude = dataMap.get(Constant.FireUser.LATITUDE);
            String longitude = dataMap.get(Constant.FireUser.LONGITUDE);


            String distance = calculateDistance(Float.parseFloat(userModel.getLatitude()),Float.parseFloat(userModel.getLongitude()),
                    Float.parseFloat(latitude), Float.parseFloat(longitude));

            nameTextView.setText(firstName + " " + lastName);
            titleTextView.setText("Distance: " + distance + " Email: " + email + " Mobile: " + mobileNumber);

            helperRelativeLayout.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(profileURL)
                    .thumbnail(0.5f)
                    .into(profileRowCircleImageView);
        } catch (Exception ignore){}
    }

    private void getSaviorsInformation(String uid){
        try {
            helperRelativeLayout.setVisibility(View.GONE);
            if(!uid.equalsIgnoreCase("")){
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(Constant.FireUser.TABLE).child(uid);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        setSaviorsInformation(dataSnapshot);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError){}
                });
            }
        } catch (Exception ignore){}
    }

    @SuppressLint("SetTextI18n")
    private void setProfile(DataSnapshot dataSnapshot){
        try {
            Map<String, String> dataMap = (Map<String, String>)dataSnapshot.getValue();
            String firstName = dataMap.get(Constant.FireUser.FIRST_NAME);
            String lastName = dataMap.get(Constant.FireUser.LAST_NAME);
            String email = dataMap.get(Constant.FireUser.EMAIL);
            String mobileNumber = dataMap.get(Constant.FireUser.MOBILE);
            String profileURL = dataMap.get(Constant.FireUser.PROFILE_URL);
            String latitude = dataMap.get(Constant.FireUser.LATITUDE);
            String longitude = dataMap.get(Constant.FireUser.LONGITUDE);
            String description = dataMap.get(Constant.FireUser.DESCRIPTION);
            String privacy = dataMap.get(Constant.FireUser.PRIVACY);
            String resume = dataMap.get(Constant.FireUser.RESUME_URL);
            userModel.setFirstName(firstName);
            userModel.setLastName(lastName);
            userModel.setEmail(email);
            userModel.setMobile(mobileNumber);
            userModel.setLatitude(latitude);
            userModel.setLongitude(longitude);
            userModel.setDescription(description);
            userModel.setPrivacy(privacy);
            userModel.setResumeURL(resume);
            if(privacy.equalsIgnoreCase(Constant.FixedValues.ACTIVE)){
                emergencyCardLinearLayout.setVisibility(View.VISIBLE);
            } else {
                emergencyCardLinearLayout.setVisibility(View.GONE);
            }
            getSaviorsInformation(resume);
            Glide.with(this)
                    .load(profileURL)
                    .thumbnail(0.5f)
                    .into(profileCircleImageView);
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

    private void sendEmergencyToClosePeople(UserModel temp){
        try {
            // Setting my current location.
            Location myCurrentLocation = new Location("point A");
            myCurrentLocation.setLatitude(Double.parseDouble(userModel.getLatitude()));
            myCurrentLocation.setLongitude(Double.parseDouble(userModel.getLongitude()));

            // Current location of a user.
            Location usersCurrentLocation = new Location("point B");
            usersCurrentLocation.setLatitude(Double.parseDouble(temp.getLatitude()));
            usersCurrentLocation.setLongitude(Double.parseDouble(temp.getLongitude()));

            float distance = myCurrentLocation.distanceTo(usersCurrentLocation);
            if (distance <= LATER_RADIUS_OF_SEARCH) {
                String message = "Hi " + temp.getFirstName() + " ,\n" +
                        userModel.getFirstName() + " has sent an emergency SMS/Email to you. Do contact him/her with this number ASAP: " + userModel.getMobile() + ".";
                //if(!userModel.getLatitude().equalsIgnoreCase("") && !userModel.getLongitude().equalsIgnoreCase("")){
                //message = message.concat("Last known address: https://www.google.com.ph/maps/");
                //message = message.concat(userModel.getLatitude());
                //message = message.concat(",");
                //message = message.concat(userModel.getLongitude() + ",17z?hl=en");
                //}
                message = message.concat("\nMany thanks,\nVirtual Companion.");
                sendSMS(temp.getMobile(), message, false);

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                String mainReferenceTable = Constant.FireEmergencyEmail.TABLE;
                String pushID = mDatabase.push().getKey();
                mDatabase.child(mainReferenceTable)
                        .child(pushID)
                        .child(Constant.FireEmergencyEmail.EMAIL)
                        .setValue(temp.getEmail());
                mDatabase.child(mainReferenceTable)
                        .child(pushID)
                        .child(Constant.FireEmergencyEmail.MOBILE)
                        .setValue(temp.getMobile());
                mDatabase.child(mainReferenceTable)
                        .child(pushID)
                        .child(Constant.FireEmergencyEmail.MESSAGE)
                        .setValue(message);
                mDatabase.child(mainReferenceTable)
                        .child(pushID)
                        .child(Constant.FireEmergencyEmail.STATUS)
                        .setValue(Constant.FixedValues.ACTIVE);

                String notificationTable = Constant.FirePushNotification.TABLE;
                mDatabase.child(notificationTable)
                        .child(pushID)
                        .child(Constant.FirePushNotification.ID)
                        .setValue(pushID);
                mDatabase.child(notificationTable)
                        .child(pushID)
                        .child(Constant.FirePushNotification.TO)
                        .setValue(temp.getUID());
            }
        } catch (Exception ignore){
            Log.d("LogsDialogs", "ignore-sendEmergencyToClosePeople: ", ignore);
        }
    }

    private void sendWarningToClosePeople(UserModel temp){
        try {
            // Setting my current location.
            Location myCurrentLocation = new Location("point A");
            myCurrentLocation.setLatitude(Double.parseDouble(userModel.getLatitude()));
            myCurrentLocation.setLongitude(Double.parseDouble(userModel.getLongitude()));

            // Current location of a user.
            Location usersCurrentLocation = new Location("point B");
            usersCurrentLocation.setLatitude(Double.parseDouble(temp.getLatitude()));
            usersCurrentLocation.setLongitude(Double.parseDouble(temp.getLongitude()));

            float distance = myCurrentLocation.distanceTo(usersCurrentLocation);
            if (distance <= LATER_RADIUS_OF_SEARCH) {
                String message = "Hi " + temp.getFirstName() + " ,\n" +
                        userModel.getFirstName() + " has sent a warning SMS/Email to you. Do contact him/her to see if they are OK: " + userModel.getMobile() + ".";
                //if(!userModel.getLatitude().equalsIgnoreCase("") && !userModel.getLongitude().equalsIgnoreCase("")){
                //message = message.concat("Last known address: https://www.google.com.ph/maps/");
                //message = message.concat(userModel.getLatitude());
                //message = message.concat(",");
                //message = message.concat(userModel.getLongitude() + ",17z?hl=en");
                //}
                message = message.concat("\nMany thanks,\nVirtual Companion.");
                sendSMS(temp.getMobile(), message, false);

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                String mainReferenceTable = Constant.FireEmergencyEmail.TABLE;
                String pushID = mDatabase.push().getKey();
                mDatabase.child(mainReferenceTable)
                        .child(pushID)
                        .child(Constant.FireEmergencyEmail.EMAIL)
                        .setValue(temp.getEmail());
                mDatabase.child(mainReferenceTable)
                        .child(pushID)
                        .child(Constant.FireEmergencyEmail.MOBILE)
                        .setValue(temp.getMobile());
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
            Log.d("LogsDialogs", "ignore-sendEmergencyToClosePeople: ", ignore);
        }
    }

    private void getEmergencyList(final boolean isList, final boolean isWarning){
        try {
            Log.d("LogsDialogs", "getEmergencyList: ");
            boolean allowEmergency = false;
            if(isList){
                allowEmergency = true;
                emergencyModels = new ArrayList<>();
            }
            if(!isList){
                if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                    allowEmergency = true;
                } else
                    ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.SEND_SMS}, Constant.PermissionReference.SMS);
            }
            if(!isList){
                for (UserModel row : userModelList) {
                    if(isWarning){
                        sendWarningToClosePeople(row);
                    } else {
                        sendEmergencyToClosePeople(row);
                    }
                }
                setPrivacyStatus(true);
            }
            if(allowEmergency){
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(Constant.FireEmergency.TABLE)
                        .child(userModel.getUID());
                myRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if(isList){
                            sendSafeContacts(dataSnapshot);
                        } else if(isWarning){
                            sendWarningContacts(dataSnapshot);
                        } else {
                            sendEmergencyContacts(dataSnapshot);
                        }
                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s){
//                        if(isList) sendSafeContacts(dataSnapshot);
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

    private void sendSafeContacts(DataSnapshot dataSnapshot){
        try {
            Map<String, String> dataMap = (Map<String, String>)dataSnapshot.getValue();
            String name = dataMap.get(Constant.FireEmergency.NAME);
            String mobile = dataMap.get(Constant.FireEmergency.MOBILE);
            String email = dataMap.get(Constant.FireEmergency.EMAIL);
            String status = dataMap.get(Constant.FireEmergency.STATUS);
            if(status.equalsIgnoreCase(Constant.FixedValues.ACTIVE)){
                String message = "Hi " + name + " ,\n" +
                        userModel.getFirstName() + " has tagged himself/herself safe. Contact number: " + mobile + ".";
//                if(!userModel.getLatitude().equalsIgnoreCase("") && !userModel.getLongitude().equalsIgnoreCase("")){
//                message = message.concat("Last known address: https://www.google.com.ph/maps/");
//                message = message.concat(userModel.getLatitude());
//                message = message.concat(",");
//                message = message.concat(userModel.getLongitude() + ",17z?hl=en");
//                }
                message = message.concat("\nMany thanks,\nVirtual Companion.");
                sendSMS(mobile, message, true);

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

    private void sendWarningContacts(DataSnapshot dataSnapshot){
        try {
            Map<String, String> dataMap = (Map<String, String>)dataSnapshot.getValue();
            String name = dataMap.get(Constant.FireEmergency.NAME);
            String mobile = dataMap.get(Constant.FireEmergency.MOBILE);
            String email = dataMap.get(Constant.FireEmergency.EMAIL);
            String status = dataMap.get(Constant.FireEmergency.STATUS);
            if(status.equalsIgnoreCase(Constant.FixedValues.ACTIVE)){
                String message = "Hi " + name + " ,\n" +
                        userModel.getFirstName() + " has sent a warning SMS/Email to you. Do contact him/her with this number to check if they are OK: " + mobile + ".";
                //if(!userModel.getLatitude().equalsIgnoreCase("") && !userModel.getLongitude().equalsIgnoreCase("")){
                //message = message.concat("Last known address: https://www.google.com.ph/maps/");
                //message = message.concat(userModel.getLatitude());
                //message = message.concat(",");
                //message = message.concat(userModel.getLongitude() + ",17z?hl=en");
                //}
                message = message.concat("\nMany thanks,\nVirtual Companion.");
                sendSMS(mobile, message, false);

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
                sendSMS(mobile, message, false);

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

    private void sendSMS(String phoneNumber, String message, boolean isSafe){
        try {
            String messageSent = "We have now sent your message.";
            if(isSafe){
                messageSent = "We're glad that you have arrived safely. Till next time.";
            }
            final String messageSentFinal = messageSent;
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
                            Toast.makeText(getBaseContext(), "" + messageSentFinal,
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

    private void soundAlarm(){
        Log.d("JustLgos", "soundAlarm");
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), notification);
            mp.start();
        } catch (Exception ignore){
            Log.d("JustLgos", "ignore: ", ignore);
        }
    }

    private void setPrivacyStatus(boolean action){
        try {
            String status = Constant.FixedValues.DEACTIVATE;
            if(action){
                status = Constant.FixedValues.ACTIVE;
            }
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            String mainReferenceTable = Constant.FireUser.TABLE;
            mDatabase.child(mainReferenceTable)
                    .child(userModel.getUID())
                    .child(Constant.FireUser.PRIVACY)
                    .setValue(status);
            mDatabase.child(mainReferenceTable)
                    .child(userModel.getUID())
                    .child(Constant.FireUser.RESUME_URL)
                    .setValue("");
        } catch (Exception ignore){}
    }

    private void initClicked(){
        try {
            sendWarningRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getEmergencyList(false, true);
                }
            });
            ringAlarmRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    soundAlarm();
                }
            });
            addEmergencyLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pageRouter.openEmergencyScreen();
                }
            });
            safeRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getEmergencyList(true, false);
                }
            });
            emergencyRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getEmergencyList(false, false);
                }
            });
            logoutImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pageRouter.openSettingsPage();
                }
            });
            emergencyCardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    emergencyCardLinearLayout.setVisibility(View.GONE);
                    setPrivacyStatus(false);
                }
            });
            emergencyRequestListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    emergencyRequestListLinearLayout.setVisibility(View.GONE);
                }
            });
        } catch (Exception ignore){}
    }

    private void init() {
        emergencyRequestListLinearLayout = (LinearLayout) findViewById(R.id.card_accept_emergency);
        emergencyRequestListButton = (Button) findViewById(R.id.accept_emergency);
        emergencyRequestRecyclerView = (RecyclerView) findViewById(R.id.user_emergency_list);

        profileRowCircleImageView = (CircleImageView) findViewById(R.id.profile);
        nameTextView = (TextView) findViewById(R.id.name);
        titleTextView = (TextView) findViewById(R.id.title);

        helperRelativeLayout = (RelativeLayout) findViewById(R.id.helper);
        addEmergencyLinearLayout = (LinearLayout) findViewById(R.id.add_emergency);
        ringAlarmRelativeLayout = (RelativeLayout) findViewById(R.id.alarm);
        sendWarningRelativeLayout = (RelativeLayout) findViewById(R.id.warning);
        emergencyCardLinearLayout = (LinearLayout) findViewById(R.id.card_emergency);
        emergencyCardButton = (Button) findViewById(R.id.cancel_emergency);
        userListRecyclerView = (RecyclerView) findViewById(R.id.user_list);
        safeRelativeLayout = (RelativeLayout) findViewById(R.id.safe);
        emergencyRelativeLayout = (RelativeLayout) findViewById(R.id.emergency);
        mapView = (MapView) findViewById(R.id.google_map);
        logoutImageView = (ImageView) findViewById(R.id.ic_password);
        profileCircleImageView = (CircleImageView) findViewById(R.id.menu_photo);
        helperRelativeLayout.setVisibility(View.GONE);
        userModelList = new ArrayList<>();
        userEmergencyModelList = new ArrayList<>();
        pageRouter = new PageRouter(this);
        userModel = new UserModel();
        mAuth = FirebaseAuth.getInstance();
        try {
            if(mAuth != null){
                userModel.setUID(mAuth.getUid());
                getProfile();
            }
        } catch (Exception ignore){}
    }

}

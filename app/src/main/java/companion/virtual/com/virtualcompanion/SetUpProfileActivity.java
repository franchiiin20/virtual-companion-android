package companion.virtual.com.virtualcompanion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import companion.virtual.com.virtualcompanion.dialog.PhotoDialog;
import companion.virtual.com.virtualcompanion.dialog.SpinnerDialog;
import companion.virtual.com.virtualcompanion.model.UserModel;
import companion.virtual.com.virtualcompanion.router.PageRouter;
import companion.virtual.com.virtualcompanion.utils.Constant;
import companion.virtual.com.virtualcompanion.utils.PageAnimation;
import companion.virtual.com.virtualcompanion.utils.date.DateConvertReadable;
import companion.virtual.com.virtualcompanion.utils.date.DateTime;
import de.hdodenhof.circleimageview.CircleImageView;

public class SetUpProfileActivity extends FragmentActivity {

    private Button continueButton;
    private RelativeLayout addRelativeLayout;
    private CircleImageView profileCircleImageView;
    private UserModel userModel;
    private SpinnerDialog spinnerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setup_profile);
        pageTransition();
        init();
        initValues();
        initClick();
    }

    private void setProfileImageURL(UploadTask.TaskSnapshot taskSnapshot){
        @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
        try {
            DatabaseReference mDatabase = FirebaseDatabase
                    .getInstance().getReference();
            userModel.setProfileURL(downloadUrl.toString());
            String userUID = userModel.getUID();
            if(!userUID.equalsIgnoreCase("")){
                mDatabase.child(Constant.FireUser.TABLE)
                        .child(userUID)
                        .child(Constant.FireUser.PROFILE_URL)
                        .setValue(userModel.getProfileURL());
            }
            Glide.with(this)
                    .load(downloadUrl)
                    .thumbnail(0.5f)
                    .into(profileCircleImageView);
            spinnerDialog.dismiss();
            continueButton.setVisibility(View.VISIBLE);
        } catch (Exception ignore){}
    }

    private void photoErrorMessage(){
        spinnerDialog.dismiss();
        Toast.makeText(getApplicationContext(),
                R.string.photo_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constant.CallBackReference.GALLERY
                && resultCode == RESULT_OK){
            if(!spinnerDialog.isShowing()){
                spinnerDialog.show();
            }
            spinnerDialog.setCancelable(false);
            spinnerDialog.setCanceledOnTouchOutside(false);
            Uri uri = data.getData();
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            String databaseString = mDatabase.push().getKey();
            StorageReference storageRef = FirebaseStorage.getInstance()
                    .getReferenceFromUrl(Constant.CloudReference.STORAGE_PROFILE
                            + "/" + userModel.getUID());
            StorageReference filePath = storageRef.child(databaseString);
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    setProfileImageURL(taskSnapshot);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    photoErrorMessage();
                }
            });
        } else if(requestCode == Constant.CallBackReference.CAMERA
                && resultCode == RESULT_OK){
            if(!spinnerDialog.isShowing()){
                spinnerDialog.show();
            }
            spinnerDialog.setCancelable(false);
            spinnerDialog.setCanceledOnTouchOutside(false);
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] dataBAOS = byteArrayOutputStream.toByteArray();
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            String databaseString = mDatabase.push().getKey();
            StorageReference storageRef = FirebaseStorage.getInstance()
                    .getReferenceFromUrl(Constant.CloudReference.STORAGE_PROFILE
                            + "/" + userModel.getUID());
            StorageReference imagesRef = storageRef.child(databaseString);
            UploadTask uploadTask = imagesRef.putBytes(dataBAOS);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    photoErrorMessage();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    setProfileImageURL(taskSnapshot);
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.PermissionReference.CAMERA: {
                callCameraIntent();
                break;
            } case Constant.PermissionReference.GALLERY: {
                callGalleryIntent();
                break;
            }
        }
    }

    public void callCameraIntent(){
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, Constant.CallBackReference.CAMERA);
        } catch (Exception ignore){}
    }

    public void callGalleryIntent(){
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, Constant.CallBackReference.GALLERY);
        } catch (Exception ignore){}
    }

    private void getPhotoUploadOption(){
        try {
            PhotoDialog photoDialog = new PhotoDialog(this);
            if(!photoDialog.isShowing()){
                photoDialog.show();
            }
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
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PageRouter pageRouter = new PageRouter(SetUpProfileActivity.this);
                pageRouter.openSetUpDescriptionPage();
            }
        });
        addRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhotoUploadOption();
            }
        });
        profileCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhotoUploadOption();
            }
        });
    }

    private void initValues(){
        continueButton.setVisibility(View.GONE);
        spinnerDialog = new SpinnerDialog(this);
        userModel = new UserModel();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userModel.setUID(user.getUid());
            getProfile();
        }
    }

    private void init(){
        continueButton = (Button)findViewById(R.id.setup_profile_continue);
        addRelativeLayout = (RelativeLayout)findViewById(R.id.photo_edit);
        profileCircleImageView = (CircleImageView)findViewById(R.id.profile_icon);
    }

}

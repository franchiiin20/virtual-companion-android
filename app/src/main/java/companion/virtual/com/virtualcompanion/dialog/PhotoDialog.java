package companion.virtual.com.virtualcompanion.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import companion.virtual.com.virtualcompanion.R;
import companion.virtual.com.virtualcompanion.utils.CheckerUtils;
import companion.virtual.com.virtualcompanion.utils.Constant;


public class PhotoDialog extends Dialog {

    private Activity activity;
    private LinearLayout cameraLinearLayout;
    private LinearLayout deviceLinearLayout;

    public PhotoDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_photo);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        setInit();
        setOnClickCommand();
    }

    private void callCameraIntent(){
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            activity.startActivityForResult(intent,
                    Constant.CallBackReference.CAMERA);
            dismiss();
        } catch (Exception ignore){}
    }

    private void callGalleryIntent(){
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            activity.startActivityForResult(intent,
                    Constant.CallBackReference.GALLERY);
            dismiss();
        } catch (Exception ignore){}
    }

    private void setOnClickCommand(){
        cameraLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckerUtils.CheckVersion()){
                    if(CheckerUtils.CheckCamera(activity)){
                        ActivityCompat.requestPermissions(activity,
                                new String[]{android.Manifest.permission.CAMERA},
                                Constant.PermissionReference.CAMERA);
                        dismiss();
                    } else {
                        callCameraIntent();
                    }
                } else {
                    callCameraIntent();
                }
            }
        });
        deviceLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckerUtils.CheckVersion()){
                    if(CheckerUtils.CheckStorage(activity)){
                        ActivityCompat.requestPermissions(activity,
                                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                Constant.PermissionReference.GALLERY);
                        dismiss();
                    } else {
                        callGalleryIntent();
                    }
                } else {
                    callGalleryIntent();
                }
            }
        });
    }

    private void setInit(){
        cameraLinearLayout = (LinearLayout)findViewById(R.id.option_camera);
        deviceLinearLayout = (LinearLayout)findViewById(R.id.option_device);
    }
}

package companion.virtual.com.virtualcompanion.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import companion.virtual.com.virtualcompanion.R;
import companion.virtual.com.virtualcompanion.interfaces.RegisterCallBack;


public class RegisterCancelDialog extends Dialog {

    private RegisterCallBack registerCallBack;
    private Activity activity;
    private Button yesBottom;
    private Button noBottom;

    public RegisterCancelDialog(Activity activity, RegisterCallBack registerCallBack) {
        super(activity);
        this.activity = activity;
        this.registerCallBack = registerCallBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_register_cancel);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setInit();
        setOnClickCommand();
    }

    private void setOnClickCommand(){
        yesBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerCallBack.onClick();
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
        yesBottom = (Button)findViewById(R.id.register_cancel_yes);
        noBottom = (Button)findViewById(R.id.register_cancel_no);
    }
}

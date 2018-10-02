package companion.virtual.com.virtualcompanion.adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.List;

import companion.virtual.com.virtualcompanion.R;
import companion.virtual.com.virtualcompanion.holder.EmergencyHolder;
import companion.virtual.com.virtualcompanion.interfaces.CallBack;
import companion.virtual.com.virtualcompanion.model.EmergencyModel;
import companion.virtual.com.virtualcompanion.utils.Constant;


public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyHolder> {

    private List<EmergencyModel> emergencyModels = Collections.emptyList();
    private Activity activity;
    private CallBack callBack;

    public EmergencyAdapter(Activity activity, List<EmergencyModel> emergencyModels, CallBack callBack){
        this.emergencyModels = emergencyModels;
        this.activity = activity;
        this.callBack = callBack;
    }

    @Override
    public EmergencyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_emergency, parent, false);
        return new EmergencyHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(EmergencyHolder holder, int position) {
        final int positionFinal = position;
        holder.nameTextView.setText(emergencyModels.get(position).getName());
        holder.emailTextView.setText(emergencyModels.get(position).getEmail());
        holder.mobileTextView.setText(emergencyModels.get(position).getMobile());
        try {
           holder.cardCardView.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View view) {
                   try {
                       DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                       mDatabase.child(Constant.FireEmergency.TABLE)
                               .child(emergencyModels.get(positionFinal).getUID())
                               .child(emergencyModels.get(positionFinal).getId())
                               .setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               callBack.onClick();
                           }
                       });
                   } catch (Exception ignore){}
                   return false;
               }
           });
        } catch (Exception ignore){}
    }

    @Override
    public int getItemCount() {
        return emergencyModels.size();
    }
}

package companion.virtual.com.virtualcompanion.adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Collections;
import java.util.List;

import companion.virtual.com.virtualcompanion.R;
import companion.virtual.com.virtualcompanion.holder.UserHolder;
import companion.virtual.com.virtualcompanion.interfaces.CallBack;
import companion.virtual.com.virtualcompanion.interfaces.LocationCallBack;
import companion.virtual.com.virtualcompanion.model.UserModel;


public class UserAdapter extends RecyclerView.Adapter<UserHolder> {

    private List<UserModel> userModelList = Collections.emptyList();
    private Activity activity;
    private LocationCallBack callBack;

    public UserAdapter(List<UserModel> userModelList, Activity activity, LocationCallBack callBack){
        this.userModelList = userModelList;
        this.activity = activity;
        this.callBack = callBack;
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_user, parent, false);
        return new UserHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
        final int positionFinal = position;
        try {
            holder.nameTextView.setText(userModelList.get(position).getFirstName() + " " + userModelList.get(position).getLastName());
            holder.titleTextView.setText(userModelList.get(position).getDescription());
            Glide.with(activity)
                    .load(userModelList.get(position).getProfileURL())
                    .into(holder.profileCircleImageView);
        } catch (Exception ignore){}
        holder.cardCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    callBack.onClick(userModelList.get(positionFinal).getLatitude(),
                            userModelList.get(positionFinal).getLongitude());
                } catch (Exception ignore){}
            }
        });
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }
}

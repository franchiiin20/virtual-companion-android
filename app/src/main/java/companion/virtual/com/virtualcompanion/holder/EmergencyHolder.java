package companion.virtual.com.virtualcompanion.holder;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import companion.virtual.com.virtualcompanion.R;


public class EmergencyHolder extends RecyclerView.ViewHolder {

    public CardView cardCardView;
    public TextView nameTextView;
    public TextView mobileTextView;
    public TextView emailTextView;

    public EmergencyHolder(View itemView) {
        super(itemView);
        nameTextView = (TextView)itemView.findViewById(R.id.emergency_name);
        mobileTextView = (TextView)itemView.findViewById(R.id.emergency_mobile);
        emailTextView = (TextView)itemView.findViewById(R.id.emergency_email);
        cardCardView = (CardView)itemView.findViewById(R.id.card);
    }
}

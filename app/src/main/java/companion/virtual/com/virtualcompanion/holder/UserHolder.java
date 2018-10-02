package companion.virtual.com.virtualcompanion.holder;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import companion.virtual.com.virtualcompanion.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserHolder extends RecyclerView.ViewHolder {

    public CircleImageView profileCircleImageView;
    public TextView nameTextView;
    public TextView titleTextView;
    public CardView cardCardView;

    public UserHolder(View itemView) {
        super(itemView);
        profileCircleImageView = (CircleImageView)itemView.findViewById(R.id.profile);
        nameTextView = (TextView)itemView.findViewById(R.id.name);
        titleTextView = (TextView)itemView.findViewById(R.id.title);
        cardCardView = (CardView)itemView.findViewById(R.id.card);
    }
}

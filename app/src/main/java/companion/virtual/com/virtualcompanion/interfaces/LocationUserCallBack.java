package companion.virtual.com.virtualcompanion.interfaces;

import companion.virtual.com.virtualcompanion.model.UserModel;

public interface LocationUserCallBack {
    public void onClick(String lat, String lon, UserModel userModel);
}
package companion.virtual.com.virtualcompanion.utils;



public class ConstantUtils {

    public class FixedData {
        public static final String SHARE_LINK = "https://www.piesy.club/post/";
        public static final String SEARCH_GROUP = "SG";
        public static final String SEARCH_PROFILE = "SP";
        public static final String GENDER_MALE = "M";
        public static final String GENDER_FEMALE = "F";
        public static final String STATUS_ACTIVE = "AC";
        public static final String STATUS_DEACTIVE = "DC";
        public static final String EMPTY = "";
        public static final String POST_NORM = "NORM";
        public static final String DEFAULT_COURGE = "id_01";
        public static final String NOTIFCATION_PROFILE = "PR";
        public static final String NOTIFCATION_GROUP = "GP";
        public static final String NOTIFCATION_POST = "PT";
        public static final String NOTIFCATION_COMMENT = "CM";
        public static final String CREATED_BY = "Created by: ";
        public static final String ENC = "UTF-8";
    }

    public class FireMessages {
        public static final String TABLE = "messages";
        public static final String TIMESTAMP = "timestamp";
        public static final String REFERENCE = "reference";
        public static final String DESCRIPTION = "description";
        public static final String PHOTO = "photoURL";
        public static final String STATUS = "status";
    }

    public class FireConversation {
        public static final String TABLE = "conversation";
        public static final String TIMESTAMP = "timestamp";
        public static final String REFERENCE = "reference";
        public static final String NAME = "name";
        public static final String PROFILE = "profileURL";
        public static final String TYPE = "type";
        public static final String DESCRIPTION = "description";
        public static final String CREATED = "createdBy";
        public static final String SEEN = "seen";
        public static final String STATUS = "status";
    }

    public class PermissionReference {
        public static final int CAMERA = 11;
        public static final int GALLERY = 22;
        public static final int LOCATION = 33;
        public static final int SMS = 66;
    }

}

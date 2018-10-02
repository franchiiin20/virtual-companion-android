package companion.virtual.com.virtualcompanion.utils;

public class Constant {

    public class CloudReference {
        public static final String STORAGE_PROFILE = "gs://virtual-companion-216904.appspot.com/profile";
        public static final String STORAGE_RESUME = "gs://virtual-companion-216904.appspot.com/resume";
    }

    public class IntentParams {
        public static final String MESSAGE_REFERERENCE = "MESSAGE_REFERERENCE";
        public static final String INTENT_REGISTER = "INTENT_REGISTER";
        public static final String INTENT_CONTRACT = "INTENT_CONTRACT";
        public static final String INTENT_POST = "INTENT_POST";
        public static final String INTENT_APPLY = "INTENT_APPLY";
        public static final String INTENT_USER = "INTENT_USER";
    }

    public class FixedValues {
        public static final String NOTIFICATION_CONTRACT = "contract";
        public static final String NOTIFICATION_SCHEDULE = "schedule";
        public static final String NOTIFICATION_REJECT = "reject";
        public static final String NOTIFICATION_PERSONAL = "personal";
        public static final String NOTIFICATION_APPLICANT = "applicant";
        public static final String POST_TYPE_HOUSE = "house";
        public static final String POST_TYPE_FULL = "full";
        public static final String POST_TYPE_PART = "part";
        public static final String TYPE_EMPLOYER = "employer";
        public static final String TYPE_SEEKER = "seeker";
        public static final String TYPE_AGENCY = "agency";
        public static final String DONE = "DN";
        public static final String ACTIVE = "AC";
        public static final String DEACTIVATE = "DC";
        public static final String PENDING = "PN";
        public static final String CONTENT_TYPE = "CONTENT_TYPE";
        public static final String FILE_PATH = "com.sec.android.app.myfiles.PICK_DATA";
        public static final String FILE_UPLOAD_TITLE = "Open file";
    }

    public class PermissionReference {
        public static final int CAMERA = 11;
        public static final int GALLERY = 22;
        public static final int LOCATION = 33;
        public static final int SMS = 66;
    }

    public class CallBackReference {
        public static final int CAMERA = 1;
        public static final int GALLERY = 2;
        public static final int LOCATION = 3;
    }

    public class FirePushNotification {
        public static final String TABLE = "pushNotification";
        public static final String ID = "ID";
        public static final String TO = "userID";
    }

    public class FireUser {
        public static final String TABLE = "users";
        public static final String FIRST_NAME = "firstName";
        public static final String LAST_NAME = "lastName";
        public static final String MOBILE = "mobileNumber";
        public static final String EMAIL = "email";
        public static final String PROFILE_URL = "profileURL";
        public static final String RESUME_URL = "resumeURL";
        public static final String DESCRIPTION = "description";
        public static final String TYPE = "type";
        public static final String UID = "UID";
        public static final String PRIVACY = "privacy";
        public static final String STATUS = "status";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
    }

    public class FireEmergency {
        public static final String TABLE = "emergency";
        public static final String TIMESTAMP = "timestamp";
        public static final String UID = "uid";
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String MOBILE = "mobile";
        public static final String EMAIL = "email";
        public static final String STATUS = "status";
    }

    public class FireEmergencyEmail {
        public static final String TABLE = "emergencyEmailHistory";
        public static final String EMAIL = "email";
        public static final String MOBILE = "mobile";
        public static final String MESSAGE = "message";
        public static final String STATUS = "status";
    }

}

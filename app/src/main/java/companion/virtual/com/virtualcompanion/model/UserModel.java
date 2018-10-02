package companion.virtual.com.virtualcompanion.model;


import java.io.Serializable;


@SuppressWarnings("serial")
public class UserModel implements Serializable {

    private String UID;
    private String firstName;
    private String lastName;
    private String mobile;
    private String email;
    private String profileURL;
    private String resumeURL;
    private String type;
    private String description;
    private String status;
    private String privacy;
    private String verificationString;
    private String longitude;
    private String latitude;

    public UserModel(){
        this.UID = "";
        this.firstName = "";
        this.lastName = "";
        this.mobile = "";
        this.email = "";
        this.profileURL = "";
        this.resumeURL = "";
        this.type = "";
        this.description = "";
        this.status = "";
        this.privacy = "";
        this.verificationString = "";
        this.longitude = "";
        this.latitude = "";
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public String getResumeURL() {
        return resumeURL;
    }

    public void setResumeURL(String resumeURL) {
        this.resumeURL = resumeURL;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVerificationString() {
        return verificationString;
    }

    public void setVerificationString(String verificationString) {
        this.verificationString = verificationString;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    }

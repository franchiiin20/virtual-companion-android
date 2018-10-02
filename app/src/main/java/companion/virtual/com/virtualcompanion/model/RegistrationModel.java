package companion.virtual.com.virtualcompanion.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RegistrationModel implements Serializable {

    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;
    private String mobileNumber;
    private String verificationString;

    public RegistrationModel(){
        this.firstName = "";
        this.lastName = "";
        this.emailAddress = "";
        this.password = "";
        this.mobileNumber = "";
        this.verificationString = "";
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getVerificationString() {
        return verificationString;
    }

    public void setVerificationString(String verificationString) {
        this.verificationString = verificationString;
    }
}

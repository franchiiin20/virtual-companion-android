package companion.virtual.com.virtualcompanion.model;


import java.io.Serializable;


@SuppressWarnings("serial")
public class EmergencyModel implements Serializable {

    private String id;
    private String name;
    private String email;
    private String mobile;
    private String status;
    private String UID;

    public EmergencyModel(){
        this.id = "";
        this.name = "";
        this.email = "";
        this.mobile = "";
        this.status = "";
        this.UID = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}

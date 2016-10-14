package cranio.betrack.pojos;

import java.util.Date;

/**
 * Created by MariaSol on 13/10/2016.
 */
public class OwnerDataPojo {

    private int id;
    private float lat;
    private float lon;
    private String name;
    private String social_reason;
    private String cuit;
    private String email;
    private String phone_number;
    private String last_hearbeat;
    private String store_type;
    private String created_at;
    private String updated_at;

    public OwnerDataPojo(int id, float latitud, float longitud, String name, String socialReason, String CUIT, String email, String phoneNumber, String lastHearbeat, String storeType, String createdAt, String updatedAt) {
        this.setId(id);
        this.setLatitud(latitud);
        this.setLongitud(longitud);
        this.setName(name);
        this.setSocialReason(socialReason);
        this.setCUIT(CUIT);
        this.setEmail(email);
        this.setPhoneNumber(phoneNumber);
        this.setLastHearbeat(lastHearbeat);
        this.setStoreType(storeType);
        this.setCreatedAt(createdAt);
        this.setUpdatedAt(updatedAt);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getLatitud() {
        return lat;
    }

    public void setLatitud(float latitud) {
        this.lat = latitud;
    }

    public float getLongitud() {
        return lon;
    }

    public void setLongitud(float longitud) {
        this.lon = longitud;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSocialReason() {
        return social_reason;
    }

    public void setSocialReason(String socialReason) {
        this.social_reason = socialReason;
    }

    public String getCUIT() {
        return cuit;
    }

    public void setCUIT(String CUIT) {
        this.cuit = CUIT;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phone_number = phoneNumber;
    }

    public String getLastHearbeat() {
        return last_hearbeat;
    }

    public void setLastHearbeat(String lastHearbeat) {
        this.last_hearbeat = lastHearbeat;
    }

    public String getStoreType() {
        return store_type;
    }

    public void setStoreType(String storeType) {
        this.store_type = storeType;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(String createdAt) {
        this.created_at = createdAt;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updated_at = updatedAt;
    }
}

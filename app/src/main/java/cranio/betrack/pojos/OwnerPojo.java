package cranio.betrack.pojos;

/**
 * Created by MariaSol on 13/10/2016.
 */
public class OwnerPojo {
   private OwnerDataPojo owner_data;
    private String type;

    public OwnerPojo(OwnerDataPojo ownerData, String ownerType){
        this.setOwnerData(ownerData);
        this.setOwnerType(ownerType);
    }

    public OwnerDataPojo getOwnerData() {
        return owner_data;
    }

    public void setOwnerData(OwnerDataPojo ownerData) {
        this.owner_data = ownerData;
    }

    public String getOwnerType() {
        return type;
    }

    public void setOwnerType(String ownerType) {
        this.type = ownerType;
    }
}
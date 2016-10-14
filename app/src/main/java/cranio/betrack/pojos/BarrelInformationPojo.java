package cranio.betrack.pojos;

import java.util.ArrayList;

/**
 * Created by MariaSol on 13/10/2016.
 */
public class BarrelInformationPojo {
    private BarrelPojo barrel;
    private ArrayList<BarrelStatusPojo> barrel_statuses;
    private OwnerPojo owner;

    public BarrelInformationPojo(BarrelPojo barreldata, ArrayList<BarrelStatusPojo> barrelStatus, OwnerPojo owner){
        this.setBarreldata(barreldata);
        this.barrel_statuses=barrelStatus;
        this.setOwner(owner);
    }

    public BarrelPojo getBarreldata() {
        return barrel;
    }

    public void setBarreldata(BarrelPojo barreldata) {
        this.barrel = barreldata;
    }

    public ArrayList<BarrelStatusPojo> getBarrelStatus() {
        return barrel_statuses;
    }

    public void setBarrelStatus(BarrelStatusPojo barrelStatus) {
        barrelStatus = barrelStatus;
    }

    public OwnerPojo getOwner() {
        return owner;
    }

    public void setOwner(OwnerPojo owner) {
        this.owner = owner;
    }
}

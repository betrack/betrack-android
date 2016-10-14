package cranio.betrack.pojos;

import java.util.Date;

/**
 * Created by MariaSol on 13/10/2016.
 */
public class BarrelStatusPojo {
    private float temperature;
    private String sent_at;

    public BarrelStatusPojo (int id, float temperature, float temperature_tstmp, String state, int barrelId, String createdAt, String updatedAt){
        this.setTemperature(temperature);
        this.setSent_at(sent_at);
    }


    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }


    public String getSent_at() {
        return sent_at;
    }

    public void setSent_at(String sent_at) {
        this.sent_at = sent_at;
    }
}

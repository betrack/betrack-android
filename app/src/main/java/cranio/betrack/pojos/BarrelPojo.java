package cranio.betrack.pojos;

import java.util.Date;

/**
 * Created by MariaSol on 12/10/2016.
 */
public class BarrelPojo {
    private int id;
    private String barrel_type;
    private int number;
    private int content;
    private String type_details;
    private int rnpa;
    private String description;
    private float alcohol;
    private float ibu;
    private String ingredients;
    private String elaboration_date;
    private String expiration_date;
    private int lot;
    private String comments;
    private String url;
    private String last_state;
    private float last_temperature;

    public BarrelPojo(int id, String type, int number, int content, String typeDetails,int rnpa, String description, int alcohol, int ibu, String ingredients, String elaboration_date, String expiration_date, int lot, String comments, String url, String last_state, float last_temperature){
        this.setId(id);
        this.setType(type);
        this.setNumber(number);
        this.setContent(content);
        this.setTypeDetails(typeDetails);
        this.setRnpa(rnpa);
        this.setDescription(description);
        this.setAlcohol(alcohol);
        this.setIbu(ibu);
        this.setIngredients(ingredients);
        this.setElaboration_date(elaboration_date);
        this.setExpiration_date(expiration_date);
        this.setLot(lot);
        this.setComments(comments);
        this.setUrl(url);
        this.setLast_temperature(last_temperature);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return barrel_type;
    }

    public void setType(String type) {
        this.barrel_type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getContent() {
        return content;
    }

    public void setContent(int content) {
        this.content = content;
    }

    public String getTypeDetails() {
        return type_details;
    }

    public void setTypeDetails(String typeDetails) {
        this.type_details = typeDetails;
    }

    public int getRnpa() {
        return rnpa;
    }

    public void setRnpa(int rnpa) {
        this.rnpa = rnpa;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(int alcohol) {
        this.alcohol = alcohol;
    }

    public float getIbu() {
        return ibu;
    }

    public void setIbu(int ibu) {
        this.ibu = ibu;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getElaboration_date() {
        return elaboration_date;
    }

    public void setElaboration_date(String elaboration_date) {
        this.elaboration_date = elaboration_date;
    }

    public String getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
    }

    public int getLot() {
        return lot;
    }

    public void setLot(int lot) {
        this.lot = lot;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLast_state() {
        return last_state;
    }

    public void setLast_state(String last_state) {
        this.last_state = last_state;
    }

    public float getLast_temperature() {
        return last_temperature;
    }

    public void setLast_temperature(float last_temperature) {
        this.last_temperature = last_temperature;
    }
}

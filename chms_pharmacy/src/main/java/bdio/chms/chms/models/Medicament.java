package bdio.chms.chms.models;

import javafx.beans.property.StringProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Medicament {

    private StringProperty id;
    private StringProperty name;
    private DoubleProperty price;
    private StringProperty description;
    private StringProperty expirationTime;
    private StringProperty type;
    private IntegerProperty quantite; // Propriété pour la quantité

    public Medicament(String nomMed) {
        this.name = new SimpleStringProperty(nomMed);
    }
    // Constructeur avec tous les paramètres
    public Medicament(String id, String name, double price, String description, String expirationTime, String type, int quantite) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.description = new SimpleStringProperty(description);
        this.expirationTime = new SimpleStringProperty(expirationTime);
        this.type = new SimpleStringProperty(type);
        this.quantite = new SimpleIntegerProperty(quantite); // Initialisation de la quantité
    }

    // Constructeur simplifié
    public Medicament(String nomMed, String text, double v, String descriptionTextFieldText, String expirationTimeTextFieldText, String typeTextFieldText) {
        this.name = new SimpleStringProperty(nomMed);
    }

    // Properties
    public StringProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public StringProperty expirationTimeProperty() {
        return expirationTime;
    }

    public StringProperty typeProperty() {
        return type;
    }

    public IntegerProperty quantiteProperty() {
        return quantite;
    }

    // Getters
    public String getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public double getPrice() {
        return price.get();
    }

    public String getDescription() {
        return description.get();
    }

    public String getExpirationTime() {
        return expirationTime.get();
    }

    public String getType() {
        return type.get();
    }

    public int getQuantite() {
        return quantite.get();
    }

    // Setters
    public void setId(String id) {
        this.id.set(id);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime.set(expirationTime);
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public void setQuantite(int quantite) {
        this.quantite.set(quantite);
    }
}

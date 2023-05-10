/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author unknown
 */
public class Entreprise {

    private int id_entreprise;
    private String titre_entreprise;
    private String description_entreprise;
    private double capital_entreprise;
    private String image_entreprise;
    private String pays;
    private int id_user;
    private double lat;
    private double lng;

    public Entreprise() {
    }

    public Entreprise(int id_entreprise, String titre_entreprise, String description_entreprise, double capital_entreprise, String image_entreprise, String pays) {
        this.id_entreprise = id_entreprise;
        this.titre_entreprise = titre_entreprise;
        this.description_entreprise = description_entreprise;
        this.capital_entreprise = capital_entreprise;
        this.image_entreprise = image_entreprise;
        this.pays = pays;
    }

    public Entreprise(String titre_entreprise, String description_entreprise, double capital_entreprise, String image_entreprise, String pays, int id_user, double lat, double lng) {
        this.titre_entreprise = titre_entreprise;
        this.description_entreprise = description_entreprise;
        this.capital_entreprise = capital_entreprise;
        this.image_entreprise = image_entreprise;
        this.pays = pays;
        this.id_user = id_user;
        this.lat = lat;
        this.lng = lng;
    }

    public Entreprise(int id_entreprise, String titre_entreprise, String description_entreprise, double capital_entreprise, String image_entreprise, String pays, int id_user, double lat, double lng) {
        this.id_entreprise = id_entreprise;
        this.titre_entreprise = titre_entreprise;
        this.description_entreprise = description_entreprise;
        this.capital_entreprise = capital_entreprise;
        this.image_entreprise = image_entreprise;
        this.pays = pays;
        this.id_user = id_user;
        this.lat = lat;
        this.lng = lng;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getId_entreprise() {
        return id_entreprise;
    }

    public void setId_entreprise(int id_entreprise) {
        this.id_entreprise = id_entreprise;
    }

    public String getTitre_entreprise() {
        return titre_entreprise;
    }

    public void setTitre_entreprise(String titre_entreprise) {
        this.titre_entreprise = titre_entreprise;
    }

    public String getDescription_entreprise() {
        return description_entreprise;
    }

    public void setDescription_entreprise(String description_entreprise) {
        this.description_entreprise = description_entreprise;
    }

    public double getCapital_entreprise() {
        return capital_entreprise;
    }

    public void setCapital_entreprise(double capital_entreprise) {
        this.capital_entreprise = capital_entreprise;
    }

    public String getImage_entreprise() {
        return image_entreprise;
    }

    public void setImage_entreprise(String image_entreprise) {
        this.image_entreprise = image_entreprise;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    @Override
    public String toString() {
        return "Entreprise{" + "id_entreprise=" + id_entreprise + ", titre_entreprise=" + titre_entreprise + ", description_entreprise=" + description_entreprise + ", capital_entreprise=" + capital_entreprise + ", image_entreprise=" + image_entreprise + ", pays=" + pays + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.id_entreprise;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Entreprise other = (Entreprise) obj;
        if (this.id_entreprise != other.id_entreprise) {
            return false;
        }
        return true;
    }

}

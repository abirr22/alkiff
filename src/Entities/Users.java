/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.Date;

/**
 *
 * @author unknown
 */
public class Users {

    private int id;
    private String nom;
    private String prenom;
    private String genre;
    private String email;
    private int tel;
     
    private Date datenaissance;
    private String password;
    private String imageuser;
    private String roles;

    public Users() {
    }

    public Users(int id, String nom, String prenom, String genre, String email, int tel, Date datenaissance, String password, String imageuser) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.genre = genre;
        this.email = email;
        this.tel = tel;
 
        this.datenaissance = datenaissance;
        this.password = password;
        this.imageuser = imageuser;
    }
    public Users(String nom, String prenom, String sexe, String email, int numero_tele, Date date, String password, String image_user) {
        this.nom = nom;
        this.prenom = prenom;
        this.genre = sexe;
        this.email = email;
        this.tel = numero_tele;
        this.datenaissance = date;
        this.password = password;
        this.imageuser = image_user;
    }


    public Users(int id, String nom, String prenom, String genre, String email, int tel,  Date datenaissance, String password, String imageuser, String roles) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.genre = genre;
        this.email = email;
        this.tel= tel;
 
        this.datenaissance = datenaissance;
        this.password = password;
        this.imageuser = imageuser;
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getGenre() {
        return genre;
    }

    public String getEmail() {
        return email;
    }

    public int getTel() {
        return tel;
    }

    public Date getDatenaissance() {
        return datenaissance;
    }

    public String getPassword() {
        return password;
    }

    public String getImageuser() {
        return imageuser;
    }

    public String getRoles() {
        return roles;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public void setDatenaissance(Date datenaissance) {
        this.datenaissance = datenaissance;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setImageuser(String imageuser) {
        this.imageuser = imageuser;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getImage_user() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

  

   

     
    
    
}


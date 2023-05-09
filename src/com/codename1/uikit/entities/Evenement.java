/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.uikit.entities;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author bhk
 */
public class Evenement {
    private int id  ; 
    
    private String nomevenement , descriptionevenement;
    
    private Date DateR ;
    private int prixevenement,nbreparticipantmax,nbreparticipant,idUsere ;

    public Evenement() {
    }

    public Evenement(int id, String nomevenement, String descriptionevenement, Date DateR, int prixevenement, int nbreparticipantmax, int nbreparticipant, int idUsere) {
        this.id = id;
        this.nomevenement = nomevenement;
        this.descriptionevenement = descriptionevenement;
        this.DateR = DateR;
        this.prixevenement = prixevenement;
        this.nbreparticipantmax = nbreparticipantmax;
        this.nbreparticipant = nbreparticipant;
        this.idUsere = idUsere;
    }

    public Evenement(String nomevenement, String descriptionevenement, Date DateR, int prixevenement, int nbreparticipantmax, int nbreparticipant, int idUsere) {
        this.nomevenement = nomevenement;
        this.descriptionevenement = descriptionevenement;
        this.DateR = DateR;
        this.prixevenement = prixevenement;
        this.nbreparticipantmax = nbreparticipantmax;
        this.nbreparticipant = nbreparticipant;
        this.idUsere = idUsere;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomevenement() {
        return nomevenement;
    }

    public void setNomevenement(String nomevenement) {
        this.nomevenement = nomevenement;
    }

    public String getDescriptionevenement() {
        return descriptionevenement;
    }

    public void setDescriptionevenement(String descriptionevenement) {
        this.descriptionevenement = descriptionevenement;
    }

    public Date getDateR() {
        return DateR;
    }

    public void setDateR(Date DateR) {
        this.DateR = DateR;
    }

    public int getPrixevenement() {
        return prixevenement;
    }

    public void setPrixevenement(int prixevenement) {
        this.prixevenement = prixevenement;
    }

    public int getNbreparticipantmax() {
        return nbreparticipantmax;
    }

    public void setNbreparticipantmax(int nbreparticipantmax) {
        this.nbreparticipantmax = nbreparticipantmax;
    }

    public int getNbreparticipant() {
        return nbreparticipant;
    }

    public void setNbreparticipant(int nbreparticipant) {
        this.nbreparticipant = nbreparticipant;
    }

    public int getIdUsere() {
        return idUsere;
    }

    public void setIdUsere(int idUsere) {
        this.idUsere = idUsere;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + this.id;
        hash = 19 * hash + Objects.hashCode(this.nomevenement);
        hash = 19 * hash + Objects.hashCode(this.descriptionevenement);
        hash = 19 * hash + Objects.hashCode(this.DateR);
        hash = 19 * hash + this.prixevenement;
        hash = 19 * hash + this.nbreparticipantmax;
        hash = 19 * hash + this.nbreparticipant;
        hash = 19 * hash + this.idUsere;
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
        final Evenement other = (Evenement) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.prixevenement != other.prixevenement) {
            return false;
        }
        if (this.nbreparticipantmax != other.nbreparticipantmax) {
            return false;
        }
        if (this.nbreparticipant != other.nbreparticipant) {
            return false;
        }
        if (this.idUsere != other.idUsere) {
            return false;
        }
        if (!Objects.equals(this.nomevenement, other.nomevenement)) {
            return false;
        }
        if (!Objects.equals(this.descriptionevenement, other.descriptionevenement)) {
            return false;
        }
        if (!Objects.equals(this.DateR, other.DateR)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Reclamation{" + "id=" + id + ", nomevenement=" + nomevenement + ", descriptionevenement=" + descriptionevenement + ", DateR=" + DateR + ", prixevenement=" + prixevenement + ", nbreparticipantmax=" + nbreparticipantmax + ", nbreparticipant=" + nbreparticipant + ", idUsere=" + idUsere + '}';
    }
    
    

    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.uikit.services;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Dialog;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.util.Resources;

import com.codename1.uikit.entities.Evenement;
import com.codename1.uikit.utils.Statics;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author asus
 */
public class ServiceRec {

    public static ServiceRec instance = null;
    public static boolean resultOk = true;
    public boolean resultOKk;
    boolean ok = false;
    String json;

    //initilisation connection request 
    private ConnectionRequest req;
    public ArrayList<Evenement> Evenement;

    public static ServiceRec getInstance() {
        if (instance == null) {
            instance = new ServiceRec();
        }
        return instance;

    }

    public ServiceRec() {
        req = new ConnectionRequest();
    }

    public void AjouterEvenement(Evenement Evenement) {
        String url = Statics.BASE_URL + "jsonEvenement/Evenement/ajout?nomevenement=" + Evenement.getNomevenement() + "&descriptionevenement=" + Evenement.getDescriptionevenement() + "&dateevenement=" + Evenement.getDateR() + "&prixevenement=" + Evenement.getPrixevenement() + "&nbreparticipantmax=" + Evenement.getNbreparticipantmax() + "&nbreparticipant=" + Evenement.getNbreparticipant() + "&idUsere=" + Evenement.getIdUsere();
        req.setUrl(url);
        req.addResponseListener(a -> {
            String str = new String(req.getResponseData());
            System.err.println("data==" + str);
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
    }

    public ArrayList<Evenement> affichageEvenements() {
        ArrayList<Evenement> result = new ArrayList<>();

        String url = Statics.BASE_URL + "jsonEvenement/Evenement/liste";
        req.setUrl(url);

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                JSONParser jsonp;
                jsonp = new JSONParser();

                try {
                    Map<String, Object> mapCommentaires = jsonp.parseJSON(new CharArrayReader(new String(req.getResponseData()).toCharArray()));

                    List<Map<String, Object>> listOfMaps = (List<Map<String, Object>>) mapCommentaires.get("root");

                    for (Map<String, Object> obj : listOfMaps) {
                        Evenement re = new Evenement();

                        //dima id fi codename one float 5outhouha
                        float id = Float.parseFloat(obj.get("idevenement").toString());
                        String nomevenement = obj.get("nomevenement").toString();
                        String descriptionevenement = obj.get("descriptionevenement").toString();
                        Date DateR = new SimpleDateFormat("yyyy-MM-dd").parse(obj.get("dateevenement").toString());
                        int prixevenement = (int) Double.parseDouble(obj.get("prixevenement").toString());
                        int nbreparticipantmax = (int) Double.parseDouble(obj.get("nbreparticipantmax").toString());
                        int nbreparticipant = (int) Double.parseDouble(obj.get("nbreparticipant").toString());
                        Map<String, Object> idUserMap = (Map<String, Object>) obj.get("idUsere");
                        double idUserDouble = (double) idUserMap.get("idUser");
                        int idUser = (int) idUserDouble;

                        //Float etat = Float.parseFloat(obj.get("etat").toString());
                        re.setId((int) id);
                        re.setNomevenement(nomevenement);
                        re.setDescriptionevenement(descriptionevenement);
                        re.setDateR(DateR);
                        re.setPrixevenement(prixevenement);
                        re.setNbreparticipantmax(nbreparticipantmax);
                        re.setNbreparticipant(nbreparticipant);
                        re.setIdUsere(idUser);
                        //insert data into ArrayList result
                        result.add(re);

                    }

                } catch (Exception ex) {

                    ex.printStackTrace();
                }

            }
        });

        NetworkManager.getInstance().addToQueueAndWait(req);//execution ta3 request sinon yet3ada chy dima nal9awha

        return result;

    }

    public boolean Delete(Evenement t) {
        String url = Statics.BASE_URL + "jsonEvenement/Evenement/supprimer/" + t.getId();
        req.setUrl(url);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOKk = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOKk;
    }

    //Delete 
    public boolean deleteEvenement(int id) {
        String url = Statics.BASE_URL + "jsonEvenement/Evenement/supprimer/" + id;

        req.setUrl(url);

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                req.removeResponseCodeListener(this);
            }
        });

        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOk;
    }

    //Update 
    public boolean modifierEvenement(Evenement Evenement) {
        String url = Statics.BASE_URL + "jsonEvenement/Evenement/modif/" + Evenement.getId() + "?nomevenement=" + Evenement.getNomevenement() + "&descriptionevenement=" + Evenement.getDescriptionevenement() + "&dateevenement=" + Evenement.getDateR() + "&prixevenement=" + Evenement.getPrixevenement() + "&nbreparticipantmax=" + Evenement.getNbreparticipantmax() + "&nbreparticipant=" + Evenement.getNbreparticipant() + "&idUsere=" + Evenement.getIdUsere();
        req.setUrl(url);

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOk = req.getResponseCode() == 200;  // Code response Http 200 ok
                req.removeResponseListener(this);
            }
        });

        NetworkManager.getInstance().addToQueueAndWait(req);//execution ta3 request sinon yet3ada chy dima nal9awha
        return resultOk;

    }

    public ArrayList<Evenement> affichageEvenementspargenre(String id) {
        ArrayList<Evenement> result = new ArrayList<>();

        String url = Statics.BASE_URL + "jsonEvenement/Evenement/liste/" + id;
        req.setUrl(url);

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                JSONParser jsonp;
                jsonp = new JSONParser();

                try {
                    Map<String, Object> mapCommentaires = jsonp.parseJSON(new CharArrayReader(new String(req.getResponseData()).toCharArray()));

                    List<Map<String, Object>> listOfMaps = (List<Map<String, Object>>) mapCommentaires.get("root");

                    for (Map<String, Object> obj : listOfMaps) {
                        Evenement re = new Evenement();

                        //dima id fi codename one float 5outhouha
                        float id = Float.parseFloat(obj.get("idevenement").toString());
                        String nomevenement = obj.get("nomevenement").toString();
                        String descriptionevenement = obj.get("descriptionevenement").toString();
                        Date DateR = new SimpleDateFormat("yyyy-MM-dd").parse(obj.get("dateevenement").toString());
                        int prixevenement = Integer.parseInt(obj.get("prixevenement").toString());
                        int nbreparticipantmax = Integer.parseInt(obj.get("nbreparticipantmax").toString());
                        int nbreparticipant = Integer.parseInt(obj.get("nbreparticipant").toString());
                        Map<String, Object> idUserMap = (Map<String, Object>) obj.get("idUser");
                        double idUserDouble = (double) idUserMap.get("idUser");
                        int idUser = (int) idUserDouble;

                        //Float etat = Float.parseFloat(obj.get("etat").toString());
                        re.setId((int) id);
                        re.setNomevenement(nomevenement);
                        re.setDescriptionevenement(descriptionevenement);
                        re.setDateR(DateR);
                        re.setPrixevenement(prixevenement);
                        re.setNbreparticipantmax(nbreparticipantmax);
                        re.setNbreparticipant(nbreparticipant);
                        re.setIdUsere(idUser);
                        //insert data into ArrayList result
                        result.add(re);

                    }

                } catch (Exception ex) {

                    ex.printStackTrace();
                }

            }
        });

        NetworkManager.getInstance().addToQueueAndWait(req);//execution ta3 request sinon yet3ada chy dima nal9awha

        return result;

    }

}

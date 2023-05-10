/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

import Entities.Entreprise;
import Utils.Statics;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.events.ActionListener;
import static com.mycompany.myapp.MyApplication.iduser;
import com.sun.mail.smtp.SMTPTransport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author unknown
 */
public class ServiceEntreprise {

    public static ServiceEntreprise instance = null;
    private ConnectionRequest req;
    public ArrayList<Entreprise> entreprises;
    public boolean resultOK;

    private ServiceEntreprise() {
        req = new ConnectionRequest();
    }

    public static ServiceEntreprise getInstance() {
        if (instance == null) {
            instance = new ServiceEntreprise();
        }
        return instance;
    }

    public ArrayList<Entreprise> parseTasks(String jsonText) {
        try {
            entreprises = new ArrayList<>();
            JSONParser j = new JSONParser();// Instanciation d'un objet JSONParser permettant le parsing du résultat json

            /*
                On doit convertir notre réponse texte en CharArray à fin de
            permettre au JSONParser de la lire et la manipuler d'ou vient 
            l'utilité de new CharArrayReader(json.toCharArray())
            
            La méthode parse json retourne une MAP<String,Object> ou String est 
            la clé principale de notre résultat.
            Dans notre cas la clé principale n'est pas définie cela ne veux pas
            dire qu'elle est manquante mais plutôt gardée à la valeur par defaut
            qui est root.
            En fait c'est la clé de l'objet qui englobe la totalité des objets 
                    c'est la clé définissant le tableau de tâches.
             */
            Map<String, Object> entreprisesListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));

            /* Ici on récupère l'objet contenant notre liste dans une liste 
            d'objets json List<MAP<String,Object>> ou chaque Map est une tâche.               
            
            Le format Json impose que l'objet soit définit sous forme
            de clé valeur avec la valeur elle même peut être un objet Json.
            Pour cela on utilise la structure Map comme elle est la structure la
            plus adéquate en Java pour stocker des couples Key/Value.
            
            Pour le cas d'un tableau (Json Array) contenant plusieurs objets
            sa valeur est une liste d'objets Json, donc une liste de Map
             */
            List<Map<String, Object>> list = (List<Map<String, Object>>) entreprisesListJson.get("root");

            //Parcourir la liste des tâches Json
            for (Map<String, Object> obj : list) {
                //Création des tâches et récupération de leurs données
                Entreprise e = new Entreprise();
                float id = Float.parseFloat(obj.get("id_entreprise").toString());
                e.setId_entreprise((int) id);
                e.setTitre_entreprise(obj.get("titre_entreprise").toString());
                e.setDescription_entreprise(obj.get("description_entreprise").toString());
                e.setCapital_entreprise((Float.parseFloat(obj.get("capital_entreprise").toString())));
                e.setPays(obj.get("pays").toString());
                e.setImage_entreprise(obj.get("image_entreprise").toString());
                // double d = Double.parseDouble(obj.get("location").toString());
                //e.setLat();
                String o = obj.get("location").toString();
                String o2 = o.substring(o.indexOf("=", 5) + 1, o.lastIndexOf(","));
                String o3 = o.substring(o.lastIndexOf("=") + 1, o.lastIndexOf('}'));

                System.out.println(o);
                System.out.println(o2);
                System.out.println(o3);
                e.setLat(Double.parseDouble(o2));
                e.setLng(Double.parseDouble(o3));
                //Ajouter la tâche extraite de la réponse Json à la liste
                entreprises.add(e);
            }

        } catch (IOException ex) {

        }
        /*
            A ce niveau on a pu récupérer une liste des tâches à partir
        de la base de données à travers un service web
        
         */
        return entreprises;
    }

    
    
    public ArrayList<Entreprise> getAllEntreprises() {
        String url = Statics.BASE_URL + "/entreprise/m/all";
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                entreprises = parseTasks(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return entreprises;
    }

    public ArrayList<Entreprise> getMesEntreprises(int id, String trie) {
        String url = Statics.BASE_URL + "/entreprise/m/Mes/?id=" + id + "&trie=" + trie;
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                entreprises = parseTasks(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return entreprises;
    }

    public ArrayList<Entreprise> getSMesEntreprises(int id, String s, String trie) {
        String url = Statics.BASE_URL + "/entreprise/m/Mes/Se/s?id=" + id + "&search=" + s + "&trie=" + s;
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                entreprises = parseTasks(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return entreprises;
    }

    public ArrayList<Entreprise> getMaEntreprise(int id) {
        String url = Statics.BASE_URL + "/entreprise/m/detail?id=" + id;
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                // System.out.println(new String(req.getResponseData()));
                entreprises = parseTasks(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return entreprises;
    }

    public boolean deleteEntreprise(int id) {
        String url = Statics.BASE_URL + "/entreprise/m/delete?id=" + id;
        req.setUrl(url);
        // req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                req.removeResponseCodeListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return true;
    }

    public boolean addEntreprise(Entreprise e) {

        String url = Statics.BASE_URL + "/entreprise/m/add?image_entreprise=" + e.getImage_entreprise()
                + "&titre_entreprise=" + e.getTitre_entreprise() + "&description_entreprise=" + e.getDescription_entreprise()
                + "&capital_entreprise=" + e.getCapital_entreprise() + "&pays=" + e.getPays() + "&user_id=" + e.getId_user()
                + "&lat=" + e.getLat() + "&lng=" + e.getLng() + "";

        req.setUrl(url);// Insertion de l'URL de notre demande de connexion
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this); //Supprimer cet actionListener
                /* une fois que nous avons terminé de l'utiliser.
                La ConnectionRequest req est unique pour tous les appels de 
                n'importe quelle méthode du Service task, donc si on ne supprime
                pas l'ActionListener il sera enregistré et donc éxécuté même si 
                la réponse reçue correspond à une autre URL(get par exemple)*/

            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }

    public void sendEmail(String email) {
        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtps.host", "smtp.gmail.com");
            props.put("mail.smtps.auth", "true");

            Session session = Session.getInstance(props, null);

            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("Confirmation Ajout Entreprise <monEmail@domaine.com>"));
            msg.setRecipients(Message.RecipientType.TO, email);
            msg.setSubject("[Careerkey] Confirmation Ajout Entreprise");
            msg.setSentDate(new Date(System.currentTimeMillis()));

            String txt = "Careerkey confirmation ajout Entreprise avec succes ";
            msg.setText(txt);

            SMTPTransport st = (SMTPTransport) session.getTransport("smtps");
            st.connect("smtp.gmail.com", 465, "fakemail58c@gmail.com", "123456123456bAc");

            st.sendMessage(msg, msg.getAllRecipients());
            System.out.println("server response :" + st.getLastServerResponse());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean modEntreprise(Entreprise e) {

        String url = Statics.BASE_URL + "/entreprise/m/modifier?image_entreprise=" + e.getImage_entreprise()
                + "&titre_entreprise=" + e.getTitre_entreprise() + "&description_entreprise=" + e.getDescription_entreprise()
                + "&capital_entreprise=" + e.getCapital_entreprise() + "&pays=" + e.getPays() + "&user_id=" + e.getId_user()
                + "&lat=" + e.getLat() + "&lng=" + e.getLng() + "&id="+e.getId_entreprise();

        req.setUrl(url);// Insertion de l'URL de notre demande de connexion
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this); //Supprimer cet actionListener
                /* une fois que nous avons terminé de l'utiliser.
                La ConnectionRequest req est unique pour tous les appels de 
                n'importe quelle méthode du Service task, donc si on ne supprime
                pas l'ActionListener il sera enregistré et donc éxécuté même si 
                la réponse reçue correspond à une autre URL(get par exemple)*/

            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }
    public void sendEmail2(String email) {
        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtps.host", "smtp.gmail.com");
            props.put("mail.smtps.auth", "true");

            Session session = Session.getInstance(props, null);

            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("Confirmation Modification Entreprise <monEmail@domaine.com>"));
            msg.setRecipients(Message.RecipientType.TO, email);
            msg.setSubject("[Careerkey] Confirmation Modification Entreprise");
            msg.setSentDate(new Date(System.currentTimeMillis()));

            String txt = "Careerkey confirmation Modification Entreprise avec succes ";
            msg.setText(txt);

            SMTPTransport st = (SMTPTransport) session.getTransport("smtps");
            st.connect("smtp.gmail.com", 465, "fakemail58c@gmail.com", "123456123456bAc");

            st.sendMessage(msg, msg.getAllRecipients());
            System.out.println("server response :" + st.getLastServerResponse());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

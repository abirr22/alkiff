/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

import Entities.Users;
import Utils.Statics;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.events.ActionListener;
import com.sun.mail.smtp.SMTPTransport;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class ServiceUsers {

    public static ServiceUsers instance = null;
    private ConnectionRequest req;
    public ArrayList<Users> users;
    public boolean resultOK;
    public Users user;

    private ServiceUsers() {
        req = new ConnectionRequest();
    }

    public static ServiceUsers getInstance() {
        if (instance == null) {
            instance = new ServiceUsers();
        }
        return instance;
    }

    public ArrayList<Users> parseTasks(String jsonText) {
        try {
            users = new ArrayList<>();
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
                Users u = new Users();
                float id = Float.parseFloat(obj.get("id").toString());
                u.setId((int) id);
                u.setNom(obj.get("nom").toString());
                u.setPrenom(obj.get("prenom").toString());
                u.setSexe(obj.get("sexe").toString());
                float tel = Float.parseFloat(obj.get("numero_tele").toString());
                u.setNumero_tele((int) tel);
                u.setDomaine(obj.get("domaine").toString());
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    u.setDate(format.parse(obj.get("date").toString()));
                } catch (ParseException ex) {
                }
                u.setEmail(obj.get("email").toString());
                u.setPassword(obj.get("password").toString());
                u.setImage_user(obj.get("image_user").toString());
                u.setRole(obj.get("roles").toString());

                //  System.out.println(u);
                //Ajouter la tâche extraite de la réponse Json à la liste
                users.add(u);
            }

        } catch (IOException ex) {

        }
        /*
            A ce niveau on a pu récupérer une liste des tâches à partir
        de la base de données à travers un service web
        
         */
        return users;
    }

    public Users verifer(String email, String password) {
        // Users user = null;
        String url = Statics.BASE_URL + "/users/m/verifier?email=" + email + "&password=" + password;
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                // System.out.println(new String(req.getResponseData()));
                //System.out.println(req.getResponseData());
                String res = new String(req.getResponseData());

                if (res.equalsIgnoreCase("null")) {
                    user = null;
                } else {
                    users = parseTasks(new String(req.getResponseData()));
                    user = users.get(0);
                }
                //  System.out.println(users);
                req.removeResponseListener(this);
            }

        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return user;
    }

    public Users veriferemail(String email) {
        // Users user = null;
        String url = Statics.BASE_URL + "/users/m/verifier/email?email=" + email;
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                // System.out.println(new String(req.getResponseData()));
                //System.out.println(req.getResponseData());
                String res = new String(req.getResponseData());

                if (res.equalsIgnoreCase("null")) {
                    user = null;
                } else {
                    users = parseTasks(new String(req.getResponseData()));
                    user = users.get(0);
                }
                //  System.out.println(users);
                req.removeResponseListener(this);
            }

        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return user;
    }

    public void sendEmail(String email, int code) {
        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtps.host", "smtp.gmail.com");
            props.put("mail.smtps.auth", "true");

            Session session = Session.getInstance(props, null);

            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("reinitialisation mot de passe  <monEmail@domaine.com>"));
            msg.setRecipients(Message.RecipientType.TO, email);
            msg.setSubject("[Careerkey] reinitialisation mot de passe ");
            msg.setSentDate(new Date(System.currentTimeMillis()));

            String txt = "reinitialisation mot de passe : code de verification : " + code;
            msg.setText(txt);

            SMTPTransport st = (SMTPTransport) session.getTransport("smtps");
            st.connect("smtp.gmail.com", 465, "fakemail58c@gmail.com", "123456123456bAc");

            st.sendMessage(msg, msg.getAllRecipients());
            System.out.println("server response :" + st.getLastServerResponse());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Users resetpassword(Users Resetuser) {
        // Users user = null;
        String url = Statics.BASE_URL + "/users/m/resetpassword?id=" + Resetuser.getId()+"&password="+Resetuser.getPassword() ;
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                // System.out.println(new String(req.getResponseData()));
                //System.out.println(req.getResponseData());
                String res = new String(req.getResponseData());

                if (res.equalsIgnoreCase("null")) {
                    user = null;
                } else {
                    users = parseTasks(new String(req.getResponseData()));
                    user = users.get(0);
                }
                //  System.out.println(users);
                req.removeResponseListener(this);
            }

        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return user;

    }
    public boolean addUser(Users u) {

        String url = Statics.BASE_URL + "/users/m/adduser?nom=" + u.getNom()
                + "&prenom=" + u.getPrenom() + "&sexe=" +u.getSexe()
                + "&tel=" + u.getNumero_tele() + "&domaine=" + u.getDomaine() + "&image=" + u.getImage_user()
                + "&date=" + u.getDate() + "&role=user" + "&email=" + u.getEmail() + "&password="+u.getPassword()+"";

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
    
    public void sendEmail2(String email, int code) {
        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtps.host", "smtp.gmail.com");
            props.put("mail.smtps.auth", "true");

            Session session = Session.getInstance(props, null);

            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("Creation Compte  <monEmail@domaine.com>"));
            msg.setRecipients(Message.RecipientType.TO, email);
            msg.setSubject("[Careerkey] Creation Compte ");
            msg.setSentDate(new Date(System.currentTimeMillis()));

            String txt = "Creation Compte : code de verification : " + code;
            msg.setText(txt);

            SMTPTransport st = (SMTPTransport) session.getTransport("smtps");
            st.connect("smtp.gmail.com", 465, "fakemail58c@gmail.com", "123456123456bAc");

            st.sendMessage(msg, msg.getAllRecipients());
            System.out.println("server response :" + st.getLastServerResponse());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Users> getallusers() {
        String url = Statics.BASE_URL + "/users/m/all";
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                users = parseTasks(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return users;
    }
    
    

}

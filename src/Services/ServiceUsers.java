/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

 
import Entities.Users;
import GUI.Accueil_Admin;
import GUI.Accueil_User;
import GUI.BaseForm;
import GUI.BaseFormBack;
import GUI.NewsfeedForm;
import GUI.ProfileForm;
import GUI.SessionManager;
import Utils.Statics;
import com.codename1.facebook.User;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Dialog;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.util.Resources;
import com.sun.mail.smtp.SMTPTransport;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
 * @author 21695
 */
public class ServiceUsers {
    
        private Resources theme; 
    //singleton

    public static ServiceUsers instance = null;

    public ArrayList<Users> users;


    
    public boolean resultOK;
    //initialisation connexion request 
    private  ConnectionRequest req;
    String json;

    /**
     *
     */
    public ServiceUsers() {
        req = new ConnectionRequest();
    }

    
      public static ServiceUsers getInstance() {
        if (instance == null) {
            instance = new ServiceUsers();
        }
        return instance;
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
            st.connect("smtp.gmail.com", 465, "abir.khlifi@esprit.tn", "espritesprit12345678");

            st.sendMessage(msg, msg.getAllRecipients());
            System.out.println("server response :" + st.getLastServerResponse());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    
    public void resetpassword(String email) {

        String url = Statics.BASE_URL + "/mobile/motpasseoublie";
        req.setUrl(url);
        req.setPost(false);
        req.addArgument("email", email);
        JSONParser j = new JSONParser();

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                json = new String(req.getResponseData());
                int json1 = req.getResponseData().length;
                if (25 >= json1) {
                    Dialog.show("verification", "User not found", "OK", null);
                } else {
                    System.out.println("++++++++>>>>>>  " + json);

                    try {
                        Map<String, Object> user = j.parseJSON(new CharArrayReader(json.toCharArray()));
                        List<Map<String, Object>> list = (List<Map<String, Object>>) user.get("root");
                        System.out.println("useeeeeeeer " + user.get("resetToken"));

                        Dialog.show("verification", "Verification code was sent to your email", "OK", null);

                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }

                }

            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
    }
        public boolean signup(String nom,String prenom,String genre,String email,String date,String password) {
 String url = Statics.BASE_URL +"/mobile/inscription?nom="+nom+"&prenom="+prenom+"&email="+email+"&genre="+genre+"&datenaissance=2022-01-11"+"&pass="+password;

        req.setUrl(url);// Insertion de l'URL de notre demande de connexion
        //  req.addArgument("nom", nom);
       // req.addArgument("prenom", prenom);
       // req.addArgument("email", email);
       // req.addArgument("genre", genre);
       // req.addArgument("datenaissance", date);
       // req.addArgument("pass", password);
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
      public boolean edituser(String id, String nom, String prenom, String email,  String genre, String tel) {

        String url = Statics.BASE_URL + "/mobile/edit-user";

        req.setUrl(url);
        req.setPost(false);
        req.addArgument("id",id);
        req.addArgument("nom", nom);
        req.addArgument("prenom", prenom);
        req.addArgument("email", email);
        req.addArgument("tel", tel );
        req.addArgument("genre", genre);
        

        JSONParser j = new JSONParser();

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                json = new String(req.getResponseData());
                int json1 = req.getResponseData().length;
                          System.out.println("reqqqqqq"+req);

                if (25 >= json1) {
                    Dialog.show("Echec", "User not found", "OK", null);
                } else {
                    System.out.println("++++++++>>>>>>  " + json);

                    try {
                        Map<String, Object> user = j.parseJSON(new CharArrayReader(json.toCharArray()));
                        List<Map<String, Object>> list = (List<Map<String, Object>>) user.get("root");
                        System.out.println("useeeeeeeer " + user.get("resetToken"));

                        Dialog.show("Succes", "Modification avec succes", "OK", null);

                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }

                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }
      //e 
    public boolean deleteUser(int id ) {
        String url = Statics.BASE_URL +"/mobile/supprimer-user?id="+id;
        
        req.setUrl(url);
        
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                    
                    req.removeResponseCodeListener(this);
            }
        });
        
      NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }
    public void editrole(String adminid, String id, String role) {
        String url = Statics.BASE_URL + "admin/edit-user";
        req.setUrl(url);
        req.setPost(false);
        req.addArgument("adminid", adminid);
        req.addArgument("id", id);
        req.addArgument("role", role);

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                int result = req.getResponseCode(); //Code HTTP 200 OK
                if (result == 200) {
                    Dialog.show("Success", "Utilisateur est modifié", "OK", null);
                    new BaseFormBack().show();
                } else {
                    Dialog.show("Failed", "Erreur", "OK", null);

                }
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);

    }
      public ArrayList<Users> getallusers() {
        String url = Statics.BASE_URL + "/mobile/aff-users";
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
               
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return null;
        
    }
      public void signin(String email, String password, Resources res) {

        String url = Statics.BASE_URL + "/mobile/connexion";
        req.setUrl(url);
        req.setPost(false);
        req.addArgument("email", email);
        req.addArgument("password", password);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                JSONParser j = new JSONParser();
                int json1 = req.getResponseData().length;

                try {
                    json = new String(req.getResponseData(), "utf-8");
                    System.out.println("++++++++>>>>>>  " + json);
                } catch (UnsupportedEncodingException ex) {
                    System.out.println(ex.getMessage());
                }
                  System.out.println("------>"+json+"****");
//                  boolean resi=(json.equals(evt)"echec");
                //  System.out.println("response "+resi);
                 System.out.println("response2 "+json.equals("echec"));
                if (json1 < 10) {
                    Dialog.show("Echec d'authentification", "email ou mot de passe est erroné", "OK", null);

                } else {
                    System.out.println("data--->" + json);
                    try {
                        Map<String, Object> user = j.parseJSON(new CharArrayReader(json.toCharArray()));
                        List<Map<String, Object>> list = (List<Map<String, Object>>) user.get("root");
                        
                        Users usr = null;
                        System.out.println("useeeeeeeer " + list.get(0));
                        for (Map<String, Object> obj : list) {
                             usr = new Users();
                            float id = Float.parseFloat(obj.get("id").toString());
                            usr.setId((int) id);

                            SessionManager.setId((int) id);
                            SessionManager.setEmail(obj.get("email").toString());
                            usr.setEmail(obj.get("email").toString());
                            SessionManager.setNom(obj.get("nom").toString());
                            usr.setNom(obj.get("nom").toString());
                            SessionManager.setPrenom(obj.get("prenom").toString());
                            SessionManager.setGenre(obj.get("genre").toString());
                            SessionManager.setPTel(obj.get("tel").toString());
                            usr.setPrenom(obj.get("prenom").toString());
                            //  Double numero = (Double) obj.get("tel");
                           // SessionManager.setel(numero.intValue());
                            System.out.println("genre>>>>>"+obj.get("genre").toString());
                            System.out.println("roles>>>>>"+obj.get("roles").toString());
                            SessionManager.setRole(obj.get("roles").toString());
                        }
                        System.out.println("user===>" + SessionManager.getEmail());
                        /*
                        if (usr.getRoles()!= null) {
                             new ProfileForm(res).show();
                        }
*/
                         if (SessionManager.getRole().equals( "[ROLE_USER]")) {
                             new Accueil_User(res).show();
                        }
                         else
                         {
                             new Accueil_Admin(res).show();
                         }
                         
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
    }

  
 
    public ArrayList<Users> parseUsers(String jsonText){
                try {
                    char firstChar = jsonText.charAt(0);
                    if(firstChar != '[')
                    {
                    jsonText="["+jsonText+"]";
                    }
                    System.out.println(jsonText);
            users=new ArrayList<>();
            JSONParser j = new JSONParser();
            Map<String,Object> tasksListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            
            List<Map<String,Object>> list = (List<Map<String,Object>>)tasksListJson.get("root");
            for(Map<String,Object> obj : list){
                Users a = new Users();
                float id = Float.parseFloat(obj.get("id").toString());
                a.setId((int)id);
                a.setEmail(obj.get("email").toString());

                users.add(a);

            }
        } catch (IOException ex) {
            
        }
        return users;
    }
    
    public boolean editUser(Users u) {
        String url = Statics.BASE_URL + "/mobile/usermob/edit?id="+u.getId()+"&pwd="+ u.getPassword(); 
               req.setUrl(url);
               System.out.println(url);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }
public ArrayList<Users> getUserByEmaail(String email){
        String url = Statics.BASE_URL+"/mobile/findusermobEmail?email="+email;
        req.setUrl(url);
         req.setPost(false);
        req.addResponseListener(new com.codename1.ui.events.ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                users = parseUsers(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        com.codename1.io.NetworkManager.getInstance().addToQueueAndWait(req);
        return users;
    }    


}
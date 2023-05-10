/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

 
import Entities.Users;
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
 
 
 
 
 
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



}
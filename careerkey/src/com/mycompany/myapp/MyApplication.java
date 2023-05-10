package com.mycompany.myapp;

import Entities.Users;
import GUI.Accueil_Admin;
import GUI.Accueil_User;
import GUI.Front_AjouterEntreprise;
import GUI.Front_MesEntreprise;
import GUI.Front_StatEntreprise;
import GUI.SignInForm;
import Services.ServiceEntreprise;
import static com.codename1.ui.CN.*;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Dialog;
import com.codename1.ui.Label;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.codename1.io.Log;
import com.codename1.ui.Toolbar;
import java.io.IOException;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.io.NetworkEvent;

/**
 * This file was generated by <a href="https://www.codenameone.com/">Codename
 * One</a> for the purpose of building native mobile applications using Java.
 */
public class MyApplication {

    private Form current;
    private Resources theme;
    public static int iduser ;
    public static Users Sessionuser ;
    public static Users Resetuser ;
    public static Users signupuser ;    
    public static int verificationcode ;
    public static String verificationemail ;
    

    public void init(Object context) {
        // use two network threads instead of one
        updateNetworkThreadCount(2);

        theme = UIManager.initFirstTheme("/theme");

        // Enable Toolbar on all Forms by default
        Toolbar.setGlobalToolbar(true);

        // Pro only feature
        Log.bindCrashProtection(true);

        addNetworkErrorListener(err -> {
            // prevent the event from propagating
            err.consume();
            if (err.getError() != null) {
                Log.e(err.getError());
            }
            Log.sendLogAsync();
            Dialog.show("Connection Error", "There was a networking error in the connection to " + err.getConnectionRequest().getUrl(), "OK", null);
        });
    }

    public void start() {
        if (current != null) {
            current.show();
            return;
        }
       //  new Accueil_Admin(theme).show();
       //    new Accueil_User(theme).show();
       //   new Front_MesEntreprise(theme,null,null).show();
       //   new Front_AjouterEntreprise(theme, current).show();
        new SignInForm(theme).show();
      //  new Front_StatEntreprise(theme,current).show();
        
    }

    public void stop() {
        current = getCurrentForm();
        if (current instanceof Dialog) {
            ((Dialog) current).dispose();
            current = getCurrentForm();
        }
    }

    public void destroy() {
    }

}
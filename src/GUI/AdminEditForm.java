/*
 * Copyright (c) 2016, Codename One
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, 
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions 
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 */
package GUI;

import Services.ServiceUsers;
import Utils.Statics;
import com.codename1.components.ScaleImageLabel;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.CheckBox;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.RadioButton;
import com.codename1.ui.Tabs;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.table.DefaultTableModel;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.table.TableModel;
import com.codename1.ui.util.Resources;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * The newsfeed form
 *
 * @author Shai Almog
 */
public class AdminEditForm extends Form {

    private ConnectionRequest req = new ConnectionRequest();
    String json;

    public AdminEditForm(Resources res, Map l) {
        super("Modifier User", new TableLayout(10, 2));
        Toolbar tb = new Toolbar(true);
        setToolbar(tb);

        tb.setTitle("Modifier User");
        tb.showToolbar();

        /* getTitleArea().setUIID("Container");
        setTitle("Admin Space");
        getContentPane().setScrollVisible(false);*/
        String url = Statics.BASE_URL + "/mobile/aff-users";
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                json = new String(req.getResponseData());
                int json1 = req.getResponseData().length;
                JSONParser j = new JSONParser();
                String roles =  l.get("roles").toString();

                Label nom = new Label("Votre nom et prénom ");
                Label email = new Label("Votre email ");

                String usernom = (String) l.get("nom");
                String userprenom = (String) l.get("prenom");
                String useremail = (String) l.get("email");
                ;
                Button supprimer = new Button("Supprimer");
                Button modif = new Button("Modifier");
                CheckBox utilisateur = new CheckBox("ROLE_USER");
                CheckBox admin = new CheckBox("ROLE_ADMIN");
                if (roles.equals("ROLE_ADMIN")) {
                    utilisateur.setSelected(true);
                    admin.setSelected(true);

                } else {
                    utilisateur.setSelected(true);

                }
                float userid = Float.parseFloat(l.get("id").toString());
                int id = (int) userid;
                modif.addActionListener(i -> {
                    if (admin.isSelected()) {
                        ServiceUsers.getInstance().editrole(String.valueOf(SessionManager.getId()), String.valueOf(id), admin.getText());
                    } else {
                        ServiceUsers.getInstance().editrole(String.valueOf(SessionManager.getId()), String.valueOf(id), utilisateur.getText());

                    }
                });
                supprimer.addActionListener(i -> {

                    ServiceUsers.getInstance().deleteUser(SessionManager.getId());
                        Dialog.show("succes", "supprimer", "OK",null);
                        new ProfileFormBack(res).show();
                 
                });

                add(nom);
                add(usernom);

                add(email);
                add(useremail);

                add(utilisateur);
                add(admin);
                add(modif);
                add(supprimer);
                tb.addCommandToLeftBar("Back", null, d -> new BaseFormBack().show());
            }
        ;

        });

                

            
        
        NetworkManager.getInstance().addToQueueAndWait(req);

    }

    private void updateArrowPosition(Button b, Label arrow) {
        arrow.getUnselectedStyle().setMargin(LEFT, b.getX() + b.getWidth() / 2 - arrow.getWidth() / 2);
        arrow.getParent().repaint();

    }

}

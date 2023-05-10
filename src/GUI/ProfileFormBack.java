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

import GUI.AdminEditForm;
import GUI.BaseFormBack;
import GUI.SignInForm;
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
public class ProfileFormBack extends Form {

    private ConnectionRequest req = new ConnectionRequest();
    String json;

    public ProfileFormBack(Resources res) {
        super("Admin Space", new TableLayout(10, 3));
        Toolbar tb = new Toolbar(true);
        setToolbar(tb);

        tb.setTitle("Admin Space");
        tb.showToolbar();
        add(new Label("username"));
        add(new Label("role"));
        add(new Label("Modifier"));
        /* getTitleArea().setUIID("Container");
        setTitle("Admin Space");
        getContentPane().setScrollVisible(false);*/
        String url = Statics.BASE_URL + "/mobile/aff-users";
        req.setUrl(url);
        req.setPost(false);

        Container check = new Container();
        CheckBox utilisateur = new CheckBox("ROLE_USER");
        CheckBox admin = new CheckBox("ROLE_ADMIN");
        /* check.add(utilisateur);
        check.add(admin);*/
        Button btnrecherche = new Button("recherche");

        add(utilisateur);
        add(admin);
        add(btnrecherche);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                json = new String(req.getResponseData());
                int json1 = req.getResponseData().length;
                JSONParser j = new JSONParser();

                try {
                    Map<String, Object> user = j.parseJSON(new CharArrayReader(json.toCharArray()));
                    List<Map<String, Object>> list = (List<Map<String, Object>>) user.get("root");
List<Map<String, Object>> listrech=list;


                    for (Map<String, Object> l : listrech) {
                        Label username = new Label((String) l.get("email"));
                        String roles =  l.get("roles").toString();
                        System.out.println(roles);

                        Button modifier = new Button("Modifier");

                        username.setUIID("TextFieldBlack");

                        addComponent(username);
                        addComponent(new Label((String) roles));

                        addComponent(modifier);
                        modifier.addActionListener(e -> {
                            new AdminEditForm(res, l).show();
                        });

                    }

                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }

            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);

        tb.addSearchCommand(e -> {
        });
        tb.addMaterialCommandToSideMenu("Utilisateurs", FontImage.MATERIAL_ACCOUNT_BOX, e -> new BaseFormBack().show());
        
        tb.addMaterialCommandToSideMenu("Logout", FontImage.MATERIAL_EXIT_TO_APP, e -> {
            new SignInForm(res).show();
            SessionManager.pref.clearAll();

        });

    }

    private void updateArrowPosition(Button b, Label arrow) {
        arrow.getUnselectedStyle().setMargin(LEFT, b.getX() + b.getWidth() / 2 - arrow.getWidth() / 2);
        arrow.getParent().repaint();

    }

}

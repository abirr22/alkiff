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
package com.codename1.uikit.pheonixui;

import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.File;
import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Button;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.util.Resources;
import com.codename1.uikit.entities.Evenement;
import com.codename1.uikit.services.ServiceRec;
import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


/**
 * The newsfeed form
 *
 * @author Shai Almog
 */
public class updaterec extends BaseForm {

    EncodedImage enim;
    Image img;
    ImageViewer imv;

    String pp = "";
    Resources r;
    int selectedidUser ;
    public updaterec(Resources res, Evenement t) {

        super("Evenement", BoxLayout.y());
        installSidemenu(res);
        getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_ARROW_LEFT, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evvt) {
                new NewsfeedForm(res).showBack();
            }
        });

        r = res;
        try {
            enim = EncodedImage.create("/giphy.gif");

            //img=URLImage.createToStorage(enim,"http://127.0.0.1:8000/uploads/Annonces/"+evt.getPathimg(), "http://127.0.0.1:8000/uploads/Annonces/"+evt.getPathimg(),URLImage.RESIZE_SCALE).scaled(500, 350);
          TextField nomevenement = new TextField("", "nomevenement", 20, TextField.ANY);
            TextField descriptionevenement = new TextField("", "descriptionevenement", 20, TextField.ANY);
            TextField prixevenement = new TextField("", "prixevenement", 20, TextField.ANY);
            TextField nbreparticipantmax = new TextField("", "nbreparticipantmax", 20, TextField.ANY);
            TextField nbreparticipant = new TextField("", "nbreparticipant", 20, TextField.ANY);
            Picker DateR = new Picker();
            ComboBox<String> comboBoxidUser = new ComboBox<>();
            comboBoxidUser.setHint("Select User");
// Make a request to the API to get the list of user names and IDs
            ConnectionRequest request = new ConnectionRequest();
            request.setUrl("http://127.0.0.1:8000/jsonEvenement/user/list");
            request.setPost(false);
            request.addResponseListener((e) -> {
                try {
                    // Parse the JSON response
                    JSONParser parser = new JSONParser();
                    Map<String, Object> userObject = parser.parseJSON(new InputStreamReader(new ByteArrayInputStream(request.getResponseData()), "UTF-8"));

                    // Convert the object to a map of integers
                    Map<String, Integer> userMap = new HashMap<>();
                    for (Map.Entry<String, Object> entry : userObject.entrySet()) {
                        String name = entry.getKey();
                        int id = (int) Double.parseDouble(entry.getValue().toString());
                        userMap.put(name, id);
                    }

                    // Populate the combo box with the user names
                    for (String name : userMap.keySet()) {
                        comboBoxidUser.addItem(name);
                    }

                    comboBoxidUser.addActionListener((evt) -> {
                        // Get the selected user ID
                        String selectedName = comboBoxidUser.getSelectedItem();
                        selectedidUser = Integer.parseInt(userMap.get(selectedName).toString());

                        // Use the selected ID as needed
                        System.out.println("Selected Name: " + selectedName);
                        System.out.println("Selected IDr: " + selectedidUser);
                    });
                } catch (IOException ex) {
                    Log.e(ex);
                }
            });
            NetworkManager.getInstance().addToQueue(request);

            //-----------------------------------------------------------------------
           
            //------------------------------------------------------------------------
         DateR.setType(Display.PICKER_TYPE_DATE);
            add(nomevenement);
            add(descriptionevenement);
            add(DateR);
            add(prixevenement);
            add(nbreparticipantmax);
            add(nbreparticipant);
            add(comboBoxidUser);
        
   
            nomevenement.setText(t.getNomevenement());
            descriptionevenement.setText(t.getDescriptionevenement());
            DateR.setDate(t.getDateR());
           prixevenement.setText(String.valueOf(t.getPrixevenement()));
            nbreparticipantmax.setText(String.valueOf(t.getNbreparticipantmax()));
            nbreparticipant.setText(String.valueOf(t.getNbreparticipant()));
            Button valide = new Button("Valide");
            valide.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent et) {

                    t.setNomevenement(nomevenement.getText());
                    t.setDescriptionevenement(descriptionevenement.getText());
                   
                    t.setDateR(DateR.getDate());
                    t.setPrixevenement(Integer.parseInt(prixevenement.getText()) );
                    t.setNbreparticipantmax(Integer.parseInt(nbreparticipantmax.getText()) );
                    t.setNbreparticipant(Integer.parseInt(nbreparticipant.getText()) );
                    t.setIdUsere(selectedidUser);
                    ServiceRec.getInstance().modifierEvenement(t);
                    new NewsfeedForm(r).showBack();

                }
            });
            add(valide);

        } catch (IOException ex) {

        }

        Button back = new Button("Back");

        back.requestFocus();
        back.addActionListener(e -> new NewsfeedForm(res).show());

    }
}

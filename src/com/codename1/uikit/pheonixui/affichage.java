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
import com.codename1.charts.ChartComponent;
import com.codename1.charts.models.CategorySeries;
import com.codename1.charts.renderers.DefaultRenderer;
import com.codename1.charts.renderers.SimpleSeriesRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.PieChart;
import com.codename1.components.ImageViewer;
import com.codename1.notifications.LocalNotification;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;
import com.codename1.uikit.entities.Evenement;
import com.codename1.uikit.services.ServiceRec;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The newsfeed form
 *
 * @author Shai Almog
 */
public class affichage extends  BaseForm {
EncodedImage enim;
 Image img;
 ImageViewer imv;
private static final String HTML_API_KEY = "AIzaSyC_i6nNp6sOrxr_VmksWPmibQn5aIHMqoo";
 


    public affichage(Resources res,Evenement evt) {
      
      super("Evenement", BoxLayout.y());
        Container c1=new Container();
     installSidemenu(res);
        getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_ARROW_LEFT, new ActionListener() {
                   @Override
                   public void actionPerformed(ActionEvent evt) {
                      new NewsfeedForm(res).showBack();
                   }
               });
        
        
         try {
            enim=EncodedImage.create("/giphy.gif");
        } catch (IOException ex) {
            //Logger.getLogger(MyApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
         Label ll=new Label();
        Label nomevenement=new Label("nomevenement : "+evt.getNomevenement());
        Label descriptionevenement=new Label("descriptionevenement : "+evt.getDescriptionevenement());
         Label DateR =new Label("Date : "+evt.getDateR());
        Label prixevenement =new Label("prixevenement : "+evt.getPrixevenement());
        Label nbreparticipantmax =new Label("nbreparticipantmax : "+evt.getNbreparticipantmax());
        Label nbreparticipant =new Label("nbreparticipant : "+evt.getNbreparticipant());
        Label idUsere =new Label("idUsere : "+evt.getIdUsere());
        
       
       
        
        Button update=new Button("Update");
        Button Delete=new Button("Delete");
        Delete.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent evvt) {
              ServiceRec.getInstance().Delete(evt);
              new NewsfeedForm(res).show();
          }
      });
        
        update.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent evvt) {
               new updaterec(res,evt).show();
          }
      });

           
              add(nomevenement);
              add(descriptionevenement);
              add(DateR) ;
              add(prixevenement) ; 
              add(nbreparticipantmax) ; 
                add(nbreparticipant) ; 
              add(idUsere) ; 
              add(update);
              add(Delete);
              
            //  add(nbrmax);
              
      
        
        
        
        
        
        
        
        LocalNotification n = new LocalNotification();
        n.setId("Baskel");
        n.setAlertBody("u got a recalamtion ");
        n.setAlertTitle("Break Time!");
        n.setAlertSound("/notification_sound_bells.mp3"); //file name must begin with notification_sound
         

        Display.getInstance().scheduleLocalNotification(n,System.currentTimeMillis() ,LocalNotification.REPEAT_MINUTE);
        Display.getInstance().showNotify();
        /*
        ArrayList<Ville> pos;
        pos=Service.getInstance().getpos(evt);
        Ville v=new Ville();
        v=pos.get(0);
        
        
                final MapContainer cnt = new MapContainer(HTML_API_KEY); 
               
                Style s = new Style();
        s.setFgColor(0xff0000);
        s.setBgTransparency(0);
                   FontImage markerImg = FontImage.createMaterial(FontImage.MATERIAL_PLACE, s, 3);
                   cnt.setCameraPosition(new Coord(v.getLat(), v.getLog()));
                   cnt.zoom(new Coord(v.getLat(), v.getLog()), 12);
                   cnt.addMarker(EncodedImage.createFromImage(markerImg, false),  new Coord(v.getLat(),v.getLog()), "", "", (ett) -> {
                   });
                   cnt.setPreferredSize(new Dimension(300,300));
            
            add(cnt);
                
        
        Button back = new Button("Back");
        Button comt= new Button("comments");
        back.requestFocus();
        back.addActionListener(e -> new NewsfeedForm(res).show());
        comt.addActionListener(e -> new affichage_cmt(res,evt).show());
        add(comt);
        */
     
    }
    
    
    
}

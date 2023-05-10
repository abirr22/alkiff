/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Entities.Entreprise;
import Services.ServiceEntreprise;
import com.codename1.charts.ChartComponent;
import com.codename1.charts.models.CategorySeries;
import com.codename1.charts.renderers.DefaultRenderer;
import com.codename1.charts.renderers.SimpleSeriesRenderer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.charts.views.PieChart;
import com.codename1.components.ImageViewer;
import com.codename1.components.ScaleImageLabel;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.googlemaps.MapContainer;
import com.codename1.io.FileSystemStorage;
import com.codename1.maps.Coord;
import com.codename1.maps.MapListener;
import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.Component;
import static com.codename1.ui.Component.BOTTOM;
import static com.codename1.ui.Component.CENTER;
import static com.codename1.ui.Component.LEFT;
import static com.codename1.ui.Component.RIGHT;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.RadioButton;
import com.codename1.ui.Tabs;
import com.codename1.ui.TextArea;
import com.codename1.ui.Toolbar;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.Resources;
import static com.mycompany.myapp.MyApplication.Sessionuser;
import static com.mycompany.myapp.MyApplication.iduser;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author unknown
 */
public class Front_MesEntreprise extends BaseForm {

    Form current;
    private static final String HTML_API_KEY = "AIzaSyBc-A_UD1XAqk3B1CyJczYw6AyQ3xUJgj0";

    public Front_MesEntreprise(Resources res, String s, String trie) {
        super("CareerKey Mes Entreprise", BoxLayout.y());
        Toolbar tb = new Toolbar(true);
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        setTitle("CareerKey Mes Entreprise");
        getContentPane().setScrollVisible(false);
        //System.out.println("zzz");

        current = this; //Récupération de l'interface(Form) en cours
        iduser=Sessionuser.getId();

        super.addSideMenu(res);

        tb.addSearchCommand(e -> {
            String text = (String) e.getSource();

            ToastBar.showMessage(text, FontImage.MATERIAL_INFO);
            if (text.length() >= 2) {
                new Front_MesEntreprise(res, text, null).show();
            }
        });

        Tabs swipe = new Tabs();
        Label spacer1 = new Label();
        Label spacer2 = new Label();

        //   addTab(swipe, res.getImage("news-item.jpg"), spacer1, "15 Likes  ", "85 Comments", "Integer ut placerat purued non dignissim neque. ");
        //   addTab(swipe, res.getImage("dog.jpg"), spacer2, "100 Likes  ", "66 Comments", "Dogs are cute: story at 11");
        ArrayList<Entreprise> entreprises;
        if (s == null) {
            entreprises = ServiceEntreprise.getInstance().getMesEntreprises(iduser, trie);
        } else {
            entreprises = ServiceEntreprise.getInstance().getSMesEntreprises(iduser, s, trie);
        }
        int lim = 0;
        for (Entreprise obj : entreprises) {
            lim++;
            if (lim == 1) {
                try {
                    Image img = Image.createImage(FileSystemStorage.getInstance().getAppHomePath() + "/" + obj.getImage_entreprise());
                    addTab(swipe, img, spacer1, "Trie Up  ", "Trie Desc", obj.getTitre_entreprise(), res);
                } catch (IOException ex) {
                }
            }

        }

        swipe.setUIID("Container");
        swipe.getContentPane().setUIID("Container");
        swipe.hideTabs();

        ButtonGroup bg = new ButtonGroup();
        int size = Display.getInstance().convertToPixels(1);
        Image unselectedWalkthru = Image.createImage(size, size, 0);
        Graphics g = unselectedWalkthru.getGraphics();
        g.setColor(0xffffff);
        g.setAlpha(100);
        g.setAntiAliased(true);
        g.fillArc(0, 0, size, size, 0, 360);
        Image selectedWalkthru = Image.createImage(size, size, 0);
        g = selectedWalkthru.getGraphics();
        g.setColor(0xffffff);
        g.setAntiAliased(true);
        g.fillArc(0, 0, size, size, 0, 360);
        RadioButton[] rbs = new RadioButton[swipe.getTabCount()];
        FlowLayout flow = new FlowLayout(CENTER);
        flow.setValign(BOTTOM);
        Container radioContainer = new Container(flow);
        for (int iter = 0; iter < rbs.length; iter++) {
            rbs[iter] = RadioButton.createToggle(unselectedWalkthru, bg);
            rbs[iter].setPressedIcon(selectedWalkthru);
            rbs[iter].setUIID("Label");
            radioContainer.add(rbs[iter]);
        }

        rbs[0].setSelected(true);
        swipe.addSelectionListener((i, ii) -> {
            if (!rbs[ii].isSelected()) {
                rbs[ii].setSelected(true);
            }
        });

        Component.setSameSize(radioContainer, spacer1, spacer2);
        add(LayeredLayout.encloseIn(swipe, radioContainer));

        ButtonGroup barGroup = new ButtonGroup();
        RadioButton all = RadioButton.createToggle("All Entreprise", barGroup);
        all.setName("All Entreprise");
        all.setUIID("SelectBar");
        RadioButton featured = RadioButton.createToggle(" Mes Entreprise", barGroup);
        featured.setUIID("SelectBar");
        featured.setName("Mes Entreprise");
        RadioButton popular = RadioButton.createToggle("33", barGroup);
        popular.setUIID("SelectBar");
        popular.setName("33");
        RadioButton myFavorite = RadioButton.createToggle("+ Ajouter", barGroup);
        myFavorite.setUIID("SelectBar");
        myFavorite.setName("+ Ajouter");
        Label arrow = new Label(res.getImage("news-tab-down-arrow.png"), "Container");

        add(LayeredLayout.encloseIn(
                GridLayout.encloseIn(3, all, featured, myFavorite),
                FlowLayout.encloseBottom(arrow)
        ));

        featured.setSelected(true);
        arrow.setVisible(false);
        addShowListener(e -> {
            arrow.setVisible(true);
            updateArrowPosition(featured, arrow, res);
        });
        bindButtonSelection(all, arrow, res);
        bindButtonSelection(featured, arrow, res);
        bindButtonSelection(popular, arrow, res);
        bindButtonSelection(myFavorite, arrow, res);

        // special case for rotation
        addOrientationListener(e -> {
            updateArrowPosition(barGroup.getRadioButton(barGroup.getSelectedIndex()), arrow, res);
        });

        //ArrayList<Entreprise> entreprises;
        //entreprises=ServiceEntreprise.getInstance().getAllEntreprises();
        for (Entreprise obj : entreprises) {
            try {
                Image img = Image.createImage(FileSystemStorage.getInstance().getAppHomePath() + "/" + obj.getImage_entreprise());
                //System.out.println(obj.getImage_entreprise());
                addButton(img, obj, false, 0, 0, res);
            } catch (IOException ex) {
            }

        }
        //chart
 
        //
        final MapContainer cnt = new MapContainer(HTML_API_KEY);
        cnt.setCameraPosition(new Coord(36.1486233, 9.67401229999996));//this breaks the code //because the Google map is not loaded yet
        add(cnt);
        Style ss = new Style();
        ss.setFgColor(0xff0000);
        ss.setBgTransparency(0);
        FontImage markerImg = FontImage.createMaterial(FontImage.MATERIAL_PLACE, ss, 3);
        cnt.addMapListener(new MapListener() {

            @Override
            public void mapPositionUpdated(Component source, int zoom, Coord center) {
                System.out.println("Map position updated: zoom=" + zoom + ", Center=" + center);
            }
        });

        for (Entreprise obj : entreprises) {
            //  cnt.setCameraPosition(new Coord(obj.getLat(), obj.getLng()));
            double lat = obj.getLat();
            double lng = obj.getLng();
            System.out.println(lat + "|" + lng);
            // cnt.setCameraPosition(new Coord(lat, lng));
            cnt.addMarker(EncodedImage.createFromImage(markerImg, false), new Coord(lat, lng), "Hi marker", "Optional long description", new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    //    System.out.println("Bounding box is "+cnt.getBoundingBox());
                    //    ToastBar.showMessage("You clicked the marker", FontImage.MATERIAL_PLACE);
                }
            });

        }

    }



    private void updateArrowPosition(Button b, Label arrow, Resources res) {
        arrow.getUnselectedStyle().setMargin(LEFT, b.getX() + b.getWidth() / 2 - arrow.getWidth() / 2);
        arrow.getParent().repaint();
        //
        //System.out.println(b.getName());
        if (b.getName().equalsIgnoreCase("All Entreprise")) {
            new Front_Entreprise(res).show();
        } else if (b.getName().equalsIgnoreCase("+ Ajouter")) {
            new Front_AjouterEntreprise(res, current).show();
         //  Front_AjouterEntreprise Front_StatEntreprise
        }

    }

    private void addTab(Tabs swipe, Image img, Label spacer, String likesStr, String commentsStr, String text, Resources res) {
        int size = Math.min(Display.getInstance().getDisplayWidth(), Display.getInstance().getDisplayHeight());
        if (img.getHeight() < size) {
            img = img.scaledHeight(size);
        }
        Label likes = new Label(likesStr);
        Style heartStyle = new Style(likes.getUnselectedStyle());
        heartStyle.setFgColor(0xff2d55);
        FontImage heartImage = FontImage.createMaterial(FontImage.MATERIAL_ARROW_UPWARD, heartStyle);
        likes.setIcon(heartImage);
        likes.setTextPosition(RIGHT);
       
        
        Label stat = new Label("Statistique");
        FontImage statt = FontImage.createMaterial(FontImage.MATERIAL_DASHBOARD , heartStyle);
        stat.setIcon(statt);

        Label comments = new Label(commentsStr);
        FontImage.setMaterialIcon(comments, FontImage.MATERIAL_ARROW_DOWNWARD);
        if (img.getHeight() > Display.getInstance().getDisplayHeight() / 2) {
            img = img.scaledHeight(Display.getInstance().getDisplayHeight() / 2);
        }
        ScaleImageLabel image = new ScaleImageLabel(img);
        image.setUIID("Container");
        image.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
        Label overlay = new Label(" ", "ImageOverlay");

        Container page1 = LayeredLayout.encloseIn(
                image,
                overlay,
                BorderLayout.south(
                        BoxLayout.encloseY(
                                stat,
                                FlowLayout.encloseIn(likes, comments),
                                spacer
                        )
                )
        );
        likes.addPointerPressedListener(l -> {
            ToastBar.showMessage("up", FontImage.MATERIAL_INFO);
            new Front_MesEntreprise(res, null, "ASC").show();
        });

        comments.addPointerPressedListener(l -> {
            ToastBar.showMessage("down", FontImage.MATERIAL_INFO);
            new Front_MesEntreprise(res, null, "DESC").show();
        });
        
        stat.addPointerPressedListener(l -> {
            ToastBar.showMessage("stat", FontImage.MATERIAL_INFO);
            new Front_StatEntreprise(res,current).show();
        });

        swipe.addTab("", page1);
    }

    private void addButton(Image img, Entreprise e, boolean liked, int likeCount, int commentCount, Resources res) {
        int height = Display.getInstance().convertToPixels(11.5f);
        int width = Display.getInstance().convertToPixels(14f);
        Button image = new Button(img.fill(width, height));
        image.setUIID("Label");
        Container cnt = BorderLayout.west(image);
        // cnt.setLeadComponent(image);
        //  TextArea ta = new TextArea(title);
        //  ta.setUIID("NewsTopLine");
        //  ta.setEditable(false);

        /* Label likes = new Label(likeCount + " Likes  ", "NewsBottomLine");
        likes.setTextPosition(RIGHT);
        if (!liked) {
            FontImage.setMaterialIcon(likes, FontImage.MATERIAL_FAVORITE);
        } else {
            Style s = new Style(likes.getUnselectedStyle());
            s.setFgColor(0xff2d55);
            FontImage heartImage = FontImage.createMaterial(FontImage.MATERIAL_FAVORITE, s);
            likes.setIcon(heartImage);
        }
        Label comments = new Label(commentCount + " Comments", "NewsBottomLine");
        FontImage.setMaterialIcon(likes, FontImage.MATERIAL_CHAT);
         */
//      
        Label titre = new Label(e.getTitre_entreprise());

        Label lsupprimer = new Label(" ");
        lsupprimer.setUIID("supp");
        Style suppstyle = new Style(lsupprimer.getUnselectedStyle());
        suppstyle.setFgColor(0xf21f1f);

        FontImage suppimage = FontImage.createMaterial(FontImage.MATERIAL_DELETE, suppstyle);
        lsupprimer.setIcon(suppimage);
        lsupprimer.setTextPosition(RIGHT);

        Label lmodifier = new Label(" ");
        lmodifier.setUIID("mod");
        Style modstyle = new Style(lmodifier.getUnselectedStyle());
        modstyle.setFgColor(0x008000);

        FontImage modimage = FontImage.createMaterial(FontImage.MATERIAL_UPDATE, modstyle);
        lmodifier.setIcon(modimage);
        lmodifier.setTextPosition(LEFT);

        Button bb = new Button(" ");
        bb.setIcon(suppimage);

//        
        cnt.add(BorderLayout.CENTER,
                BoxLayout.encloseY(
                        BoxLayout.encloseX(titre),
                        BoxLayout.encloseX(lmodifier, lsupprimer)
                ));

        add(cnt);

        lsupprimer.addPointerPressedListener(l -> {
            //  System.out.println("ee");
            Dialog dig = new Dialog("Suppression");
            if (dig.show("Suppression", " sure ?", "Annuler", "Oui")) {
                dig.dispose();
            } else {
                dig.dispose();
                if (ServiceEntreprise.getInstance().deleteEntreprise(e.getId_entreprise())) {
                    new Front_MesEntreprise(res, null, null).show();
                }
                // ToastBar.showMessage("Entreprise supprimée", FontImage.MATERIAL_INFO);

            }

        });
        lmodifier.addPointerPressedListener(l -> {
            //  System.out.println("ee");
            Dialog dig = new Dialog("Modification");
            if (dig.show("Modification", " sure ?", "Annuler", "Oui")) {
                dig.dispose();
            } else {
                dig.dispose();

                new Front_ModifierEntreprise(res, e.getId_entreprise()).show();

                // ToastBar.showMessage("Entreprise supprimée", FontImage.MATERIAL_INFO);
            }

        });
        //  image.addActionListener(e -> details(title));
        image.addPointerPressedListener(l -> details(e.getTitre_entreprise()));
    }

    private void details(String title) {

        ToastBar.showMessage(title, FontImage.MATERIAL_INFO);

    }

    private void bindButtonSelection(Button b, Label arrow, Resources res) {
        b.addActionListener(e -> {
            if (b.isSelected()) {
                updateArrowPosition(b, arrow, res);
            }
        });
    }
}

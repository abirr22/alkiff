/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Entities.Entreprise;
import Services.ServiceEntreprise;
import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.components.ToastBar;
import com.codename1.googlemaps.MapContainer;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Storage;
import com.codename1.io.Util;
import com.codename1.maps.Coord;
import com.codename1.maps.MapListener;
import com.codename1.ui.Button;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import static com.codename1.ui.Component.LEFT;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.Slider;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.ImageIO;
import com.codename1.ui.util.Resources;
import static com.mycompany.myapp.MyApplication.Sessionuser;
import static com.mycompany.myapp.MyApplication.iduser;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javafx.scene.control.ToolBar;

/**
 *
 * @author unknown
 */
public class Front_AjouterEntreprise extends BaseForm {

    private static final String HTML_API_KEY = "AIzaSyBc-A_UD1XAqk3B1CyJczYw6AyQ3xUJgj0";
    String fileName, imgtype;
    double lat = 0, lng;

    public Front_AjouterEntreprise(Resources res, Form previous) {
        setTitle("Ajouter Entreprise");
        setLayout(BoxLayout.y());
        
                iduser=Sessionuser.getId();

        Label ltitre = new Label("Titre :");
        TextField Titre = new TextField("", "Titre", 20, TextArea.ANY);
        Label ldesc = new Label("Description :");
        TextArea Desc = new TextField("", "Description", 20, TextArea.ANY);
        Label lCap = new Label("Capital : ");
        Slider Cap = new Slider();
        Cap.setEditable(true);
        Cap.setMinValue(500);
        Cap.setMaxValue(1000000);
        Cap.addDataChangedListener((evt, index) -> {
            lCap.setText("Capital : " + Cap.getProgress());
        });
        Label lpays = new Label("Pays :");
        ComboBox Pays = new ComboBox<>();
        Pays.addItem("Choisir pays");
        Pays.addItem("TN");
        Pays.addItem("DZ");
        Pays.addItem("FR");
        Pays.addItem("GER");

//upload
        Button btnCapture = new Button("Upload");
        Label limage = new Label();
        limage.setHeight(250);
        limage.setWidth(250);

        btnCapture.addActionListener((e) -> {
            String path = Capture.capturePhoto(Display.getInstance().getDisplayWidth(), -1);
            // System.out.println(path);
            if (path != null) {
                try {
                    int height = Display.getInstance().convertToPixels(31.5f);
                    int width = Display.getInstance().convertToPixels(34f);

                    Image img = Image.createImage(path);
                    System.out.println(path);
                    int fileNameIndex = path.lastIndexOf("/") + 1;
                    fileName = path.substring(fileNameIndex, path.length() - 5);
                    System.out.println(fileName);

                    int fileNameIndex1 = path.lastIndexOf(".") + 1;
                    imgtype = path.substring(fileNameIndex1, path.length());
                    System.out.println(imgtype);
                    System.out.println(path);
                    fileName += "." + imgtype;
                    System.out.println(fileName);

                    limage.setIcon(img.fill(width, height));
                    OutputStream os = FileSystemStorage.getInstance().openOutputStream(FileSystemStorage.getInstance().getAppHomePath() + fileName);

                    if (imgtype.equalsIgnoreCase("jpg")) {
                        ImageIO.getImageIO().save(img, os, ImageIO.FORMAT_JPEG, 0.9f);
                    } else {
                        ImageIO.getImageIO().save(img, os, ImageIO.FORMAT_PNG, 0.9f);
                    }
                    os.close();
                    this.revalidate();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
//upload
//map
        Label lmap = new Label("Map Location :");
        final MapContainer cnt = new MapContainer(HTML_API_KEY);
        cnt.setCameraPosition(new Coord(36.1486233, 9.67401229999996));//this breaks the code //because the Google map is not loaded yet

        Style s = new Style();
        s.setFgColor(0xff0000);
        s.setBgTransparency(0);
        FontImage markerImg = FontImage.createMaterial(FontImage.MATERIAL_PLACE, s, 3);

        cnt.addMapListener(new MapListener() {

            @Override
            public void mapPositionUpdated(Component source, int zoom, Coord center) {
                System.out.println("Map position updated: zoom=" + zoom + ", Center=" + center);
            }
        });

        cnt.addLongPressListener(e -> {
            //  System.out.println(e.getX());
            cnt.clearMapLayers();
            System.out.println(cnt.getCoordAtPosition(e.getX(), e.getY()).getLatitude());
            System.out.println(cnt.getCoordAtPosition(e.getX(), e.getY()).getLongitude());

            lat = cnt.getCoordAtPosition(e.getX(), e.getY()).getLatitude() - 3.925;
            lng = cnt.getCoordAtPosition(e.getX(), e.getY()).getLongitude() + 2.3;
            cnt.setCameraPosition(new Coord(lat, lng));
            cnt.addMarker(EncodedImage.createFromImage(markerImg, false), cnt.getCameraPosition(), "Hi marker", "Optional long description", new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    //    System.out.println("Bounding box is "+cnt.getBoundingBox());
                    //    ToastBar.showMessage("You clicked the marker", FontImage.MATERIAL_PLACE);
                }
            });
            ToastBar.showMessage("Received longPress at " + e.getX() + ", " + e.getY(), FontImage.MATERIAL_3D_ROTATION);
        });

//map  
        Button btnValider = new Button("Ajouter Entreprise");
        //
        Label lretour = new Label(" ");
        lretour.setUIID("retour");
        Style modstyle = new Style(lretour.getUnselectedStyle());
        modstyle.setFgColor(0x008000);

        FontImage retourimage = FontImage.createMaterial(FontImage.MATERIAL_ARROW_BACK, modstyle);
        lretour.setIcon(retourimage);
        lretour.setTextPosition(RIGHT);
        //     

        addAll(BoxLayout.encloseX(BoxLayout.encloseX(lretour)),
                BoxLayout.encloseX(BoxLayout.encloseX(ltitre, Titre)),
                BoxLayout.encloseX(BoxLayout.encloseX(ldesc, Desc)),
                BoxLayout.encloseX(BoxLayout.encloseX(lCap)), Cap,
                BoxLayout.encloseX(BoxLayout.encloseX(lpays, Pays)),
                BoxLayout.encloseX(BoxLayout.encloseX(btnCapture, limage)), lmap, cnt, btnValider);

        lretour.addPointerPressedListener(l -> retour(res));

        btnValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if ((Titre.getText().length() == 0) || (Desc.getText().length() == 0)) {
                    Dialog.show("Alert", "SVP Remplir les champs de  Titre et description ", new Command("OK"));
                } else if (Cap.getProgress() == 500) {
                    Dialog.show("Alert", "SVP Remplir le champs de   Capital ", new Command("OK"));
                } else if (lat == 0) {
                    Dialog.show("Alert", "SVP choisir  location  ", new Command("OK"));
                } else {
                    try {
                        Entreprise e = new Entreprise(Titre.getText(), Desc.getText(), Cap.getProgress(),
                                fileName, Pays.getSelectedItem().toString(), iduser, lat, lng);
                        if (ServiceEntreprise.getInstance().addEntreprise(e)) {
//mail
                            String email = Sessionuser.getEmail();
                            ServiceEntreprise.getInstance().sendEmail(email);
//mail                            
                            Dialog.show("Success", "Connection accepted", new Command("OK"));
                            new Front_MesEntreprise(res, null, null).show();
                        } else {
                            Dialog.show("ERROR", "Server error", new Command("OK"));
                        }
                    } catch (NumberFormatException e) {
                        Dialog.show("ERROR", "Status must be a number", new Command("OK"));
                    }

                }

            }
        });

    }

    private void retour(Resources res) {

        new Front_MesEntreprise(res, null, null).show();
    }
    

}

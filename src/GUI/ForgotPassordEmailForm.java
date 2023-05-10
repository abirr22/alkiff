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

import Entities.Users;
import Services.ServiceUsers;
import com.codename1.components.FloatingHint;
import com.codename1.components.ToastBar;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.util.Resources;
import static com.mycompany.myapp.MyApplication.Sessionuser;
import java.util.Random;
import static com.mycompany.myapp.MyApplication.verificationcode;
import static com.mycompany.myapp.MyApplication.la;
import static com.mycompany.myapp.MyApplication.verificationemail;
import static com.mycompany.myapp.MyApplication.Resetuser;
import static com.mycompany.myapp.MyApplication.signupuser;
import static com.mycompany.myapp.MyApplication.verificationcode;
import java.util.ArrayList;

/**
 * Signup UI
 *
 * @author Shai Almog
 */
public class ForgotPassordEmailForm extends BaseForm {

    public ForgotPassordEmailForm(Resources res) {
        super(new BorderLayout());
        Toolbar tb = new Toolbar(true);
        setToolbar(tb);
        tb.setUIID("Container");
        getTitleArea().setUIID("Container");
        Form previous = Display.getInstance().getCurrent();
        tb.setBackCommand("", e -> previous.showBack());
        setUIID("SignIn");

        TextField email = new TextField("", "E-Mail", 20, TextField.EMAILADDR);

        email.setSingleLineTextArea(false);

        Button next = new Button("Next");
        Button signIn = new Button("Sign In");
        signIn.addActionListener(e -> previous.showBack());
        signIn.setUIID("Link");
        Label alreadHaveAnAccount = new Label("Already have an account?");

        Container content = BoxLayout.encloseY(
                new Label("Send Verification Code", "LogoLabel"),
                new FloatingHint(email),
                createLineSeparator()
        );
        content.setScrollableY(true);
        add(BorderLayout.CENTER, content);
        add(BorderLayout.SOUTH, BoxLayout.encloseY(
                next,
                FlowLayout.encloseCenter(alreadHaveAnAccount, signIn)
        ));
        next.requestFocus();
        next.addActionListener(e -> {
                                                        
                                ArrayList <Users> users = new ArrayList();
                                ServiceUsers sl =new ServiceUsers();
                                users=sl.getUserByEmaail(email.getText());
                                Users tmp = null;                         
                                for (Users fi : users) {
                                    System.out.println("salem " +fi);
                                    tmp = fi;
                                }
                                if(tmp == null)
                                {
                                Dialog.show("ERROR", "Email n'existe pas", new Command("OK"));
                                }
                                else
                                {
                                int len =10;
                                String val = "azertyuiopqsdfghjklmwxcvbnAZERTYUIOPQSDFGHJKLM1234567890";
                                Random sb = new Random();
                                Integer randomCode = sb.nextInt(999999);
                                la=randomCode;
                                System.out.println(la);
                                tmp.setPassword(la.toString());
                                sl.editUser(tmp);
                                ServiceUsers.getInstance().sendEmail2(tmp.getEmail(),la);
                                   Dialog.show("Success","Mail envoyee",new Command("OK"));
                                new SignInForm(res).show();
                                }
            Random rand = new Random();
            int randomCode = rand.nextInt(999999);
            verificationcode = randomCode;
            verificationemail = email.getText();
            //
            Users user;
            
             System.out.println(usr);
            

            //
        });
    }
public void sendMail(String Email,String Token) {
        ConnectionRequest req = new ConnectionRequest();
        req.setUrl("http://localhost/Email/sendmail.php?email="+ Email+"&token="+Token);

        req.addResponseListener(new ActionListener<NetworkEvent>() {

            @Override
            public void actionPerformed(NetworkEvent evt) {

                byte[] data = (byte[]) evt.getMetaData();
                String s = new String(data);
                System.err.println("Mail Sent");
            }
        });

        NetworkManager.getInstance().addToQueue(req);
    }
}

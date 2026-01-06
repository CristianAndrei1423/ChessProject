package UIPanels;

import Utils.Main;
import Utils.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpPanel extends JPanel {
    JTextField emailField;
    JPasswordField passField;
    JLabel alreadyInUse;
    MainFrame frame;
    public SignUpPanel(MainFrame frame){
        setLayout(new GridLayout(1, 2));
        this.frame = frame;
        // on the left
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(30, 40, 60));
        leftPanel.setLayout(new GridBagLayout());
        JLabel title = new JLabel("Chess Master");
        title.setFont(new Font("Serif", Font.BOLD, 40));
        title.setForeground(new Color(55, 1, 251, 239));
        leftPanel.add(title);

        // on the right
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(255, 255 ,255));
        rightPanel.setLayout(new GridBagLayout());

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS)); // ??
        form.setBackground(new Color(255, 255 ,255));

        JLabel lblWelcome = new JLabel("Make an account:");
        // lblWelcome.setFont(Style.FONT_TITLE);
        lblWelcome.setForeground(Color.BLACK);
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        emailField = new JTextField();
        passField = new JPasswordField();
        emailField.setMaximumSize(new Dimension(200, 30));
        passField.setMaximumSize(new Dimension(200, 30));


        JButton btnSignUp = createButton("Sign Up", new Color(31, 220, 105));
        btnSignUp.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnLogin = createButton("Already have an account?", new Color(73, 255, 0));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Action: Go to Main Menu
        btnSignUp.addActionListener(new SignUpHandler());
        btnLogin.addActionListener(e -> frame.showCard("LOGIN"));

        JLabel emailAddress = new JLabel("Email Address");
        emailAddress.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel pass = new JLabel("Password");
        pass.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel or = new JLabel("----------Or----------");
        or.setAlignmentX(Component.CENTER_ALIGNMENT);

        alreadyInUse = new JLabel("Email already exists");
        alreadyInUse.setForeground(Color.red);
        alreadyInUse.setAlignmentX(Component.CENTER_ALIGNMENT);
        alreadyInUse.setVisible(false);

        form.add(lblWelcome);
        form.add(Box.createVerticalStrut(10));
        form.add(alreadyInUse);
        form.add(Box.createVerticalStrut(10));
        form.add(emailAddress);
        form.add(emailField);
        form.add(Box.createVerticalStrut(10));
        form.add(pass);
        form.add(passField);
        form.add(Box.createVerticalStrut(10));
        form.add(btnSignUp);
        form.add(Box.createVerticalStrut(10));
        form.add(or);
        form.add(Box.createVerticalStrut(10));
        form.add(btnLogin);

        rightPanel.add(form);

        add(leftPanel);
        add(rightPanel);
    }

    private class SignUpHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            // here test email/password coresponds to user
            User user = null;
            user = Main.getChessGame().newAccount(emailField.getText(), passField.getText(), 0);
            if(user == null)
            {
                alreadyInUse.setVisible(true);
                return;
            }
            alreadyInUse.setVisible(false);
            frame.showCard("MENU");
        }
    }

    public static JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return btn;
    }
}

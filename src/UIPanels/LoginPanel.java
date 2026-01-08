package UIPanels;

import Utils.Main;
import Utils.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class LoginPanel extends JPanel {
    JTextField emailField;
    JPasswordField passField;
    JLabel incorrectPass;
    MainFrame frame;
    public LoginPanel(MainFrame frame) {

        setLayout(new GridLayout(1, 2));
        this.frame = frame;
        // on the left
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(PanelColors.DARKER_BLUE);
        leftPanel.setLayout(new GridBagLayout());
        JLabel title = new JLabel("Chess Master");
        title.setFont(new Font("Serif", Font.BOLD, 40));
        title.setForeground(new Color(55, 1, 251, 239));
        leftPanel.add(title);

        // on the right
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(PanelColors.THEME_DARK_BLUE);
        rightPanel.setLayout(new GridBagLayout());

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(PanelColors.THEME_DARK_BLUE);

        JLabel lblWelcome = new JLabel("Welcome Back");
        lblWelcome.setForeground(Color.BLACK);
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        emailField = new JTextField();
        passField = new JPasswordField();

        JButton btnLogin = createButton("Sign In", Color.GREEN);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnSignUp = createButton("Sign Up", Color.GREEN);
        btnSignUp.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnLogin.addActionListener(new LoginHandler());
        btnSignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.showCard("SIGNUP");
            }
        });

        JLabel emailAddress = new JLabel("Email Address");
        emailAddress.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailAddress.setForeground(Color.BLACK);

        JLabel pass = new JLabel("Password");
        pass.setAlignmentX(Component.CENTER_ALIGNMENT);
        pass.setForeground(Color.BLACK);

        JLabel or = new JLabel("----------Or----------");
        or.setAlignmentX(Component.CENTER_ALIGNMENT);
        or.setForeground(Color.BLACK);

        incorrectPass = new JLabel("Incorrect Password / Email");
        incorrectPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        incorrectPass.setForeground(Color.RED);
        incorrectPass.setVisible(false);

        form.add(lblWelcome);
        form.add(Box.createVerticalStrut(10));
        form.add(incorrectPass);
        form.add(Box.createVerticalStrut(10));
        form.add(emailAddress);
        form.add(emailField);
        form.add(Box.createVerticalStrut(10));
        form.add(pass);
        form.add(passField);
        form.add(Box.createVerticalStrut(10));
        form.add(btnLogin);
        form.add(Box.createVerticalStrut(10));
        form.add(or);
        form.add(Box.createVerticalStrut(10));
        form.add(btnSignUp);

        rightPanel.add(form);

        add(leftPanel);
        add(rightPanel);

    }

    private class LoginHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            // here test email/password coresponds to user
            User user;
            user = Main.getChessGame().login(emailField.getText(), passField.getText());
            if(user == null)
            {
                incorrectPass.setVisible(true);
                return;
            }
            incorrectPass.setVisible(false);
            MainFrame.showCard("MENU");
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
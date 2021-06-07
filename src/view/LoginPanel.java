package view;

import app.ClientApp;

import javax.net.ssl.KeyManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

public class LoginPanel extends JPanel {


    /**
     * UI Design 관련
     */
    Font hint = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
    Font userInput = new Font(Font.DIALOG_INPUT, Font.BOLD, 16);

    private JButton loginButton;

    public LoginPanel(ClientFrame clientFrame, ClientApp client) {

        JLayeredPane loginPane = new JLayeredPane();
        loginPane.setPreferredSize(getSize());
        loginPane.setBounds(0, 0, 900, 600);

        URL image = ClientFrame.class.getClassLoader().getResource("background.jpg");
        ImageIcon imageIcon = new ImageIcon(image);

        Image resized = imageIcon.getImage().getScaledInstance(900, 600, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(resized);

        JLabel label = new JLabel(imageIcon);
        label.setBounds(0, 0, 900, 600);
        label.setPreferredSize(getMaximumSize());
        label.setVisible(true);


        JTextField nickname = new JTextField();
        nickname.setBorder(BorderFactory.createCompoundBorder(
                nickname.getBorder(),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)));
        nickname.setBounds(300, 360, 300, 40);
        nickname.setText("nickname");
        nickname.setFont(hint);
        nickname.setHorizontalAlignment(SwingConstants.LEADING);
        nickname.setForeground(Color.GRAY);

        nickname.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nickname.getText().equals("nickname")) {
                    nickname.setText("");
                } else {
                    nickname.setText(nickname.getText());
                }
                nickname.setFont(userInput);
                nickname.setForeground(Color.DARK_GRAY);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (nickname.getText().equals("nickname") || nickname.getText().length() == 0) {
                    nickname.setText("nickname");
                    nickname.setForeground(Color.GRAY);
                    nickname.setFont(hint);
                } else {
                    nickname.setText(nickname.getText());
                    nickname.setForeground(Color.DARK_GRAY);
                }
            }
        });


        nickname.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Toolkit.getDefaultToolkit().beep();
                    loginButton.doClick();
                }
            }
        });


        loginButton = new JButton();
        loginButton.setBounds(350, 420, 200, 50);
        loginButton.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        loginButton.setText("START");
        loginButton.addActionListener(e -> {
            if (nickname.getText().length() != 0 && !nickname.getText().equals("nickname"))
                System.out.println("UserID: " + nickname.getText());
            client.login(nickname.getText().replaceAll("#", ""));
        });




        // 다 적용하는 부분
        loginPane.add(label, BorderLayout.CENTER, 0);
        loginPane.setLayer(label, -1);
        loginPane.add(nickname, BorderLayout.CENTER, 1);
        loginPane.setLayer(nickname, 0);
        loginPane.add(loginButton, BorderLayout.CENTER, 2);
        loginPane.setLayer(loginButton, 1);

        clientFrame.getContentPane().add(loginPane);
        clientFrame.pack();
        clientFrame.setSize(900, 600);
        clientFrame.setVisible(true);
        clientFrame.setLocationRelativeTo(null);
    }
}

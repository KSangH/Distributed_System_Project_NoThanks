package view.cardLabel;

import utils.Colors;
import view.ClientFrame;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class UserCardLabel extends JLayeredPane {

    public UserCardLabel(String cardNumber) {

        Color cardColor;

        URL imageFile = ClientFrame.class.getClassLoader().getResource("userNumber.png");
        ImageIcon cardIcon = new ImageIcon(imageFile);
        JLabel background = new JLabel(cardIcon);
        background.setBounds(0, 0, 90, 120);

        JPanel card = new JPanel();
        switch (Integer.parseInt(cardNumber) / 3) {
            case 1:
                cardColor = Colors.color3to5;
                break;
            case 2:
                cardColor = Colors.color6to8;
                break;
            case 3:
                cardColor = Colors.color9to11;
                break;
            case 4:
                cardColor = Colors.color12to14;
                break;
            case 5:
                cardColor = Colors.color15to17;
                break;
            case 6:
                cardColor = Colors.color18to20;
                break;
            case 7:
                cardColor = Colors.color21to23;
                break;
            case 8:
                cardColor = Colors.color24to26;
                break;
            case 9:
                cardColor = Colors.color27to29;
                break;
            case 10:
                cardColor = Colors.color30to32;
                break;
            case 11:
                cardColor = Colors.color33to35;
                break;
            default:
                cardColor = Colors.colorTransparent;
                break;
        }
        card.setBackground(cardColor);
        card.setBounds(10, 6, background.getWidth() - 20, background.getHeight() - 18);

        JLabel numberText = new JLabel(cardNumber);
        numberText.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
        numberText.setBackground(Colors.colorTransparent);
        numberText.setForeground(Color.WHITE);
        numberText.setBorder(null);
        numberText.setHorizontalAlignment(JLabel.CENTER);
        numberText.setLocation(0,0);
        numberText.setBounds(0, 0, background.getWidth(), background.getHeight());

        JLabel smallText1 = new JLabel(cardNumber);
        smallText1.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        smallText1.setBackground(Colors.colorTransparent);
        smallText1.setForeground(Color.WHITE);
        smallText1.setBorder(null);
        smallText1.setLocation(0,0);
        smallText1.setBounds(15, 8, background.getWidth(), 20);

        JLabel smallText2 = new JLabel(cardNumber);
        smallText2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
        smallText2.setBackground(Colors.colorTransparent);
        smallText2.setForeground(Color.WHITE);
        smallText2.setBorder(null);
        smallText2.setHorizontalAlignment(JLabel.CENTER);
        smallText2.setLocation(0,0);
        smallText2.setBounds(0, 0, background.getWidth(), background.getHeight());


        setLayout(null);
        setBounds(200, 0, 90, 120);
        add(background, 0);
        setLayer(background, 0);
        add(card, 1);
        setLayer(card, 1);
        add(numberText, 2);
        setLayer(numberText, 2);
        add(smallText1, 3);
        setLayer(smallText1, 3);
    }
}

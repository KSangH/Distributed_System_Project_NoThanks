package view.cardLabel;

import utils.Colors;
import view.ClientFrame;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class TabCardLabel extends JLayeredPane {

    private Color cardColor = Color.MAGENTA;

    public TabCardLabel(String cardNumber) {

        setPreferredSize(new Dimension(45, 48));
        setOpaque(false);

        if(cardNumber.isEmpty()) {
            setBackground(Colors.colorPrimaryRed);
            return;
        }

        URL imageFile = ClientFrame.class.getClassLoader().getResource("tabNumber.png");
        ImageIcon cardIcon = new ImageIcon(imageFile);
        JLabel background = new JLabel(cardIcon);
        background.setBounds(0, 0, 45, 48);

        JPanel card = new JPanel();
        if(!cardNumber.isEmpty()) {
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
        }
        card.setBackground(cardColor);
        card.setBounds(6, 4, background.getWidth() - 12, background.getHeight() - 15);

        JLabel numberText = new JLabel(cardNumber);
        numberText.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        numberText.setBackground(Colors.colorTransparent);
        numberText.setForeground(Color.WHITE);
        numberText.setBorder(null);
        numberText.setHorizontalAlignment(JLabel.CENTER);
        numberText.setLocation(0, 0);
        numberText.setBounds(0, 0, background.getWidth(), background.getHeight()-5);



        add(background, 0);
        setLayer(background, 0);
        add(card, 1);
        setLayer(card, 1);
        add(numberText, 2);
        setLayer(numberText, 2);
    }
}

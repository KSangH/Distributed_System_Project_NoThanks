package view;

import utils.Colors;
import view.cardLabel.CenterCardLabel;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class CenterTabPanel extends JPanel {

    protected Dimension _arcs = new Dimension(20, 20);

    private JLabel remainingCard, playedChipsLabel;
    private JLayeredPane playedChips;
    private CenterCardLabel flippedCard;

    public CenterTabPanel() {

        remainingCard = new JLabel();
        URL iconFile1 = ClientFrame.class.getClassLoader().getResource("centerRemaining.png");
        ImageIcon cardIcon = new ImageIcon(iconFile1);
        remainingCard.setIcon(cardIcon);
        remainingCard.setBounds(10, 10, 62, 84);
        remainingCard.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        remainingCard.setForeground(Colors.colorPrimaryRed);

        flippedCard = new CenterCardLabel("");
        flippedCard.setBounds(10, 10, 62, 84);


        playedChips = new JLayeredPane();
        playedChips.setBounds(10, 10, 62, 84);
        playedChips.setBackground(Colors.colorTransparent);

        JLabel chipBackground = new JLabel();
        URL iconFile3 = ClientFrame.class.getClassLoader().getResource("centerChip.png");
        ImageIcon chipIcon = new ImageIcon(iconFile3);
        chipBackground.setIcon(chipIcon);
        chipBackground.setBounds(0, 5, playedChips.getWidth(), playedChips.getHeight());

        playedChipsLabel = new JLabel("0");
        playedChipsLabel.setFont(new Font(Font.SERIF, Font.BOLD, 18));
        playedChipsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playedChipsLabel.setForeground(Color.WHITE);
        playedChipsLabel.setBounds(0, 0, playedChips.getWidth() - 6, playedChips.getHeight());

        playedChips.add(chipBackground, 0);
        playedChips.setLayer(chipBackground, 0);
        playedChips.add(playedChipsLabel, 1);
        playedChips.setLayer(playedChipsLabel, 1);

        setLayout(new GridLayout(1, 2));
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10), getBorder()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();

        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;
        graphics.setColor(Colors.colorBackground);

        graphics.fillRoundRect(0, 0, width, height, _arcs.width, _arcs.height);
    }

    public void updateInfo(int currentChip, int currentCard) {
        removeAll();

        playedChipsLabel.setText(String.valueOf(currentChip));

        if(currentCard == 0) {
            add(remainingCard);
        } else {
            flippedCard = new CenterCardLabel(String.valueOf(currentCard));
            add(flippedCard);
        }
        add(playedChips);
        revalidate();
        repaint();
    }
}

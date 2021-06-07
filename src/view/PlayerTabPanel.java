package view;

import utils.Colors;
import view.cardLabel.TabCardLabel;

import javax.swing.*;
import java.awt.*;

public class PlayerTabPanel extends JPanel {

    protected Dimension _arcs = new Dimension(40, 30);

    private int tabIndex;

    Font plainFont = new Font(Font.SANS_SERIF, Font.PLAIN, 16);

    private JLabel playerName;
    private JPanel cardSlot;
    private TabCardLabel playerCard;

    Color tint = Colors.colorTabUnFocused;

    public PlayerTabPanel(int tabIndex, String playerNickname) {
        this.tabIndex = tabIndex;

        setOpaque(true);
        setBackground(Colors.colorBackground);
        setLayout(new BorderLayout(0, 5));
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30), getBorder()));
        playerName = new JLabel(playerNickname);
        playerName.setForeground(Color.WHITE);
        playerName.setFont(plainFont);
        playerName.setBounds(10, 10, getWidth() - 20, 15);

        cardSlot = new JPanel();
        cardSlot.setOpaque(false);
        if (tabIndex % 2 == 0) {
            playerName.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            cardSlot.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        } else {
            playerName.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            cardSlot.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        }

        add(playerName, BorderLayout.NORTH);
        add(cardSlot, BorderLayout.CENTER);

    }

    @Override
    protected void paintComponent(Graphics g) {

        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;

        graphics.setColor(tint);
        if (tabIndex % 2 != 0) {
            graphics.fillRoundRect(0, 0, width - 15, height - 5, _arcs.width, _arcs.height);
        } else {
            graphics.fillRoundRect(15, 0, width - 15, height - 5, _arcs.width, _arcs.height);
        }
    }

    /*
        setTurn() : 현재 턴인 사람을 UI를 강조하기 위한 함수
        파라미터 : 턴여부(Boolean)
        반환형 : 현재객체(GamePanel)
     */
    public PlayerTabPanel setTurn(boolean b) {
        if (b) {
            tint = Colors.colorTabFocused;
            playerName.setForeground(Color.DARK_GRAY);
        } else {
            tint = Colors.colorTabUnFocused;
            playerName.setForeground(Color.WHITE);
        }

        revalidate();
        repaint();
        return this;
    }

    /*
        userClear() : 유저와 게임 정보를 모두 초기화하는 함수
        반환형 : 현재객체(GamePanel)
     */
    public PlayerTabPanel userClear() {
        setUser("");
        setTurn(false);
        gameClear();
        return this;
    }

    /*
        gameClear() : 게임 정보를 초기화하는 함수
        반환형 : 현재객체(GamePanel)
     */
    public PlayerTabPanel gameClear() {
        setCards("");
        return this;
    }

    /*
        setUser() : 유저 이름을 설정하는 함수
        파라미터 : 유저네임(String)
     */
    public void setUser(String username) {
        playerName.setText(username);
    }

    /*
        setCards() : 카드목록이 들어오면 카드객체를 추가하는 함수
        파라미터 : 카드목록문자열(String)
     */
    public void setCards(String cardInfo) {
        cardSlot.removeAll();

        String[] numbers = cardInfo.replace("[", "").replace("]", "").split(",");
        if (numbers[0].equals("")) return;
        for (String num : numbers) {
            playerCard = new TabCardLabel(num);
            playerCard.setBounds(0, 0, 45, 48);
            cardSlot.add(playerCard);
        }
        cardSlot.revalidate();
        cardSlot.repaint();

        revalidate();
        repaint();
    }

}

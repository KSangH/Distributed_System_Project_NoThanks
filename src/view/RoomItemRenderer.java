package view;

import app.ClientApp;
import game.Room;
import utils.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class RoomItemRenderer extends JPanel implements ListCellRenderer<Room> {

    private Color stateColor;


    private JLabel idLabel = new JLabel();
    private JLabel nameLabel = new JLabel();
    private JLabel playerLabel = new JLabel();
    private JLabel defaultLabel = new JLabel("/5 명");

    private ClientApp client;

    private JPanel roomItem;

    public RoomItemRenderer() {
        super();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(getBorder(), BorderFactory.createEmptyBorder(10, 30, 10, 20)));


        JPanel players = new JPanel();
        players.setLayout(new GridLayout(1, 2));
        players.setBackground(Colors.colorTransparent);
        players.add(playerLabel);
        players.add(defaultLabel);

        add(idLabel, BorderLayout.WEST);
        add(nameLabel, BorderLayout.CENTER);
        add(players, BorderLayout.EAST);
    }


    @Override
    public Component getListCellRendererComponent(JList<? extends Room> list, Room room, int index, boolean isSelected, boolean cellHasFocus) {
        // rendering
        idLabel.setText("No." + room.getRoomid() + "      ");
        nameLabel.setText(room.getRoomname());
        playerLabel.setText(String.valueOf(room.getPlayerCnt()));
        if(room.getPlayerCnt() < 5) {
            playerLabel.setForeground(Color.BLUE);
        }
        /** 테스트용 랜덤값 */
//        playerLabel.setText(String.valueOf((int)(Math.ceil(Math.random() * 10) % 5 + 1)));



        // ui
        playerLabel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        idLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        playerLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        defaultLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));



        setSize(610, 65);
        setPreferredSize(getSize());



        // 방 클릭 시
        if (cellHasFocus) {
            stateColor = Color.WHITE;
        } else {
            stateColor = Colors.colorDisabled;
        }

        if(playerLabel.getText().equals("5") || room.isBlocked()) {
            setEnabled(false);
            setFocusable(false);
            stateColor = Colors.colorDarkBlue;
        }


        roomItem = new JPanel();
        roomItem.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 4));
        roomItem.setBackground(Colors.colorBackground);
        roomItem.add(this);

        return roomItem;
    }

    protected Dimension _arcs = new Dimension(20, 20);

    @Override
    protected void paintComponent(Graphics g)
    {
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;

        graphics.setColor(stateColor);
        graphics.fillRoundRect(0,  0, width, height, _arcs.width, _arcs.height);

        if(stateColor == Color.WHITE) {     // border for the selected item
            graphics.setStroke(new BasicStroke(2.0f));
            graphics.setPaint(Color.BLACK);
            graphics.drawRoundRect(1,  1, width-2, height-2, _arcs.width-5, _arcs.height-5);
        }
    }

    public void setClient(ClientApp client) {
        this.client = client;
    }

    public void setStateColor(Color stateColor) {
        this.stateColor = stateColor;
    }
}

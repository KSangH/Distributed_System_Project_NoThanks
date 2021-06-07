package view;

import app.ClientApp;
import utils.Colors;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.DateFormat;
import java.util.Date;

public class ChatPanel extends JPanel {

    private JTextArea mainChat;
    private JComboBox<String> selectBox;

    private String roomid;

    /**
     * Chat Panel
     */
    public ChatPanel(ClientApp client) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(240, getHeight()));
        setBackground(null);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10), new LineBorder(Color.BLACK, 2, false)));

        JTextField chatTitle = new JTextField();
        chatTitle.setEditable(false);
        chatTitle.setText("CHAT!");
//   todo font     chatTitle.setFont(new Font(Font.SCADA, Font.BOLD, 24));
        chatTitle.setFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
        chatTitle.setForeground(Colors.colorPrimaryRed);
        chatTitle.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0), null));
        chatTitle.setHorizontalAlignment(SwingConstants.CENTER);

        mainChat = new JTextArea();
        mainChat.setWrapStyleWord(false);
        mainChat.setEditable(false);
        mainChat.setLineWrap(true);
        mainChat.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        mainChat.setForeground(Color.DARK_GRAY);
        JScrollPane chatScrollPane = new JScrollPane(mainChat);

        JPanel chatInputPanel = new JPanel(new BorderLayout());


        selectBox = new JComboBox<>();
        selectBox.addItem("전체");

        JTextField chatInput = new JTextField();
        chatInput.addActionListener(e -> {
            String range = "전체";
            if (((String) selectBox.getSelectedItem()).equals("방")) {
                range = getRoomid();
            }
            if (chatInput.getText().isEmpty()) return;
            client.sendChat(range, chatInput.getText());
            chatInput.setText("");
        });
        chatInput.setEnabled(true);

        selectBox.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2), selectBox.getBorder()));
        chatInput.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2), chatInput.getBorder()));

        chatInputPanel.add(selectBox, BorderLayout.WEST);
        chatInputPanel.add(chatInput, BorderLayout.CENTER);

        add(chatTitle, BorderLayout.NORTH);
        add(chatScrollPane, BorderLayout.CENTER);
        add(chatInputPanel, BorderLayout.SOUTH);
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
        selectBox.removeAllItems();
        if(!roomid.isEmpty())
            selectBox.addItem("방");
        selectBox.addItem("전체");

    }


    /*
        printMessage() : 채팅 메시지를 출력하는 함수
        파라미터 : 메시지(String)
     */
    public void printMessage(String msg) {
        Date date_now = new Date(System.currentTimeMillis());
        String date = "[" + DateFormat.getTimeInstance().format(date_now) + "] ";
        mainChat.append(date + msg + "\n");
        mainChat.setCaretPosition(mainChat.getDocument().getLength());
    }

    public void printStateMessage(boolean state) {
        if(state) {
            mainChat.append("-------방에 입장했습니다-------" + "\n");
        } else {
            mainChat.append("-------방을 퇴장했습니다-------" + "\n");
        }
        mainChat.setCaretPosition(mainChat.getDocument().getLength());
    }
}

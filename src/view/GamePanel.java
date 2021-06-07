package view;

import app.ClientApp;
import game.Room;
import utils.Colors;
import utils.NoThanksTheme;
import view.cardLabel.UserCardLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


public class GamePanel extends JPanel {

    private Room room;
    private ClientApp client;
    private PlayerTabPanel[] gamePanels;
    private Iterator<PlayerTabPanel> playerTabs;

    private JButton startButton;
    private JLabel roomPlayerLabel;
    private JLabel cardLabel, chipLabel, userChipsLabel;
    private JLayeredPane userPanel, myCardPanel, myCardPanel2, gameLayeredPane;
    private CenterTabPanel centerPanel;
    private int currentCard, currentChip = 0;


    public GamePanel(ClientFrame clientFrame, ClientApp client, Room room) {

        NoThanksTheme.registerCustomDefaultsSource("utils");
        NoThanksTheme.install();
        UIManager.put("Button.arc", 999);
        UIManager.put("Button.borderWidth", 2);
        UIManager.put("Component.arc", 10);
        UIManager.put("ProgressBar.arc", 999);
        UIManager.put("TextComponent.arc", 999);

        this.room = room;
        this.client = client;

        clientFrame.setBackground(Colors.colorBackground);

        JPanel jpanel = new JPanel();
        jpanel.setLayout(new BorderLayout());


        jpanel.setBackground(Colors.colorBackground);
        jpanel.setPreferredSize(getSize());
        jpanel.setBounds(0, 0, 900, 600);
        setVisible(true);

        /** Main Panel */
        JPanel mainPage = new JPanel();
        mainPage.setLayout(null);

        /** Top bar Panel */

        JPanel mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(null);
        mainMenuPanel.setPreferredSize(new Dimension(mainPage.getWidth(), 100));
        mainMenuPanel.setBackground(Colors.colorBackground);
        mainMenuPanel.setSize(700, 100);
        mainMenuPanel.setLocation(0, 0);

        JPanel text = new JPanel();
        text.setLayout(new GridLayout(2, 1));
        text.setBackground(Color.WHITE);
        text.setSize(200, 55);
        text.setLocation(35, 25);
        text.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 2),
                BorderFactory.createEmptyBorder(5, 1, 2, 1)));

        // Display room info (방 이름, player 수)
        JLabel roomNameLabel = new JLabel("  " + room.getRoomname());
        roomNameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        roomNameLabel.setForeground(Colors.colorPrimaryRed);

        roomPlayerLabel = new JLabel("  (Players: " + room.getPlayerCnt() + "/5)");
        roomPlayerLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

        text.add(roomNameLabel);
        text.add(roomPlayerLabel);


        URL logoFile = ClientFrame.class.getClassLoader().getResource("logo.png");
        ImageIcon logoIcon = new ImageIcon(logoFile);
        Image resized = logoIcon.getImage().getScaledInstance(85, 85, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(resized);
        JLabel logo = new JLabel(logoIcon);
        logo.setSize(90, 90);
        logo.setLocation(300, 5);
        logo.setVisible(true);

        JButton quitBtn = new JButton();
        quitBtn.setText("LEAVE GAME");
        quitBtn.setBackground(Colors.colorDarkBlue);
        quitBtn.setForeground(Color.BLACK);
        quitBtn.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        quitBtn.setSize(170, 30);
        quitBtn.setLocation(470, 35);
        quitBtn.addActionListener(e -> {
            client.requestExitRoom();
            clientFrame.getChatPanel().printStateMessage(false);
            clientFrame.setMainView();
        });


        mainMenuPanel.add(text);
        mainMenuPanel.add(logo);
        mainMenuPanel.add(quitBtn);


        /** Room Game Panel */
        gameLayeredPane = new JLayeredPane();
        gameLayeredPane.setBounds(0, 100, 665, 500);


        // gameContent 패널은 중간에 2x2로 나를 제외한 유저들의 정보를 표시하는 곳
        JPanel gameContent = new JPanel();
        gameContent.setLayout(new GridLayout(2, 2, 10, 5));
        gameContent.setBackground(Colors.colorBackground);
        gameContent.setSize(gameLayeredPane.getWidth(), 310);
        gameContent.setLocation(0, 0);

        // 각 Player panel 초기화
        gamePanels = new PlayerTabPanel[4];
        for (int i = 0; i < 4; i++) {
            gamePanels[i] = new PlayerTabPanel(i, "(blank)");
            gamePanels[i].setOpaque(false);     // 각 PlayerTabPanel 배경 흰색 안되게
            gameContent.add(gamePanels[i]);
        }

        /** Center Panel */
        centerPanel = new CenterTabPanel();
        centerPanel.setOpaque(false);
        centerPanel.setSize(140, 100);
        centerPanel.setLocation((gameContent.getWidth() - centerPanel.getWidth()) / 2, (gameContent.getHeight() - centerPanel.getHeight()) / 2 - 5);


        /** User Card Panel */
        userPanel = new JLayeredPane();
        userPanel.setBackground(Colors.colorBackground);
        userPanel.setBounds(0, 310, gameLayeredPane.getWidth(), 170);

        JPanel bgd = new JPanel();
        bgd.setBackground(Colors.colorBackground);
        bgd.setBounds(0, 0, userPanel.getWidth(), userPanel.getHeight());

        JLabel userBackground = new JLabel();
        URL iconFile1 = ClientFrame.class.getClassLoader().getResource("userPanel.png");
        ImageIcon addIcon = new ImageIcon(iconFile1);
        userBackground.setIcon(addIcon);
        userBackground.setBackground(Colors.colorBackground);
        userBackground.setBounds(0, 0, userPanel.getWidth(), userPanel.getHeight());


        cardLabel = new JLabel();
        URL iconFile2 = ClientFrame.class.getClassLoader().getResource("cardBack.png");
        ImageIcon cardIcon = new ImageIcon(iconFile2);
        cardLabel.setIcon(cardIcon);
        cardLabel.setBounds(20, 0, 117, 161);
        cardLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (cardLabel.isEnabled()) {
                    client.pickCard(String.valueOf(room.getRoomid()));
                    cardLabel.setEnabled(false);
                    chipLabel.setEnabled(false);
                }
            }
        });

        chipLabel = new JLabel();
        URL iconFile3 = ClientFrame.class.getClassLoader().getResource("chip.png");
        ImageIcon chipIcon = new ImageIcon(iconFile3);
        chipLabel.setIcon(chipIcon);
        chipLabel.setBounds(userPanel.getWidth() - 130, 0, 100, 100);
        chipLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(chipLabel.isEnabled()) {
                    client.spendChip(String.valueOf(room.getRoomid()));
                    cardLabel.setEnabled(false);
                    chipLabel.setEnabled(false);
                }
            }
        });


        userChipsLabel = new JLabel("your chips: " + 0);
        userChipsLabel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        userChipsLabel.setForeground(Color.WHITE);
        userChipsLabel.setBackground(Colors.colorTransparent);
        userChipsLabel.setBounds(userPanel.getWidth() - 130, 95, 100, 50);

        /** Game start Button */
        startButton = new JButton("START GAME");
        startButton.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        startButton.setForeground(Color.WHITE);
        startButton.setBackground(Colors.colorDisabled);
        startButton.addActionListener(e -> {
            client.startGame(String.valueOf(room.getRoomid()));
            startButton.setEnabled(false);
            startButton.setVisible(false);
        });
        startButton.setBounds(userPanel.getWidth() / 2 - 90, userPanel.getHeight() / 3, 180, 50);
        startButton.setEnabled(false);


        /** User cards Panel */
        myCardPanel = new JLayeredPane();
//        myCardPanel.setBounds(170, 30, 360, 125);
        myCardPanel2 = new JLayeredPane();


        // 적용
        userPanel.add(bgd, -1);
        userPanel.setLayer(bgd, -1);
        userPanel.add(userBackground, 0);
        userPanel.setLayer(userBackground, 0);
        userPanel.add(cardLabel, 1);
        userPanel.setLayer(cardLabel, 1);
        userPanel.add(chipLabel, 2);
        userPanel.setLayer(chipLabel, 2);
        userPanel.add(userChipsLabel, 3);
        userPanel.setLayer(userChipsLabel, 3);
        userPanel.add(startButton, 5);
        userPanel.setLayer(startButton, 5);
        userPanel.add(myCardPanel, 6);
        userPanel.setLayer(myCardPanel, 6);
        // for 12+ cards picked
        userPanel.add(myCardPanel2, 7);
        userPanel.setLayer(myCardPanel2,7);


        gameLayeredPane.add(gameContent, 0);
        gameLayeredPane.setLayer(gameContent, 0);
        gameLayeredPane.add(userPanel, 1);
        gameLayeredPane.setLayer(userPanel, 1);
        gameLayeredPane.add(centerPanel, 2);
        gameLayeredPane.setLayer(centerPanel, 2);


        /** ============= */

        mainPage.add(mainMenuPanel);
        mainPage.add(gameLayeredPane);

        /** Chat Panel */
        clientFrame.getChatPanel().setRoomid(String.valueOf(room.getRoomid()));

        jpanel.add(mainPage, BorderLayout.CENTER);
        jpanel.add(clientFrame.getChatPanel(), BorderLayout.EAST);


        clientFrame.getContentPane().add(jpanel);
        clientFrame.pack();
        clientFrame.setSize(900, 600);
        clientFrame.setVisible(true);

        clientFrame.getChatPanel().printStateMessage(true);

    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void updateRoom(String[] roomStatusInfo) {
        ArrayList<String> players = new ArrayList<>();
        for (int i = 2; i < roomStatusInfo.length; i++) {
            players.add(roomStatusInfo[i]);
        }
        Room updatedRoom = new Room(room.getRoomid(), room.getRoomname(), Integer.parseInt(roomStatusInfo[1]), players);
        setRoom(updatedRoom);

        startButton.setVisible(true);

        cardLabel.setEnabled(false);
        chipLabel.setEnabled(false);

        if (roomStatusInfo[2].equals(client.username) && room.getPlayerCnt() >= 2) { // 방장 && 2인이상
            startButton.setEnabled(true);
            startButton.setText("START GAME");
            startButton.setBackground(Colors.colorDarkBlue);
            startButton.setForeground(Color.WHITE);
        } else {
            startButton.setEnabled(false);
            startButton.setText("(Waiting)");
            startButton.setBackground(Colors.colorTransparent);
            startButton.setForeground(Color.GRAY);
        }
        roomPlayerLabel.setText("  (Players: " + room.getPlayerCnt() + "/5)");


        playerTabs = Arrays.stream(gamePanels).iterator();
        for (int i = 2; i < roomStatusInfo.length; i++) {
            if (!roomStatusInfo[i].equals(client.username)) {
                playerTabs.next().userClear().setUser(roomStatusInfo[i]);
            }
        }
        while (playerTabs.hasNext()) {
            playerTabs.next().userClear();
        }

        // 초기화 작업
        centerPanel.updateInfo(0, 0);
        centerPanel.repaint();

        userChipsLabel.setText("your chips: 0" );

        myCardPanel.removeAll();
        myCardPanel.revalidate();
        myCardPanel.repaint();

        myCardPanel2.removeAll();
        myCardPanel2.revalidate();
        myCardPanel2.repaint();

        revalidate();
        repaint();
    }

    public void updateGame(String[] gameStatusInfo) {
        startButton.setVisible(false);

        int myindex = Integer.parseInt(gameStatusInfo[1]);
        int turn = Integer.parseInt(gameStatusInfo[2]);
        currentChip = Integer.parseInt(gameStatusInfo[3]);
        currentCard = Integer.parseInt(gameStatusInfo[4]);
        int mychip = Integer.parseInt(gameStatusInfo[5]);


        if (turn == myindex && !gameStatusInfo[4].equals("0")) {    // my turn
            cardLabel.setEnabled(true);
            if (mychip > 0) {
                chipLabel.setEnabled(true);
            }
        } else {
            cardLabel.setEnabled(false);
            chipLabel.setEnabled(false);
        }

        centerPanel.updateInfo(currentChip, currentCard);

        // update text for my chips &  card sum
        userChipsLabel.setText("your chips: " + mychip);

        if (turn == (myindex + 1) % room.getPlayerCnt() || turn == myindex) {      // msg after my turn -> apply my actions & results to View
            String[] numbers = gameStatusInfo[myindex + 6].replace("[", "").replace("]", "").split(",");
            if (!numbers[0].equals("")) {

                int cardCount = numbers.length;

                myCardPanel.removeAll();
//                myCardPanel.setBounds(160, 30, 400, 125);


                if(cardCount <= 11) {
                    myCardPanel.setBounds(150, 30, 400, 125);

//                    myCardPanel.setBounds(160, 30, 400, 125);
                } else {
                    myCardPanel2.removeAll();
                    myCardPanel.setBounds(150, 0, 400, 125);
                    myCardPanel2.setBounds(135, 60, 450, 125);
                }


                for (int i = 0; i < cardCount; i++) {
                    UserCardLabel myCard = new UserCardLabel(numbers[i]);

                    if(i < 11) {
                        myCard.setBounds(i * 30, 0, 90, 125);
                        myCardPanel.add(myCard, i);
                        myCardPanel.setLayer(myCard, i);
                    } else {
                        myCard.setBounds((i - 11) * 30, 0, 90, 125);
                        myCardPanel2.add(myCard, i);
                        myCardPanel2.setLayer(myCard, i);
                    }
//                        myCard.setBounds(i * 360 / cardCount, 0, 90, 125);

                }

            }
        }

        // update other player's status to each panel
        playerTabs = Arrays.stream(gamePanels).iterator();
        for (int p = 0; p < room.getPlayerCnt(); p++) {
            if(p != myindex)
                playerTabs.next().setTurn(p == turn).setCards(gameStatusInfo[p + 6]);
        }


        revalidate();
        repaint();
    }
}

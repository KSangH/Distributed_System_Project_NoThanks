package view;

import app.ClientApp;
import game.Room;
import utils.Colors;
import utils.NoThanksTheme;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

public class MainPanel extends JPanel {

    private ClientApp client;

    private JPanel roomListPanel;
    private JScrollPane roomScrollPane;


    public MainPanel(ClientFrame clientFrame, ClientApp client) {
        NoThanksTheme.registerCustomDefaultsSource("utils");
        NoThanksTheme.install();
        UIManager.put( "Button.arc", 999 );
        UIManager.put( "Button.borderWidth", 2);
        UIManager.put( "Component.arc", 10 );
        UIManager.put( "ProgressBar.arc", 999 );
        UIManager.put( "TextComponent.arc", 999 );

        this.client = client;

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
        mainMenuPanel.setLocation(0,0);

        JButton createRoomBtn = new JButton();
        URL iconFile1 = ClientFrame.class.getClassLoader().getResource("add.png");
        ImageIcon addIcon = new ImageIcon(iconFile1);
        createRoomBtn.setIcon(addIcon);
        createRoomBtn.setSize(50, 50);
        createRoomBtn.setLocation(25,25);
        createRoomBtn.addActionListener(e -> {
            String roomName = JOptionPane.showInputDialog("방 제목을 입력하세요");
            if (roomName == null || roomName.equals(""))
                return;
            client.requestMakeRoom(roomName);
        });


        JButton refreshBtn = new JButton();
        URL iconFile2 = ClientFrame.class.getClassLoader().getResource("refresh.png");
        ImageIcon refreshIcon = new ImageIcon(iconFile2);
        refreshBtn.setIcon(refreshIcon);
        refreshBtn.setForeground(Colors.colorPrimaryRed);
        refreshBtn.setBackground(Color.WHITE);
        refreshBtn.setSize(50, 50);
        refreshBtn.setPreferredSize(getSize());
        refreshBtn.setLocation(85, 25);
        refreshBtn.setVisible(true);
        refreshBtn.addActionListener(e -> {
            client.requestRoomList();
        });




        URL logoFile = ClientFrame.class.getClassLoader().getResource("logo.png");
        ImageIcon logoIcon = new ImageIcon(logoFile);
        Image resized = logoIcon.getImage().getScaledInstance(85, 85, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(resized);
        JLabel logo = new JLabel(logoIcon);
        logo.setSize(90 , 90);
        logo.setLocation(300, 5);

        JButton logoutBtn = new JButton();
        logoutBtn.setText("LOGOUT");
        logoutBtn.setBackground(Colors.colorDarkBlue);
        logoutBtn.setForeground(Color.BLACK);
        logoutBtn.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        logoutBtn.setSize(120, 30);
        logoutBtn.setLocation(530,35);
        logoutBtn.addActionListener(e -> {
            client.logout();
            clientFrame.setLoginView();
        });



        mainMenuPanel.add(createRoomBtn);
        mainMenuPanel.add(refreshBtn);
        mainMenuPanel.add(logo);
        mainMenuPanel.add(logoutBtn);




        /** Room List Panel */
        roomListPanel = new JPanel();
        roomListPanel.setLayout(null);
        roomListPanel.setPreferredSize(new Dimension(jpanel.getWidth(), 100));
        roomListPanel.setBackground(Colors.colorBackground);
        roomListPanel.setSize(750, 500);
        roomListPanel.setLocation(0, 100);
        roomListPanel.setVisible(true);


        /** ============= */

        mainPage.add(mainMenuPanel);
        mainPage.add(roomListPanel);

        /** Chat Panel */
        clientFrame.getChatPanel().setRoomid("");


        jpanel.add(mainPage, BorderLayout.CENTER);
        jpanel.add(clientFrame.getChatPanel(), BorderLayout.EAST);



        clientFrame.getContentPane().add(jpanel);
        clientFrame.setSize(900, 600);
        clientFrame.setVisible(true);
        clientFrame.setLocationRelativeTo(null);
    }


    public void createRoomList(String[] msg) {
        roomListPanel.removeAll();

        DefaultListModel<Room> model = new DefaultListModel<>();

        int n = Integer.parseInt(msg[1]);
        for(int i = 0; i < n; i++) {
            Room room = new Room(Integer.parseInt(msg[4 * i + 2]), msg[4 * i + 3], Integer.parseInt(msg[4 * i + 4]), msg[4 * i + 5].equals("[게임 중]"));
            model.addElement(room);
        }

        JList<Room> list = new JList(model) {
            private static final long serialVersionUID = 1L;

            @Override
            public int locationToIndex(Point location) {
                int index = super.locationToIndex(location);
                System.out.println("location to index: " + index);

                if (index != -1 && !getCellBounds(index, index).contains(location)) {
                    clearSelection();
                    return -1;
                }
                else {
                    return index;
                }
            }
        };
        // set cell renderer
        RoomItemRenderer renderer = new RoomItemRenderer();
        renderer.setClient(client);
        list.setCellRenderer(renderer);
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(list.locationToIndex(e.getPoint()) == -1)
                    return;
                if(!list.getSelectedValue().isBlocked())
                    client.requestEnterRoom(list.getSelectedValue());
            }
        });
        list.setBackground(Colors.colorBackground);


        roomScrollPane = new JScrollPane(list);
        roomScrollPane.setSize(630, 450);
        roomScrollPane.setBackground(Colors.colorBackground);
        roomScrollPane.setLocation(25,0);
        roomScrollPane.setBorder(null);
        roomScrollPane.getVerticalScrollBar().setBackground(Colors.colorBackground);

        roomListPanel.add(roomScrollPane);
        roomListPanel.revalidate();
        roomListPanel.repaint();
    }

}

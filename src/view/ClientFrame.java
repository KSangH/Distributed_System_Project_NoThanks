package view;

import app.ClientApp;
import game.Game;
import game.Room;
import utils.NoThanksTheme;

import javax.swing.*;

/**
 * @author 쇼쇼
 */
public class ClientFrame extends JFrame {

    private ClientApp client;  // FIXME: Client.java 클래스에서 UI와 통신부분 분리해서 만들어진 클래스 (todo)


    private LoginPanel loginPanel;
    private ChatPanel chatPanel;
    private MainPanel mainPanel;
    private GamePanel gamePanel;

    public ClientFrame(ClientApp client) {
        this.client = client;


        NoThanksTheme.registerCustomDefaultsSource("utils");
        NoThanksTheme.install();
        UIManager.put( "Button.arc", 999 );
        UIManager.put( "Component.arc", 999 );
        UIManager.put( "ProgressBar.arc", 999 );
        UIManager.put( "TextComponent.arc", 999 );

        setLoginView();

        setSize(900, 600);
        setResizable(false);
        setTitle("NoThanks");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public ChatPanel getChatPanel() {
        return chatPanel;
    }

    public void setChatPanel() {
        this.chatPanel = new ChatPanel(client);
    }

    public void setLoginView() {
        getContentPane().removeAll();

        loginPanel = new LoginPanel(this, client);
        revalidate();
        repaint();
    }

    public void setMainView() {
        getContentPane().removeAll();

        mainPanel = new MainPanel(this, client);
        client.requestRoomList();
        revalidate();
        repaint();
    }

    public void setGameView(String[] msg) {
        getContentPane().removeAll();

        Room room = new Room(Integer.parseInt(msg[1]), msg[2]);
        gamePanel = new GamePanel(this, client, room);
        revalidate();
        repaint();
    }

    public void printChatMessage (String[] msg) {
        getChatPanel().printMessage("[" + msg[1] + "/" + msg[2] + "] " + msg[3]);
    }

    public void printRoomList (String[] msg) {
        mainPanel.createRoomList(msg);
    }

    public void updateRoomStatus(String[] roomPlayerInfo) {
        gamePanel.updateRoom(roomPlayerInfo);
    }

    public void updateGameStatus(String[] gameInfo) {
        gamePanel.updateGame(gameInfo);
    }
}

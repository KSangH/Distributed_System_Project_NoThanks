package app;

import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.util.Date;

/*
    app.Server : 서버의 GUI 환경과 CM을 시작하는 메인 클래스
 */
public class ServerApp extends JFrame {

    private CMServerStub m_serverStub;
    private ServerEventHandler m_eventHandler; // 이벤트 핸들러
    private JTextArea mainLog; // 로그를 기록하는 텍스트 패널
    private JScrollPane mainScroll; // 스크롤 패널

    /*
        생성자
     */
    public ServerApp() {
        init();
        m_serverStub = new CMServerStub();
        m_eventHandler = new ServerEventHandler(m_serverStub, this);
        m_serverStub.startCM();
        printMessage("서버가 시작되었습니다.");
    }

    /*
        init() : GUI 환경을 구성하는 함수
     */
    private void init() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        mainLog = new JTextArea();
        mainLog.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        mainLog.setEditable(false);

        mainScroll = new JScrollPane(mainLog);

        mainPanel.add(mainScroll);
        add(mainPanel);

        // JFrame 설정
        setTitle("NoThanks Main Server");
        setSize(500, 500);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /*
        printMessage() : 로그 메시지를 출력하는 함수
        파라미터 : 메시지(String)
     */
    public void printMessage(String msg) {
        Date date_now = new Date(System.currentTimeMillis());
        String date = "[" + DateFormat.getTimeInstance().format(date_now) + "] ";
        mainLog.append(date + msg + "\n");
        mainLog.setCaretPosition(mainLog.getDocument().getLength());
    }

    /*
        getServerEventHandler() : 이벤트 핸들러 반환하는 함수
        반환값 : 이벤트핸들러(app.ServerEventHandler)
     */
    public ServerEventHandler getServerEventHandler() {
        return m_eventHandler;
    }

    /*
        getServerStub() : 서버스텁 반환하는 함수
        반환값 : 서버스텁객체(CMServerStub)
     */
    public CMServerStub getServerStub() {
        return m_serverStub;
    }


    public static void main(String[] args) {
        ServerApp server = new ServerApp();
        CMServerStub cmStub = server.getServerStub();
        cmStub.setAppEventHandler(server.getServerEventHandler());
    }
}

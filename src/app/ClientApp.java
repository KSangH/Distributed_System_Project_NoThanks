package app;

import game.Room;
import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;
import view.ClientFrame;
import view.MainPanel;

import javax.swing.*;

public class ClientApp {

    private CMClientStub m_clientStub;
    private ClientEventHandler m_eventHandler;

    private ClientFrame clientFrame;

    public static String username;

    public ClientApp() {
        m_clientStub = new CMClientStub();
        m_eventHandler = new ClientEventHandler(m_clientStub, this);
        m_clientStub.startCM();

        clientFrame = new ClientFrame(this);
    }

    public static void main(String[] args) {
        ClientApp client = new ClientApp();
        CMClientStub cmStub = client.getClientStub();
        cmStub.setAppEventHandler(client.getClientEventHandler());
    }
    /*
        clientController() : 클라이언트로 들어온 이벤트의 GUI를 처리하는 컨트롤러
        파라미터 : 메시지(String)
     */
    public void clientController(String dummymsg) {
        String[] msg = dummymsg.split("#");
        switch (msg[0]) {

            // 로그인 성공 => 로그인 시 사용할 수 있는 기능 활성화, 방목록 요청
            case Information.LOGIN_SUCCESS:
                username = m_clientStub.getMyself().getName();
                clientFrame.setChatPanel();
                clientFrame.setMainView();
                break;

            // 동일닉네임 사용 => 중복여부를 알림
            case Information.LOGIN_DUPLICATE:
                JOptionPane.showMessageDialog(clientFrame, "중복된 아이디입니다");
                break;

            // 방입장거부, 방만들기거부, 게임종료 이벤트 => 메시지 알림 [수신/메시지]
            case Information.ROOM_ENTER_REJECT:
            case Information.ROOM_MAKE_REJECT:
            case Information.GAME_END_INFO:
                JOptionPane.showMessageDialog(clientFrame, msg[1]);
                break;

            // 방목록 수신 => 방목록 패널에 방입장 버튼 추가 [수신/방개수#(방번호#방제목#인원수#게임상태)]
            case Information.ROOM_LIST_INFO:
                clientFrame.printRoomList(msg);
                break;

            // 방만들기성공, 방입장성공이 수신 => mainPage에서 gamePage로 전환 [수신/게임방번호#방제목]
            case Information.ROOM_MAKE_SUCCESS:
            case Information.ROOM_ENTER_SUCCESS:
                clientFrame.setGameView(msg);
                break;

            case Information.ROOM_STATUS_INFO:
                clientFrame.updateRoomStatus(msg);
                break;

            // 게임상태를 수신받았을 때 => 게임 패널에 정보 업데이트 [수신/나의인덱스#현재턴#현재칩#현재카드#나의칩#유저들의카드#~]
            case Information.GAME_STATUS_INFO:
                clientFrame.updateGameStatus(msg);
                break;

            // 채팅이 수신되었을 때 [수신/전체or방#보낸사람#메시지내용]
            case Information.GAME_CHAT_INFO:
                clientFrame.printChatMessage(msg);
                break;
        }
    }

    /*
        sendServer() : 서버로 이벤트를 전송하는 함수
        파라미터 : 이벤트코드(String), 메시지(String...)
     */
    private void sendServer(String code, String... param) {
        CMDummyEvent dummyEvent = new CMDummyEvent();
        String msg = code + "#" + String.join("#", param);
        dummyEvent.setDummyInfo(msg);
        m_clientStub.send(dummyEvent, "SERVER");
    }

    /*
        getClient() : 클라이언트 객체를 반환하는 함수
        반환값 : 클라이언트객체(Client)
     */
    private ClientApp getClient() {
        return this;
    }

    /*
        getClientEventHandler() : 클라이언트 이벤트핸들러 객체를 반환하는 함수
        반환값 : ClientEventHandler
     */
    public ClientEventHandler getClientEventHandler() {
        return m_eventHandler;
    }

    /*
        getClientStub() : 클라이언트스텁 객체를 반환하는 함수
        반환값 : CMClientStub
     */
    public CMClientStub getClientStub() {
        return m_clientStub;
    }


    /*
        아래는 모두 특정 이벤트를 서버로 요청하는 함수이다.
     */
    public void login(String nickname) {
        m_clientStub.loginCM(nickname, nickname);
    }

    public void logout() {
        m_clientStub.logoutCM();
    }

    public void sendChat(String range, String content) { sendServer(Information.GAME_CHAT, range, content); }

    public void requestRoomList() {
        sendServer(Information.ROOM_LIST);
    }

    public void requestMakeRoom (String id) {
        sendServer(Information.ROOM_MAKE, id);
    }

    public void requestEnterRoom (Room room) {
        sendServer(Information.ROOM_ENTER, String.valueOf(room.getRoomid()));
    }

    public void requestExitRoom () {
        sendServer(Information.ROOM_EXIT);
        requestRoomList();
    }

    public void startGame(String roomid) {
        sendServer(Information.GAME_START, roomid);
    }

    public void pickCard(String roomid) {
        sendServer(Information.GAME_NEXT,roomid, "RECV");
    }

    public void spendChip(String roomid) {
        sendServer(Information.GAME_NEXT, roomid, "PASS");
    }
}


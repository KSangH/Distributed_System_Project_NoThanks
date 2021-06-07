package app;

import game.Room;
import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMSessionEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

import java.util.ArrayList;
import java.util.HashMap;

/*
    app.ServerEventHandler : 클라이언트에서 서버로 들어오는 메시지를 처리하는 클래스
 */
public class ServerEventHandler implements CMAppEventHandler {

    private ServerApp m_server; // 서버 객체
    private CMServerStub m_serverStub;
    private int roomKey; // 방의ID 값으로 이용할 변수
    private HashMap<Integer, Room> rooms; // 방의 리스트

    /*
        생성자
        파라미터 : CMServerStub, app.Server
     */
    public ServerEventHandler(CMServerStub m_serverStub, ServerApp server) {
        this.m_server = server;
        this.m_serverStub = m_serverStub;
        this.rooms = new HashMap<>();
        this.roomKey = 1;
    }

    /*
        processEvent() : 이벤트 처리 함수
        파라미터 : 이벤트(CMEvent)
     */
    @Override
    public void processEvent(CMEvent cmEvent) {
        switch (cmEvent.getType()) {
            case CMInfo
                    .CM_SESSION_EVENT:
                eventLogin(cmEvent);
                break;
            case CMInfo.CM_DUMMY_EVENT:
                eventGame(cmEvent);
                break;
        }
    }

    /*
        eventLogin() : 로그인 관련 이벤트 관할 함수
        파라미터 : 이벤트(CMEvent)
     */
    private void eventLogin(CMEvent cme) {
        CMSessionEvent se = (CMSessionEvent) cme;
        switch (se.getID()) {
            case CMSessionEvent.LOGIN:
                // 로그인 시, 서버와 클라이언트에 로그인 메시지 출력
                m_server.printMessage(se.getUserName() + "님이 로그인하였습니다.");
                m_serverStub.replyEvent(se, 1);
                sendAll(Information.GAME_CHAT_INFO, "전체", "시스템", se.getUserName() + "님이 로그인 하였습니다.");
                break;
            case CMSessionEvent.LOGOUT:
                // 로그아웃 시, 서버와 클라이언트에 로그아웃 메시지 출력, 본인이 방에서 종료한 경우, 방퇴장 함수 실행
                m_server.printMessage(se.getUserName() + "님이 로그아웃하였습니다.");
                leaveRoom(se.getUserName());
                sendAll(Information.GAME_CHAT_INFO, "전체", "시스템", se.getUserName() + "님이 로그아웃 하였습니다.");
                break;
        }
    }

    /*
        eventGame() : 방, 게임 관련 이벤트를 처리하는 함수
        파라미터 : 이벤트(CMEvent)
     */
    private void eventGame(CMEvent cme) {
        CMDummyEvent de = (CMDummyEvent) cme;
        String code = de.getDummyInfo();
        String[] msg = code.split("#");
        Room room;
        switch (msg[0]) {
            // 클라이언트 => 서버로 방의 리스트를 요청하여 처리하는 코드 []
            case Information.ROOM_LIST:
                m_server.printMessage(de.getSender() + "님이 방목록을 요청하였습니다.");
                String list = rooms.size() + "#";
                for (Room i : rooms.values()) {
                    list += i.getRoomid() + "#" + i.getRoomname() + "#" + i.getSize() + "#" + (i.isGame() ? "[게임 중]": "[대기 중]") + "#";
                }
                send(de.getSender(), Information.ROOM_LIST_INFO, list);
                break;
            // 클라이언트 => 서버로 방의 입장을 요청하여 처리하는 코드 [방번호]
            case Information.ROOM_ENTER:
                m_server.printMessage(de.getSender() + "(이)가 " + msg[1] + "번방으로 게임입장을 요청하였습니다.");
                room = rooms.getOrDefault(Integer.parseInt(msg[1]), null);
                if (room == null) {
                    // 방이 없는 경우
                    send(de.getSender(), Information.ROOM_ENTER_REJECT, "방이 존재하지 않습니다.");
                } else if (room.enterRoom(de.getSender())) {
                    // 방 입장에 성공한 경우, 해당 유저에게 성공여부, 방의 사용자들에게 입장여부 알림
                    send(de.getSender(), Information.ROOM_ENTER_SUCCESS,
                            String.valueOf(room.getRoomid()), room.getRoomname());
                    sendRoom(room, Information.ROOM_STATUS_INFO, room.getRoomStatus());
                    sendRoom(room, Information.GAME_CHAT_INFO, "방", "시스템", de.getSender() + "님이 입장하였습니다.");
                } else {
                    // 방 입장에 실패한 경우
                    send(de.getSender(), Information.ROOM_ENTER_REJECT, "인원이 가득 차거나 이미 시작한 방입니다.");
                }
                break;
            // 클라이언트 => 서버로 방의 퇴장을 요청하여 처리하는 코드 []
            case Information.ROOM_EXIT:
                m_server.printMessage(de.getSender() + "님이 게임퇴장을 요청하였습니다.");
                leaveRoom(de.getSender());
                break;
            // 클라이언트 => 서버로 방의 생성을 요청하여 처리하는 코드 [방의제목]
            case Information.ROOM_MAKE:
                m_server.printMessage(de.getSender() + "님이 " + msg[1] + "방만들기를 요청했습니다.");
                if (rooms.size() < 10) {
                    // 방을 생성
                    room = new Room(roomKey++, msg[1]);
                    room.enterRoom(de.getSender());
                    rooms.put(room.getRoomid(), room);
                    // 방을 성공적으로 만들었다는 메시지 전송, 방의 상태 정보 전송, 입장 정보 전송
                    send(de.getSender(), Information.ROOM_MAKE_SUCCESS,
                            String.valueOf(room.getRoomid()), room.getRoomname());
                    sendRoom(room, Information.ROOM_STATUS_INFO, room.getRoomStatus());
                    sendRoom(room, Information.GAME_CHAT_INFO, "방", "시스템", de.getSender() + "님이 입장하였습니다.");
                } else {
                    // 서버에 10개 이상 방을 생성할 수 없음.
                    send(de.getSender(), Information.ROOM_MAKE_REJECT, "현재 서버의 방의 개수를 초과하였습니다.");
                }
                break;

            // 클라이언트 => 서버로 게임의 시작을 요청하여 처리하는 코드 [방번호]
            case Information.GAME_START:
                m_server.printMessage(de.getSender() + "님이 " + msg[1] + "번방의 게임을 시작했습니다.");
                room = rooms.get(Integer.parseInt(msg[1]));
                // 게임을 시작
                boolean start = room.startGame();
                // 게임 시작에 성공했으면 메시지 송출
                if (start) {
                    sendRoom(room, Information.GAME_CHAT_INFO, "방", "시스템", "게임이 시작되었습니다.");
                    sendRoom(room, Information.GAME_STATUS_INFO);
                }
                break;
            // 클라이언트 => 서버로 게임 중 PASS/RECV를 요청하여 처리하는 코드 [방번호#버튼]
            case Information.GAME_NEXT:
                m_server.printMessage(de.getSender() + "님이 " + msg[1] + "방에서 " + msg[2] + "버튼을 눌렀습니다.");
                room = rooms.get(Integer.parseInt(msg[1]));
                room.setNext(de.getSender(), msg[2]);
                sendRoom(room, Information.GAME_STATUS_INFO);
                // 아래는 게임이 종료되었을 경우 실행
                if(!room.isGame()){
                    sendRoom(room, Information.GAME_CHAT_INFO, "방", "시스템", "게임이 종료되었습니다.");
                    sendRoom(room, Information.GAME_END_INFO, room.getGameResult());
                    sendRoom(room, Information.ROOM_STATUS_INFO, room.getRoomStatus());
                    m_server.printMessage( room.getRoomid() + "번방에서 게임이 종료되었습니다.");
                }
                break;
            // 클라이언트 => 서버로 채팅을 요청하여 처리하는 코드 [전체or방번호#메시지]
            case Information.GAME_CHAT:
                m_server.printMessage(de.getSender() + "님이 " + msg[1] + "로 '" + msg[2] + "' 채팅을 보냈습니다.");
                if (msg[1].equals("전체")) {
                    sendAll(Information.GAME_CHAT_INFO, "전체", de.getSender(), msg[2]);
                } else {
                    sendRoom(rooms.getOrDefault(Integer.parseInt(msg[1]), null),
                            Information.GAME_CHAT_INFO, "방", de.getSender(), msg[2]);
                }
                break;
        }
    }

    /*
        leaveRoom() : 강제종료 or 방퇴장 or 방입장 중복클릭 된 경우, 해당 유저가 방에 있는 경우 퇴장시키는 함수
        파라미터 : 유저네임(String)
     */
    private void leaveRoom(String username) {
        for (Room room : rooms.values()) {
            if (room.isUser(username)) { // 방에 소속 된 유저인 경우.
                room.exitRoom(username);
                sendRoom(room, Information.GAME_CHAT_INFO, "방", "시스템", username + "님이 퇴장하였습니다.");
                sendRoom(room, Information.ROOM_STATUS_INFO, room.getRoomStatus());
                // 게임 중인 경우, 해당 함수가 실행되면 게임을 중단
                if(room.isGame()){
                    room.endGame();
                    sendRoom(room, Information.GAME_CHAT_INFO, "방", "시스템", "게임이 중단되었습니다.");
                }
            }
        }
    }

    /*
        sendRoom() : 방에 있는 모든 사람에게 이벤트를 보내는 함수
        파라미타 : 방객체(Room), 이벤트코드(String), 메시지(String...)
     */
    private void sendRoom(Room room, String code, String... param) {
        if (room == null) return;
        ArrayList<String> users;
        CMDummyEvent dummyEvent = new CMDummyEvent();
        switch(code){
            // 게임 정보를 보내는 경우엔, 인원 별로 메시지가 다르므로 별도 처리
            case Information.GAME_STATUS_INFO:
                users = room.getUsers();
                for (int i = 0; i < users.size(); i++) {
                    String msg = code + "#" + String.join("#", room.getGame().getGameStatus(i));
                    dummyEvent.setDummyInfo(msg);
                    m_serverStub.send(dummyEvent, users.get(i));
                }
                break;
            // 일반적인 방의 사용자 모두에게 메시지 전송
            default:
                String msg = code + "#" + String.join("#", param);
                dummyEvent.setDummyInfo(msg);
                users = room.getUsers();
                for (String target : users) {
                    m_serverStub.send(dummyEvent, target);
                }
        }
    }

    /*
        sendAll() : 로그인 된 모든 사용자에게 이벤트를 보내는 함수
        파라미타 : 이벤트코드(String), 메시지(String...)
     */
    private void sendAll(String code, String... param) {
        CMDummyEvent dummyEvent = new CMDummyEvent();
        String msg = code + "#" + String.join("#", param);
        dummyEvent.setDummyInfo(msg);
        m_serverStub.broadcast(dummyEvent);
    }

    /*
        send() : 특정 타겟에게 이벤트를 보내는 함수
        파라미타 : 받는유저네임(String), 이벤트코드(String), 메시지(String...)
     */
    private void send(String target, String code, String... param) {
        CMDummyEvent dummyEvent = new CMDummyEvent();
        String msg = code + "#" + String.join("#", param);
        dummyEvent.setDummyInfo(msg);
        m_serverStub.send(dummyEvent, target);
    }
}

package game;

import java.util.ArrayList;

/*
    Room : 방에 대한 정보를 관리하는 클래스
 */
public class Room {
    private ArrayList<String> users; // 현재 방의 유저 목록
    private int roomid; // 현재 방의 ID
    private String roomname; // 현재 방의 제목
    private Game game; // 현재 방의 게임 객체

    // 05.24 신지우
    private int playerCnt;      // 방의 인원수
    private boolean isBlocked;  // 게임 진행 중 여부

    /*
        생성자
        파라미터 : 방의 ID(int), 방의 제목(String)
     */
    public Room(int id, String name) {
        users = new ArrayList<>();
        roomid = id;
        roomname = name;
    }

    /** 생성자 type2:
     * 방 목록 생성 시
     * @param roomid, roomname, playerCnt, isBlocked
     */
    public Room(int roomid, String roomname, int playerCnt, boolean isBlocked) {
        this.roomid = roomid;
        this.roomname = roomname;
        this.playerCnt = playerCnt;
        this.isBlocked = isBlocked;
    }

    /** 생성자 type3
     * 방 정보 update 용도
     * @return
     */
    public Room(int roomid, String roomname, int playerCnt, ArrayList users) {
        this.roomid = roomid;
        this.roomname = roomname;
        this.playerCnt = playerCnt;
        this.users = new ArrayList();
        this.users.addAll(users);
    }

    /*
        getRoomid() : 방의 ID를 반환하는 함수.
        반환값 : 방의ID(int)
     */
    public int getRoomid() {
        return roomid;
    }

    /*
        getRoomname() : 방의 제목를 반환하는 함수.
        반환값 : 방의 제목(String)
     */
    public String getRoomname() {
        return roomname;
    }

    /*
        getUsers() : 방의 유저목록을 반환하는 함수
        반환값 : 유저리스트(ArrayList)
     */
    public ArrayList<String> getUsers() {
        return users;
    }

    /*
        getGame() : 방의 게임객체를 반환하는 함수
        반환값 : 게임객체(Game)
     */
    public Game getGame(){
        return game;
    }

    /*
        getSize() : 현재 방의 유저 수를 반환하는 함수.
        반환값 : 유저수(int)
     */
    public int getSize() {
        return users.size();
    }

    /*
        getRoomStatus() : 방의 유저 목록을 문자열로 반환하는 함수.
        반환값 : 인원수#유저목록(#)(String)
     */
    public String getRoomStatus() {
        String status = "";
        status += users.size();
        for (String user : users) {
            status += "#" + user;
        }
        return status;
    }

    /*
        isUser() : 현재 방에 소속된 유저인지 확인하는 함수
        파라미터 : 유저네임(String)
        반환값 : 유저여뷰(boolean)
     */
    public boolean isUser(String username) {
        if (users.indexOf(username) < 0) {
            return false;
        } else {
            return true;
        }
    }

    /*
        isGame() : 현재 게임이 진행 중인지 확인하는 함수
        반환값 : 진행중(true)/대기중(false) (boolean)
     */
    public boolean isGame(){
        if(game != null && !game.isEnd()){
            return true; // 게임 이미시작함.
        }
        return false;
    }

    /*
        enterRoom() : 현재 방의 입장하는 함수
        파라미터 : 유저네임(String)
        반환값 : 성공여부(boolean)
     */
    public boolean enterRoom(String username) {
        if (users.size() == 5) {
            return false;  // 인원 초과
        }
        if(isGame()){
            return false; // 게임이 이미 시작됨.
        }

        // 이 방에 소속되어 있지 않은 경우에만 추가 (중복 클릭 대처)
        if (!isUser(username)) {
            users.add(username);
        }
        return true;
    }

    /*
        exitRoom() : 현재 방의 퇴장하는 함수
        파라미터 : 유저네임(String)
        반환값 : 성공여부(boolean)
     */
    public boolean exitRoom(String username) {
        if (isUser(username)) {
            users.remove(username);
            return true;
        } else {
            return false;
        }
    }

    /*
        startGame() : 게임을 시작하는 함수
        반환값 : 성공여부(boolean)
     */
    public boolean startGame(){
        if(getSize() >= 2){
            // 2명 이상인 경우 게임 시작
            game = new Game(getSize());
            return true;
        } else {
            return false;
        }
    }

    /*
        endGame() : 게임을 강제로 종료하는 함수 (게임 중 나가는 경우 작동)
     */
    public void endGame(){
        game = null;
    }

    /*
        setNext() : 게임 중 PASS, RECV 버튼을 누른 경우 작동하는 함수
        파라미터 : 유저네임(String), 해당 유저가 선택한 코드(String)
        반환값 : 성공여부(boolean)
     */
    public boolean setNext(String name, String code){
        if(game.isEnd()){
            return false;
        }
        return game.setNext(users.indexOf(name), code);
    }

    /*
        getGameResult() : 게임 결과를 게임 객체에서 받아오는 함수
        반환값 : 게임결과(String)
     */
    public String getGameResult(){
        if(game == null) return "점수 집계에 오류가 발생하였습니다.";
        return game.getGameResult(getUsers());
    }

    public int getPlayerCnt() {
        return playerCnt;
    }

    public void setPlayerCnt(int playerCnt) {
        this.playerCnt = playerCnt;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}

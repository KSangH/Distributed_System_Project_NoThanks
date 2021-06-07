package app;

/*
    Infomation : 사용자 지정 코드를 정의하는 클래스
 */

/** Refactored: Infomation.java
 *
 * 서버 요청/응답 관련된 코드
 */
public class Information {

    // CLIENT 
    static final String LOGIN = "0"; // 로그인 버튼 연동
    // SERVER => CLIENT  
    static final String LOGIN_SUCCESS = "00"; // CMLogin에서 정상로그인 시 반환
    static final String LOGIN_DUPLICATE = "01"; // CMLogin에서 중복로그인 시 반환

    // CLIENT => SERVER
    static final String ROOM_LIST = "1";  // 방 리스트 요청 []
    // SERVER => CLIENT
    static final String ROOM_LIST_INFO = "10"; // 방 리스트 정보 반환 [방개수#(방번호#방제목#게임인원#게임상태)]

    // CLIENT => SERVER
    static final String ROOM_ENTER = "2"; // 방에 입장 요청 [방번호]
    // SERVER => CLIENT
    static final String ROOM_ENTER_REJECT = "21"; // 방 입장 거부 [메시지]
    static final String ROOM_ENTER_SUCCESS = "22"; // 방 입장 승인  [방번호#방제목]

    // CLIENT => SERVER
    static final String ROOM_EXIT = "3"; // 방을 나가기 요청 []
    // SERVER => CLIENT
    static final String ROOM_STATUS_INFO = "20"; // 방의 인원정보 전송 [인원수#(유저네임)]

    // CLIENT => SERVER
    static final String GAME_START = "4"; // 게임을 시작 요청 [방번호]
    static final String GAME_NEXT = "5";  // 게임 중 버튼을 눌렀을 때 [방번호#클릭한버튼]
    // SERVER => CLIENT
    static final String GAME_STATUS_INFO = "50"; // 게임 정보 반환 [나의인덱스#현재턴#현재칩#현재카드#나의칩#(유저별카드목록)]
    static final String GAME_END_INFO = "51"; // 게임 종료 반환 [게임결과]

    // CLIENT => SERVER
    static final String GAME_CHAT = "7"; // 게임 채팅 요청 [전체/방번호#내용]
    // SERVER => CLIENT
    static final String GAME_CHAT_INFO = "70"; // [전체/방#시스템/유저네임#내용]

    static final String ROOM_MAKE = "8"; // 방 생성 요청 [방제목]
    static final String ROOM_MAKE_SUCCESS = "80"; // 방 생성 성공 [방번호#방제목]
    static final String ROOM_MAKE_REJECT = "81"; // 방 생성 실패 [메시지]


}

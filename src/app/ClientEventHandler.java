package app;

import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMSessionEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;

/*
    ClientEventHandler : 서버에서 클라이언트로 들어오는 메시지를 처리하는 클래스
 */
public class ClientEventHandler implements CMAppEventHandler {

    private ClientApp m_client; // 클라이언트 객체
    private CMClientStub m_clientStub;

    /*
        생성자
        파라미터 : CMClientStub, Client
     */
    public ClientEventHandler(CMClientStub m_clientStub, ClientApp client){
        this.m_client = client;
        this.m_clientStub = m_clientStub;
    }

    /*
        processEvent() : 이벤트 처리 함수
        파라미터 : 이벤트(CMEvent)
    */
    @Override
    public void processEvent(CMEvent cmEvent) {
        switch(cmEvent.getType()){
            // 로그인 이벤트의 경우, 유저의 유효성 여부를 클라이언트 컨트롤러로 전송
            case CMInfo
                    .CM_SESSION_EVENT:
                CMSessionEvent se = (CMSessionEvent) cmEvent;
                switch (se.getID()){
                    case CMSessionEvent
                            .LOGIN_ACK:
                        if(se.isValidUser() != 1){
                            m_client.clientController(Information.LOGIN_DUPLICATE);
                        } else {
                            m_client.clientController(Information.LOGIN_SUCCESS);
                        }
                }
                break;
            // 해당 이벤트 내용을 클라이언트 컨트롤러로 전송
            case CMInfo.CM_DUMMY_EVENT:
                CMDummyEvent de = (CMDummyEvent) cmEvent;
                m_client.clientController(de.getDummyInfo());
                break;
        }

    }
}

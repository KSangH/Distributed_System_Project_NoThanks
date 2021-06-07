package app;

import app.ClientApp;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;

public class Client5 {
    public static void main(String[] args) {
        ClientApp client = new ClientApp();
        CMClientStub cmStub = client.getClientStub();
        cmStub.setAppEventHandler(client.getClientEventHandler());
    }
}

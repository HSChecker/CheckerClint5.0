package com.mccspace.hs.gui.pane.selectServer.serverInform;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableView;

import java.io.IOException;

/**
 * ServerInform类
 * Git to： http://hs.mccspace.com:3000/Qing_ning/CheckerClient5.0/
 *
 * @TIME 2020/5/24 16:24
 * @AUTHOR 韩硕~
 */

public class ServerInform {

    public SimpleStringProperty name = new SimpleStringProperty();
    public SimpleStringProperty address = new SimpleStringProperty();
    public SimpleStringProperty port = new SimpleStringProperty();
    public SimpleStringProperty state = new SimpleStringProperty();
    public SimpleStringProperty playerNum = new SimpleStringProperty();
    public SimpleStringProperty serverInform = new SimpleStringProperty();

    public ServerInform(String name, String address, String port, TableView Table) throws IOException {
        this.name.set(name);
        this.address.set(address);
        this.port.set(port);
        state.set("连接中......");
        playerNum.set("");
        serverInform.set("");
        Table.refresh();
        new Link(address,Integer.parseInt(port),state,playerNum,serverInform,Table);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getPort() {
        return port.get();
    }

    public SimpleStringProperty portProperty() {
        return port;
    }

    public void setPort(String port) {
        this.port.set(port);
    }

    public String getState() {
        return state.get();
    }

    public SimpleStringProperty stateProperty() {
        return state;
    }

    public void setState(String state) {
        this.state.set(state);
    }

    public String getPlayerNum() {
        return playerNum.get();
    }

    public SimpleStringProperty playerNumProperty() {
        return playerNum;
    }

    public void setPlayerNum(String playerNum) {
        this.playerNum.set(playerNum);
    }

    public String getServerInform() {
        return serverInform.get();
    }

    public SimpleStringProperty serverInformProperty() {
        return serverInform;
    }

    public void setServerInform(String serverInform) {
        this.serverInform.set(serverInform);
    }
}


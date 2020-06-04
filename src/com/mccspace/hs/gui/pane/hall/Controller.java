package com.mccspace.hs.gui.pane.hall;

import com.mccspace.hs.connect.listen.ConnectListen;
import com.mccspace.hs.manager.GlobalVariable;
import com.mccspace.hs.tools.Base64Crypto;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import json.JSONObject;

import java.io.IOException;

/**
 * Controller类
 * Git to： http://hs.mccspace.com:3000/Qing_ning/CheckerClient5.0/
 *
 * @TIME 2020/5/24 22:54
 * @AUTHOR 韩硕~
 */

public class Controller {

    @FXML
    private BorderPane pane;

    @FXML
    private ImageView myHead;

    @FXML
    private Label myName;

    @FXML
    private VBox friendList;

    @FXML
    void addFriend(ActionEvent event) {

    }

    @FXML
    void setting(ActionEvent event) {

    }

    @FXML
    void inform(ActionEvent event) {

    }

    @FXML
    void shop(ActionEvent event) {

    }

    @FXML
    void home(ActionEvent event) {

    }

    @FXML
    void start(ActionEvent event) {
        try {
            pane.setCenter(FXMLLoader.load(getClass().getResource("/com/mccspace/hs/gui/pane/hall/pane/start/start.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        var data = new JSONObject();
        data.put("player",GlobalVariable.connect.getName());
        GlobalVariable.connect.addListen(new ConnectListen("getPlayerInform") {
            @Override
            public void run(JSONObject data) {
                Platform.runLater(()->{
                    myName.setText(data.getString("name"));
                    Base64Crypto.decoderBase64File(data.getString("head"),"bin/head/"+data.getString("name")+".png");
                    myHead.setImage(new Image("file:bin/head/"+data.getString("name")+".png"));
                });
            }
        });
        GlobalVariable.connect.PrintPacket("getPlayerInform",data);
    }

}

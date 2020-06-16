package com.mccspace.hs.gui.pane.hall.pane.start;

import com.mccspace.hs.connect.listen.ConnectListen;
import com.mccspace.hs.manager.GlobalVariable;
import com.mccspace.hs.manager.OneTimeTtorage;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import json.JSONObject;

import java.awt.event.KeyAdapter;
import java.io.IOException;
import java.util.Optional;

/**
 * Controller类
 * Git to： http://hs.mccspace.com:3000/Qing_ning/CheckerClient5.0/
 *
 * @TIME 2020/5/25 0:48
 * @AUTHOR 韩硕~
 */

public class Controller {


    @FXML
    private TextField roomPassword;

    @FXML
    private VBox createBtn;

    @FXML
    private TextField joinID;

    @FXML
    private VBox joinBtn;

    @FXML
    private ChoiceBox<String> chooseType;

    @FXML
    private Button create;

    @FXML
    private Button join;

    @FXML
    private Button joinRoom;

    @FXML
    private Button createRoom;

    @FXML
    public void initialize() {
        joinBtn.setVisible(false);
        createBtn.setVisible(false);
        chooseType.setItems(FXCollections.observableArrayList("西洋跳棋(100)"));
        create.setDisable(true);
        join.setDisable(true);
        chooseType.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue ov, Number value, Number new_value) {
                create.setDisable(false);
            }
        });
        joinID.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                join.setDisable(false);
            }
        });
    }

    @FXML
    void createRoom(ActionEvent event) {
        joinBtn.setVisible(false);
        createBtn.setVisible(true);
        createRoom.setDefaultButton(true);
        joinRoom.setDefaultButton(false);
    }

    @FXML
    void joinRoom(ActionEvent event) {
        joinBtn.setVisible(true);
        createBtn.setVisible(false);
        createRoom.setDefaultButton(false);
        joinRoom.setDefaultButton(true);
    }

    @FXML
    void create(ActionEvent event) {
        var data = new JSONObject();
        data.put("type",chooseType.getValue());
        if(roomPassword.getText().equals("")){
            data.put("usePassword",false);
        } else {
            data.put("usePassword",true);
            data.put("password",roomPassword.getText());
        }
        try {
            GlobalVariable.stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/com/mccspace/hs/gui/pane/room/checker/checker.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        GlobalVariable.connect.PrintPacket("createRoom", data);

    }

    @FXML
    void join(ActionEvent event) {
        var data = new JSONObject();
        data.put("roomID",Integer.parseInt(joinID.getText()));

        var dialog = new Dialog<>();
        dialog.setTitle("加入房间");
        dialog.setContentText("正在尝试加入房间......");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        closeButton.setVisible(false);
        GlobalVariable.connect.addListen(new ConnectListen("joinRoom") {
            @Override
            public void run(JSONObject data) throws IOException {
                if(data.getBoolean("result")){
                    Platform.runLater(()->{
                        dialog.close();
                        try {
                            GlobalVariable.stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/com/mccspace/hs/gui/pane/room/checker/checker.fxml"))));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    Platform.runLater(()->{
                        dialog.close();
                        TextInputDialog dialog = new TextInputDialog("请输入房间密码......");
                        dialog.setTitle("房间密码确认");
                        dialog.setContentText("输入房间密码：");
                        Optional result = dialog.showAndWait();
                        GlobalVariable.connect.addListen(new ConnectListen("joinRoomPws") {
                            @Override
                            public void run(JSONObject data) throws IOException {
                                Platform.runLater(() -> {
                                    if(data.getBoolean("result")) {
                                        try {
                                            GlobalVariable.stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/com/mccspace/hs/gui/pane/room/checker/checker.fxml"))));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    else{
                                        Alert alert = new Alert(Alert.AlertType.WARNING);
                                        alert.setTitle("警告");
                                        alert.setContentText("密码错误！");
                                        alert.showAndWait();
                                    }
                                });
                            }
                        });
                        if (result.isPresent()) {
                            var turn = new JSONObject();
                            turn.put("pws",result.get());
                            GlobalVariable.connect.PrintPacket("joinRoomPws",turn);
                        }
                    });
                }
            }
        });

        GlobalVariable.connect.PrintPacket("joinRoom", data);
    }

}

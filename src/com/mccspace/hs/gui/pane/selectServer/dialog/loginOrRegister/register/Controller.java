package com.mccspace.hs.gui.pane.selectServer.dialog.loginOrRegister.register;

import com.mccspace.hs.connect.listen.ConnectListen;
import com.mccspace.hs.manager.GlobalVariable;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controller类
 * Git to： http://hs.mccspace.com:3000/Qing_ning/CheckerClient5.0/
 *
 * @TIME 2020/5/26 9:28
 * @AUTHOR 韩硕~
 */

public class Controller {

    private ToggleGroup group;

    @FXML
    private GridPane pane;

    @FXML
    private TextField CAPTCHA;

    @FXML
    private RadioButton mail;

    @FXML
    private RadioButton phone;

    @FXML
    private TextField num;

    @FXML
    private Button getCAPTCHA;

    @FXML
    private Button register;

    @FXML
    void getCAPTCHA(ActionEvent event) {
        var data = new JSONObject();
        data.put("type",(group.getSelectedToggle()==mail)?"email":"phone");
        data.put("num",num.getText());
        GlobalVariable.connect.PrintPacket("register",data);
        CAPTCHA.setDisable(false);
        getCAPTCHA.setDisable(true);
        num.setDisable(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=60;i>0;i--){
                    final int finalI = i;
                    Platform.runLater(() -> {
                        getCAPTCHA.setText(finalI +"秒后继续");
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Platform.runLater(() -> {
                    getCAPTCHA.setText("重新获取");
                    getCAPTCHA.setDisable(false);
                    num.setDisable(false);
                });
            }
        }).start();
    }

    @FXML
    void register(ActionEvent event) {
        var data = new JSONObject();
        data.put("CAPTCHA",CAPTCHA.getText());
        GlobalVariable.connect.addListen(new ConnectListen("matchCAPTCHA") {
            @Override
            public void run(JSONObject data) {
                if(data.getBoolean("result")){
                    Platform.runLater(() -> {
                        Stage stage = (Stage) pane.getScene().getWindow();
                        try {
                            pane = FXMLLoader.load(getClass().getResource("initUser/init.fxml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Scene scene = new Scene(pane);
                        stage.setScene(scene);
                        TextField email = null,phone = null;
                        for(var a:pane.getChildren()){
                            if(a.getId() == null)
                                continue;
                            if(a.getId().equals("email"))
                                email = (TextField) a;
                            if(a.getId().equals("phone"))
                                email = (TextField) a;
                        }
                        if(group.getSelectedToggle() == mail){
                            email.setText(num.getText());
                            email.setDisable(true);
                        } else {
                            phone.setText(num.getText());
                            phone.setDisable(true);
                        }
                    });
                } else {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("警告");
                        alert.setContentText("验证码输入有误！");
                        alert.showAndWait();
                        register.setDisable(false);
                    });
                }
            }
        });
        GlobalVariable.connect.PrintPacket("matchCAPTCHA",data);
        register.setDisable(true);
    }

    @FXML
    public void initialize() throws IOException {

        group = new ToggleGroup();
        mail.setToggleGroup(group);
        phone.setToggleGroup(group);

        num.setDisable(true);
        getCAPTCHA.setDisable(true);
        register.setDisable(true);
        CAPTCHA.setDisable(true);

        num.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(group.getSelectedToggle() == mail){
                    Pattern regex = Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
                    Matcher matcher = regex.matcher(t1);
                    if (matcher.matches())
                        getCAPTCHA.setDisable(false);
                }
            }
        });
        CAPTCHA.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(t1.equals(""))
                    register.setDisable(true);
                else
                    register.setDisable(false);
            }
        });

        //以下是全局设置
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                num.setDisable(false);
                if (group.getSelectedToggle() != null) {
                    RadioButton button = (RadioButton) group.getSelectedToggle();
                    if(button == mail){
                        num.setPromptText("邮箱");
                    } else {
                        num.setPromptText("手机号");
                    }
                }
            }
        });

        //以下功能待开发
        phone.setDisable(true);

    }
}

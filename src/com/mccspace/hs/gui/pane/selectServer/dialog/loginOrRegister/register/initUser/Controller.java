package com.mccspace.hs.gui.pane.selectServer.dialog.loginOrRegister.register.initUser;

import com.mccspace.hs.connect.listen.ConnectListen;
import com.mccspace.hs.manager.GlobalVariable;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Controller类
 * Git to： http://hs.mccspace.com:3000/Qing_ning/CheckerClient5.0/
 *
 * @TIME 2020/6/3 10:57
 * @AUTHOR 韩硕~
 */

public class Controller {

    private ToggleGroup group;

    @FXML
    private TextField user;

    @FXML
    private PasswordField pass1;

    @FXML
    private PasswordField pass2;

    @FXML
    private Button sure_;

    @FXML
    private DatePicker date;

    @FXML
    private RadioButton woman;

    @FXML
    private TextField phone;

    @FXML
    private TextField name;

    @FXML
    private RadioButton man;

    @FXML
    private GridPane pane;

    @FXML
    private TextField email;

    @FXML
    void sure(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("警告");

        if(!pass1.getText().equals(pass2.getText())){
            alert.setContentText("两次密码输入不一致！");
            Platform.runLater(()->{
                alert.showAndWait();
            });
            return;
        }

        if(!pass1.getText().matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$")){
            alert.setHeaderText("密码强度过低！");
            alert.setContentText("密码必须包含数字和字母，且长度在6~18位之间");
            Platform.runLater(()->{
                alert.showAndWait();
            });
            return;
        }

        var data = new JSONObject();
        data.put("user",user.getText());
        data.put("password",pass1.getText());
        data.put("sex",(group.getSelectedToggle()==man)?"man":"woman");
        data.put("email",email.getText());
        data.put("phone",phone.getText());
        data.put("name",name.getText());
        data.put("birth",date.getValue().toString());
        sure_.setDisable(true);

        GlobalVariable.connect.addListen(new ConnectListen("initUser") {
            @Override
            public void run(JSONObject data) throws IOException {
                if(data.getBoolean("result")){
                    Platform.runLater(()->{
                        ((Stage)pane.getScene().getWindow()).close();
                        try {
                            GlobalVariable.connect.setName(user.getText());
                            GlobalVariable.stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/com/mccspace/hs/gui/pane/hall/hall.fxml"))));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    alert.setContentText(data.getString("reason"));
                    Platform.runLater(()-> {
                        alert.showAndWait();
                        sure_.setDisable(false);
                    });
                }
            }
        });

        GlobalVariable.connect.PrintPacket("initUser",data);
    }

    @FXML
    public void initialize() {

        sure_.setDisable(false);

        group = new ToggleGroup();
        woman.setToggleGroup(group);
        man.setToggleGroup(group);

        date.getEditor().textProperty().addListener(listenNotNull);
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
                verification();
            }
        });
        email.textProperty().addListener(listenNotNull);
        phone.textProperty().addListener(listenNotNull);
        name.textProperty().addListener(listenNotNull);
        user.textProperty().addListener(listenNotNull);
        pass1.textProperty().addListener(listenNotNull);
        pass2.textProperty().addListener(listenNotNull);

    }

    private void verification() {
        if(!date.getEditor().getText().equals("") && group.getSelectedToggle()!=null && !name.getText().equals("") && (!phone.getText().equals("") || !email.getText().equals("")) && !user.getText().equals("") && !pass1.getText().equals("") && !pass2.getText().equals(""))
            sure_.setDisable(false);
        else
            sure_.setDisable(true);
    }

    private ChangeListener listenNotNull = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
            verification();
        }
    };

}

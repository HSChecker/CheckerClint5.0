package com.mccspace.hs.gui.pane.selectServer.dialog.loginOrRegister.login;

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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Controller��
 * Git to�� http://hs.mccspace.com:3000/Qing_ning/CheckerClient5.0/
 *
 * @TIME 2020/5/25 21:05
 * @AUTHOR ��˶~
 */

public class Controller {

    private VBox userLoginPane = new VBox();
    private VBox qqPane;
    private VBox wxPane;
    private VBox mailPane;

    @FXML
    public void initialize() throws IOException {

        ToggleGroup group = new ToggleGroup();
        qq.setToggleGroup(group);
        wx.setToggleGroup(group);
        mail.setToggleGroup(group);
        userPas.setToggleGroup(group);

        //������userLoginPane��ʼ��
        TextField user,password;
        Button login;
        userLoginPane.getChildren().add(user = new TextField());
        userLoginPane.getChildren().add(password = new PasswordField());
        userLoginPane.getChildren().add(login = new Button("��¼"));
        userLoginPane.setSpacing(10);
        userLoginPane.setPadding(new Insets(30,30,30,30));
        userLoginPane.setAlignment(Pos.CENTER);
        login.setDisable(true);
        login.setPrefWidth(150);
        user.setPromptText("�˺�");
        password.setPromptText("����");
        login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                var pa = new JSONObject();
                pa.put("type","userPass");
                pa.put("num",user.getText());
                pa.put("password",password.getText());
                GlobalVariable.connect.PrintPacket("login",pa);
                login.setDisable(true);
                GlobalVariable.connect.addListen(new ConnectListen("login") {
                    @Override
                    public void run(JSONObject data) throws IOException {
                        if(data.getBoolean("result")){
                            Platform.runLater(()->{
                                ((Stage)pane.getScene().getWindow()).close();
                                try {
                                    GlobalVariable.connect.setName(pa.getString("num"));
                                    GlobalVariable.stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/com/mccspace/hs/gui/pane/hall/hall.fxml"))));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        } else {
                            Platform.runLater(()-> {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("����");
                                alert.setContentText("�˺Ų����ڻ��������");
                                alert.showAndWait();
                                login.setDisable(false);
                            });
                        }
                    }
                });
            }
        });

        var les = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!user.getText().equals("") && !password.getText().equals(""))
                    login.setDisable(false);
                else
                    login.setDisable(true);
            }
        };
        user.textProperty().addListener(les);
        password.textProperty().addListener(les);


        //������ȫ������
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {
                    RadioButton button = (RadioButton) group.getSelectedToggle();
                    if(button == userPas){
                        pane.getChildren().remove(1);
                        pane.add(userLoginPane,0,1);
                    }
                }
            }
        });

        //���¹��ܴ�����
        qq.setDisable(true);
        wx.setDisable(true);
        mail.setDisable(true);

    }

    @FXML
    private GridPane pane;

    @FXML
    private RadioButton qq;

    @FXML
    private RadioButton wx;

    @FXML
    private RadioButton mail;

    @FXML
    private RadioButton userPas;
}

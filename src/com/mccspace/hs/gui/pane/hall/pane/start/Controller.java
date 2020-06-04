package com.mccspace.hs.gui.pane.hall.pane.start;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import json.JSONObject;

/**
 * ControllerÀà
 * Git to£º http://hs.mccspace.com:3000/Qing_ning/CheckerClient5.0/
 *
 * @TIME 2020/5/25 0:48
 * @AUTHOR º«Ë¶~
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
    public void initialize() {
        joinBtn.setVisible(false);
        createBtn.setVisible(false);
        chooseType.setItems(FXCollections.observableArrayList("Î÷ÑóÌøÆå(100)"));
        create.setDisable(true);
        chooseType.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue ov, Number value, Number new_value) {
                create.setDisable(false);
            }
        });
    }

    @FXML
    void createRoom(ActionEvent event) {
        joinBtn.setVisible(false);
        createBtn.setVisible(true);
    }

    @FXML
    void joinRoom(ActionEvent event) {
        joinBtn.setVisible(true);
        createBtn.setVisible(false);
    }

    @FXML
    void create(ActionEvent event) {
        var data = new JSONObject();
        data.put("type",chooseType.getValue());
        if(roomPassword.getText().trim().equals("")){
            data.put("usePassword",false);
        } else {
            data.put("usePassword",true);
            data.put("password",roomPassword.getText());
        }

    }

    @FXML
    void join(ActionEvent event) {

    }

}

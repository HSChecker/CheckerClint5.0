package com.mccspace.hs.gui.pane.selectServer.dialog.loginOrRegister;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.io.IOException;

/**
 * ControllerÀà
 * Git to£º http://hs.mccspace.com:3000/Qing_ning/CheckerClient5.0/
 *
 * @TIME 2020/5/25 20:54
 * @AUTHOR º«Ë¶~
 */

public class Controller {

    @FXML
    void initialize() throws IOException {
        login.setDefaultButton(true);
        pane.setVgap(30);
        pane.add(FXMLLoader.load(getClass().getResource("login/loginPane.fxml")),0,1);
    }

    @FXML
    private Button login;

    @FXML
    private Button register;

    @FXML
    private GridPane pane;

    @FXML
    void login(ActionEvent event) throws IOException {
        login.setDefaultButton(true);
        register.setDefaultButton(false);
        pane.getChildren().remove(1);
        pane.add(FXMLLoader.load(getClass().getResource("login/loginPane.fxml")),0,1);
    }

    @FXML
    void register(ActionEvent event) throws IOException {
        register.setDefaultButton(true);
        login.setDefaultButton(false);
        pane.getChildren().remove(1);
        pane.add(FXMLLoader.load(getClass().getResource("register/register.fxml")),0,1);
    }

}

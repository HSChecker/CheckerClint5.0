package com.mccspace.hs.gui.pane.welcome;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Contraller类
 * Git to： http://hs.mccspace.com:3000/Qing_ning/CheckerClient5.0/
 *
 * @TIME 2020/5/24 15:38
 * @AUTHOR 韩硕~
 */

public class Controller {

    @FXML
    private GridPane pane;

    @FXML
    void enter(ActionEvent event) throws IOException {
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/com/mccspace/hs/gui/pane/selectServer/selectServer.fxml"))));
    }

    @FXML
    void setting(ActionEvent event) {

    }

    @FXML
    void readme(ActionEvent event) {

    }


}

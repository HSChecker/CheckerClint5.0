package com.mccspace.hs.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Start {

    public Start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("pane/welcome/welcome.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}

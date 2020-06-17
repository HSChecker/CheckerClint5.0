package com.mccspace.hs;

import com.mccspace.hs.gui.Start;
import com.mccspace.hs.manager.GlobalVariable;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * RUN类
 * Git to： http://hs.mccspace.com:3000/Qing_ning/CheckerClient5.0/
 *
 * @TIME 2020/5/24 14:33
 * @AUTHOR 韩硕~
 */

public class RUN extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        GlobalVariable.stage = primaryStage;
        new Start(primaryStage);
    }

    public static void main(String[] args) {

        launch(args);

    }
}

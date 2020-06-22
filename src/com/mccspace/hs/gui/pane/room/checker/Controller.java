package com.mccspace.hs.gui.pane.room.checker;

import com.mccspace.hs.connect.listen.ConnectListen;
import com.mccspace.hs.game.CheckerBoard;
import com.mccspace.hs.game.ChessType;
import com.mccspace.hs.manager.GlobalVariable;
import com.mccspace.hs.manager.OneTimeTtorage;
import com.mccspace.hs.tools.Base64Crypto;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import json.JSONArray;
import json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Controller类
 * Git to： http://hs.mccspace.com:3000/Qing_ning/CheckerClient5.0/
 *
 * @TIME 2020/6/4 20:56
 * @AUTHOR 韩硕~
 */

public class Controller {

    @FXML
    private Canvas canvas;

    @FXML
    private TabPane blackPane;

    @FXML
    private Button exit;

    @FXML
    private Button imlose;

    @FXML
    private Button ready;

    @FXML
    private Label allStep;

    @FXML
    private TabPane whitePane;

    @FXML
    private Label roomID;

    @FXML
    private Button imRegret;

    @FXML
    private Label allTime;

    @FXML
    private Label nowTeam;

    @FXML
    private Button htplay;

    @FXML
    private GridPane checkerBoard;

    private CheckerBoard checker;

    private List<List<Integer>> probably = new ArrayList<>();

    private List<Button> buttonList = new ArrayList<>();

    private List<Integer> upChess = new ArrayList<>();

    @FXML
    public void initialize() {
        canvas.setMouseTransparent(true);
        GlobalVariable.connect.removeListen("updateRoomInform");
        GlobalVariable.connect.addListen(new ConnectListen("updateRoomInform", true) {
            @Override
            public void run(JSONObject data) {
                Platform.runLater(() -> {
                    roomID.setText(String.valueOf(data.getInt("roomID")));
                    JSONObject b, w;
                    boolean ready;
                    try {
                        b = data.getJSONObject("black");
                        ready = data.getBoolean("blackReady");
                        turnInform(b, blackPane, ready);
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }
                    try {
                        w = data.getJSONObject("white");
                        ready = data.getBoolean("whiteReady");
                        turnInform(w, whitePane, ready);
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }
                });
            }

            private void turnInform(JSONObject w, TabPane whitePane, boolean ready) throws IOException {
                boolean always = false;
                for (var a : whitePane.getTabs()) {
                    if (a.getText().equals("选手信息")) {
                        updateInform(w, a, ready);
                        always = true;
                    }
                }
                if (!always) {
                    Tab a = new Tab();
                    a.setText("选手信息");
                    a.setContent(FXMLLoader.load(getClass().getResource("/com/mccspace/hs/gui/pane/room/checker/pane/playerInform/playerInform.fxml")));
                    updateInform(w, a, ready);
                    whitePane.getTabs().add(a);
                }
            }

            private void updateInform(JSONObject b, Tab a, boolean ready) {
                if (ready)
                    ((ImageView) a.getContent().lookup("#ready")).setImage(new Image("file:bin/image/ready.png"));
                else
                    try {
                        ((ImageView) a.getContent().lookup("#ready")).setImage(null);
                    } catch (Exception e) {

                    }
                Base64Crypto.decoderBase64File(b.getString("head"), "bin/head/" + b.getString("user") + ".png");
                ((ImageView) a.getContent().lookup("#head")).setImage(new Image("file:bin/head/" + b.getString("user") + ".png"));
                ((Label) a.getContent().lookup("#sex")).setText(b.getString("sex"));
                ((Label) a.getContent().lookup("#name")).setText(b.getString("name"));
                ((Label) a.getContent().lookup("#age")).setText(String.valueOf(b.getInt("age")));
                ((Label) a.getContent().lookup("#gameNum")).setText(b.getInt("gamenum") + "局 / " + new DecimalFormat("#00.00").format(b.getInt("winnum") / (b.getInt("gamenum") == 0 ? 1 : b.getInt("gamenum")) * 100) + "%");
            }
        });
        GlobalVariable.connect.PrintPacket("updateRoomInform", new JSONObject());

        GlobalVariable.connect.removeListen("updataChecker");
        GlobalVariable.connect.addListen(new ConnectListen("updataChecker", true) {
            @Override
            public void run(JSONObject data) throws IOException {
                boolean wait = data.getBoolean("wait");
                for (var btn : buttonList) {
                    btn.setDisable(wait);
                }
                for (var a : data.getJSONArray("upChess").toList()) {
                    upChess.add((Integer) a);
                }
                var board = data.getJSONObject("board");
                checker = new CheckerBoard(board.getLong("notEmote"), board.getLong("isBlack"), board.getLong("isKing"));
                Platform.runLater(() -> {
                    for (int i = 0; i < 50; i++) {
                        if (checker.getChess(i) == ChessType.White)
                            buttonList.get(i).setGraphic(new ImageView(new Image("file:bin/image/styel(" + 1 + ")/白棋.png")));
                        else if (checker.getChess(i) == ChessType.Black)
                            buttonList.get(i).setGraphic(new ImageView(new Image("file:bin/image/styel(" + 1 + ")/黑棋.png")));
                        else if (checker.getChess(i) == ChessType.WhiteKing)
                            buttonList.get(i).setGraphic(new ImageView(new Image("file:bin/image/styel(" + 1 + ")/白王.png")));
                        else if (checker.getChess(i) == ChessType.BlackKing)
                            buttonList.get(i).setGraphic(new ImageView(new Image("file:bin/image/styel(" + 1 + ")/黑王.png")));
                        else if (checker.getChess(i) == ChessType.Emote)
                            buttonList.get(i).setGraphic(new ImageView());
                    }
                    var gc = canvas.getGraphicsContext2D();
                    for (var a : data.getJSONArray("nowPro").toList()) {
                        probably.add((List<Integer>) a);
                        if (!wait) {
                            Random r = new Random();
                            gc.setStroke(Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
                            gc.setLineWidth(5);
                            List<Double> pointX = new ArrayList<Double>();
                            List<Double> pointY = new ArrayList<Double>();
                            for (int i : (List<Integer>) a) {
                                pointX.add(buttonList.get(i).getWidth() * (getX(i) + 0.5));
                                pointY.add(buttonList.get(i).getHeight() * (getY(i) + 0.5));
                            }
                            gc.strokePolyline(ListToDouble(pointX), ListToDouble(pointY), pointX.size());
                        } else {
                            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                        }
                    }
                    if(data.getJSONArray("upChess") != null) {
                        List<Object> upChess = data.getJSONArray("upChess").toList();
                        for (int i = 0; i < 50; i++) {
                            if (upChess.contains((Object) i))
                                buttonList.get(i).setStyle("-fx-background-color:wheat");
                            else
                                buttonList.get(i).setStyle("-fx-background-color:#E0E0E0");
                        }
                    }
                });
            }
        });


        GlobalVariable.connect.addListen(new ConnectListen("win") {
            @Override
            public void run(JSONObject data) {
                Platform.runLater(()->{
                    if(data.getBoolean("win")){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("游戏结果");
                        alert.setContentText("YOU HAVE WIN！！！");
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("游戏结果");
                        alert.setContentText("YOU HAVE LOSE");
                        alert.showAndWait();
                    }
                });
            }
        });

        buttonList = new ArrayList<>();

        final int[] choose = {-1};

        var listen = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                var btn = (Button) actionEvent.getSource();
                int btnIndexOf = buttonList.indexOf(btn);
                List<Integer> start = new ArrayList<>();
                List<Integer> end = new ArrayList<>();
                for (var i : probably) {
                    start.add(i.get(0));
                    if (i.get(0).equals(choose[0]))
                        end.add(i.get(i.size() - 1));
                }
                if (choose[0] == -1 && start.contains(btnIndexOf)) {
                    choose[0] = btnIndexOf;
                    btn.setStyle("-fx-background-color:#434388");
                } else if (choose[0] != -1 && end.contains(btnIndexOf)) {
                    for (var i : probably) {
                        if (i.get(0).equals(choose[0]) && i.get(i.size() - 1).equals(btnIndexOf)) {
                            var data = new JSONObject();
                            data.put("play", i);
                            GlobalVariable.connect.PrintPacket("play", data);

                            buttonList.get(choose[0]).setStyle("-fx-background-color:#E0E0E0");
                            choose[0] = -1;
                            break;//后期添加多路径选择
                        }
                    }
                } else if (choose[0] != -1 && start.contains(btnIndexOf)) {
                    btn.setStyle("-fx-background-color:#434388");
                    buttonList.get(choose[0]).setStyle("-fx-background-color:#E0E0E0");
                    choose[0] = btnIndexOf;
                }
            }
        };

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if ((i + j) % 2 == 1) {
                    var btn = new Button();
                    btn.setOnAction(listen);
                    btn.setMaxSize(60, 60);
                    checkerBoard.add(btn, j, i);
                    buttonList.add(btn);
                }
            }
        }
    }

    @FXML
    void imRegret(ActionEvent event) {

    }

    @FXML
    void imlose(ActionEvent event) {

    }

    @FXML
    void ready(ActionEvent event) {
        GlobalVariable.connect.PrintPacket("ready", new JSONObject());
        ready.setDisable(true);
    }

    @FXML
    void htplay(ActionEvent event) {

    }

    @FXML
    void exit(ActionEvent event) {


    }

    public static int getX(int now) {
        int ny = (int) (now / 5);
        return (now - ny * 5) * 2 + ((ny % 2) == 0 ? 1 : 0);
    }

    public static int getY(int now) {
        return (int) (now / 5);
    }

    public static double[] ListToDouble(List<Double> list) {
        Double[] doubles = new Double[list.size()];
        list.toArray(doubles);
        if (doubles == null) {
            return null;
        }
        double[] result = new double[doubles.length];
        for (int i = 0; i < doubles.length; i++) {
            result[i] = doubles[i].doubleValue();
        }
        return result;
    }

}
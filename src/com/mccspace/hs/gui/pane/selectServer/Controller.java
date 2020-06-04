package com.mccspace.hs.gui.pane.selectServer;

import com.mccspace.hs.connect.Connect;
import com.mccspace.hs.gui.pane.selectServer.serverInform.ServerInform;
import com.mccspace.hs.manager.GlobalVariable;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.*;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Contraller类
 * Git to： http://hs.mccspace.com:3000/Qing_ning/CheckerClient5.0/
 *
 * @TIME 2020/5/24 15:38
 * @AUTHOR 韩硕~
 */

public class Controller {

    @FXML
    private BorderPane pane;

    private ObservableList dataList = FXCollections.observableArrayList();

    @FXML
    public void initialize() throws IOException {

        BufferedReader br = new BufferedReader(new FileReader("bin\\serverInform"));

        String s;
        while ((s = br.readLine()) != null) {
            String[] a = s.split(" ");
            dataList.add(new ServerInform(a[0], a[1], a[2], list));
        }
        br.close();

        playerNum.setCellValueFactory(new PropertyValueFactory<>("playerNum"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        state.setCellValueFactory(new PropertyValueFactory<>("state"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        serverInform.setCellValueFactory(new PropertyValueFactory<>("serverInform"));
        port.setCellValueFactory(new PropertyValueFactory<>("port"));

        list.setItems(dataList);

        editBtn.setDisable(true);
        joinBtn.setDisable(true);
        deleteBtn.setDisable(true);

        list.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if(newValue.toString()!="-1") {
                    editBtn.setDisable(false);
                    joinBtn.setDisable(false);
                    deleteBtn.setDisable(false);
                } else {
                    editBtn.setDisable(true);
                    joinBtn.setDisable(true);
                    deleteBtn.setDisable(true);
                }
            }
        });
    }

    @FXML
    private Button editBtn;

    @FXML
    private Button joinBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private TableView<?> list;

    @FXML
    private TableColumn<?, ?> address;

    @FXML
    private TableColumn<?, ?> serverInform;

    @FXML
    private TableColumn<?, ?> port;

    @FXML
    private TableColumn<?, ?> name;

    @FXML
    private TableColumn<?, ?> state;

    @FXML
    private TableColumn<?, ?> playerNum;

    @FXML
    void add(ActionEvent event) throws IOException {

        var dialog = new Dialog<>();
        dialog.setTitle("添加服务器");
        dialog.getDialogPane().setContent(FXMLLoader.load(getClass().getResource("dialog/addServer/addServer.fxml")));

        var add = new ButtonType("添加服务器", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(add);

        dialog.setResultConverter(new Callback<ButtonType, Object>() {
            @Override
            public Object call(ButtonType buttonType) {
                if (buttonType == add) {
                    var address = (TextField)((GridPane)dialog.getDialogPane().getContent()).getChildren().get(4);
                    var port = (TextField)((GridPane)dialog.getDialogPane().getContent()).getChildren().get(5);
                    var name = (TextField)((GridPane)dialog.getDialogPane().getContent()).getChildren().get(3);
                    if(address.getText().matches("^((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}$"))
                        if(port.getText().matches("[0-9]*"))
                            if(!name.getText().equals("")){
                                try {
                                    dataList.add(new ServerInform(name.getText(),address.getText(),port.getText(),list));
                                    PrintWriter bw = new PrintWriter(new FileWriter("bin\\serverInform",true));
                                    bw.println(name.getText()+" "+address.getText()+" "+port.getText());
                                    bw.close();
                                    return null;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("输入有误");
                    alert.setHeaderText("请检查输入是否符合规范");

                    alert.showAndWait();
                }
                return null;
            }
        });

        dialog.showAndWait();

    }

    @FXML
    void edit(ActionEvent event) throws IOException {

        var dialog = new Dialog<>();
        dialog.setTitle("编辑信息");
        dialog.getDialogPane().setContent(FXMLLoader.load(getClass().getResource("dialog/addServer/addServer.fxml")));

        var add = new ButtonType("完成修改", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(add);

        ServerInform si = (ServerInform) list.getSelectionModel().getSelectedItem();

        ((TextField)((GridPane)dialog.getDialogPane().getContent()).getChildren().get(4)).setText(si.getAddress());
        ((TextField)((GridPane)dialog.getDialogPane().getContent()).getChildren().get(5)).setText(si.getPort());
        ((TextField)((GridPane)dialog.getDialogPane().getContent()).getChildren().get(3)).setText(si.getName());

        dialog.setResultConverter(new Callback<ButtonType, Object>() {
            @Override
            public Object call(ButtonType buttonType) {
                if (buttonType == add) {
                    var address = (TextField)((GridPane)dialog.getDialogPane().getContent()).getChildren().get(4);
                    var port = (TextField)((GridPane)dialog.getDialogPane().getContent()).getChildren().get(5);
                    var name = (TextField)((GridPane)dialog.getDialogPane().getContent()).getChildren().get(3);
                    if(address.getText().matches("^((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}$"))
                        if(port.getText().matches("[0-9]*"))
                            if(!name.getText().equals("")){
                                try {
                                    dataList.add(new ServerInform(name.getText(),address.getText(),port.getText(),list));
                                    dataList.remove(si);
                                    PrintWriter bw = new PrintWriter(new FileWriter("bin\\serverInform"));
                                    for(var a:dataList)
                                        bw.println(((ServerInform)a).getName() + " " + ((ServerInform)a).getAddress() + " " + ((ServerInform)a).getPort());
                                    bw.close();
                                    return null;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("输入有误");
                    alert.setHeaderText("请检查输入是否符合规范");

                    alert.showAndWait();
                }
                return null;
            }
        });

        dialog.showAndWait();
    }

    @FXML
    void delete(ActionEvent event) {
        ServerInform si = (ServerInform) list.getSelectionModel().getSelectedItem();
        try {
            dataList.remove(si);
            PrintWriter bw = new PrintWriter(new FileWriter("bin\\serverInform"));
            for(var a:dataList)
                bw.println(((ServerInform)a).getName() + " " + ((ServerInform)a).getAddress() + " " + ((ServerInform)a).getPort());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void join(ActionEvent event) {

        ServerInform si = (ServerInform) list.getSelectionModel().getSelectedItem();

        var socket = new Socket();

        var dialog = new Dialog<>();
        dialog.setTitle("连接服务器");
        dialog.setContentText("正在尝试连接服务器......");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        closeButton.setVisible(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket.connect(new InetSocketAddress(si.getAddress(), Integer.parseInt(si.getPort())));
                    GlobalVariable.connect = new Connect(socket);
                    Platform.runLater(() -> {
                        dialog.close();
                    });
                    Platform.runLater(() -> {
                        var dialog = new Dialog<>();
                        dialog.setTitle("登录账号");
                        try {
                            dialog.getDialogPane().setContent(FXMLLoader.load(getClass().getResource("/com/mccspace/hs/gui/pane/selectServer/dialog/loginOrRegister/loginOrRegister.fxml")));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
                        Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                        closeButton.setVisible(false);
                        dialog.showAndWait();
                    });
                } catch (ConnectException e) {
                    Platform.runLater(() -> {
                        dialog.setContentText("连接失败！请检查ip域");
                        closeButton.setVisible(true);
                    });
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        dialog.showAndWait();
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/com/mccspace/hs/gui/pane/welcome/welcome.fxml"))));
    }
    


}

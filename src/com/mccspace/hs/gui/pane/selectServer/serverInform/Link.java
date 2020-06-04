package com.mccspace.hs.gui.pane.selectServer.serverInform;

import com.mccspace.hs.connect.listen.ConnectIntyerface;
import com.mccspace.hs.connect.listen.ConnectListen;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableView;
import json.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Link??
 * Git to?? http://hs.mccspace.com:3000/Qing_ning/CheckerClient5.0/
 *
 * @TIME 2020/5/24 16:28
 * @AUTHOR ???~
 */

public class Link implements Runnable{

    private String address;
    private int port;
    private SimpleStringProperty state;
    private SimpleStringProperty playerNum;
    private SimpleStringProperty inform;
    private TableView Table;

    private PrintWriter bw;

    private Thread thread;

    private List<ConnectIntyerface> Listen = new ArrayList<ConnectIntyerface>();

    public Link(String address, int port, SimpleStringProperty state, SimpleStringProperty playerNum, SimpleStringProperty inform, TableView Table) {
        this.address = address;
        this.port = port;
        this.state = state;
        this.playerNum = playerNum;
        this.inform = inform;
        this.Table = Table;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        Socket socket = new Socket();
        try {

            socket.connect(new InetSocketAddress(address, port));
            state.set("Õý³£");
            Table.refresh();

            var br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bw = new PrintWriter(socket.getOutputStream());

            bw.println("getInform");
            bw.flush();
            addListen(new ConnectListen("getInform") {
                @Override
                public void run(JSONObject data) {
                    playerNum.set(data.getString("player"));
                    inform.set(data.getString("inform"));
                    Table.refresh();
                    Link.this.thread.stop();
                }
            });

            while(true){
                String m = br.readLine();
                if(m!=null) {
                    String[] packet = m.split(" ");
                    for (ConnectIntyerface li : Listen) {
                        if (li.whetherRun(packet[0])) {
                            li.run(new JSONObject(packet[1]));
                            if (!li.isPermanent())
                                Listen.remove(li);
                        }
                    }
                } else {
                    return;
                }
            }
        } catch (IOException e) {
            state.set("³¬Ê±");
            inform.set("");
            playerNum.set("");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Table.refresh();
    }

    public void addListen(ConnectIntyerface ct){
        Listen.add(ct);
    }
}

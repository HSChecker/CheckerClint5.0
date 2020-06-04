package com.mccspace.hs.connect;

import com.mccspace.hs.connect.listen.ConnectIntyerface;
import json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Connect类
 * Git to： http://hs.mccspace.com:3000/Qing_ning/CheckerClient5.0/
 *
 * @TIME 2020/5/25 21:27
 * @AUTHOR 韩硕~
 */

public class Connect implements Runnable{

    private Socket socket;

    private PrintWriter bw;

    private String name;

    private List<ConnectIntyerface> Listen = new CopyOnWriteArrayList<>();

    public Connect(Socket socket) {
        this.socket = socket;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try{

            var br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bw = new PrintWriter(socket.getOutputStream());

            while(true){
                String m = br.readLine();
                if(m!=null) {
                    var date = new Date();
                    var time = "["+(date.getYear()+1900)+"/"+date.getMonth()+"/"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()+"]";
                    System.out.println(time+"接受包："+m);
                    String[] packet = m.split(" ");
                    String json = "";
                    for(int i=1;i<packet.length;i++)
                        json += packet[i];
                    for (ConnectIntyerface li : Listen) {
                        if (li.whetherRun(packet[0])) {
                            if(json.equals(""))
                                li.run(new JSONObject());
                            else
                                li.run(new JSONObject(json));
                            if (!li.isPermanent())
                                Listen.remove(li);
                        }
                    }
                } else {
                    return;
                }
            }

        } catch (SocketException e){
            var date = new Date();
            var time = "["+(date.getYear()+1900)+"/"+date.getMonth()+"/"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()+"]";
            System.out.println(time+"连接异常！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void PrintPacket(String name,JSONObject packet) {
        var date = new Date();
        var time = "["+(date.getYear()+1900)+"/"+date.getMonth()+"/"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()+"]";
        System.out.println(time+"发送包："+ name + " " + packet.toString());
        bw.println(name + " " + packet.toString());
        bw.flush();
    }

    public void addListen(ConnectIntyerface ct){
        Listen.add(ct);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
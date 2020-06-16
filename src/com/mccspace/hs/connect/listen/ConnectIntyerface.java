package com.mccspace.hs.connect.listen;


import json.JSONObject;

import java.io.IOException;

/**
 * ConnectIntyerface???
 *
 * @author HanShuo
 * @Date 2020/5/1 17:16
 */
public interface ConnectIntyerface{

    boolean whetherRun(String connectName);
    void run(JSONObject data) throws IOException;
    boolean isPermanent();

    String getType();

}

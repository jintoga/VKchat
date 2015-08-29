package com.vk.sdk.api.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by DAT on 8/27/2015.
 */
public class MyVKApiSendMsgResponse {

    private int msg_id;

    public MyVKApiSendMsgResponse(JSONObject from) {
        this.parse(from);
    }

    public MyVKApiSendMsgResponse parse(JSONObject source) {
        try {
            this.msg_id = source.getInt("response");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public int getMsg_id() {
        return msg_id;
    }
}



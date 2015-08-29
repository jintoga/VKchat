package com.example.dat.vkchat.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.dat.vkchat.Adapters.CustomChatAdapter;
import com.example.dat.vkchat.Model.Contact;
import com.example.dat.vkchat.Model.Message;
import com.example.dat.vkchat.R;
import com.example.dat.vkchat.Services.NotifyIncomingMsg;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by DAT on 8/29/2015.
 */
public class FragmentChat extends Fragment {


    ArrayList<Message> listMsg = new ArrayList<>();
    CustomChatAdapter customChatAdapter;
    ImageButton buttonSend;
    EditText editTextMsg;
    ListView listViewChat;
    Contact receiver = null;

    static boolean refreshRunner = false;
    static int msg_counter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        receiver = (Contact) bundle.getSerializable("contact");
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        getIDs(view);
        setEvents();
        msg_counter = getCurrentMsgNumber();
        refreshRunner = true;
        refreshToDetectIncomingMsg();
        return view;
    }


    private void getIDs(View view) {
        buttonSend = (ImageButton) view.findViewById(R.id.imageButtonSend);
        editTextMsg = (EditText) view.findViewById(R.id.editTextMsg);
        listViewChat = (ListView) view.findViewById(R.id.listViewChat);

    }

    String user_id = "146312781";

    private void setEvents() {
        customChatAdapter = new CustomChatAdapter(getActivity(), listMsg, receiver);
        listViewChat.setAdapter(customChatAdapter);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CharBuffer cbuf = null;
                try {
                    Charset charset = Charset.forName("UTF-8");
                    CharsetDecoder decoder = charset.newDecoder();
                    CharsetEncoder encoder = charset.newEncoder();
                    ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(editTextMsg.getText() + ""));
                    cbuf = decoder.decode(bbuf);
                } catch (CharacterCodingException e) {
                    e.printStackTrace();
                }
                String msg = cbuf.toString();

                requestSendMsg(msg, String.valueOf(receiver.getUser_id()));

            }
        });
    }

    private int getCurrentMsgNumber() {
        final int[] count = {0};
        VKRequest request = VKApi.messages().myGetMsgHistoryMethod(VKParameters.from(VKApiConst.COUNT, "10", VKApiConst.USER_ID, receiver.getUser_id()));
        request.secure = false;
        request.useSystemLanguage = false;
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.d("Response", response.toString());
                try {
                    JSONObject jsonObject = response.json.getJSONObject("response");
                    int msg_count = jsonObject.getInt("count");
                    Log.d("msg_count", msg_count + "");
                    count[0] = msg_count;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                super.attemptFailed(request, attemptNumber, totalAttempts);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
            }

            @Override
            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                super.onProgress(progressType, bytesLoaded, bytesTotal);
            }
        });
        return count[0];
    }

    private void refreshToDetectIncomingMsg() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    while (refreshRunner) {
                        Thread.sleep(500);      //VK Api allows no more than 3 requests per second so lets make 2 requests per second for safety
                        Log.d("DoINBackGround", "On doInBackground...");
                        responseUpdateChat();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /*Log.d("DoINBackGround", "On doInBackground...");
                responseUpdateChat();*/
                return null;
            }

            protected void onPreExecute() {
                Log.d("PreExceute", "On pre Exceute......");
            }
        }.execute();


    }

    private void requestSendMsg(final String msg, final String user_id) {


        VKRequest request = VKApi.messages().mySendingMsgMethod(VKParameters.from(VKApiConst.MESSAGE, msg, VKApiConst.USER_ID, user_id));
        request.secure = false;
        request.useSystemLanguage = false;
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.d("Response", response.toString());
                /*Message sentMsg = new Message();
                sentMsg.setBody(msg);
                sentMsg.setUser_id(Integer.valueOf(user_id));
                listMsg.add(sentMsg);
                customChatAdapter.notifyDataSetChanged();*/
                refreshToDetectIncomingMsg();
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                super.attemptFailed(request, attemptNumber, totalAttempts);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
            }

            @Override
            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                super.onProgress(progressType, bytesLoaded, bytesTotal);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        refreshRunner = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        refreshRunner = false;
    }

    private void responseUpdateChat() {
        VKRequest request = VKApi.messages().myGetMsgHistoryMethod(VKParameters.from(VKApiConst.COUNT, "10", VKApiConst.USER_ID, receiver.getUser_id()));
        request.secure = false;
        request.useSystemLanguage = false;
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.d("Response", response.toString());
                try {
                    JSONObject jsonObject = response.json.getJSONObject("response");
                    //Log.d("jsonObject", jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                    int msg_count = jsonObject.getInt("count");
                    Log.d("msg_count", msg_count + "");
                    listMsg.clear();
                    //Log.d("jsonArray", jsonArray.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject joMsg = jsonArray.getJSONObject(i);
                        //Log.d("joContact", joMsg.toString());
                        String body = joMsg.getString("body");
                        int user_id = joMsg.getInt("user_id");
                        int from_id = joMsg.getInt("from_id");
                        Message message = new Message();
                        message.setUser_id(user_id);
                        message.setFrom_id(from_id);
                        message.setBody(body);
                        listMsg.add(message);
                    }
                    Collections.reverse(listMsg);
                    if (msg_count != msg_counter) {
                        customChatAdapter.notifyDataSetChanged();
                        msg_counter = msg_count;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                super.attemptFailed(request, attemptNumber, totalAttempts);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
            }

            @Override
            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                super.onProgress(progressType, bytesLoaded, bytesTotal);
            }
        });
    }
}

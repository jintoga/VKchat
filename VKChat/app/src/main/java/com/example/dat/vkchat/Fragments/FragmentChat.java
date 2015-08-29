package com.example.dat.vkchat.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.dat.vkchat.R;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * Created by DAT on 8/29/2015.
 */
public class FragmentChat extends Fragment {
    Button buttonSend;
    EditText editTextMsg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        getIDs(view);
        setEvents();
        return view;
    }


    private void getIDs(View view) {
        buttonSend = (Button) view.findViewById(R.id.button_send);
        editTextMsg = (EditText) view.findViewById(R.id.editText_msg);

    }

    private void setEvents() {
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id = "146312781";
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

                requestSendMsg(msg, user_id);
            }
        });
    }

    private void requestSendMsg(String msg, String user_id) {


        VKRequest request = VKApi.messages().mySendingMsgMethod(VKParameters.from(VKApiConst.MESSAGE, msg, VKApiConst.USER_ID, user_id));
        request.secure = false;
        request.useSystemLanguage = false;
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.d("Response", response.toString());
                try {
                    JSONObject jsonObject = response.json.getJSONObject("response");
                    Log.d("MSG sent", jsonObject.toString());


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

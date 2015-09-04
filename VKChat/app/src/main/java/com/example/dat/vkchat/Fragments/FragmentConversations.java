package com.example.dat.vkchat.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dat.vkchat.Model.Contact;
import com.example.dat.vkchat.R;

/**
 * Created by DAT on 9/4/2015.
 */
public class FragmentConversations extends Fragment {
    FragmentChat fragmentChat;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversations, container, false);
        getIDs(view);
        return view;
    }

    public void addFriendToChat(Contact receiver) {
        fragmentChat.addFragmentToViewPager(receiver);
    }

    private void getIDs(View view) {
        fragmentChat = (FragmentChat) getChildFragmentManager().findFragmentById(R.id.fragmentChatInsideConversations);
    }
}

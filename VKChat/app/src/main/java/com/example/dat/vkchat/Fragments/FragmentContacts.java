package com.example.dat.vkchat.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dat.vkchat.Adapters.CustomContactsAdapter;
import com.example.dat.vkchat.LoginActivity;
import com.example.dat.vkchat.Model.Contact;
import com.example.dat.vkchat.R;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class FragmentContacts extends Fragment {

    private ArrayList<Contact> contacts;
    private RecyclerView recyclerViewContacts;
    private CustomContactsAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        getIDs(view);
        setEvents();
        return view;
    }


    private void getIDs(View view) {
        recyclerViewContacts = (RecyclerView) view.findViewById(R.id.recyclerView_contacts);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewContacts.setLayoutManager(layoutManager);

    }

    private void setEvents() {
        contacts = ((LoginActivity) getActivity()).getContacts();
        mAdapter = new CustomContactsAdapter(getActivity(), contacts);
        recyclerViewContacts.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    public void clearContactsList() {
        contacts.clear();
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }


}

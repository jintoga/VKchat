package com.example.dat.vkchat.Adapters;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.dat.vkchat.Fragments.FragmentChat;
import com.example.dat.vkchat.LoginActivity;
import com.example.dat.vkchat.Model.Contact;
import com.example.dat.vkchat.R;
import com.example.dat.vkchat.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by DAT on 8/26/2015.
 */
public class CustomContactsAdapter extends RecyclerView.Adapter<CustomContactsAdapter.ViewHolder> {

    private ArrayList<Contact> contacts;
    private LayoutInflater inflater;
    private Context context;

    public CustomContactsAdapter(Context context, ArrayList<Contact> listModels) {
        inflater = LayoutInflater.from(context);
        this.contacts = listModels;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_item_contact, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Contact contact = contacts.get(position);
        if (contact.getAvatar_url() != "") {
            Picasso.with(context).load(contact.getAvatar_url())./*centerInside().*/resize(120, 120)/*.transform(new RoundedTransformation(10, 10))*/.into(holder.imageViewContactAvatar);
        } else {
            Picasso.with(context).load(R.drawable.vk_avatar)./*centerInside().*/resize(120, 120)/*.transform(new RoundedTransformation(10, 10))*/.into(holder.imageViewContactAvatar);
        }
        holder.textViewContactName.setText(contact.getName());
        if (contact.getIsOnline() == 1) {
            holder.textViewStatus.setText("online");
            holder.textViewStatus.setTextColor(context.getResources().getColor(R.color.accent));
            holder.floatingActionButtonChat.setImageResource(R.drawable.ic_chat_black_18dp);
        } else {
            holder.textViewStatus.setText("offline");
            holder.textViewStatus.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            holder.floatingActionButtonChat.setImageResource(R.drawable.ic_email_black_18dp);
        }
        holder.floatingActionButtonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CLICK", contacts.get(position).getName());
                goToChat(contacts.get(position));
            }
        });

    }

    private void goToChat(Contact receiver) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("contact", receiver);
        android.support.v4.app.FragmentTransaction fragmentTransaction = ((LoginActivity) context).getSupportFragmentManager().beginTransaction();
        FragmentChat fragmentChat = new FragmentChat();
        fragmentChat.setArguments(bundle);
        fragmentTransaction.replace(R.id.frameContainer, fragmentChat);
        fragmentTransaction.commit();

    }

    @Override
    public int getItemCount() {
        if (contacts != null) {
            //Log.d("contacts size", contacts.size() + "");
            return contacts.size();
        } else {
            return 0;
        }
    }

    private static int clickedPosition;

    public static class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {
        TextView textViewContactName, textViewStatus;
        CircleImageView imageViewContactAvatar;
        FloatingActionButton floatingActionButtonChat;
        /*public MyViewHolderClicks mListener;*/

        public ViewHolder(View itemView) {
            super(itemView);
            textViewContactName = (TextView) itemView.findViewById(R.id.textViewContactName);
            textViewStatus = (TextView) itemView.findViewById(R.id.textViewStatus);
            imageViewContactAvatar = (CircleImageView) itemView.findViewById(R.id.imageViewContactAvatar);
            floatingActionButtonChat = (FloatingActionButton) itemView.findViewById(R.id.floatingActionButtonChat);
        }

       /* @Override
        public void onClick(View view) {
            clickedPosition = getAdapterPosition();
            if (view instanceof ImageView) {
                mListener.onImageViewClick((ImageView) view);
            } else {
                mListener.onTextViewClick((TextView) view);
            }
        }

        public static interface MyViewHolderClicks {
            public void onImageButtonClick(ImageView callerImage);


        }*/
    }
}

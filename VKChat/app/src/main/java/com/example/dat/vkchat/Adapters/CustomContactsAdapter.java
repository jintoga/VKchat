package com.example.dat.vkchat.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
    public void onBindViewHolder(ViewHolder holder, int position) {
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
        } else {
            holder.textViewStatus.setText("offline");
            holder.textViewStatus.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
        }

    }

    @Override
    public int getItemCount() {
        if (contacts != null) {
            Log.d("contacts size", contacts.size() + "");
            return contacts.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewContactName, textViewStatus;
        CircleImageView imageViewContactAvatar;


        public ViewHolder(View itemView) {
            super(itemView);
            textViewContactName = (TextView) itemView.findViewById(R.id.textViewContactName);
            textViewStatus = (TextView) itemView.findViewById(R.id.textViewStatus);
            imageViewContactAvatar = (CircleImageView) itemView.findViewById(R.id.imageViewContactAvatar);
        }
    }
}

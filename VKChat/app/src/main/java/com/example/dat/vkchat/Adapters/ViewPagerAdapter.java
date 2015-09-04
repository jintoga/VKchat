package com.example.dat.vkchat.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.dat.vkchat.Model.Contact;
import com.example.dat.vkchat.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by DAT on 8/31/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private final ArrayList<Contact> contacts = new ArrayList<>();

    private Context context;

    public ViewPagerAdapter(FragmentManager fm, Context context) {

        super(fm);
        this.context = context;
        Log.d("SAA", "A");
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, Contact contact) {
        mFragmentList.add(fragment);
        contacts.add(contact);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return contacts.get(position).getName();
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_item_tab, null);
        TextView tabItemName = (TextView) view.findViewById(R.id.textViewTabItemName);
        CircleImageView tabItemAvatar = (CircleImageView) view.findViewById(R.id.imageViewTabItemAvatar);


        tabItemName.setText(contacts.get(position).getName());
        tabItemName.setTextColor(context.getResources().getColor(android.R.color.background_light));
        if (contacts.get(position).getAvatar_url() != "") {
            Picasso.with(context).load(contacts.get(position).getAvatar_url()).resize(50, 50).into(tabItemAvatar);
        } else {
            Picasso.with(context).load(R.drawable.vk_avatar).resize(50, 50).into(tabItemAvatar);
        }
        return view;
    }
}

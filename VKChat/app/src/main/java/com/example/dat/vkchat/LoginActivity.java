package com.example.dat.vkchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dat.vkchat.Fragments.FragmentChat;
import com.example.dat.vkchat.Fragments.FragmentContacts;
import com.example.dat.vkchat.Model.Contact;
import com.squareup.picasso.Picasso;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class LoginActivity extends AppCompatActivity {
    private static final String[] sMyScope = new String[]{
            VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.NOHTTPS,
            VKScope.MESSAGES,
            VKScope.DOCS
    };

    private ImageView imageViewAvatar;
    private TextView textViewName;
    private TextView textViewEmail;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getIDs();
        setEvents();
        showLogin();
    }

    private void getIDs() {
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        imageViewAvatar = (ImageView) findViewById(R.id.imageViewProfile);
        textViewName = (TextView) findViewById(R.id.textView_username);
    }

    private Fragment fragment;
    private FragmentTransaction fragmentTransaction;

    private void setEvents() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                toolbar.setTitle("Closed");
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                toolbar.setTitle("Open");
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.Home:
                        //fragment = new FragmentHome();
                        break;
                    case R.id.Contacts:
                        fragment = new FragmentContacts();
                        break;
                    case R.id.Chat:
                        fragment = new FragmentChat();
                        break;
                    case R.id.Settings:
                        //fragment = new FragmentSettings();
                        break;
                    default:
                        break;
                }
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameContainer, fragment);
                fragmentTransaction.commit();
                return true;
            }
        });
    }

    private void showLogin() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new LoginFragment())
                .commit();
    }

    private void showLogout() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new LogoutFragment())
                .commit();
    }

    public static class LoginFragment extends Fragment {
        public LoginFragment() {
            super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_login, container, false);
            v.findViewById(R.id.button_sign_in).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VKSdk.login(getActivity(), sMyScope);
                }
            });
            return v;
        }

    }

    public static class LogoutFragment extends Fragment {
        public LogoutFragment() {
            super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_logout, container, false);


            v.findViewById(R.id.button_sign_out).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VKSdk.logout();
                    if (!VKSdk.isLoggedIn()) {
                        ((LoginActivity) getActivity()).showLogin();
                        ((LoginActivity) getActivity()).clearUserOldData();
                    }
                }
            });
            return v;
        }


    }


    private void clearUserOldData() {
        imageViewAvatar.setImageResource(R.drawable.boy);
        textViewName.setText("User");
        if (contacts != null)
            contacts.clear();
        FragmentContacts fragmentContacts = (FragmentContacts) this.getSupportFragmentManager().findFragmentById(R.id.frameContainer);
        if (fragmentContacts != null) {
            Log.d("FR", fragmentContacts.toString());
            fragmentContacts.clearContactsList();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                if (res != null) {
                    setUserData();
                    requestContacts();
                    requestMessages();
                    showLogout();
                } else {
                    showLogin();
                    clearUserOldData();
                }
            }

            @Override
            public void onError(VKError error) {
                // User didn't pass Authorization
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (VKSdk.isLoggedIn()) {
            setUserData();
            requestContacts();
            requestMessages();
            showLogout();
        } else {
            showLogin();
            clearUserOldData();
        }

    }

    private void setUserData() {
// User passed Authorization
        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "first_name,photo_200"));
        request.secure = false;
        request.useSystemLanguage = false;
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.d("Response", response.toString());

                try {
                    JSONArray jsonArray = response.json.getJSONArray("response");
                    //String name = jsonArray.get("first_name");
                    Log.d("jsonArray", jsonArray.toString());
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    Log.d("jsonObject", jsonObject.toString());
                    String first_name = jsonObject.getString("first_name");
                    String last_name = jsonObject.getString("last_name");
                    Log.d("Name", first_name);
                    String photo_url = "";
                    if (jsonObject.getString("photo_200") != null) {
                        photo_url = jsonObject.getString("photo_200");
                        Log.d("photo_url", photo_url);
                        Picasso.with(getApplicationContext()).load(photo_url).into(imageViewAvatar);
                    } else {
                        Picasso.with(getApplicationContext()).load(R.drawable.vk_avatar).into(imageViewAvatar);
                    }
                    textViewName.setText(first_name + " " + last_name);

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

    private ArrayList<Contact> contacts = null;

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    private void requestContacts() {
        VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "first_name,photo_200"));
        request.secure = false;
        request.useSystemLanguage = false;
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.d("Response", response.toString());
                contacts = new ArrayList<Contact>();
                try {
                    JSONObject jsonObject = response.json.getJSONObject("response");
                    Log.d("jsonObject", jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                    Log.d("jsonArray", jsonArray.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject joContact = jsonArray.getJSONObject(i);
                        Log.d("joContact", joContact.toString());

                        String first_name = joContact.getString("first_name");
                        String last_name = joContact.getString("last_name");
                        String full_name = first_name + " " + last_name;
                        int status = joContact.getInt("online");
                        String avatar_url = "";
                        if (joContact.isNull("photo_200") == false) {
                            avatar_url = joContact.getString("photo_200");
                        }
                        Contact contact = new Contact(full_name, avatar_url, status);
                        contacts.add(contact);
                    }
                    Log.d("contacts size", contacts.size() + "");

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

    private void requestMessages() {
        VKRequest request = VKApi.messages().getDialogs(VKParameters.from(VKApiConst.OUT, "1", VKApiConst.COUNT, "200"));

        request.secure = false;
        request.useSystemLanguage = false;
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.d("Response", response.toString());
                try {
                    JSONObject jsonObject = response.json.getJSONObject("response");
                    Log.d("jsonObject", jsonObject.toString());


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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

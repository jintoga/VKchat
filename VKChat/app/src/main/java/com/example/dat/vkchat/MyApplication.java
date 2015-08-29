package com.example.dat.vkchat;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;
import com.vk.sdk.util.VKUtil;


/**
 * Created by DAT on 8/25/2015.
 */
public class MyApplication extends Application {
    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
                // VkAccessToken is invalid
                Toast.makeText(MyApplication.this, "AccessToken invalidated", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MyApplication.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
        printHashKey();
    }

    public void printHashKey() {

        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        for (String line : fingerprints) {
            Log.d("fingerprints:", line);
        }
    }


}

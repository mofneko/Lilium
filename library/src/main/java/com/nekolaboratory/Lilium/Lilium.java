package com.nekolaboratory.Lilium;

import android.app.Activity;

import com.nekolaboratory.Lilium.network.LiliumConfig;
import com.unity3d.player.UnityPlayer;

import org.json.JSONException;

/**
 * @author Yusuke Arakawa
 */

public class Lilium {

    public void attest(String baseUri, String userId, AttestCallback callback) {
        new Core(UnityPlayer.currentActivity).initialize(callback, baseUri, userId, null);
    }

    public void attest(String baseUri, String userId, AttestCallback callback, String liliumConfig) throws JSONException {
        new Core(UnityPlayer.currentActivity).initialize(callback, baseUri, userId, new LiliumConfig(liliumConfig));
    }

    public void showErrorPlayService() {
        new Core(UnityPlayer.currentActivity).showErrorPlayService();
    }

    public void attest(Activity context, String baseUri, String userId, AttestCallback callback) {
        new Core(context).initialize(callback, baseUri, userId, null);
    }

    public void attest(Activity context, String baseUri, String userId, AttestCallback callback, LiliumConfig liliumConfig) {
        new Core(context).initialize(callback, baseUri, userId, liliumConfig);
    }

    public void showErrorPlayService(Activity context) {
        new Core(context).showErrorPlayService();
    }
}

package com.nekolaboratory.Lilium;

import android.app.Activity;

import com.unity3d.player.UnityPlayer;

/**
 * @author Yusuke Arakawa
 */

public class Lilium {

    public void attest(String baseUri, String userId, AttestCallback callback) {
        new Core(UnityPlayer.currentActivity).initialize(callback, baseUri, userId);
    }

    public void showErrorPlayService() {
        new Core(UnityPlayer.currentActivity).showErrorPlayService();
    }

    public void attest(Activity context, String baseUri, String userId, AttestCallback callback) {
        new Core(context).initialize(callback, baseUri, userId);
    }

    public void showErrorPlayService(Activity context) {
        new Core(context).showErrorPlayService();
    }
}

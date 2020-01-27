package com.nekolaboratory.Lilium;

import android.app.Activity;

import com.unity3d.player.UnityPlayer;

/**
 * @author Yusuke Arakawa
 */

public class Lilium {

    public void attest(String userId, String apiKey, String nonce, AttestCallback callback) {
        attest(userId, apiKey, nonce, callback, null);
    }

    public void attest(String userId, String apiKey, String nonce, AttestCallback callback, String liliumConfig) {
        new Core(UnityPlayer.currentActivity).attest(callback, userId, apiKey, nonce, liliumConfig);
    }

    public void attest(String baseUri, String userId, AttestCallback callback) {
        attest(baseUri, userId, callback, null);
    }

    public void attest(String baseUri, String userId, AttestCallback callback, String liliumConfig) {
        new Core(UnityPlayer.currentActivity).prepare(callback, baseUri, userId, liliumConfig);
    }

    @Deprecated
    public void attestWithConfig(String baseUri, String userId, AttestCallback callback, String liliumConfig) {
        new Core(UnityPlayer.currentActivity).prepare(callback, baseUri, userId, liliumConfig);
    }

    public void showErrorPlayService() {
        showErrorPlayService(UnityPlayer.currentActivity);
    }

    public void attest(Activity context, String userId, String apiKey, String nonce, AttestCallback callback) {
        attest(context, userId, apiKey, nonce, callback, null);
    }

    public void attest(Activity context, String userId, String apiKey, String nonce, AttestCallback callback, LiliumConfig liliumConfig) {
        new Core(context).attest(callback, userId, apiKey, nonce, liliumConfig);
    }

    public void attest(Activity context, String baseUri, String userId, AttestCallback callback) {
        attest(context, baseUri, userId, callback, null);
    }

    public void attest(Activity context, String baseUri, String userId, AttestCallback callback, LiliumConfig liliumConfig) {
        new Core(context).prepare(callback, baseUri, userId, liliumConfig);
    }

    @Deprecated
    public void attestWithConfig(Activity context, String baseUri, String userId, AttestCallback callback, String liliumConfig) {
        new Core(context).prepare(callback, baseUri, userId, liliumConfig);
    }

    public void showErrorPlayService(Activity context) {
        new Core(context).showErrorPlayService();
    }
}

package com.nekolaboratory.Lilium;

/**
 * @author Yusuke Arakawa
 */

public interface InstantAttestCallback extends AttestCallback {

    void onSuccess(String response);

    void onFailed(String exception);
}
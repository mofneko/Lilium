package com.nekolaboratory.Lilium.network;

import com.nekolaboratory.Lilium.LiliumException;

/**
 * @author Yusuke Arakawa
 */

public interface LiliumRequestCallback<LiliumResponse> {
    void onFailed(LiliumException exception);

    void onSuccess(LiliumResponse liliumResponse);
}
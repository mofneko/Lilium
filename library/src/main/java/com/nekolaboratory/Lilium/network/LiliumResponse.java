package com.nekolaboratory.Lilium.network;

import com.nekolaboratory.Lilium.LiliumException;

import org.json.JSONException;

/**
 * @author Yusuke Arakawa
 */

public interface LiliumResponse {
    LiliumResponse deserialize(String json) throws JSONException;

    int getStatusCode();

    void setStatusCode(int statusCode);

    LiliumException getLiliumException();

    void setLiliumException(LiliumException liliumException);

    boolean hasException();
}

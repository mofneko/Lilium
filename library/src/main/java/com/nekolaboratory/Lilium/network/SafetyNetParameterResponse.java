package com.nekolaboratory.Lilium.network;

import android.util.Base64;

import com.nekolaboratory.Lilium.LiliumException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Yusuke Arakawa
 */

public class SafetyNetParameterResponse implements LiliumResponse {
    private byte[] nonce;
    private String apiKey;
    private int statusCode;
    private LiliumException liliumException;

    public String getApiKey() {
        return this.apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public byte[] getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        try {
            this.nonce = Base64.decode(nonce.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (Exception e) {
            this.nonce = nonce.getBytes();
        }
    }

    @Override
    public SafetyNetParameterResponse deserialize(String json) throws JSONException {
        SafetyNetParameterResponse safetyNetParameterResponse = new SafetyNetParameterResponse();

        JSONObject jsonObject = new JSONObject(json);
        if (jsonObject.has("api_key")) {
            safetyNetParameterResponse.setApiKey(jsonObject.getString("api_key"));
        }
        if (jsonObject.has("nonce")) {
            safetyNetParameterResponse.setNonce(jsonObject.getString("nonce"));
        }
        return safetyNetParameterResponse;
    }

    @Override
    public int getStatusCode() {
        return this.statusCode;
    }

    @Override
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public LiliumException getLiliumException() {
        return this.liliumException;
    }

    @Override
    public void setLiliumException(LiliumException liliumException) {
        this.liliumException = liliumException;
    }

    @Override
    public boolean hasException() {
        return this.liliumException != null;
    }
}

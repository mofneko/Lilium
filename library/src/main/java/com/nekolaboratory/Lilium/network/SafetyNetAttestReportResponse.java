package com.nekolaboratory.Lilium.network;

import com.nekolaboratory.Lilium.LiliumException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Yusuke Arakawa
 */

public class SafetyNetAttestReportResponse implements LiliumResponse {
    private int statusCode;
    private LiliumException liliumException;
    private String responseBody;

    public String getResponseBody() {
        return this.responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public SafetyNetAttestReportResponse deserialize(String json) throws JSONException {
        SafetyNetAttestReportResponse safetyNetParameterResponse = new SafetyNetAttestReportResponse();

        JSONObject jsonObject = new JSONObject(json);
        if (jsonObject.has("response_body")) {
            safetyNetParameterResponse.setResponseBody(jsonObject.getString("response_body"));
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

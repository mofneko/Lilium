package com.nekolaboratory.Lilium.network;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Yusuke Arakawa
 */

public class LiliumConfig {

    private String safetyNetAttestReportRequestPackageName = "package_name";
    private String safetyNetAttestReportRequestUserId = "user_id";
    private String safetyNetAttestReportRequestVer = "ver";
    private String safetyNetAttestReportRequestAtn = "atn";
    private String safetyNetAttestReportRequestAtnError = "atn_error";
    private String safetyNetAttestReportRequestAtnErrorMsg = "atn_error_msg";
    private String safetyNetParameterResponseApiKey = "api_key";
    private String safetyNetParameterResponseNonce = "nonce";
    private String safetyNetParameterRequestPackageName = "package_name";
    private String safetyNetParameterRequestUserId = "user_id";

    public LiliumConfig() {
    }

    public LiliumConfig(String json) throws JSONException {
        deserialize(json);
    }

    private void deserialize(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        if (jsonObject.has("output_json_mapping")) {
            JSONObject outputJsonConfig = (JSONObject) jsonObject.get("output_json_mapping");
            if (outputJsonConfig.has("attest_report")) {
                JSONObject attestReportJson = (JSONObject) outputJsonConfig.get("attest_report");
                if (attestReportJson.has("package_name")) {
                    setSafetyNetAttestReportRequestPackageName(attestReportJson.getString("package_name"));
                }
                if (attestReportJson.has("user_id")) {
                    setSafetyNetAttestReportRequestUserId(attestReportJson.getString("user_id"));
                }
                if (attestReportJson.has("ver")) {
                    setSafetyNetAttestReportRequestVer(attestReportJson.getString("ver"));
                }
                if (attestReportJson.has("atn")) {
                    setSafetyNetAttestReportRequestAtn(attestReportJson.getString("atn"));
                }
                if (attestReportJson.has("atn_error")) {
                    setSafetyNetAttestReportRequestAtnError(attestReportJson.getString("atn_error"));
                }
                if (attestReportJson.has("atn_error_msg")) {
                    setSafetyNetAttestReportRequestAtnErrorMsg(attestReportJson.getString("atn_error_msg"));
                }
            }
            if (jsonObject.has("parameter_request")) {
                JSONObject parameterRequestJson = (JSONObject) jsonObject.get("parameter_request");
                if (parameterRequestJson.has("package_name")) {
                    setSafetyNetParameterRequestPackageName(parameterRequestJson.getString("package_name"));
                }
                if (parameterRequestJson.has("user_id")) {
                    setSafetyNetParameterRequestUserId(parameterRequestJson.getString("user_id"));
                }
            }
            if (jsonObject.has("parameter_response")) {
                JSONObject parameterResponseJson = (JSONObject) jsonObject.get("parameter_response");
                if (parameterResponseJson.has("api_key")) {
                    setSafetyNetParameterResponseApiKey(parameterResponseJson.getString("api_key"));
                }
                if (parameterResponseJson.has("nonce")) {
                    setSafetyNetParameterResponseNonce(parameterResponseJson.getString("nonce"));
                }
            }
        }
    }

    public String getSafetyNetAttestReportRequestPackageName() {
        return safetyNetAttestReportRequestPackageName;
    }

    public void setSafetyNetAttestReportRequestPackageName(String safetyNetAttestReportRequestPackageName) {
        this.safetyNetAttestReportRequestPackageName = safetyNetAttestReportRequestPackageName;
    }

    public String getSafetyNetAttestReportRequestUserId() {
        return safetyNetAttestReportRequestUserId;
    }

    public void setSafetyNetAttestReportRequestUserId(String safetyNetAttestReportRequestUserId) {
        this.safetyNetAttestReportRequestUserId = safetyNetAttestReportRequestUserId;
    }

    public String getSafetyNetAttestReportRequestVer() {
        return safetyNetAttestReportRequestVer;
    }

    public void setSafetyNetAttestReportRequestVer(String safetyNetAttestReportRequestVer) {
        this.safetyNetAttestReportRequestVer = safetyNetAttestReportRequestVer;
    }

    public String getSafetyNetAttestReportRequestAtn() {
        return safetyNetAttestReportRequestAtn;
    }

    public void setSafetyNetAttestReportRequestAtn(String safetyNetAttestReportRequestAtn) {
        this.safetyNetAttestReportRequestAtn = safetyNetAttestReportRequestAtn;
    }

    public String getSafetyNetAttestReportRequestAtnError() {
        return safetyNetAttestReportRequestAtnError;
    }

    public void setSafetyNetAttestReportRequestAtnError(String safetyNetAttestReportRequestAtnError) {
        this.safetyNetAttestReportRequestAtnError = safetyNetAttestReportRequestAtnError;
    }

    public String getSafetyNetAttestReportRequestAtnErrorMsg() {
        return safetyNetAttestReportRequestAtnErrorMsg;
    }

    public void setSafetyNetAttestReportRequestAtnErrorMsg(String safetyNetAttestReportRequestAtnErrorMsg) {
        this.safetyNetAttestReportRequestAtnErrorMsg = safetyNetAttestReportRequestAtnErrorMsg;
    }

    public String getSafetyNetParameterResponseApiKey() {
        return safetyNetParameterResponseApiKey;
    }

    public void setSafetyNetParameterResponseApiKey(String safetyNetParameterResponseApiKey) {
        this.safetyNetParameterResponseApiKey = safetyNetParameterResponseApiKey;
    }

    public String getSafetyNetParameterResponseNonce() {
        return safetyNetParameterResponseNonce;
    }

    public void setSafetyNetParameterResponseNonce(String safetyNetParameterResponseNonce) {
        this.safetyNetParameterResponseNonce = safetyNetParameterResponseNonce;
    }

    public String getSafetyNetParameterRequestPackageName() {
        return safetyNetParameterRequestPackageName;
    }

    public void setSafetyNetParameterRequestPackageName(String safetyNetParameterRequestPackageName) {
        this.safetyNetParameterRequestPackageName = safetyNetParameterRequestPackageName;
    }

    public String getSafetyNetParameterRequestUserId() {
        return safetyNetParameterRequestUserId;
    }

    public void setSafetyNetParameterRequestUserId(String safetyNetParameterRequestUserId) {
        this.safetyNetParameterRequestUserId = safetyNetParameterRequestUserId;
    }
}
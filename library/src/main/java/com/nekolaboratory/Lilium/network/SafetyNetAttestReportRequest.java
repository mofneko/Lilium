package com.nekolaboratory.Lilium.network;

import com.nekolaboratory.Lilium.BuildConfig;
import com.nekolaboratory.Lilium.LiliumException;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yusuke Arakawa
 */

public class SafetyNetAttestReportRequest implements LiliumRequest {

    private String packageName;
    private String userId;
    private String safetynetJwt;
    private LiliumException safetynetError;

    public SafetyNetAttestReportRequest(String packageName, String userId, String safetynetJwt, LiliumException liliumException) {
        setPackageName(packageName);
        setUserId(userId);
        setSafetynetJwt(safetynetJwt);
        setSafetynetError(liliumException);
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSafetynetJwt() {
        return this.safetynetJwt;
    }

    public void setSafetynetJwt(String safetynetJwt) {
        this.safetynetJwt = safetynetJwt;
    }

    public LiliumException getSafetynetError() {
        return safetynetError;
    }

    public void setSafetynetError(LiliumException safetynetError) {
        this.safetynetError = safetynetError;
    }

    @Override
    public String endpoint() {
        return "report";
    }

    @Override
    public String serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("package_name", getPackageName());
        map.put("user_id", getUserId());
        map.put("ver", BuildConfig.VERSION_NAME);
        map.put("atn", getSafetynetJwt());
        if (getSafetynetError() != null) {
            map.put("atn_error", getSafetynetError().getStatusCode());
            map.put("atn_error_msg", getSafetynetError().getException());
        } else {
            map.put("atn_error", null);
            map.put("atn_error_msg", null);
        }
        JSONObject jsonObject = new JSONObject(map);
        return jsonObject.toString();
    }
}

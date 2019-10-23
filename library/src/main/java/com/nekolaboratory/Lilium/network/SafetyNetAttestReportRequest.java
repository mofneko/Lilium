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
    private LiliumConfig liliumConfig;
    private LiliumException safetynetError;

    public SafetyNetAttestReportRequest(String packageName, String userId, String safetynetJwt, LiliumConfig liliumConfig, LiliumException liliumException) {
        setPackageName(packageName);
        setUserId(userId);
        setSafetynetJwt(safetynetJwt);
        setLiliumConfig(liliumConfig);
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

    public LiliumConfig getLiliumConfig() {
        return liliumConfig;
    }

    public void setLiliumConfig(LiliumConfig liliumConfig) {
        this.liliumConfig = liliumConfig;
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
        map.put(getLiliumConfig().getSafetyNetAttestReportRequestPackageName(), getPackageName());
        map.put(getLiliumConfig().getSafetyNetAttestReportRequestUserId(), getUserId());
        map.put(getLiliumConfig().getSafetyNetAttestReportRequestVer(), BuildConfig.VERSION_NAME);
        map.put(getLiliumConfig().getSafetyNetAttestReportRequestAtn(), getSafetynetJwt());
        if (getSafetynetError() != null) {
            map.put(getLiliumConfig().getSafetyNetAttestReportRequestAtnError(), getSafetynetError().getStatusCode());
            map.put(getLiliumConfig().getSafetyNetAttestReportRequestAtnErrorMsg(), getSafetynetError().getException().getMessage());
        } else {
            map.put(getLiliumConfig().getSafetyNetAttestReportRequestAtnError(), null);
            map.put(getLiliumConfig().getSafetyNetAttestReportRequestAtnErrorMsg(), null);
        }
        JSONObject jsonObject = new JSONObject(map);
        return jsonObject.toString();
    }
}

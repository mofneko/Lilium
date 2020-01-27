package com.nekolaboratory.Lilium.network;

import com.nekolaboratory.Lilium.LiliumConfig;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yusuke Arakawa
 */

public class SafetyNetParameterRequest implements LiliumRequest {

    private String packageName;
    private String userId;
    private LiliumConfig liliumConfig;

    public SafetyNetParameterRequest(String packageName, String userId, LiliumConfig liliumConfig) {
        setPackageName(packageName);
        setUserId(userId);
        setLiliumConfig(liliumConfig);
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

    public LiliumConfig getLiliumConfig() {
        return liliumConfig;
    }

    public void setLiliumConfig(LiliumConfig liliumConfig) {
        this.liliumConfig = liliumConfig;
    }

    @Override
    public String endpoint() {
        return "prepare";
    }

    @Override
    public String serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put(getLiliumConfig().getSafetyNetParameterRequestPackageName(), getPackageName());
        map.put(getLiliumConfig().getSafetyNetParameterRequestUserId(), getUserId());
        JSONObject jsonObject = new JSONObject(map);
        return jsonObject.toString();
    }
}

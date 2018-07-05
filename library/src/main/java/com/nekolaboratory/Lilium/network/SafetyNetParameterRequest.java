package com.nekolaboratory.Lilium.network;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yusuke Arakawa
 */

public class SafetyNetParameterRequest implements LiliumRequest {

    private String packageName;
    private String userId;

    public SafetyNetParameterRequest(String packageName, String userId) {
        setPackageName(packageName);
        setUserId(userId);
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

    @Override
    public String endpoint() {
        return "prepare";
    }

    @Override
    public String serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("package_name", getPackageName());
        map.put("user_id", getUserId());
        JSONObject jsonObject = new JSONObject(map);
        return jsonObject.toString();
    }
}

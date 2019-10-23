package com.nekolaboratory.Lilium;

import android.app.Activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.nekolaboratory.Lilium.network.LiliumConfig;
import com.nekolaboratory.Lilium.network.LiliumHttpClient;
import com.nekolaboratory.Lilium.network.SafetyNetAttestReportRequest;
import com.nekolaboratory.Lilium.network.SafetyNetAttestReportRequestCallback;
import com.nekolaboratory.Lilium.network.SafetyNetAttestReportResponse;
import com.nekolaboratory.Lilium.network.SafetyNetParameterRequest;
import com.nekolaboratory.Lilium.network.SafetyNetParameterRequestCallback;
import com.nekolaboratory.Lilium.network.SafetyNetParameterResponse;

/**
 * @author Yusuke Arakawa
 */

public class Core {
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private Activity activity;
    private AttestCallback callback;
    private String userId;
    private String baseUri;
    private LiliumConfig liliumConfig;

    private Core() {
    }

    public Core(Activity currentActivity) {
        setActivity(currentActivity);
    }

    public boolean showErrorPlayService() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(getContext());
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this.getContext(), result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }

    private void attest(SafetyNetParameterResponse requestParameter) {
        new SafetyNetWrap(getContext(), new SafetyNetWrap.SafetyNetJwsDelegate() {
            @Override
            public void onSuccess(String attestationResponse) {
                AttestCallback baseCallback = getAttestCallback();
                if (baseCallback != null) {
                    SafetyNetAttestReportRequest safetyNetAttestReportRequest = new SafetyNetAttestReportRequest(getPackageName(), getUserId(), attestationResponse, getLiliumConfig(), null);
                    if (baseCallback instanceof InstantAttestCallback) {
                        final InstantAttestCallback instantAttestCallback = (InstantAttestCallback) baseCallback;
                        SafetyNetAttestReportRequestCallback callback = new SafetyNetAttestReportRequestCallback() {
                            @Override
                            public void onFailed(LiliumException exception) {
                                if (LiliumException.UNEXPECTED_ERROR.equals(exception.getStatusCode())) {
                                    exception.setStatusCode(LiliumException.ATTEST_REPORT_ERROR);
                                    instantAttestCallback.onFailed(exception.getStatusCode());
                                }
                            }

                            @Override
                            public void onSuccess(SafetyNetAttestReportResponse safetyNetAttestReportResponse) {
                                instantAttestCallback.onSuccess(safetyNetAttestReportResponse.getResponseBody());
                            }
                        };
                        new LiliumHttpClient(getBaseUri()).post(safetyNetAttestReportRequest, new SafetyNetAttestReportResponse(), callback);
                    } else if (baseCallback instanceof DefaultAttestCallback) {
                        final DefaultAttestCallback defaultAttestCallback = (DefaultAttestCallback) baseCallback;
                        defaultAttestCallback.onResult(safetyNetAttestReportRequest.serialize());
                    }
                    return;
                }
            }

            @Override
            public void onFailed(LiliumException liliumException) {
                AttestCallback baseCallback = getAttestCallback();
                if (baseCallback != null) {
                    final SafetyNetAttestReportRequest safetyNetAttestReportRequest = new SafetyNetAttestReportRequest(getPackageName(), getUserId(), null, getLiliumConfig(), liliumException);
                    if (baseCallback instanceof InstantAttestCallback) {
                        final InstantAttestCallback instantAttestCallback = (InstantAttestCallback) baseCallback;
                        SafetyNetAttestReportRequestCallback callback = new SafetyNetAttestReportRequestCallback() {
                            @Override
                            public void onFailed(LiliumException exception) {
                                if (LiliumException.UNEXPECTED_ERROR.equals(exception.getStatusCode())) {
                                    exception.setStatusCode(LiliumException.ATTEST_REPORT_ERROR);
                                    instantAttestCallback.onFailed(exception.getStatusCode());
                                }
                            }

                            @Override
                            public void onSuccess(SafetyNetAttestReportResponse safetyNetAttestReportResponse) {
                                instantAttestCallback.onSuccess(safetyNetAttestReportResponse.getResponseBody());
                            }
                        };
                        new LiliumHttpClient(getBaseUri()).post(safetyNetAttestReportRequest, new SafetyNetAttestReportResponse(), callback);
                    } else if (baseCallback instanceof DefaultAttestCallback) {
                        final DefaultAttestCallback defaultAttestCallback = (DefaultAttestCallback) baseCallback;
                        defaultAttestCallback.onResult(safetyNetAttestReportRequest.serialize());
                    }
                    return;
                }
            }
        }).attest(requestParameter.getNonce(), requestParameter.getApiKey());
    }

    public void initialize(AttestCallback attestCallback, String baseUri, String userId, LiliumConfig liliumConfig) {
        setAttestCallback(attestCallback);
        setBaseUri(baseUri);
        setUserId(userId);
        setLiliumConfig(liliumConfig);
        SafetyNetParameterRequestCallback callback = new SafetyNetParameterRequestCallback() {
            @Override
            public void onFailed(LiliumException exception) {
                if (LiliumException.UNEXPECTED_ERROR.equals(exception.getStatusCode())) {
                    exception.setStatusCode(LiliumException.PREPARE_UNEXPECTED_ERROR);
                }
                AttestCallback baseCallback = getAttestCallback();
                if (baseCallback != null) {
                    SafetyNetAttestReportRequest safetyNetAttestReportRequest = new SafetyNetAttestReportRequest(getPackageName(), getUserId(), null, getLiliumConfig(), exception);
                    if (baseCallback instanceof InstantAttestCallback) {
                        final InstantAttestCallback instantAttestCallback = (InstantAttestCallback) baseCallback;
                        instantAttestCallback.onFailed(safetyNetAttestReportRequest.serialize());
                    } else if (baseCallback instanceof DefaultAttestCallback) {
                        final DefaultAttestCallback defaultAttestCallback = (DefaultAttestCallback) baseCallback;
                        defaultAttestCallback.onResult(safetyNetAttestReportRequest.serialize());
                    }
                }
            }

            @Override
            public void onSuccess(SafetyNetParameterResponse safetyNetParameterResponse) {
                attest(safetyNetParameterResponse);
            }
        };
        new LiliumHttpClient(getBaseUri()).post(new SafetyNetParameterRequest(getPackageName(), getUserId(), getLiliumConfig()), new SafetyNetParameterResponse(getLiliumConfig()), callback);
    }

    private String getPackageName() {
        return this.activity.getApplication().getPackageName();
    }

    private Activity getContext() {
        return this.activity;
    }

    private void setActivity(Activity activity) {
        this.activity = activity;
    }

    public LiliumConfig getLiliumConfig() {
        return liliumConfig;
    }

    public void setLiliumConfig(LiliumConfig liliumConfig) {
        if (liliumConfig == null) {
            this.liliumConfig = new LiliumConfig();
        } else {
            this.liliumConfig = liliumConfig;
        }
    }

    public AttestCallback getAttestCallback() {
        return callback;
    }

    public void setAttestCallback(AttestCallback callback) {
        this.callback = callback;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBaseUri() {
        return this.baseUri;
    }

    private void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }
}

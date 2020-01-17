package com.nekolaboratory.Lilium;

import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * @author Yusuke Arakawa
 */

public class SafetyNetWrap {
    private Context context;
    private SafetyNetDelegate safetyNetDelegate;

    public SafetyNetWrap(Context context, SafetyNetDelegate safetyNetDelegate) {
        this.context = context;
        setDelegate(safetyNetDelegate);
    }

    public void attest(byte[] nonce, String apiKey) {
        int isApiAvailability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (isApiAvailability != ConnectionResult.SUCCESS) {
            this.safetyNetDelegate.onFailed(LiliumException.getLiliumExceptionFromPlayServiceCode(isApiAvailability));
            return;
        }
        SafetyNet.getClient(context).attest(nonce, apiKey)
                .addOnSuccessListener(new OnSuccessListener<SafetyNetApi.AttestationResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.AttestationResponse response) {
                        if (SafetyNetWrap.this.safetyNetDelegate instanceof SafetyNetJwsDelegate) {
                            SafetyNetWrap.this.safetyNetDelegate.onSuccess(response.getJwsResult());
                        }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Exception e = new Exception(Status.RESULT_CANCELED.toString());
                        LiliumException liliumException = LiliumException.getLiliumExceptionFromException(e);
                        liliumException.setStatusCode(LiliumException.ATTEST_UNEXPECTED_ERROR);
                        SafetyNetWrap.this.safetyNetDelegate.onFailed(liliumException);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        LiliumException liliumException = LiliumException.getLiliumExceptionFromException(e);
                        if (ApiException.class.equals(e.getClass())) {
                            ApiException apiException = (ApiException) e;
                            liliumException.setStatusCode(String.format(LiliumException.ATTEST_API_ERROR, CommonStatusCodes.getStatusCodeString(apiException.getStatusCode())));
                        } else {
                            liliumException.setStatusCode(LiliumException.ATTEST_UNEXPECTED_ERROR);
                        }
                        SafetyNetWrap.this.safetyNetDelegate.onFailed(liliumException);
                    }
                });
    }

    public void setDelegate(SafetyNetDelegate safetyNetDelegate) {
        this.safetyNetDelegate = safetyNetDelegate;
    }

    private interface SafetyNetDelegate<E> {
        void onFailed(LiliumException liliumException);

        void onSuccess(E attestationResponse);
    }

    public interface SafetyNetJwsDelegate extends SafetyNetDelegate<String> {
        void onSuccess(String attestationResponse);
    }
}

package com.nekolaboratory.Lilium;

import com.google.android.gms.common.ConnectionResult;

import java.net.HttpURLConnection;

/**
 * @author Yusuke Arakawa
 */

public class LiliumException extends Exception {
    public static final String PREPARE_RETURNS_4XX = "PREPARE_RETURNS_4XX";
    public static final String PREPARE_RETURNS_5XX = "PREPARE_RETURNS_5XX";
    public static final String PREPARE_UNEXPECTED_ERROR = "PREPARE_UNEXPECTED_ERROR";
    public static final String PLAY_SERVICE_UNAVAILABLE = "PLAY_SERVICE_UNAVAILABLE";
    public static final String ATTEST_API_ERROR = "ATTEST_API_ERROR_%s";
    public static final String ATTEST_UNEXPECTED_ERROR = "ATTEST_UNEXPECTED_ERROR";
    public static final String ATTEST_REPORT_ERROR = "ATTEST_REPORT_ERROR";
    public static final String UNEXPECTED_ERROR = "UNEXPECTED_ERROR";
    public static final String PLAY_SERVICE_ERROR_MISSING = "PLAY_SERVICE_ERROR_MISSING";
    public static final String PLAY_SERVICE_ERROR_UPDATING = "PLAY_SERVICE_ERROR_UPDATING";
    public static final String PLAY_SERVICE_ERROR_VERSION_UPDATE_REQUIRED = "PLAY_SERVICE_ERROR_VERSION_UPDATE_REQUIRED";
    public static final String PLAY_SERVICE_ERROR_DISABLED = "PLAY_SERVICE_ERROR_DISABLED";
    public static final String PLAY_SERVICE_ERROR_INVALID = "PLAY_SERVICE_ERROR_INVALID";

    private Exception exception;
    private String statusCode;

    private LiliumException() {

    }

    public static LiliumException getLiliumExceptionFromHttpResponseCode(int responseCode) {
        if (responseCode >= HttpURLConnection.HTTP_INTERNAL_ERROR) {
            return LiliumException.getLiliumExceptionFromStatusCode(PREPARE_RETURNS_5XX);
        } else if (responseCode >= HttpURLConnection.HTTP_BAD_REQUEST) {
            return LiliumException.getLiliumExceptionFromStatusCode(PREPARE_RETURNS_4XX);
        }
        return LiliumException.getLiliumExceptionFromStatusCode(UNEXPECTED_ERROR);
    }

    public static LiliumException getLiliumExceptionFromPlayServiceCode(int responseCode) {
        LiliumException liliumException = new LiliumException();
        liliumException.statusCode = PLAY_SERVICE_UNAVAILABLE;
        switch (responseCode) {
            case ConnectionResult.SERVICE_MISSING:
                liliumException.setExceptionMessage(PLAY_SERVICE_ERROR_MISSING);
                break;
            case ConnectionResult.SERVICE_UPDATING:
                liliumException.setExceptionMessage(PLAY_SERVICE_ERROR_UPDATING);
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                liliumException.setExceptionMessage(PLAY_SERVICE_ERROR_VERSION_UPDATE_REQUIRED);
                break;
            case ConnectionResult.SERVICE_DISABLED:
                liliumException.setExceptionMessage(PLAY_SERVICE_ERROR_DISABLED);
                break;
            case ConnectionResult.SERVICE_INVALID:
                liliumException.setExceptionMessage(PLAY_SERVICE_ERROR_INVALID);
                break;
        }
        return liliumException;
    }

    public static LiliumException getLiliumExceptionFromStatusCode(String statusCode) {
        LiliumException liliumException = new LiliumException();
        liliumException.statusCode = statusCode;
        return liliumException;
    }

    public static LiliumException getLiliumExceptionFromException(Exception exception) {
        LiliumException liliumException = new LiliumException();
        liliumException.statusCode = UNEXPECTED_ERROR;
        liliumException.exception = exception;
        return liliumException;
    }

    public Exception getException() {
        return exception;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exception = new Exception(exceptionMessage);
    }
}

package com.nekolaboratory.Lilium.network;

import android.os.AsyncTask;

import com.nekolaboratory.Lilium.LiliumException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Yusuke Arakawa
 */

public class LiliumHttpClient {
    private String baseUri;

    public LiliumHttpClient(String baseUri) {
        setBaseUri(baseUri);
    }

    public void post(final LiliumRequest liliumRequest, final LiliumResponse liliumResponse, final LiliumRequestCallback callback) {
        new LiliumHttpClientTask(getBaseUri(), liliumRequest, liliumResponse, callback).execute();
    }

    private String getBaseUri() {
        return this.baseUri;
    }

    private void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }
}

class LiliumHttpClientTask extends AsyncTask<Void, Void, LiliumResponse> {
    private String baseUri;
    private LiliumRequest liliumRequest;
    private LiliumResponse liliumResponse;
    private LiliumRequestCallback callback;

    private LiliumRequest getLiliumRequest() {
        return liliumRequest;
    }

    private void setLiliumRequest(LiliumRequest liliumRequest) {
        this.liliumRequest = liliumRequest;
    }

    private LiliumResponse getLiliumResponse() {
        return liliumResponse;
    }

    private void setLiliumResponse(LiliumResponse liliumResponse) {
        this.liliumResponse = liliumResponse;
    }

    private LiliumRequestCallback getCallback() {
        return callback;
    }

    private void setCallback(LiliumRequestCallback callback) {
        this.callback = callback;
    }

    private String getBaseUri() {
        return this.baseUri;
    }

    private void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    LiliumHttpClientTask(String baseUri, LiliumRequest liliumRequest, LiliumResponse liliumResponse, LiliumRequestCallback callback) {
        setBaseUri(baseUri);
        setLiliumRequest(liliumRequest);
        setLiliumResponse(liliumResponse);
        setCallback(callback);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected LiliumResponse doInBackground(Void... params) {
        final HttpURLConnection urlConnection;
        try {
            URL url = new URL(getBaseUri() + "/" + getLiliumRequest().endpoint());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            OutputStream outputStream = urlConnection.getOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            printStream.print(getLiliumRequest().serialize());
            printStream.close();
            getLiliumResponse().setStatusCode(urlConnection.getResponseCode());
        } catch (Exception e) {
            getLiliumResponse().setLiliumException(LiliumException.getLiliumExceptionFromException(e));
            return getLiliumResponse();
        }
        String buffer = "";
        InputStream inputStream = null;
        try {
            inputStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            buffer = reader.readLine();
        } catch (FileNotFoundException fileNotFoundException) {
            getLiliumResponse().setLiliumException(LiliumException.getLiliumExceptionFromException(fileNotFoundException));
            InputStream errorInputStream = urlConnection.getErrorStream();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(errorInputStream, "UTF-8"));
                buffer = reader.readLine();
            } catch (Exception e) {
            } finally {
                if (errorInputStream != null) {
                    try {
                        errorInputStream.close();
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
            getLiliumResponse().setLiliumException(LiliumException.getLiliumExceptionFromException(e));
            return getLiliumResponse();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                }
            }
            urlConnection.disconnect();
        }
        try {
            if (getLiliumResponse().getStatusCode() != HttpURLConnection.HTTP_OK) {
                LiliumException liliumException = LiliumException.getLiliumExceptionFromHttpResponseCode(getLiliumResponse().getStatusCode());
                liliumException.setExceptionMessage(buffer);
                getLiliumResponse().setLiliumException(liliumException);
                return getLiliumResponse();
            }
            getLiliumResponse().deserialize(buffer);
            return getLiliumResponse();
        } catch (Exception e) {
            getLiliumResponse().setLiliumException(LiliumException.getLiliumExceptionFromException(e));
            return getLiliumResponse();
        }
    }

    @Override
    protected void onPostExecute(LiliumResponse response) {
        super.onPostExecute(response);
        if (response.hasException()) {
            getCallback().onFailed(response.getLiliumException());
        } else {
            getCallback().onSuccess(response);
        }
    }
}

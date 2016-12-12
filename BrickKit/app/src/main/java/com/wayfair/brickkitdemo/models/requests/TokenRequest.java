package com.wayfair.brickkitdemo.models.requests;

import android.util.Base64;

import com.android.volley.Request;
import com.wayfair.golleycore.core.models.GolleyBaseRequest;

import java.util.HashMap;

/**
 * Request to get Twitter API token.
 *
 * Corresponding response is {@link com.wayfair.brickkitdemo.models.responses.TokenResponse}
 */
public class TokenRequest extends GolleyBaseRequest {
    private static final String CONSUMER_KEY = "PeItiRrlTnNkIudVPosZ97zdr";
    private static final String CONSUMER_SECRET = "Zcp4td83QB5T0l1nw5ytML3ufZCK7zcGMLdTQ2JB0hRKyXwEdR";

    @Override
    public String urlParameters() {
        return "grant_type=client_credentials";
    }

    @Override
    public HashMap<String, String> headers() {
        HashMap<String, String> headers = new HashMap<>();
        String auth = "Basic "
                + Base64.encodeToString((CONSUMER_KEY
                        + ":" + CONSUMER_SECRET).getBytes(),
                Base64.NO_WRAP);
        headers.put("Authorization", auth);
        return headers;
    }

    @Override
    public String domain() {
        return "https://api.twitter.com";
    }

    @Override
    public String path() {
        return "oauth2/token";
    }

    @Override
    public int method() {
        return Request.Method.POST;
    }
}
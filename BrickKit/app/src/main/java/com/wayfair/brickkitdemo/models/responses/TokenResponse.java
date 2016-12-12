package com.wayfair.brickkitdemo.models.responses;

import com.wayfair.golleycore.core.models.GolleyBaseResponse;

/**
 * Response from request to get Twitter API token.
 *
 * Response object corresponding to {@link com.wayfair.brickkitdemo.models.requests.TokenRequest}
 */
public class TokenResponse extends GolleyBaseResponse {
    public String tokenType;
    public String accessToken;
}

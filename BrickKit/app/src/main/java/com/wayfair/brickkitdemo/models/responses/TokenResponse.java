package com.wayfair.brickkitdemo.models.responses;

import com.wayfair.golleycore.core.models.GolleyBaseResponse;

public class TokenResponse extends GolleyBaseResponse {
    public String tokenType;
    public String accessToken;
}

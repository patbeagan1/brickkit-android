package com.wayfair.brickkit.models.responses;

import com.wayfair.golleycore.core.models.GolleyBaseResponse;

public class TokenResponse extends GolleyBaseResponse {
    public String tokenType;
    public String accessToken;
}

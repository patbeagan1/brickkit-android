package com.wayfair.brickkit.models.requests;

import com.wayfair.brickkit.models.responses.TokenResponse;
import com.wayfair.golleycore.core.models.GolleyBaseRequest;

import java.util.HashMap;

public class StatusesUserTimeline extends GolleyBaseRequest {
    public String screenName;

    @Override
    public String domain() {
        return "https://api.twitter.com";
    }

    @Override
    public String path() {
        return "1.1/statuses/user_timeline.json";
    }

    @Override
    public String urlParameters() {
        return null;
    }
}

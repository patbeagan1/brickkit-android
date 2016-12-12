package com.wayfair.brickkitdemo.models.requests;

import com.wayfair.golleycore.core.models.GolleyBaseRequest;

/**
 * Request to get user status time from Twitter.
 *
 * Corresponding response is {@link com.wayfair.brickkitdemo.models.responses.Status}
 */
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

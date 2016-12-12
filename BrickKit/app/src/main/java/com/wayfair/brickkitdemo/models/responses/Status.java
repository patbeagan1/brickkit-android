package com.wayfair.brickkitdemo.models.responses;

import com.wayfair.golleycore.core.models.GolleyBaseResponse;

/**
 * Response to request to get user status time from Twitter.
 *
 * Corresponding response is {@link com.wayfair.brickkitdemo.models.requests.StatusesUserTimeline}
 */
public class Status extends GolleyBaseResponse {
    public long id;
    public String createdAt;
    public String text;
}

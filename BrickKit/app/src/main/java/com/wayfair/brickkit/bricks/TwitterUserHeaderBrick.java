package com.wayfair.brickkit.bricks;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wayfair.brickkit.R;
import com.wayfair.bricks.BaseBrick;
import com.wayfair.bricks.BrickSize;
import com.wayfair.bricks.BrickViewHolder;
import com.wayfair.bricks.ViewHolderRegistry;

public class TwitterUserHeaderBrick extends BaseBrick {
    public static final String BRICK_TEMPLATE = "cms/bricks/twitter_user_brick";

    public String value;
    public String hint;
    private View.OnClickListener removeClick;
    private View.OnClickListener addClick;

    static {
        ViewHolderRegistry.register(BRICK_TEMPLATE, new ViewHolderRegistry.GenerateViewHolderInterface() {
            @Override
            public BrickViewHolder generateViewHolder(ViewGroup parent) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.twitter_user_header, parent, false);

                return new TwitterUserHeaderBrickHolder(itemView);
            }
        });
    }

    public TwitterUserHeaderBrick(Context context, BrickSize spanSize, String value,
                                  String hint, View.OnClickListener removeClick, View.OnClickListener addClick) {
        super(context, spanSize);
        this.value = value;
        this.hint = hint;
        this.removeClick = removeClick;
        this.addClick = addClick;
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder) {
        if (holder instanceof TwitterUserHeaderBrickHolder) {
            TwitterUserHeaderBrickHolder twitterUserHeaderBrickHolder = (TwitterUserHeaderBrickHolder) holder;

        }
    }

    @Override
    public String getTemplate() {
        return BRICK_TEMPLATE;
    }

    private static class TwitterUserHeaderBrickHolder extends BrickViewHolder {
        ImageView userAvatar;
        Button follow;
        TextView userName;
        TextView userTag;
        TextView userDescription;
        TextView userLink;
        TextView userActivity;

        TwitterUserHeaderBrickHolder(View itemView) {
            super(itemView);

            userAvatar = (ImageView) itemView.findViewById(R.id.user_avatar);
            follow = (Button) itemView.findViewById(R.id.follow_user);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userTag = (TextView) itemView.findViewById(R.id.user_tag);
            userDescription = (TextView) itemView.findViewById(R.id.user_description);
            userLink = (TextView) itemView.findViewById(R.id.user_link);
            userActivity = (TextView) itemView.findViewById(R.id.user_activity);
        }
    }
}

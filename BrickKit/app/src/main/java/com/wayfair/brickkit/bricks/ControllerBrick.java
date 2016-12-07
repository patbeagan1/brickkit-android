package com.wayfair.brickkit.bricks;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wayfair.brickkit.R;
import com.wayfair.bricks.BaseBrick;
import com.wayfair.bricks.BrickPadding;
import com.wayfair.bricks.BrickSize;
import com.wayfair.bricks.BrickViewHolder;
import com.wayfair.bricks.ViewHolderRegistry;

public class ControllerBrick extends BaseBrick {
    public static final String BRICK_TEMPLATE = "cms/bricks/controller_brick";

    public String value;
    public String hint;
    private View.OnClickListener removeClick;
    private View.OnClickListener addClick;

    static {
        ViewHolderRegistry.register(BRICK_TEMPLATE, new ViewHolderRegistry.GenerateViewHolderInterface() {
            @Override
            public BrickViewHolder generateViewHolder(ViewGroup parent) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.controller_brick, parent, false);

                return new ControllerBrickHolder(itemView);
            }
        });
    }

    public ControllerBrick(Context context, BrickSize spanSize, String value,
                           String hint, View.OnClickListener removeClick, View.OnClickListener addClick) {
        super(context, spanSize);

        this.value = value;
        this.hint = hint;
        this.removeClick = removeClick;
        this.addClick = addClick;
    }

    public ControllerBrick(Context context, BrickSize spanSize, BrickPadding padding, String value,
                           String hint, View.OnClickListener removeClick, View.OnClickListener addClick) {
        super(context, spanSize, padding);

        this.value = value;
        this.hint = hint;
        this.removeClick = removeClick;
        this.addClick = addClick;
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder) {
        if (holder instanceof ControllerBrickHolder) {
            ControllerBrickHolder controllerBrickHolder = (ControllerBrickHolder) holder;
            controllerBrickHolder.textInputLayout.setHint(hint);
            controllerBrickHolder.editText.setText(value);
            controllerBrickHolder.editText.addTextChangedListener(
                    new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            // Do nothing
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            // Do nothing
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            value = s.toString();
                        }
                    }
            );
            controllerBrickHolder.downButton.setOnClickListener(removeClick);
            controllerBrickHolder.upButton.setOnClickListener(addClick);
        }
    }

    @Override
    public String getTemplate() {
        return BRICK_TEMPLATE;
    }

    public static class ControllerBrickHolder extends BrickViewHolder {
        TextInputLayout textInputLayout;
        TextInputEditText editText;
        Button downButton;
        Button upButton;

        ControllerBrickHolder(View itemView) {
            super(itemView);

            textInputLayout = (TextInputLayout) itemView.findViewById(R.id.text_input_layout);
            editText = (TextInputEditText) itemView.findViewById(R.id.text_input_edit_text);
            downButton = (Button) itemView.findViewById(R.id.down_button);
            upButton = (Button) itemView.findViewById(R.id.up_button);
        }
    }
}

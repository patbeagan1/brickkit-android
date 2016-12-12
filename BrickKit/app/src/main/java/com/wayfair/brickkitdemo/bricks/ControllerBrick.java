package com.wayfair.brickkitdemo.bricks;

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

import com.wayfair.brickkitdemo.R;
import com.wayfair.brickkit.brick.BaseBrick;
import com.wayfair.brickkit.padding.BrickPadding;
import com.wayfair.brickkit.size.BrickSize;
import com.wayfair.brickkit.BrickViewHolder;
import com.wayfair.brickkit.ViewHolderRegistry;

/**
 * Brick that provides add / remove click events and maintains an integer value.
 */
public class ControllerBrick extends BaseBrick {
    private static final String BRICK_TEMPLATE = "cms/bricks/controller_brick";

    public String value;
    private String hint;
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

    /**
     * Constructor.
     *
     * @param context context this brick exists in
     * @param spanSize size information for this brick
     * @param padding padding for this brick
     * @param value initial value to use in {@link android.widget.EditText}
     * @param hint hint text to use in {@link android.widget.EditText}
     * @param removeClick click listener for remove button
     * @param addClick click listener for add button
     */
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

    /**
     * {@link BrickViewHolder} for ControllerBrick.
     */
    private static final class ControllerBrickHolder extends BrickViewHolder {
        private final TextInputLayout textInputLayout;
        private final TextInputEditText editText;
        private final Button downButton;
        private final Button upButton;

        /**
         * Constructor for ControllerBrickHolder.
         *
         * @param itemView view for this brick
         */
        private ControllerBrickHolder(View itemView) {
            super(itemView);

            textInputLayout = (TextInputLayout) itemView.findViewById(R.id.text_input_layout);
            editText = (TextInputEditText) itemView.findViewById(R.id.text_input_edit_text);
            downButton = (Button) itemView.findViewById(R.id.down_button);
            upButton = (Button) itemView.findViewById(R.id.up_button);
        }
    }
}

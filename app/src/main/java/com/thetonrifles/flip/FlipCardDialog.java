package com.thetonrifles.flip;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FlipCardDialog extends DialogFragment implements View.OnClickListener {

    private View mLayout;
    private View mFrontView;
    private View mBackView;

    public static FlipCardDialog newInstance() {
        FlipCardDialog dialog = new FlipCardDialog();
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.view_card, container);
        // getting ui elements
        mLayout = layout.findViewById(R.id.card_root);
        mFrontView = layout.findViewById(R.id.card_front);
        mBackView = layout.findViewById(R.id.card_back);
        // defining click handler
        mLayout.setOnClickListener(this);
        mFrontView.setOnClickListener(this);
        mBackView.setOnClickListener(this);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_root:
                if (isVisible()) {
                    hide();
                }
                break;
            case R.id.card_front:
                doFlip();
                break;
            case R.id.card_back:
                doFlip();
                break;
        }
    }

    private void doFlip() {
        FlipAnimation anim = new FlipAnimation(mFrontView, mBackView);
        if (mFrontView.getVisibility() == View.GONE) {
            anim.reverse();
        }
        mLayout.startAnimation(anim);
    }

    public boolean reverse() {
        FlipAnimation anim = new FlipAnimation(mFrontView, mBackView);
        if (mFrontView.getVisibility() == View.GONE) {
            anim.reverse();
            mLayout.startAnimation(anim);
            return true;
        }
        return false;
    }

    public void hide() {
        super.dismiss();
        mFrontView.setVisibility(View.VISIBLE);
        mBackView.setVisibility(View.GONE);
    }

    public boolean hideOrReverse() {
        if (isVisible()) {
            if (!reverse()) {
                hide();
            }
            return true;
        }
        return false;
    }

}

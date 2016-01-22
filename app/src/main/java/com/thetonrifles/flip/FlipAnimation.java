package com.thetonrifles.flip;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class FlipAnimation extends Animation {

    private Camera mCamera;
    private View mFromView;
    private View mToView;
    private float mCenterX;
    private float mCenterY;
    private boolean mForward = true;

    /**
     * Creates a 3D flip animation between two views.
     *
     * @param fromView First view in the transition.
     * @param toView   Second view in the transition.
     */
    public FlipAnimation(View fromView, View toView) {
        mFromView = fromView;
        mToView = toView;
        setDuration(700);
        setFillAfter(false);
        setInterpolator(new AccelerateDecelerateInterpolator());
    }

    public void reverse() {
        mForward = false;
        View switchView = mToView;
        mToView = mFromView;
        mFromView = switchView;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCenterX = width / 2;
        mCenterY = height / 2;
        mCamera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        // Angle around the y-axis of the rotation at the given time
        // calculated both in radians and degrees.
        final double radians = Math.PI * interpolatedTime;
        float degrees = (float) (180.0 * radians / Math.PI);

        // Once we reach the midpoint in the animation, we need to hide the
        // source view and show the destination view. We also need to change
        // the angle by 180 degrees so that the destination does not come in
        // flipped around
        if (interpolatedTime >= 0.5f) {
            degrees -= 180.f;
            mFromView.setVisibility(View.GONE);
            mToView.setVisibility(View.VISIBLE);
        }

        if (mForward) {
            //determines direction of rotation when flip begins
            degrees = -degrees;
        }

        final Matrix matrix = t.getMatrix();
        mCamera.save();
        mCamera.translate(0.0f, 0.0f, (float) (150.0 * Math.sin(radians)));
        mCamera.rotateY(degrees);
        mCamera.getMatrix(matrix);
        mCamera.restore();
        matrix.preTranslate(-mCenterX, -mCenterY);
        matrix.postTranslate(mCenterX, mCenterY);
    }

}
package io.insightchain.inbwallet.mvps.view.widget.smarttablayout;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

public abstract class SmartTabIndicationInterpolator {

    public static final SmartTabIndicationInterpolator SMART = new SmartIndicationInterpolator();
    public static final SmartTabIndicationInterpolator LINEAR = new LinearIndicationInterpolator();

    static final int ID_SMART = 0;
    static final int ID_LINEAR = 1;

    public static SmartTabIndicationInterpolator of(int id) {
        switch (id) {
            case ID_SMART:
                return SMART;
            case ID_LINEAR:
                return LINEAR;
            default:
                throw new IllegalArgumentException("Unknown id: " + id);
        }
    }

    public abstract float getLeftEdge(float offset);

    public abstract float getRightEdge(float offset);

    public float getThickness(float offset) {
        return 1f; //Always the same thickness by default
    }

    public static class SmartIndicationInterpolator extends SmartTabIndicationInterpolator {

        private static final float DEFAULT_INDICATOR_INTERPOLATION_FACTOR = 3.0f;

        private final Interpolator leftEdgeInterpolator;
        private final Interpolator rightEdgeInterpolator;

        public SmartIndicationInterpolator() {
            this(DEFAULT_INDICATOR_INTERPOLATION_FACTOR);
        }

        public SmartIndicationInterpolator(float factor) {
            leftEdgeInterpolator = new AccelerateInterpolator(factor);
            rightEdgeInterpolator = new DecelerateInterpolator(factor);
        }

        @Override
        public float getLeftEdge(float offset) {
            return leftEdgeInterpolator.getInterpolation(offset);
        }

        @Override
        public float getRightEdge(float offset) {
            return rightEdgeInterpolator.getInterpolation(offset);
        }

        @Override
        public float getThickness(float offset) {
            return 1f / (1.0f - getLeftEdge(offset) + getRightEdge(offset));
        }

    }

    public static class LinearIndicationInterpolator extends SmartTabIndicationInterpolator {

        @Override
        public float getLeftEdge(float offset) {
            return offset;
        }

        @Override
        public float getRightEdge(float offset) {
            return offset;
        }

    }
}

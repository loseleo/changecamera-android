/*
 * Copyright (c) 2018. Shanghai Jifen Culture Communications Co., Ltd.
 * All rights reserved.
 */

package com.beige.camera.common.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.View;

/**
 * @author Created by lihong  on 2018/8/17 16:57
 * 可设置阴影颜色，圆角，面积，偏移量
 * 可设置View的背景形状，颜色，圆角
 */
public class ShadowDrawable extends Drawable {

    private Paint mPaint;
    /**
     * 可选值{@link #SHAPE_ROUND,#SHAPE_CIRCLE}
     */
    private int mShapeType;
    private int mShapeRadius;//背景圆角
    private int mShadowRadius;
    private int mOffsetX;
    private int mOffsetY;
    private int mBgColor[];//如果是多种颜色，则是渐变色
    private RectF mRect;

    public final static int SHAPE_ROUND = 1;
    public final static int SHAPE_CIRCLE = 2;

    private ShadowDrawable(int shape, int[] bgColor, int shapeRadius, int shadowColor, int shadowRadius, int offsetX, int offsetY) {
        this.mShapeType = shape;
        this.mBgColor = bgColor;
        this.mShapeRadius = shapeRadius;
        this.mShadowRadius = shadowRadius;
        this.mOffsetX = offsetX;
        this.mOffsetY = offsetY;
        mPaint = new Paint();
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setAntiAlias(true);
        mPaint.setShadowLayer(shadowRadius, offsetX, offsetY, shadowColor);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        mRect = new RectF(left + mShadowRadius - mOffsetX, top + mShadowRadius - mOffsetY, right - mShadowRadius - mOffsetX,
                bottom - mShadowRadius - mOffsetY);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Paint newPaint = new Paint();
        if (mBgColor != null) {
            if (mBgColor.length == 1) {
                newPaint.setColor(mBgColor[0]);
            } else {
                newPaint.setShader(new LinearGradient(mRect.left, mRect.height() / 2, mRect.right, mRect.height() / 2, mBgColor,
                        null, Shader.TileMode.CLAMP));
            }
        }
        newPaint.setAntiAlias(true);
        if (mShapeType == SHAPE_ROUND) {
            canvas.drawRoundRect(mRect, mShapeRadius, mShapeRadius, mPaint);
            canvas.drawRoundRect(mRect, mShapeRadius, mShapeRadius, newPaint);
        } else {
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), Math.min(mRect.width(), mRect.height()) / 2, mPaint);
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), Math.min(mRect.width(), mRect.height()) / 2, newPaint);

        }
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


    public static class Builder {
        private int mShape;
        private int mShapeRadius;
        private int mShadowColor;
        private int mShadowRadius;
        private int mOffsetX = 0;
        private int mOffsetY = 0;
        private int[] mBgColor;

        public Builder() {
            mShape = ShadowDrawable.SHAPE_ROUND;
            mShapeRadius = 12;
            mShadowColor = Color.parseColor("#4d000000");
            mShadowRadius = 18;
            mOffsetX = 0;
            mOffsetY = 0;
            mBgColor = new int[1];
            mBgColor[0] = Color.TRANSPARENT;
        }

        public Builder setShape(int mShape) {
            this.mShape = mShape;
            return this;
        }

        public Builder setShapeRadius(int ShapeRadius) {
            this.mShapeRadius = ShapeRadius;
            return this;
        }

        public Builder setShadowColor(int shadowColor) {
            this.mShadowColor = shadowColor;
            return this;
        }

        public Builder setShadowRadius(int shadowRadius) {
            this.mShadowRadius = shadowRadius;
            return this;
        }

        public Builder setOffsetX(int OffsetX) {
            this.mOffsetX = OffsetX;
            return this;
        }

        public Builder setOffsetY(int OffsetY) {
            this.mOffsetY = OffsetY;
            return this;
        }

        public Builder setBgColor(int BgColor) {
            this.mBgColor[0] = BgColor;
            return this;
        }

        public Builder setBgColor(int[] BgColor) {
            this.mBgColor = BgColor;
            return this;
        }

        public ShadowDrawable builder() {
            return new ShadowDrawable(mShape, mBgColor, mShapeRadius, mShadowColor, mShadowRadius, mOffsetX, mOffsetY);
        }
    }


    /**
     * 提供一个通用的方法
     *
     * @param view
     * @param bgColor
     * @param shapeRadius
     * @param shadowColor
     * @param shadowRadius
     * @param offsetX
     * @param offsetY
     */
    public static void setShadowDrawable(View view, int bgColor, int shapeRadius, int shadowColor, int shadowRadius, int offsetX, int offsetY) {
        ShadowDrawable drawable = new ShadowDrawable.Builder()
                .setBgColor(bgColor)
                .setShapeRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .builder();
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ViewCompat.setBackground(view, drawable);
    }

    public static void setShadowDrawable(View view, ShadowDrawable drawable) {
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ViewCompat.setBackground(view, drawable);
    }

}

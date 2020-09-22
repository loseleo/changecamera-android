package com.beige.camera.advertisement.core.infoflow.span;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ReplacementSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class AdLogoSpan extends ReplacementSpan {

    private int width;
    private Context context;
    private final float hPadding;
    private final float vPadding;
    private final float cornerRadius;
    private RectF bgRectF = new RectF();
    private final float textSize;
    private final int backgroundColor;
    private final int textColor;
    private final int marginLeft;


    public AdLogoSpan(Context context) {
        this.context = context;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        hPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, metrics);
        vPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5f, metrics);
        cornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, metrics);
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 9, metrics);
        backgroundColor = Color.argb(51,255,255,255);
        textColor = Color.argb(166,255,255,255);
        marginLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, metrics);
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        paint.setTextSize(textSize);
        float textWidth = paint.measureText(text, start, end);
        width = (int) (textWidth + hPadding * 2);
        return width + marginLeft;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        canvas.translate(marginLeft,0);
        paint.setColor(backgroundColor);
        paint.setTextSize(textSize);
        int lineHeight = bottom - top;
        float textHeight = paint.descent() - paint.ascent();
        float textTop = top + (lineHeight - textHeight) / 2;
        float xStart = marginLeft + x;
        bgRectF.set(xStart, Math.max(textTop - vPadding, top), xStart + width, Math.min(textTop + textHeight + vPadding, bottom));
        canvas.drawRoundRect(bgRectF, cornerRadius, cornerRadius, paint);
        paint.setColor(textColor);
        canvas.drawText(text.toString(), start, end, xStart + hPadding, textTop - paint.ascent(), paint);
    }
}

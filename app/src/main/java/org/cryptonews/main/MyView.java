package org.cryptonews.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class MyView extends View {

    TextPaint paint;
    DynamicLayout layout;
    String text;

    public MyView(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void init() {
        text = "T";
        paint = new TextPaint();
        paint.setAntiAlias(true);
        paint.setTextSize(14*getResources().getDisplayMetrics().density);
        paint.setColor(Color.BLACK);
        int width = (int) paint.measureText(text);
        DynamicLayout.Builder builder =  DynamicLayout.Builder.obtain(text,paint,width)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setIncludePad(false);
        layout = builder.build();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthRequirement = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthRequirement;
        } else {
            width = layout.getWidth() + getPaddingLeft() + getPaddingRight();
            if (widthMode == MeasureSpec.AT_MOST) {
                if (width > widthRequirement) {
                    width = widthRequirement;
                    // too long for a single line so relayout as multiline
                    layout = new DynamicLayout(text, paint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
                }
            }
        }
        int height;
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightRequirement = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightRequirement;
        } else {
            height = layout.getHeight() + getPaddingTop() + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightRequirement);
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        layout.draw(canvas);
        canvas.restore();
    }

    public void setText(String s) {
        this.text = s;
        init();
    }
}

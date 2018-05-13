package com.czdpzc.photomooc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.support.v7.*;

/**
 * 新建一个ImageView，在surfaceView上画矩形框
 * 屏幕与矩形框的适配问题还没有解决，现在是固定rect
 * Created by 2b on 2018/4/3.
 */

public class DrawImageView extends ImageView {
    public DrawImageView(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    Paint paint = new Paint();
    {
        paint.setAntiAlias(true);//设置抗锯齿
        paint.setColor(0xf1ed04);
        paint.setStyle(Paint.Style.STROKE);//只绘制图形轮廓
        paint.setStrokeWidth(5f);//设置线宽
        paint.setAlpha(100);
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(new Rect(220,450,440,600), paint);
    }
}

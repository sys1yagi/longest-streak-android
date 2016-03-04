package com.sys1yagi.longeststreakandroid.view;

import com.sys1yagi.longeststreakandroid.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.EventLog;
import android.view.View;

import java.util.List;

public class LongestStreakGrassesView extends View {

    List<EventLog> eventLogs;

    Paint paint = new Paint();

    public LongestStreakGrassesView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LongestStreakGrassesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setEventLogs(List<EventLog> eventLogs) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int grass1 = ContextCompat.getColor(getContext(), R.color.grass_1);
        int grass2 = ContextCompat.getColor(getContext(), R.color.grass_2);

        int size = Math.min(getWidth() / 20, getHeight() / 7);
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 7; j++) {
                paint.setColor(j % 2 == 0 ? grass1 : grass2);
                int x = size * i;
                int y = size * j;
                canvas.drawRect(x, y, x + size, y + size, paint);
            }
        }

//        grass_0 0
//        grass_1 1-3
//        grass_2 4-7
//        grass_3 8-12
//        grass_4 12以上
    }
}

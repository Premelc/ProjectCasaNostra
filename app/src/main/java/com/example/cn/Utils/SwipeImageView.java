package com.example.cn.Utils;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import org.jetbrains.annotations.NotNull;

public class SwipeImageView extends AppCompatImageView {
    public SwipeImageView(@NonNull @NotNull Context context) {
        super(context);
    }

    public SwipeImageView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeImageView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width;
        int height;
        if(widthMeasureSpec < heightMeasureSpec){
            height = getMeasuredHeight();
            width = Math.round(height * 1.0f);
        } else{
            width = getMeasuredWidth();
            height = Math.round(width * 1.0f);
        }

        setMeasuredDimension(width, height);
    }
}
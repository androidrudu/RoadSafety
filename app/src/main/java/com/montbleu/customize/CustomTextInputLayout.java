package com.montbleu.customize;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CustomTextInputLayout extends TextInputLayout {
    private Object mCollapsingTextHelper;
    private Rect bounds;
    private Method recalculateMethod;

    public CustomTextInputLayout(Context context) {
        this(context, null);
    }

    public CustomTextInputLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("NewApi")
    public CustomTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        adjustBounds();
    }

    private void init() {
        //collapsingTextHelper.setCollapsedTextGravity(Gravity.START);
        try {
            Field cthField = TextInputLayout.class.getDeclaredField("collapsingTextHelper");
            cthField.setAccessible(true);
            mCollapsingTextHelper = cthField.get(this);

            Field boundsField = mCollapsingTextHelper.getClass().getDeclaredField("collapsedBounds");
            boundsField.setAccessible(true);
            bounds = (Rect) boundsField.get(mCollapsingTextHelper);

            recalculateMethod = mCollapsingTextHelper.getClass().getDeclaredMethod("recalculate");
        }
        catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException e) {
            mCollapsingTextHelper = null;
            bounds = null;
            recalculateMethod = null;
            e.printStackTrace();
        }
    }

    @SuppressLint("RestrictedApi")
    private void adjustBounds() {
        if (mCollapsingTextHelper == null) {
            return;
        }

        try {
            bounds.left = getEditText().getLeft() + getEditText().getPaddingLeft();
            recalculateMethod.invoke(mCollapsingTextHelper);
        }
        catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}

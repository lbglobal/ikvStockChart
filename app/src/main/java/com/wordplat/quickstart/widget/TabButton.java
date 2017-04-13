package com.wordplat.quickstart.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.wordplat.quickstart.R;
import com.wordplat.quickstart.utils.AppUtils;

/**
 * 带提示红点的Tab按键
 *
 * @author liutao
 */
public class TabButton extends RadioButton {
    private int radius = 0;
    private int offset = 0;
    private Paint paint = null;
    private boolean isShow = false;

    private int topDrawableWidth = 0;

    public TabButton(Context context) {
        this(context, null);
    }

    public TabButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.radioButtonStyle);
    }

    public TabButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setButtonDrawable(null);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabButton);
        radius = (int) a.getDimension(R.styleable.TabButton_dotSize, AppUtils.dpTopx(context, 4));
        a.recycle();

        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        offset = radius * 2;

        Drawable[] drawables = null;
        try {
            drawables = getCompoundDrawables();

            if(drawables == null || drawables[1] == null){
                return;
            }

            topDrawableWidth = drawables[1].getIntrinsicWidth();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            drawables = null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isShow) {
            canvas.drawCircle((getWidth()/2+topDrawableWidth/2), offset, radius, paint);
        }
    }

    public void setShowTag(boolean isShow) {
        this.isShow = isShow;
        invalidate();
    }
}
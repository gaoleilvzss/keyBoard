package com.parkingwang.keyboard.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parkingwang.vehiclekeyboard.R;

public class RadiusButton extends TextView {

    //data
    private int width = 0;
    private int height = 0;
    private int roundRadius = 0;
    private int leftTopRadius = 0;
    private int leftBottomRadius = 0;
    private int rightTopRadius = 0;
    private int rightBottomRadius = 0;
    private int bgColor = Color.LTGRAY;
    private boolean isTouching = false;

    //img and text
    private Drawable leftDrawable = null;
    private int drawableWidth = 20;
    private int drawableHeight = 20;
    private int leftDrawablePaddingRight = 0;
    private String textString;
    private int textSize = 30;
    private int textColor = Color.BLACK;

    //onDraw
    Paint paint;
    Path path;
    RectF rectF;
    Rect rect;

    public RadiusButton(Context context, int width, int height) {
        super(context);

        this.width = width;
        this.height = height;

        this.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        this.setClickable(true);
    }

    public RadiusButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        getDataFromAttrs(context, attrs);
        this.setClickable(true);
    }

    public RadiusButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getDataFromAttrs(context, attrs);
        this.setClickable(true);
    }

    private void getDataFromAttrs(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundRadiusButton);
        leftTopRadius = ta.getDimensionPixelOffset(R.styleable.RoundRadiusButton_leftTopRadius, 0);
        leftBottomRadius = ta.getDimensionPixelOffset(R.styleable.RoundRadiusButton_leftBottomRadius, 0);
        rightTopRadius = ta.getDimensionPixelOffset(R.styleable.RoundRadiusButton_rightTopRadius, 0);
        rightBottomRadius = ta.getDimensionPixelOffset(R.styleable.RoundRadiusButton_rightBottomRadius, 0);
        bgColor = ta.getColor(R.styleable.RoundRadiusButton_bgColor, Color.LTGRAY);
        leftDrawable = ta.getDrawable(R.styleable.RoundRadiusButton_leftDrawable);
        drawableWidth = ta.getDimensionPixelOffset(R.styleable.RoundRadiusButton_drawableWidth, 0);
        drawableHeight = ta.getDimensionPixelOffset(R.styleable.RoundRadiusButton_drawableHeight, 0);
        leftDrawablePaddingRight =
                ta.getDimensionPixelOffset(R.styleable.RoundRadiusButton_leftDrawablePaddingRight, 0);
        textString = ta.getString(R.styleable.RoundRadiusButton_textString);
        textSize = ta.getDimensionPixelOffset(R.styleable.RoundRadiusButton_textSize, 0);
        textColor = ta.getColor(R.styleable.RoundRadiusButton_textColor, Color.BLACK);
        ta.recycle();
    }

    public void setRoundRadius(int roundRadius) {
        this.roundRadius = roundRadius;
        invalidate();
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
        invalidate();
    }

    public void setLeftDrawable(Drawable leftDrawable, int drawableWidth, int drawableHeight,
                                int paddingRight) {
        this.leftDrawable = leftDrawable;
        this.drawableWidth = drawableWidth;
        this.drawableHeight = drawableHeight;
        this.leftDrawablePaddingRight = paddingRight;
        invalidate();
    }

    public void setTextString(String textString) {
        this.textString = textString;
        invalidate();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isClickable()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isTouching = true;
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    isTouching = false;
                    invalidate();
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (width == 0 || height == 0) {
            width = getWidth();
            height = getHeight();
        }

        if (paint == null) {
            paint = new Paint();
        }
        if (path == null) {
            path = new Path();
        } else {
            path.reset();
        }
        if (rectF == null) {
            rectF = new RectF();
        }
        if (rect == null) {
            rect = new Rect();
        }

        paint.setColor(bgColor);
        paint.setAntiAlias(true);//抗锯齿
        paint.setStrokeWidth(0);//线的宽度设为0，避免画圆弧的时候部分圆弧与边界相切
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        path.setFillType(Path.FillType.WINDING);

        path.moveTo(0, leftTopRadius);
        rectF.set(0, 0, 2 * leftTopRadius, 2 * leftTopRadius);
        path.addArc(rectF, 180, 90);
        path.lineTo(width - rightTopRadius, 0);
        rectF.set(width - rightTopRadius * 2, 0, width, rightTopRadius * 2);
        path.addArc(rectF, -90, 90);
        path.lineTo(width, height - rightBottomRadius);
        rectF.set(width - rightBottomRadius * 2, height - rightBottomRadius * 2, width,
                height);
        path.addArc(rectF, 0, 90);
        path.lineTo(leftBottomRadius, height);
        rectF.set(0, height - leftBottomRadius * 2, leftBottomRadius * 2, height);
        path.addArc(rectF, 90, 90);
        path.lineTo(0, leftTopRadius);
        path.close();

        path.moveTo(0,leftTopRadius);
        path.lineTo(width-rightTopRadius,0);
        path.lineTo(width,height-rightBottomRadius);
        path.lineTo(leftBottomRadius,height);
        path.close();
        canvas.drawPath(path, paint);
        if (isTouching) {
            paint.setColor(getContext().getResources().getColor(R.color.black_tran_30));
            canvas.drawPath(path, paint);
        }

        //text, drawable两个一起计算位置
        if (!TextUtils.isEmpty(textString)) {
            paint.setStrokeWidth(1.5f);
            paint.setColor(textColor);
            paint.setTextSize(textSize);
            rect.setEmpty();
            paint.getTextBounds(textString, 0, textString.length(), rect);

            float leftBitmap = 0;
            float topBitmap = 0;
            if (leftDrawable != null) {
                if (leftDrawable != null) {
                    leftBitmap = (1.0f * width - drawableWidth - rect.width() - leftDrawablePaddingRight) / 2;
                    topBitmap = (1.0f * height - drawableHeight) / 2;
                    leftDrawable.setBounds((int) leftBitmap, (int) topBitmap,
                            (int) (leftBitmap + drawableWidth),
                            (int) (topBitmap + drawableHeight));

                    leftDrawable.draw(canvas);
                }
            }

            float textX = 0;
            float textY =
                    1.0f * height / 2 + paint.getTextSize() / 2 - paint.getFontMetrics().descent / 2;
            if (leftBitmap == 0 && topBitmap == 0) {
                textX = width / 2 - rect.width() / 2;
            } else {
                textX = leftBitmap + drawableWidth + leftDrawablePaddingRight;
            }
            canvas.drawText(textString, textX, textY, paint);
        }
    }
}

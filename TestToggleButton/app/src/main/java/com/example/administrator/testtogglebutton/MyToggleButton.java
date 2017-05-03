package com.example.administrator.testtogglebutton;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2017/5/3.
 */

public class MyToggleButton extends View {
    private Bitmap bmSwitchBackground;
    private Bitmap bmSlideButton;
    private int maxSlideDistance;
    private int slideDistance;
    private Paint paint;
    private boolean isOpen = false;
    private long actionDownTime;
    private float downX;

    @Override
    protected int getWindowAttachCount() {
        return super.getWindowAttachCount();
    }

    public MyToggleButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initView();
    }

    private void initView() {
        paint = new Paint();
        paint.setAntiAlias(true);
        bmSwitchBackground = BitmapFactory.decodeResource(getResources(), R.drawable.switch_background);
        bmSlideButton = BitmapFactory.decodeResource(getResources(), R.drawable.slide_button);
        maxSlideDistance = bmSwitchBackground.getWidth() - bmSlideButton.getWidth();

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        actionDownTime = System.currentTimeMillis();
                        downX = motionEvent.getX();
                        if (isOpen == true) {
                            slideDistance = maxSlideDistance;
                        } else {
                            slideDistance = 0;
                        }

                        break;

                    case MotionEvent.ACTION_MOVE:
                        float endX = motionEvent.getX();
                        //手指的位置，弹起位置-落指位置 可能为负，若为负，则判断滑动距离用slideDistance = maxSlideDistance - (int) (downX - endX);
                        Log.e("endX:", "" + endX);
                        if (isOpen == false) {
                            slideDistance = (int) (endX - downX);

                        } else {
                            slideDistance = maxSlideDistance - (int) (downX - endX);
                        }
                        if (slideDistance > maxSlideDistance) {
                            slideDistance = maxSlideDistance;
                            Log.e("slide1:", "" + slideDistance);
                        } else if (slideDistance < 0) {
                            slideDistance = 0;
                            Log.e("slide2:", "" + slideDistance);
                        }
//                        else if (slideDistance < 0 && isOpen == true){
//                            slideDistance = 0;
//                            Log.e("slide3:", ""+slideDistance);
//                        }
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        long actionUpTime = System.currentTimeMillis();
                        slideDistance = (int) (motionEvent.getX() - downX);
                        if (actionUpTime - actionDownTime < 500 && Math.abs(Math.abs(slideDistance)) < 5) {
                            isOpen = !isOpen;
                            toggleButtonSwitchChanged();
                        } else {
                            if (slideDistance < maxSlideDistance / 2 && isOpen == false) {
                                isOpen = false;
                            } else if (slideDistance > maxSlideDistance / 2 && isOpen == false) {
                                isOpen = true;
                            } else if (slideDistance > -maxSlideDistance / 2 && isOpen == true) {
                                isOpen = true;
                            } else if (slideDistance < -maxSlideDistance / 2 && isOpen == true) {
                                isOpen = false;
                            }
                            toggleButtonSwitchChanged();
                        }
                        break;
                }
                return true;
            }
        });
    }

    private void toggleButtonSwitchChanged() {
//        isOpen = !isOpen;
        if (isOpen) {
            slideDistance = maxSlideDistance;
        } else {
            slideDistance = 0;
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);父类方法。

        setMeasuredDimension(bmSwitchBackground.getWidth(), bmSwitchBackground.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        canvas.drawBitmap(bmSwitchBackground, 0, 0, paint);
        canvas.drawBitmap(bmSlideButton, slideDistance, 0, paint);
    }
}

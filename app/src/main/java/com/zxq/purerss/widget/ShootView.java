package com.zxq.purerss.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

public class ShootView extends View {
    private static final int DEGREE_60 = 60;

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF mBounds = new RectF();
    private int mRadius;
    private int mCenterX;
    private int mCenterY;


    private ValueAnimator mPlayAnimator;
    private float swipeAgenl = mRadius;//延伸的距离


    private float mShootLineTotalRotateAngle;//旋转角度
    private ValueAnimator mPreShootLineTotalRotateAnimator;


    private float padding = 20;
    private ValueAnimator paddingAnimator;

    private float fix = 4;
    private ValueAnimator fixAnimator;


    private static final float SHOOT_LINE_ROTATE_END_RADIANS = (float) (Math.PI / 6.0);//旋转结束弧度
    private static final float SHOOT_LINE_ROTATE_START_RADIANS = (float) (Math.PI / 2.0);//旋转开始弧度
    private static final float SHOOT_LINE_ROTATE_START_DEGREE =
            (float) Math.toDegrees(SHOOT_LINE_ROTATE_END_RADIANS);//旋转开始角度
    private static final int PRE_SHOOT_LINE_TOTAL_ROTATE_DURATION = 10000;

    public static final Property<ShootView, Float> SHOOT_LINE_TOTAL_ROTATE_DEGREE =
            new Property<ShootView, Float>(Float.class, null) {
                @Override
                public Float get(ShootView object) {
                    return object.mShootLineTotalRotateAngle;
                }

                @Override
                public void set(ShootView object, Float value) {
                    object.mShootLineTotalRotateAngle = value;
                    object.invalidate();
                }
            };

    public ShootView(Context context) {
        this(context, null);
    }

    public ShootView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShootView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.parseColor("#ffc6c6c6"));

        paddingAnimator = ValueAnimator.ofFloat(20, 0);
        paddingAnimator.setInterpolator(new LinearInterpolator());
        paddingAnimator.setDuration(PRE_SHOOT_LINE_TOTAL_ROTATE_DURATION);
        paddingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                padding = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        fixAnimator = ValueAnimator.ofFloat(4, 0);
        fixAnimator.setInterpolator(new LinearInterpolator());
        fixAnimator.setDuration(1200);
        fixAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fix = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        mPreShootLineTotalRotateAnimator =
                ValueAnimator.ofFloat(-(SHOOT_LINE_ROTATE_START_DEGREE / 2.0f) - 240.0f,
                        -(SHOOT_LINE_ROTATE_START_DEGREE / 2.0f) - 120.0f);
        mPreShootLineTotalRotateAnimator.setInterpolator(new LinearInterpolator());
        mPreShootLineTotalRotateAnimator.setDuration(PRE_SHOOT_LINE_TOTAL_ROTATE_DURATION);
        mPreShootLineTotalRotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mShootLineTotalRotateAngle = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        drawTest(canvas);
        drawArc(canvas);
        drawCircle(canvas);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBounds.set(0 + getPaddingLeft(), 0 + getPaddingTop(), getWidth() - getPaddingRight(),
                getHeight() - getPaddingBottom());

        mRadius = (int) (Math.min(mBounds.width(), mBounds.height()) / 2);
        mCenterX = (int) mBounds.centerX();
        mCenterY = (int) mBounds.centerY();
        swipeAgenl = mRadius * 2 / 3f;
        startPlay();
    }

    float mStartX;
    float mStartY;

    int colors[] = {Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.CYAN, Color.GRAY};

    private void drawTest(Canvas canvas) {
        drawCircle(canvas);
        canvas.save();
        canvas.translate(mCenterX, mCenterY);

        int i = 0;
        canvas.save();
        canvas.rotate(-DEGREE_60 * i);
        Path path = new Path();

        float stopX = (float) Math.sqrt(3) * mRadius / 2f;
        float stopY = -mRadius / 2f;

        canvas.drawLine(0, -mRadius, stopX, stopY, mPaint);
        mPaint.setColor(Color.RED);
        canvas.drawPoint(stopX, stopY, mPaint);
        mPaint.setColor(Color.RED);

        //canvas.drawLine(0, 0, stopX, stopY, mPaint);

        RectF oval = new RectF(-mRadius, -mRadius, mRadius, mRadius);


        RectF rectF = new RectF(-mRadius, -2 * mRadius, mRadius, 0);

        mPaint.setStyle(Paint.Style.FILL);
        path.addArc(oval, -90, 60);


        //计算直接点的
        float tagX = (float) (Math.sin(Math.toRadians(60)) * mRadius / Math.cos(Math.toRadians(30 - swipeAgenl)) * Math.sin(Math.toRadians(60 - swipeAgenl)));
        float tagY = (float) (Math.sin(Math.toRadians(60)) * mRadius / Math.cos(Math.toRadians(30 - swipeAgenl)) * Math.cos(Math.toRadians(60 - swipeAgenl))) - mRadius;

//        float tagX = Math.sqrt(Math.pow(mRadius,2.0) - Math.pow(Math.sin(Math.toRadians(60) * swipeAgenl), 2.0));

//        float L = (float) Math.sqrt(Math.pow(mRadius, 2.0) - Math.pow(Math.sin(Math.toRadians(60)) * swipeAgenl, 2.0));
//        float consL = L / mRadius;
//        float sinL = (float) Math.sin(Math.toRadians(60)) * swipeAgenl / mRadius;
//        float LL = (float) (L + Math.sin(Math.toRadians(30)) * swipeAgenl);
//
//        float tagX = LL * sinL;
//        float tagY = LL * consL - mRadius;

        canvas.drawPoint(tagX, tagY, mPaint);

//        path.moveTo(stopX, stopY);
//        path.lineTo(tagX, tagY);
//        path.lineTo(0, -mRadius);
        canvas.drawPath(path, mPaint);
//        }


    }

    private void drawArc(Canvas canvas) {

        canvas.save();
        canvas.translate(mCenterX, mCenterY);
//        canvas.rotate(mShootLineTotalRotateAngle);
//        for (int i = 0; i < 6; i++) {
        canvas.save();
        canvas.rotate(0);
        Path path = new Path();
        mPaint.setColor(colors[0]);
        RectF oval = new RectF(-mRadius, -mRadius, mRadius, mRadius);
        mPaint.setStyle(Paint.Style.STROKE);

//            mRadius- padding
        double asin = Math.toDegrees(Math.asin((mRadius - padding) / mRadius / 2)) * 2;
//            path.addArc(oval, -90, (float) asin);

        float stopX = (float) Math.sqrt(3) * mRadius / 2f;
        float stopY = -mRadius / 2f;
        float L = (float) Math.sqrt(Math.pow(mRadius, 2.0) - Math.pow(Math.sin(Math.toRadians(60)) * swipeAgenl, 2.0));
        float consL = L / mRadius;
        float sinL = (float) Math.sin(Math.toRadians(60)) * swipeAgenl / mRadius;
        float LL = (float) (L + Math.sin(Math.toRadians(30)) * swipeAgenl);

        float tagX = LL * sinL;
        float tagY = LL * consL - mRadius;

        Log.i("points", "stop:" + stopX + "stopy:" + stopY + "tagx:" + tagX + "tagy:" + tagY);
        path.moveTo(stopX - padding, stopY - padding - fix);
        path.lineTo(tagX - padding, tagY - padding);
        path.lineTo(0, -mRadius);
        canvas.drawPath(path, mPaint);
        canvas.restore();
//        }
        canvas.restore();
    }

    private void drawCircle(Canvas canvas) {
        mPaint.setColor(colors[1]);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.save();
        canvas.translate(mCenterX, mCenterY);
        canvas.drawCircle(0.0f, 0.0f, mRadius, mPaint);
        canvas.restore();
    }

    public void startPlay() {
        mPlayAnimator = ValueAnimator.ofFloat(mRadius, mRadius / 5f);
        mPlayAnimator.setDuration(PRE_SHOOT_LINE_TOTAL_ROTATE_DURATION);
        mPlayAnimator.setInterpolator(new LinearInterpolator());
        mPlayAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                swipeAgenl = (float) animation.getAnimatedValue();
                Log.i("swipeAgenl", "" + swipeAgenl);
                invalidate();
            }
        });
        mPlayAnimator.start();
        mPreShootLineTotalRotateAnimator.start();
        paddingAnimator.start();
        fixAnimator.start();
    }
}

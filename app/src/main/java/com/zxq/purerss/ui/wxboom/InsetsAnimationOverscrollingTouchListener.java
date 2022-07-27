package com.zxq.purerss.ui.wxboom;

/**
 * created by xiaoqing.zhou
 * on 2022/2/14
 * fun
 */

import android.graphics.Insets;
import android.graphics.Rect;
import android.os.Build;
import android.os.CancellationSignal;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowInsets;
import android.view.WindowInsetsAnimationControlListener;
import android.view.WindowInsetsAnimationController;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.zxq.purerss.utils.KeyboardAnimationUtil;

/**
 * 重写"View.OnTouchListener"用来适配可滚动视图,来控制软键盘的inset与可见性
 * 当应用再可滚动视图之上,如RecycleView上,他会track(追踪)滚动的手势,并且发送一个请求,通过
 * [android.view.WindowInsetsController.controlWindowInsetsAnimation]
 * 来控制软键盘（IME）的inset（软键盘部分填充物）
 * 当执行的时候,将根据用户的滚动位置设置 IME的 in/off
 */
@RequiresApi(api = Build.VERSION_CODES.Q)
public class InsetsAnimationOverscrollingTouchListener implements View.OnTouchListener {
    //动画控制器
    private WindowInsetsAnimationController insetsAnimationController;
    //当前的需要的监听器对象
    private WindowInsetsAnimationControlListener currentControlRequest;
    //初始的屏幕填充物
    private Insets startImeInsets = Insets.NONE;
    //填充物是否被使用
    private Boolean isImeShownAtStart = false;
    //是否处理
    private Boolean isHandling = false;
    //最后触摸点的X
    private float lastTouchX = 0f;
    //最后触摸点的Y
    private float lastTouchY = 0f;
    //window的最后位置的Y
    private float lastWindowY = 0;
    //bounds容器
    private Rect bounds = new Rect();
    //用于取消正在进行的操作
    private CancellationSignal cancellationSignal;
    //动画插值器
    private LinearInterpolator linearInterpolator = new LinearInterpolator();

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                //当按下屏幕的时候
                //获取起始位置的坐标
                lastTouchX = event.getX();
                lastTouchY = event.getY();
                //将view当前的宽高信息存在bounds之中
                KeyboardAnimationUtil.copyBounds(view, bounds);
                lastWindowY = bounds.top;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                //当手指移动的时候
                //此时的View（就是demo中的RecycleView）可能正在以WindowInsetsAnimation的形式移动
                //过程中，我们需要确保它对我们触摸的响应
                //我们通过跟踪RecycleView在窗口中的Y位置来实现这一操做的同时检查当前的bounds的差异
                KeyboardAnimationUtil.copyBounds(view, bounds);
                float windowOffsetY = bounds.top - lastWindowY;
                //到起始点的X方向的距离
                float dx = event.getX() - lastTouchX;
                //到起始点的Y轴的距离
                float dy = event.getY() - lastTouchY + windowOffsetY;

                if (!isHandling) {
                    //做一下触摸拦截，防止左右滑动的时候触发，和是否比touch slop大（触发事件的最小移动距离）
                    float dx1 = Math.abs(dx);
                    float dy1 = Math.abs(dy);
                    if (dx1 < dy1 && dy1 >= ViewConfiguration.get(view.getContext()).getScaledTouchSlop()) {
                        isHandling = true;
                    }
                }

                if (isHandling) {
                    //开始进行处理
                    if (currentControlRequest != null) {
                        updateImeInsets(dy);
                    } else if (!isImeShownAtStart && dy < 0 && view.canScrollVertically(-1)) {
                        //1.当前没有控制，输入法就不要显示
                        //2.用户正在向上滑动，
                        //3.开始控制输入法
                        startControlRequest(view);
                    }
                    //记录下X, Y位置，以及视图的Y窗口位用来给下一个触摸事件
                    lastTouchY = event.getY();
                    lastTouchX = event.getX();
                    lastWindowY = bounds.top;
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                finish();
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                //取消当前的[WindowInsetsAnimationController],结束动画，恢复到手势开始时的状态。
                insetsAnimationController.finish(isImeShownAtStart);
                //操作取消
                cancellationSignal.cancel();
                //恢复初始状态
                reset();
                break;
            }
        }

        return false;
    }

    //根据手指的移动距离 更新屏幕的填充物
    @RequiresApi(api = Build.VERSION_CODES.R)
    private boolean updateImeInsets(float dy) {

        if (insetsAnimationController == null) {
            Log.e("insetsAnimationController:", "我空的");
            return false;
        } else {
            //获取控制器对象
            WindowInsetsAnimationController controller = insetsAnimationController;
            //隐藏时的bottom位置
            int hiddenBottom = controller.getHiddenStateInsets().bottom;
            //显示时候的bottom位置
            int shownBottom = controller.getShownStateInsets().bottom;

            int startBottom;
            int endBottom;
            if (isImeShownAtStart) {
                //填充物被使用的时候
                startBottom = shownBottom;
                endBottom = hiddenBottom;
            } else {
                //填充物没被使用的时候
                startBottom = hiddenBottom;
                endBottom = shownBottom;
            }
            //计算出新的填充物 利用初始值和当前值 附加滚动条dy的值
            int insetBottom = Math.round((startImeInsets.bottom + controller.getCurrentInsets().bottom - dy));
            //将填充物位置限定再startBottom和endBottom之间
            //(这样子就不会输入框在键盘完全弹出来之后还在向上/向下移动了)
            if (startBottom < endBottom) {
                if (insetBottom < startBottom) {
                    insetBottom = startBottom;
                }
                if (insetBottom > endBottom) {
                    insetBottom = endBottom;
                }
            }
            if (endBottom < startBottom) {
                if (insetBottom < endBottom) {
                    insetBottom = endBottom;
                }
                if (insetBottom > startBottom) {
                    insetBottom = startBottom;
                }
            }

            controller.setInsetsAndAlpha
                    //将计算出的填充物给视图来挤压屏幕空间
                            (Insets.of(0, 0, 0,
                                    insetBottom),
                                    //透明度
                                    1f,
                                    //由此返回fraction（人话：动画进度）
                                    ((float) (insetBottom - startBottom) / (endBottom - startBottom)));

            return true;
        }
    }

    //开启控制
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void startControlRequest(View view) {
        //追踪输入法填充物和可见性
        startImeInsets = view.getRootWindowInsets().getInsets(WindowInsets.Type.ime());
        isImeShownAtStart = view.getRootWindowInsets().isVisible(WindowInsets.Type.ime());
        //为了控制一个WindowInsetsAnimation，需要传入一个监听器
        //这个监听器跟踪当前请求，并存储当前的WindowInsetsAnimationController
        WindowInsetsAnimationControlListener listener =
                new WindowInsetsAnimationControlListener() {
                    @Override
                    public void onReady(@NonNull WindowInsetsAnimationController controller, int i) {
                        if (currentControlRequest == this) {
                            onRequestReady(controller);
                        } else {
                            finish();
                        }
                    }

                    @Override
                    public void onFinished(@NonNull WindowInsetsAnimationController windowInsetsAnimationController) {

                    }

                    @Override
                    public void onCancelled(@Nullable WindowInsetsAnimationController windowInsetsAnimationController) {
                        reset();
                    }
                };
       /* cancellationSignal = view.getWindowInsetsController().controlWindowInsetsAnimation(
                WindowInsets.Type.ime(),
                -1,
                linearInterpolator,
                null,
                listener
        );*/

        currentControlRequest = listener;
    }

    //准备控制器
    private void onRequestReady(WindowInsetsAnimationController controller) {
        insetsAnimationController = controller;
    }

    //结束当前的WindowInsetsAnimationController，结束掉动画和切换
    //如果用户滚动了超过50%，IME的可见性设置为结束状态。
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void finish() {

        if (insetsAnimationController != null) {

            if (insetsAnimationController.getCurrentFraction() >= 0.5f) {
                //进度大于50%
                insetsAnimationController.finish(!isImeShownAtStart);
            } else {
                //进度小于50%
                insetsAnimationController.finish(isImeShownAtStart);
            }

        }

        cancellationSignal.cancel();
        reset();
    }

    //回归初始状态
    private void reset() {
        isHandling = false;
        lastTouchX = 0f;
        lastTouchY = 0f;
        lastWindowY = 0;
        bounds.setEmpty();

        insetsAnimationController = null;
        currentControlRequest = null;
        startImeInsets = Insets.NONE;
        isImeShownAtStart = false;
    }

}
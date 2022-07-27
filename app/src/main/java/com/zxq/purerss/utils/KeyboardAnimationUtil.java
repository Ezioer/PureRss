package com.zxq.purerss.utils;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.zxq.purerss.R;

/**
 * created by xiaoqing.zhou
 * on 2022/2/14
 * fun
 */
public class KeyboardAnimationUtil {
    private static int[] tmpIntArr = new int[2];

    public static void copyBounds(View view, Rect rect) {

        //判断一下
        if (view.isLaidOut() && view.isAttachedToWindow()) {
            rect.set(0, 0, view.getWidth(), view.getHeight());
            view.getLocationInWindow(tmpIntArr);
            rect.offset(tmpIntArr[0], tmpIntArr[1]);
        } else {
            throw new IllegalArgumentException("无法获取View的bounds,它未赋值?或者未绑定在Windows中");
        }
    }

    //把当前的ViewGroup.getClipToPadding存在Tag里面，用于恢复
    public static void storeClipToPadding(ViewGroup viewGroup) {
        viewGroup.setTag(R.id.viewgroup_clip_padding, viewGroup.getClipToPadding());
    }

    //从storeClipChildren中恢复ViewGroup.getClipToPadding的值
    public static void restoreClipToPadding(ViewGroup viewGroup) {
        Object stored = viewGroup.getTag(R.id.viewgroup_clip_padding);
        if (stored instanceof Boolean) {
            //确认类型正确
            viewGroup.setClipToPadding((Boolean) stored);
        }
        viewGroup.setTag(R.id.viewgroup_clip_padding, null);

    }

    //存储当前的ViewGroup的Tag中的值
    public static void storeClipChildren(ViewGroup viewGroup) {
        viewGroup.setTag(R.id.viewgroup_clip_children, viewGroup.getClipChildren());
    }

    //从storeClipChildren中恢复ViewGroup.getClipChildren的值
    public static void restoreClipChildren(ViewGroup viewGroup) {
        Object stored = viewGroup.getTag(R.id.viewgroup_clip_children);
        if (stored instanceof Boolean) {
            viewGroup.setClipChildren((Boolean) stored);
        }
        viewGroup.setTag(R.id.viewgroup_clip_children, null);
    }


    public static Float lerp(@NonNull int startValue, @NonNull int endValue, @NonNull float fract) {
        return startValue + (endValue - startValue) * fract;
    }
}

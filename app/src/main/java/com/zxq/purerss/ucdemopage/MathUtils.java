package com.zxq.purerss.ucdemopage;

/**
 * created by xiaoqing.zhou
 * on 2021/10/14
 * fun
 */
class MathUtils {


    static int constrain(int amount, int low, int high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

    static float constrain(float amount, float low, float high) {
        return amount < low ? low : (amount > high ? high : amount);


    }
}

package com.th.utils;

import java.math.BigDecimal;

/**
 * @author TanHaooo
 * @date 2021/3/26 16:24
 */
public class MathUtil {
    //先通过百分位获取一个精度到10分位的值，然后再通过 12 34 67 89的排序形式获取0 0.5 1中的某一个值
    public static double round(double value, int scale, int roundingMode) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(scale, roundingMode);
        double d = bd.doubleValue();
        bd = null;
        double decimal = (int) (d * 10) % 10;
        if (decimal >= 1 && decimal <= 2)
            decimal = 0;
        else if (decimal >= 3 && decimal <= 7)
            decimal = 0.5;
        else if (decimal >= 8 && decimal <= 9)
            decimal = 1;
        return (int) d + decimal;
    }

    public static double roundTwo(double value, int scale, int roundingMode){
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(scale, roundingMode);
        return bd.doubleValue();
    }
}

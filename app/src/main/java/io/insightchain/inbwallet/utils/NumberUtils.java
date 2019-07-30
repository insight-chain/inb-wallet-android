package io.insightchain.inbwallet.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * Created by lijilong on 04/19.
 */

public class NumberUtils {
    private NumberUtils() {
        throw new AssertionError();
    }

    /***
     * //格式化数字，用逗号分割
     * @param number 12345678.12345
     * @return 123, 456, 78.12345
     */
    public static String formatNumberWithCommaSplit(Double number, int decimalNumber) {
        if (number == null) {
            if (decimalNumber == 0) {
                return "0";
            } else if (decimalNumber == 1) {
                return "0.0";
            } else if (decimalNumber == 2) {
                return "0.00";
            } else if (decimalNumber == 3) {
                return "0.000";
            } else if (decimalNumber == 4) {
                return "0.0000";
            }
            return "";
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setRoundingMode(RoundingMode.HALF_UP);
        numberFormat.setGroupingUsed(true);//使用000,000,00.0000方式显示
        numberFormat.setMinimumFractionDigits(decimalNumber);//设置固定4位小数位
        numberFormat.setMaximumFractionDigits(decimalNumber);
        return numberFormat.format(number);

    }

    public static String formatNumberWithCommaSplitFloor(Double number, int decimalNumber) {
        if (number == null) {
            if (decimalNumber == 0) {
                return "0";
            } else if (decimalNumber == 1) {
                return "0.0";
            } else if (decimalNumber == 2) {
                return "0.00";
            } else if (decimalNumber == 3) {
                return "0.000";
            } else if (decimalNumber == 4) {
                return "0.0000";
            }
            return "";
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setRoundingMode(RoundingMode.FLOOR);
        numberFormat.setGroupingUsed(true);//使用000,000,00.0000方式显示
        numberFormat.setMinimumFractionDigits(decimalNumber);//设置固定4位小数位
        numberFormat.setMaximumFractionDigits(decimalNumber);
        return numberFormat.format(number);
    }

    /***
     * //格式化数字，用逗号分割
     * @param number 12345678.12345
     * @return 123, 456, 78.12345
     */
    public static String formatNumberWithCommaSplit(Float number, int decimalNumber) {
        if (number == null) {
            if (decimalNumber == 0) {
                return "0";
            } else if (decimalNumber == 1) {
                return "0.0";
            } else if (decimalNumber == 2) {
                return "0.00";
            } else if (decimalNumber == 3) {
                return "0.000";
            } else if (decimalNumber == 4) {
                return "0.0000";
            }
            return "";
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setRoundingMode(RoundingMode.HALF_UP);
        numberFormat.setGroupingUsed(true);//使用000,000,00.0000方式显示
        numberFormat.setMinimumFractionDigits(decimalNumber);//设置固定4位小数位
        numberFormat.setMaximumFractionDigits(decimalNumber);
        return numberFormat.format(number);

    }

    public static String formatNumberWithCommaSplitFloor(Float number, int decimalNumber) {
        if (number == null) {
            if (decimalNumber == 0) {
                return "0";
            } else if (decimalNumber == 1) {
                return "0.0";
            } else if (decimalNumber == 2) {
                return "0.00";
            } else if (decimalNumber == 3) {
                return "0.000";
            } else if (decimalNumber == 4) {
                return "0.0000";
            }
            return "";
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setRoundingMode(RoundingMode.FLOOR);
        numberFormat.setGroupingUsed(true);//使用000,000,00.0000方式显示
        numberFormat.setMinimumFractionDigits(decimalNumber);//设置固定4位小数位
        numberFormat.setMaximumFractionDigits(decimalNumber);
        return numberFormat.format(number);

    }

    /***
     * //格式化数字，用逗号分割
     * @param number 12345678.12345
     * @return 123, 456, 78.12345
     */
    public static String formatNumberNoComma(Double number, int decimalNumber) {
        if (number == null) {
            if (decimalNumber == 0) {
                return "0";
            } else if (decimalNumber == 1) {
                return "0.0";
            } else if (decimalNumber == 2) {
                return "0.00";
            } else if (decimalNumber == 3) {
                return "0.000";
            } else if (decimalNumber == 4) {
                return "0.0000";
            }
            return "";
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setRoundingMode(RoundingMode.HALF_UP);
        numberFormat.setGroupingUsed(false);//使用00000000.0000方式显示
        numberFormat.setMinimumFractionDigits(decimalNumber);//设置固定4位小数位
        numberFormat.setMaximumFractionDigits(decimalNumber);
        return numberFormat.format(number);
    }

    /***
     * //格式化数字，用逗号分割
     * @param number 12345678.12345
     * @return 123, 456, 78.12345 或 --
     */
    public static String formatNumberNoCommas(Double number, int decimalNumber) {
        if (number == null || number == 0) {
            return "--";
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setRoundingMode(RoundingMode.FLOOR);
        numberFormat.setGroupingUsed(false);//使用00000000.0000方式显示
        numberFormat.setMinimumFractionDigits(decimalNumber);//设置固定4位小数位
        numberFormat.setMaximumFractionDigits(decimalNumber);
        return numberFormat.format(number);
    }

    public static String formatNumberNoCommaFloor(Double number, int decimalNumber) {
        if (number == null) {
            if (decimalNumber == 0) {
                return "0";
            } else if (decimalNumber == 1) {
                return "0.0";
            } else if (decimalNumber == 2) {
                return "0.00";
            } else if (decimalNumber == 3) {
                return "0.000";
            } else if (decimalNumber == 4) {
                return "0.0000";
            }
            return "";
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setRoundingMode(RoundingMode.FLOOR);
        numberFormat.setGroupingUsed(false);//使用00000000.0000方式显示
        numberFormat.setMinimumFractionDigits(decimalNumber);//设置固定4位小数位
        numberFormat.setMaximumFractionDigits(decimalNumber);
        return numberFormat.format(number);
    }

    public static String formatNumberNoComma(Float number, int decimalNumber) {
        if (number == null) {
            if (decimalNumber == 0) {
                return "0";
            } else if (decimalNumber == 1) {
                return "0.0";
            } else if (decimalNumber == 2) {
                return "0.00";
            } else if (decimalNumber == 3) {
                return "0.000";
            } else if (decimalNumber == 4) {
                return "0.0000";
            }
            return "";
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setRoundingMode(RoundingMode.HALF_UP);
        numberFormat.setGroupingUsed(false);//使用00000000.0000方式显示
        numberFormat.setMinimumFractionDigits(decimalNumber);//设置固定4位小数位
        numberFormat.setMaximumFractionDigits(decimalNumber);
        return numberFormat.format(number);
    }

    /***
     * //格式化数字，用逗号分割,小于1时保留4位小数
     * @param number 12345678.12345
     * @return 123, 456, 78.12345
     */
    public static String formatVotePrice(Double number, int decimalNumber) {
        if (number == null) {
            if (decimalNumber == 0) {
                return "0";
            } else if (decimalNumber == 1) {
                return "0.0";
            } else if (decimalNumber == 2) {
                return "0.00";
            } else if (decimalNumber == 3) {
                return "0.000";
            } else if (decimalNumber == 4) {
                return "0.0000";
            }
            return "";
        }
        if (number < 1) {
            decimalNumber = 4;
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setRoundingMode(RoundingMode.HALF_UP);
        numberFormat.setGroupingUsed(true);//使用000,000,00.0000方式显示
        numberFormat.setMinimumFractionDigits(decimalNumber);//设置固定4位小数位
        numberFormat.setMaximumFractionDigits(decimalNumber);
        return numberFormat.format(number);

    }

    /***
     * null或等于0的数字显示--
     */
    public static String formatNumber(Integer number) {
        if (number == null || number == 0) {
            return "--";
        }
        return String.valueOf(number);
    }

    /**
     * 四舍五入
     *
     * @param scale 设置小数
     * @return
     */
    public static float MathRound(Float orignal, int scale) {
        if (orignal == null)
            return 0f;
        int roundingMode = 4;//表示四舍五入，可以选取其他模式
        BigDecimal bd = new BigDecimal(orignal);
        bd = bd.setScale(scale, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public static Double MathRound(Double orignal, int scale) {
        if (orignal == null)
            return 0d;
        int roundingMode = 4;//表示四舍五入，可以选取其他模式
        BigDecimal bd = new BigDecimal(orignal);
        bd = bd.setScale(scale, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    public static BigDecimal stripTrailingZeros(double d) {//去除double尾巴的0
        return new BigDecimal(d).stripTrailingZeros();
    }
}

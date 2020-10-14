package ua.kyiv.sa.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MathUtils {

    private static final MathContext MATH_CONTEXT = new MathContext(16, RoundingMode.HALF_UP);


    public static BigDecimal safeDivideBigDecimals(BigDecimal dividend, BigDecimal divisor) {
        if (divisor == null || divisor.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (dividend == null) {
            return BigDecimal.ZERO;
        }
        return dividend.divide(divisor, MATH_CONTEXT).stripTrailingZeros();
    }


    public static BigDecimal safeDivideLongs(long dividend, long divisor) {
        if (divisor == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(dividend).divide(BigDecimal.valueOf(divisor), MATH_CONTEXT).stripTrailingZeros();
    }

    public static BigDecimal getPercentage(Long part, Long total) {
        return MathUtils.safeDivideLongs(part * 100, total);
    }
}
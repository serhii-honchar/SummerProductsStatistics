package ua.kyiv.sa.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductAccumulator {
    private long productsCount;
    private BigDecimal totalPrice;
    private long totalRatingCount;
    private long totalRatingFiveCount;

    public ProductAccumulator() {
        this.productsCount = 0L;
        this.totalPrice = BigDecimal.ZERO;
        this.totalRatingCount = 0L;
        this.totalRatingFiveCount = 0L;
    }
}

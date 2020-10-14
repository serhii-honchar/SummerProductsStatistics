package ua.kyiv.sa.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class ProductResult {
    private BigDecimal avgPrice;
    private BigDecimal fiveStarsShare;
}

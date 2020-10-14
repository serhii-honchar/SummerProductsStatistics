package ua.kyiv.sa.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Product {
    private String originCountry;
    private BigDecimal price;
    private long ratingCount;
    private long ratingFiveCount;

}

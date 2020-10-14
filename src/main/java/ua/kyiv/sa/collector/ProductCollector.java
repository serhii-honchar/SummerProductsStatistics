package ua.kyiv.sa.collector;

import ua.kyiv.sa.util.MathUtils;
import ua.kyiv.sa.model.Product;
import ua.kyiv.sa.model.ProductAccumulator;
import ua.kyiv.sa.model.ProductResult;

import java.math.BigDecimal;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class ProductCollector implements Collector<Product, ProductAccumulator, ProductResult> {

    @Override
    public Supplier<ProductAccumulator> supplier() {
        return ProductAccumulator::new;
    }

    @Override
    public BiConsumer<ProductAccumulator, Product> accumulator() {
        return (a, p) -> {
            if (p.getPrice() != null) {
                a.setProductsCount(a.getProductsCount() + 1);
                a.setTotalPrice(a.getTotalPrice().add(p.getPrice()));
            }
            a.setTotalRatingCount(a.getTotalRatingCount() + p.getRatingCount());
            a.setTotalRatingFiveCount(a.getTotalRatingFiveCount() + p.getRatingFiveCount());
        };
    }

    @Override
    public BinaryOperator<ProductAccumulator> combiner() {
        return (acc1, acc2) -> {
            acc1.setTotalRatingFiveCount(acc1.getTotalRatingFiveCount() + acc2.getTotalRatingFiveCount());
            acc1.setTotalRatingCount(acc1.getTotalRatingCount() + acc2.getTotalRatingCount());
            acc1.setTotalPrice(acc1.getTotalPrice().add(acc2.getTotalPrice()));
            return acc1;
        };
    }

    @Override
    public Function<ProductAccumulator, ProductResult> finisher() {
        return accumulator -> {
            ProductResult productResult = new ProductResult();
            productResult.setAvgPrice(MathUtils.safeDivideBigDecimals(accumulator.getTotalPrice(), BigDecimal.valueOf(accumulator.getProductsCount())));
            productResult.setFiveStarsShare(MathUtils.getPercentage(accumulator.getTotalRatingFiveCount(), accumulator.getTotalRatingCount()));
            return productResult;
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.UNORDERED);
    }
}

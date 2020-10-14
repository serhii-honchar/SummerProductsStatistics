package ua.kyiv.sa.factory;

import org.apache.commons.lang3.StringUtils;
import ua.kyiv.sa.model.Product;
import ua.kyiv.sa.util.Constants;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class ProductFactory {

    public static Optional<Product> fromParsedString(String[] parsedLine, Map<String, Integer> indexByColumnName) {
        try {
            String price = StringUtils.trim(parsedLine[indexByColumnName.get(Constants.PRICE_COLUMN_NAME)]);
            String ratingCount = StringUtils.trim(parsedLine[indexByColumnName.get(Constants.RATING_COUNT_COLUMN_NAME)]);
            String ratingFiveCount = StringUtils.trim(parsedLine[indexByColumnName.get(Constants.RATING_FIVE_COUNT_COLUMN_NAME)]);
            return Optional.of(
                    new Product(
                            parsedLine[indexByColumnName.get(Constants.COUNTRY_COLUMN_NAME)],
                            StringUtils.isNotBlank(price) ? new BigDecimal(price) : BigDecimal.ZERO,
                            StringUtils.isNotBlank(ratingCount) ? Long.parseLong(ratingCount) : 0L,
                            StringUtils.isNotBlank(ratingFiveCount) ? Long.parseLong(ratingFiveCount) : 0L));
        } catch (Exception e) {
            System.out.println("Cannot parse some values in array - " + Arrays.toString(parsedLine) + ", " + e);
        }
        return Optional.empty();
    }

}

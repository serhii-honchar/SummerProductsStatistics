package ua.kyiv.sa.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String COUNTRY_COLUMN_NAME = "origin_country";
    public static final String PRICE_COLUMN_NAME = "price";
    public static final String RATING_COUNT_COLUMN_NAME = "rating_count";
    public static final String RATING_FIVE_COUNT_COLUMN_NAME = "rating_five_count";

    public static final String FILEPATH = "src/main/resources/dataset/test-task_dataset_summer_products.csv";

}

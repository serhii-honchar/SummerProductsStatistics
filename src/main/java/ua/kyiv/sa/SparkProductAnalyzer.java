package ua.kyiv.sa;

import org.apache.spark.sql.SparkSession;
import ua.kyiv.sa.util.Constants;

import static org.apache.spark.sql.functions.*;
import static org.apache.spark.sql.types.DataTypes.*;

public class SparkProductAnalyzer {

    public static void main(String[] args) {

        SparkSession session = SparkSession
                .builder()
                .config("spark.master", "local")
                .appName("Spark Product dataset Analyzer")
                .getOrCreate();

        session.read()
                .option("header", "true")
                .option("quote", "\"")
                .option("escape", "\"")
                .option("multiLine", true)
                .csv(Constants.FILEPATH)
                .select(
                        col(Constants.COUNTRY_COLUMN_NAME).alias("country").cast(StringType),
                        col(Constants.PRICE_COLUMN_NAME).alias("price").cast(DoubleType),
                        col(Constants.RATING_COUNT_COLUMN_NAME).alias("rating_count").cast(LongType),
                        col(Constants.RATING_FIVE_COUNT_COLUMN_NAME).alias("rating_five_count").cast(LongType))
                .groupBy(col("country"))
                .agg(
                        avg("price").as("avgPrice"),
                        (sum("rating_five_count").cast(DoubleType).$div(sum("rating_count").cast(DoubleType)))
                                .$times(100).as("five_percentage"))
                .orderBy("country")
                .show();
    }
}

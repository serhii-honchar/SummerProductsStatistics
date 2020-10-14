package ua.kyiv.sa;


import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import ua.kyiv.sa.collector.ProductCollector;
import ua.kyiv.sa.factory.ProductFactory;
import ua.kyiv.sa.model.Product;
import ua.kyiv.sa.model.ProductResult;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

public class ProductAnalyzer {

    public static void main(String[] args) throws IOException {
        String filename = "src/main/resources/dataset/test-task_dataset_summer_products.csv";

        //get map containing index of table column and its name
        Map<String, Integer> indexByColumnName = getIndexByColumnNameMap(filename);

        //reading file
        CSVReader reader = new CSVReader(new FileReader(filename));

        //setup reader to skip the first line of the file (already read)
        reader.skip(1);

        Map<String, ProductResult> result = Stream

                //read file line by line
                .iterate(new String[]{}, Objects::nonNull, readLine(reader))

                //remove lines without values
                .filter(x -> x != null && x.length > 0)

                //create model required for further calculations
                .map(x -> ProductFactory.fromParsedString(x, indexByColumnName))
                .flatMap(Optional::stream)

//                uncomment to remove products without countries
//                .filter(x -> !x.getOriginCountry().equals(""))

                //collect and calculate statistics
                .collect(
                        groupingBy(
                                Product::getOriginCountry,
                                new ProductCollector()
                        ))

                //order map by country (key)
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(
                        toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (a, b) -> a,
                                LinkedHashMap::new));


        printReport(result);

    }

    private static Map<String, Integer> getIndexByColumnNameMap(String fileName) throws IOException {
        //reading the first line with headers
        List<String> headers = Files.lines(Paths.get(fileName))
                .limit(1)
                .map(x -> x.split(","))
                .flatMap(Stream::of)
                .collect(Collectors.toList());

        //populating the map with header's indexes
        return IntStream.range(0, headers.size())
                .boxed()
                .collect(toMap(headers::get, i -> i));
    }

    private static UnaryOperator<String[]> readLine(CSVReader reader) {
        return x -> {
            String[] nextLine;
            try {
                nextLine = reader.readNext();
                return nextLine;
            } catch (IOException | CsvValidationException e) {
                e.printStackTrace();
            }
            return null;
        };
    }


    private static void printReport(Map<String, ProductResult> result) {
        System.out.println("=======================================================================");
        System.out.println(String.format("|   %10s   ||   %10s   ||   %20s   |", "Country", "AvgPrice", "Share of five-star products"));
        System.out.println("=======================================================================");
        result.forEach((key, value) -> System.out.println(String.format("|       %2s       ||   %10s   ||   %20s          |",
                key, value.getAvgPrice(), value.getFiveStarsShare())));
        System.out.println("=======================================================================");
    }
}

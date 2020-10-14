package ua.kyiv.sa;


import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import ua.kyiv.sa.collector.ProductCollector;
import ua.kyiv.sa.factory.ProductFactory;
import ua.kyiv.sa.model.Product;
import ua.kyiv.sa.model.ProductResult;
import ua.kyiv.sa.util.Constants;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

public class ProductAnalyzer {

    public static void main(String[] args) throws IOException {

        //get map containing index of table column and its name
        Map<String, Integer> indexByColumnName = getIndexByColumnNameMap(Constants.FILEPATH);

        //reading file
        CSVReader reader = new CSVReaderBuilder(new FileReader(Constants.FILEPATH))
                //setup reader to skip the first line of the file (already read)
                .withSkipLines(1)
                .build();

        Map<String, ProductResult> result = Stream
                //read file line by line
                .iterate(Optional.of(new String[]{}), Optional::isPresent, readLine(reader))
                .flatMap(Optional::stream)

                //remove lines without values
                .filter(x -> x.length > 0)

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


        if (result.size() > 0) {
            printReport(result);
        } else {
            System.out.println("No results");
        }

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

    private static UnaryOperator<Optional<String[]>> readLine(CSVReader reader) {
        return x -> {
            String[] nextLine;
            try {
                nextLine = reader.readNext();
                return Optional.ofNullable(nextLine);
            } catch (IOException | CsvValidationException e) {
                System.err.println("Exception while reading from file" + e);
            }
            return Optional.empty();
        };
    }


    private static void printReport(Map<String, ProductResult> result) {
        String border = "=================================================================================";
        System.out.println(border);
        System.out.println(String.format("|   %10s   ||   %20s   ||   %20s   |", "Country", "AvgPrice", "Share of five-star products"));
        System.out.println(border);
        result.forEach((key, value) -> System.out.println(String.format("|       %2s       ||   %20s   ||   %20s          |",
                key, value.getAvgPrice(), value.getFiveStarsShare())));
        System.out.println(border);
    }
}

Repository contains two solutions of the entry task based on: 
1) Spark (run ua/kyiv/sa/SparkProductAnalyzer.java)
Program output: 
+-------+-----------------+-----------------+
|country|         avgPrice|  five_percentage|
+-------+-----------------+-----------------+
|   null|5.549411764705883|36.98587614465311|
|     AT|              7.0|             null|
|     CN| 8.34162928759893|48.50286227212926|
|     GB|              9.0|51.20967741935484|
|     SG|             3.38|42.18036529680365|
|     US|9.397096774193548|49.57595410326765|
|     VE|            8.298|47.61904761904761|
+-------+-----------------+-----------------+
2) Java Stream API (required JDK9 and later, to start the application - run ua/kyiv/sa/ProductAnalyzer.java
| Country  |       AvgPrice     |  Share of five-star products |
|----------|--------------------|------------------------------|
|          |  5.549411764705882 |  36.98587614465311           |
|   AT     |                  7 |                  0           |
|   CN     |  8.341629287598945 |  48.50286227212926           |
|   GB     |                  9 |  51.20967741935484           |
|   SG     |               3.38 |  42.18036529680365           |
|   US     |  9.397096774193548 |  49.57595410326765           |
|   VE     |              8.298 |  47.61904761904762           |

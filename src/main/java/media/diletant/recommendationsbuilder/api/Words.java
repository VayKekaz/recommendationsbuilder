package media.diletant.recommendationsbuilder.api;

import io.micrometer.core.instrument.util.IOUtils;

public class Words {
  public final static String endingsPattern =
      "\\B(ый|ой|ая|ое|ые|ому|а|о|у|е|ого|ему|и|ство|ых|ох|ия|ий|ь|я|он|ют|ат)\\b";
  public final static String stoppingPattern = "\\b(" + String.join(
      "|",
      getResourceAsStringArray("stopwords.txt")
  ) + ")\\b";

  public final static String[] history = getResourceAsStringArray("historyterms.txt");

  private static String[] getResourceAsStringArray(String resourceName) {
    String[] returnValue;
    var classLoader = Words.class.getClassLoader();
    var stopWordsStream = classLoader.getResourceAsStream(resourceName);
    returnValue = IOUtils.toString(stopWordsStream).split("\\s+");
    return returnValue;
  }
}

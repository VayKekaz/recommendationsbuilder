package media.diletant.recommendationsbuilder.api;

import io.micrometer.core.instrument.util.IOUtils;

public class Words {
  public final static String[] stopping = getResourceAsStringArray("stopwords.txt");
  public final static String[] history = getResourceAsStringArray("historyterms.txt");

  private static String[] getResourceAsStringArray(String resourceName) {
    String[] returnValue;
    var classLoader = Words.class.getClassLoader();
    var stopWordsStream = classLoader.getResourceAsStream(resourceName);
    returnValue = IOUtils.toString(stopWordsStream).split("\\s+");
    return returnValue;
  }
}

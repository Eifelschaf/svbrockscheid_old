package svbrockscheid.de.svbrockscheid;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Matthias on 19.09.2014.
 */
public class ValueParser {

    private static final Pattern resultPattern = Pattern.compile("\\$([a-zA-Z0-9]+) = '(.+)'");

    public static Map<String, String> parse(String ergebnissDatei) {
        HashMap<String, String> ergebnisse = new HashMap<String, String>();
        Matcher ergebnisMatcher = resultPattern.matcher(ergebnissDatei);
        while (ergebnisMatcher.find()) {
            ergebnisse.put(ergebnisMatcher.group(0), ergebnisMatcher.group(1));
        }
        return ergebnisse;
    }
}

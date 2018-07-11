package me.blayyke.cbot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MiscUtils {
    private static final Pattern urlPattern = Pattern.compile("(https?://(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]\\.[^\\s]{2,})");

    //Optional Http(s):// -> optional prefix (www. ect) -> Website name (google ect) -> .domain (com, org ect)/letters
    private static final Pattern pastebinPattern = Pattern.compile("^(https?://)?(www.)?pastebin.com/([a-zA-Z0-9]+)$");
    private static final Pattern pastebinRawPattern = Pattern.compile("(https?://)?(www.)?pastebin.com/raw/([a-zA-Z0-9]+)$");
    private static final Pattern gistPattern = Pattern.compile("^(https?://)?gist.githubusercontent.com/([A-Za-z0-9]+)/[a-zA-Z0-9]+/raw/[A-Za-z0-9]+/[a-zA-Z]+$");

    public static String joinStringArray(String[] args, String separator) {
        return joinStringArray(args, separator, 0);
    }

    public static String joinStringArray(String[] args, String separator, int startIndex) {
        if (startIndex > args.length) startIndex = 0;

        StringBuilder builder = new StringBuilder();
        for (; startIndex < args.length; startIndex++) {
            builder.append(args[startIndex]);
            if (startIndex >= args.length - 1) continue;
            builder.append(separator);
        }

        return builder.toString();
    }

    public static String getActionFromUrl(String url) {
        if (actionIsUrl(url)) {
            Matcher matcher = pastebinPattern.matcher(url);
            if (pastebinRawPattern.matcher(url).matches()) return CBHttp.getInstance().get(url);
            else if (gistPattern.matcher(url).matches()) return CBHttp.getInstance().get(url);
            else if (matcher.matches()) {
                return CBHttp.getInstance().get("https://pastebin.com/raw/" + matcher.group(3));
            } else throw new IllegalArgumentException("Action URL is not of supported type!");
        }
        return null;
    }

    public static boolean actionIsUrl(String url) {
        return urlPattern.matcher(url).matches();
    }

    public static String getCode(String action, String code) {
        if (MiscUtils.actionIsUrl(action)) {
            if (code == null) code = MiscUtils.getActionFromUrl(action);
            return code;
        } else return action;
    }
}
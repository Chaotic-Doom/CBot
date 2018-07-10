package me.blayyke.cbot;

public class MiscUtils {
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
}
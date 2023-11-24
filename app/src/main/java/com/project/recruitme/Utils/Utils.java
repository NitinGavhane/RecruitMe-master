package com.project.recruitme.Utils;

import java.text.DecimalFormat;

public class Utils {

    public static Integer tryParse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static String truncateString(String input) {
        if (input.length() > 45) {
            return input.substring(0, 40) + "...";
        } else {
            return input;
        }
    }

    public static String formatFloatRange(float num1, float num2) {
        DecimalFormat df = new DecimalFormat("#.00");
        String formattedNum1 = df.format(num1);
        String formattedNum2 = df.format(num2);
        return formattedNum1 + "L to " + formattedNum2+"L";
    }
    public static Float tryParseFloat(String text) {
        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
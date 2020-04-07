package cds_automation.utilities;

public class StringUtils {

    public static final String EMPTY_STRING = "";
    public static final String SPACE = " ";

    public static String leftPad(String str, int finalLength) {
        return leftPad(str, finalLength, null);
    }

    public static String rightPad(String str, int finalLength) {
        return rightPad(str, finalLength, null);
    }

    public static String leftPad(String str, int finalLength, String padChar) {
        if ( finalLength < str.length() ) {
            return str.substring(0, finalLength);
        } else if ( finalLength == str.length() ) {
            return str;
        }

        if ( padChar == null ) {
            return String.format("%1$"+finalLength+"s", str);
        }
        else {
            for ( int i = str.length() ; i < finalLength ; i++ ) {
                str = padChar + str;
            }
            return str;
        }
    }


    public static String rightPad(String str, int finalLength, String padChar) {
        if ( finalLength < str.length() ) {
            return str.substring(0, finalLength);
        } else if ( finalLength == str.length() ) {
            return str;
        }

        if ( padChar == null ) {
            return String.format("%1$-"+finalLength+"s", str);
        }
        else {
            for ( int i = str.length() ; i < finalLength ; i++ ) {
                str = str + padChar;
            }
            return str;
        }
    }

    public static String getValueFromKeyValueArray(String[] keyValue) {

        if (keyValue.length == 1) {
            return SPACE;
        }

        return keyValue[1];
    }
    
    public static String returnEmptyStringIfNull(String value) {
        if (value == null) {
            return EMPTY_STRING;
        }
        
        return value;
    }
}

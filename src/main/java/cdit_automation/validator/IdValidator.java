package cdit_automation.validator;

import java.util.stream.Stream;

public class IdValidator {

  private IdValidator() {
    // Not Needed
  }

  private static final String NRIC_PATTERN = "[ST]\\d{7}[A-Z]";
  private static final String FIN_PATTERN = "[FG]\\d{7}[A-Z]";
  private static final String ID_PATTERN = "[STFG]\\d{7}[A-Z]";

  public static boolean isNricValid(String nric) {
    if (nric == null) {
      return false;
    }

    nric = nric.toUpperCase();

    if (nric.length() != 9 || !isNricMatchFormat(nric) || !isIdPrefixAcceptable(nric)) {
      return false;
    }

    return isValidCheckSum(nric);
  }

  public static boolean isFinValid(String fin) {
    if (fin == null) {
      return false;
    }

    fin = fin.toUpperCase();

    if (fin.length() != 9 || !isFinMatchFormat(fin) || !isIdPrefixAcceptable(fin)) {
      return false;
    }

    return isValidCheckSum(fin);
  }

  public static boolean isIdValid(String ic) {
    if (ic == null) {
      return false;
    }

    ic = ic.toUpperCase();

    if (ic.length() != 9 || !isIdMatchFormat(ic) || !isIdPrefixAcceptable(ic)) {
      return false;
    }

    return isValidCheckSum(ic);
  }

  private static boolean isIdPrefixAcceptable(String ic) {
    return !(ic.startsWith("S555") || ic.startsWith("S888"));
  }

  private static boolean isIdMatchFormat(String ic) {
    return ic.matches(ID_PATTERN);
  }

  private static boolean isNricMatchFormat(String nric) {
    return nric.matches(NRIC_PATTERN);
  }

  private static boolean isFinMatchFormat(String fin) {
    return fin.matches(FIN_PATTERN);
  }

  private static boolean isValidCheckSum(String id) {
    String prefix = String.valueOf(id.charAt(0));
    String digits = id.substring(1, 8);
    String checksum = String.valueOf(id.charAt(8));
    String[] checkSumDigits = getCheckDigits(prefix);

    int sum = getSum(prefix, digits);

    return checkSumDigits[10 - (sum % 11)].equals(checksum);
  }

  private static int getSum(String prefix, String digits) {
    int sum = 0;
    String[] add4Prefixes = {"T", "G"};
    int[] weights = {2, 7, 6, 5, 4, 3, 2};

    if (Stream.of(add4Prefixes).anyMatch(x -> x.equals(prefix))) {
      sum += 4;
    }

    for (int i = 0; i < digits.length(); i++) {
      sum += Integer.parseInt(String.valueOf(digits.charAt(i))) * weights[i];
    }
    return sum;
  }

  private static String[] getCheckDigits(String prefix) {
    String[] nricPrefixes = {"S", "T"};

    if (Stream.of(nricPrefixes).anyMatch(x -> x.equals(prefix))) {
      return new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "Z", "J"};
    } else {
      return new String[] {"K", "L", "M", "N", "P", "Q", "R", "T", "U", "W", "X"};
    }
  }
}

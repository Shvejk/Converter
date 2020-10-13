package converter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);

        int sourceRadix = readInteger(scanner);
        String inputNumberAsString = readString(scanner);
        int destinationRadix = readInteger(scanner);

        if (sourceRadix == -1 || destinationRadix == -1) {
            System.out.println("Error: radixes must be numbers");
            return;
        }
        if (!isValidateInput(sourceRadix, inputNumberAsString, destinationRadix)) {
            return;
        }

        StringBuilder finalNumber = new StringBuilder();

        String integerPart = getIntegerPart(inputNumberAsString);
        int integerPartInBase10 = 0;

        if (sourceRadix == 1) {
            integerPartInBase10 = integerPart.length();
        } else {
            integerPartInBase10 = Integer.parseInt(integerPart, sourceRadix);
        }

        String integerPartInNewBase = decimalToDestinationBaseAsString(integerPartInBase10, destinationRadix);
        finalNumber.append(integerPartInNewBase);

        if (numberHasDecimalPoint(inputNumberAsString)) {
            finalNumber.append(".");
        }

        if (destinationRadix != 1 && numberHasDecimalPoint(inputNumberAsString)) {
            double fractionalPartInBase10 = convertFractionToBaseTen(inputNumberAsString, sourceRadix);
            for (int i = 0; i < 5; i++) {
                double nextDecimalValue = destinationRadix * fractionalPartInBase10;
                int integerPartOfDecimal = (int) nextDecimalValue;
                String integerPartInDestinationBase = decimalToDestinationBaseAsString(integerPartOfDecimal, destinationRadix);
                finalNumber.append(integerPartInDestinationBase);
                fractionalPartInBase10 = getFractionalPart(String.valueOf(nextDecimalValue));
            }
            System.out.println(finalNumber);
        } else if (destinationRadix == 1) {
            System.out.println("1".repeat(integerPartInBase10));
        } else {
            System.out.println(integerPartInNewBase);
        }
    }

    public static String getIntegerPart(String number) {
        if (numberHasDecimalPoint(number)) {
            int decimalPointIndex = getIndexOfDecimalPoint(number);
            return number.substring(0, decimalPointIndex);
        }
        return number;
    }

    public static double getFractionalPart(String number) {
        if (numberHasDecimalPoint(number)) {
            int decimalPointIndex = getIndexOfDecimalPoint(number);
            return Double.parseDouble(number.substring(decimalPointIndex));
        }
        return -1;
    }

    public static boolean numberHasDecimalPoint(String number) {
        return getIndexOfDecimalPoint(number) != -1;
    }

    public static int charToDecimal(char ch) {
        if (Character.isUpperCase(ch)) {
            ch = Character.toLowerCase(ch);
        }
        return ch - 'a' + 10;
    }

    public static String decimalToDestinationBaseAsString(int number, int radix) {
        return Integer.toString(number, radix);
    }

    //returns -1 if there is no decimal point
    public static int getIndexOfDecimalPoint(String number) {
        for (int i = 0; i < number.length(); i++) {
            if (number.charAt(i) == '.') {
                return i;
            }
        }
        return -1;
    }

    public static double convertFractionToBaseTen(String inputNumber, int sourceRadix) {
        double fractionalPartInBaseTen = 0;
        int powerOfSourceRadix = 1;
        int indexOfDecimal = getIndexOfDecimalPoint(inputNumber);

        for (int i = indexOfDecimal + 1; i < inputNumber.length(); i++) {
            char ch = inputNumber.charAt(i);
            int valueOfChar = 0;

            if (Character.isLetter(ch)) {
                valueOfChar = charToDecimal(ch);
            } else {
                valueOfChar = Character.getNumericValue(ch);
            }
            fractionalPartInBaseTen += (valueOfChar / Math.pow(sourceRadix, powerOfSourceRadix));
            powerOfSourceRadix++;
        }
        return fractionalPartInBaseTen;
    }

    public static boolean isValidateInput(int sourceRadix, String inputNumberAsString, int destinationRadix) {
        if (!checkRadix(sourceRadix) || !checkRadix(destinationRadix)) {
            System.out.println("Error: radix must be greater than 0 and less than 37");
            return false;
        }
        if (!checkInputNumberAsString(sourceRadix, inputNumberAsString)) {
            System.out.println("Error: input number contains characters outside of source radix");
            return false;
        }
        return true;
    }

    public static boolean checkRadix(int radix) {
        return radix > 0 && radix < 37;
    }

    public static boolean checkInputNumberAsString(int sourceRadix, String inputNumberAsString) {
        if (sourceRadix == 1) {
            for (char ch : inputNumberAsString.toCharArray()) {
                if (ch != '1') {
                    return false;
                }
            }
        } else {
            for (int i = 0; i < inputNumberAsString.length(); i++) {
                char ch = inputNumberAsString.charAt(i);
                int valueOfChar = 0;
                if (Character.isLetter(ch)) {
                    valueOfChar = charToDecimal(ch);
                } else {
                    valueOfChar = Character.getNumericValue(ch);
                }

                if (valueOfChar >= sourceRadix) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int readInteger(Scanner scanner) {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            return -1;
        }
    }

    public static String readString(Scanner scanner) {
        return scanner.next();
    }
}

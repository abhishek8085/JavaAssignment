/**
 * This program will determine all possible subsets for given number.
 *
 * @version 1.0 : Subsets.java, 2015/09/06
 * @author Abhishek Indukumar
 * 
 *
 */
class Subsets {
    /**
     *
     * @param args command line arguments
     *             "n number"
     */
    public static void main(String[] args) {
        int number = Integer.parseInt(args[0]) - 1;
        String[] array = new String[number + 1];

        //Create n elements in array
        for (int i = 0; i <= number; i++) {
            array[i] = ((Integer) (i + 1)).toString();
        }

        createAndDisplaySubset(array);
    }

    /**
     * @param array Array of n natural numbers
     */
    private static void createAndDisplaySubset(String[] array) {
        int numberOfSubsets = (int) Math.pow(2, array.length);

        System.out.print("{ ");
        for (int i = 0; i < numberOfSubsets; i++) {
            printActiveIndex(appendZeroes(array.length, toBinary(i)), array);

            //Don't print "," for last element
            if (i < numberOfSubsets - 1) {
                System.out.print(", ");
            }
        }
        System.out.println(" }");
    }

    /**
     * Appends zeroes until binary string reaches
     * total length
     *
     * @param totalLength  The total length of output string
     * @param binaryString The input string to which zeroes need to be appended
     * @return
     */
    private static String appendZeroes(int totalLength, String binaryString) {
        String tempString = "";
        int numberOfZeros = totalLength - binaryString.length();
        for (int i = 0; i < numberOfZeros; i++) {
            tempString += "0";
        }
        return tempString + binaryString;
    }

    /**
     * Prints the indexes of string array which corresponds to
     * character "1" in binary string given.
     *
     * @param binaryString Contains "1"s and "0"s character
     * @param array        Array of Strings(elements of sets)
     */
    private static void printActiveIndex(String binaryString, String[] array) {
        System.out.print("{");
        int counter = 0;
        for (char index : binaryString.toCharArray()) {
            if ((index + "").equals("1")) {
                System.out.print(array[counter]);
            }
            counter++;
        }
        System.out.print("}");
    }

    /**
     * Converts integer to binary notation
     *
     * @param integer integer to be  converted
     * @return String of binary representation
     * on input integer.
     */
    private static String toBinary(int integer) {
        int tempInteger = integer;
        String tempBinaryString = "";
        String binaryString = "";
        while (tempInteger != 0) {
            tempBinaryString += tempInteger % 2 == 0 ? "0" : "1";
            tempInteger = tempInteger / 2;
        }

        for (int i = tempBinaryString.length(); i > 0; i--) {
            binaryString += tempBinaryString.charAt(i - 1);
        }

        return binaryString;
    }
}

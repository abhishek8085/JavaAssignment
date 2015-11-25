import java.util.*;

import static java.lang.Math.sqrt;

class OnceOrMany {

    public static boolean singelton(String literal, String aNewString) {
        return (literal == aNewString);
    }

    public static void main(String args[]) {
        String aString = "xyz";
        //
        System.out.println("1.  xyz == aString: " + "xyz" == aString);
        System.out.println("2.  xyz == aString: " + ("xyz" == aString));

        String newString = new String("xyz");
        System.out.println("xyz == new String(xyz)\n    " + ("xyz" == newString));

        System.out.println("1: " + singelton("xyz", "xyz"));
        System.out.println("2: " + singelton("xyz", new String("xyz")));
        System.out.println("3: " + singelton("xyz", "xy" + "z"));
        System.out.println("4: " + singelton("x" + "y" + "z", "xyz"));
        System.out.println("5: " + singelton("x" + "y" + new String("z"), "xyz"));
        System.out.println("6: " + singelton("x" + ("y" + "z"), "xyz"));
        System.out.println("7: " + singelton('x' + ("y" + "z"), "xyz"));
    }
}




/**
 *
 * n: is a integer in the range between 2 and 100000 m: is the mirror of n
 * (n=73, m=37) bN: is the binary representation of n bM: is the binary
 * representation of m
 *
 *
 * This Program prints number that satisfy below criteria:
 *
 *
 * n is the k.st prime number (73 is the 21. prime number) m is mirror of k.st
 * prime number (37 is the 12. prime number) bN is a palindrome
 *
 * @version 1.0 : Numbers.java, 2015/08/30
 * @author Abhishek Indukumar
 * 
 *
 */
class Numbers {

    private static Vector<Integer> primeNumber = new Vector<Integer>();
    private static int number=24;

    public static void main(String[] args) {
        System.out.print(number+" = ");
        for (int i = 2; i <= number; i++) {
            boolean prime = true;
            for (int p : primeNumber) {
                double sqrtNumber = sqrt(i);

                if (p > sqrtNumber) {
                    break;
                }
                if (i % p == 0) {
                    prime = false;
                    break;
                }
            }

            if (prime) {
                primeNumber.add(i);
            }
        }
        factorise(primeNumber,number);
    }

    private static void factorise( Vector<Integer> primeNumber, int number)
    {
        StringBuilder sb = new StringBuilder();
        int copyNumber = number;
        for ( int prime : primeNumber )
        {
            while ( copyNumber % prime == 0 )
            {
                copyNumber = copyNumber / prime;
                sb.append(prime+" * ");
            }
        }
        sb.setLength(sb.length()-2);
        System.out.println(sb.toString());
    }


    /**
     *
     * @param number
     *            any number
     * @return reverse of the number
     */
    public static Integer reverseNumber(Integer number) {
        return Integer.parseInt(reverseString(number.toString()));
    }

    /**
     *
     * @param string
     *            any string
     * @return reverse of the string
     */
    public static String reverseString(String string) {
        StringBuffer stringBuffer = new StringBuffer(string);
        return stringBuffer.reverse().toString();
    }
}

/**
 * Stores all prime numbers
 *
 * @author Abhishek Indukumar
 * 
 */
class PrimeNumberStore {
/*    private Vector<Integer> primeNumbers= new Vector<Integer>();

    *//**
     * This method stores prime numbers
     *
     * @param number
     *            prime number
     * @param position
     *            position of prime number
     *//*
    public void addPrimeNumberToStore(int number, int position) {
        primeNumbersVsPosition.put(number, position);
    }

    *//**
     *
     * @return all prime numbers
     *//*
    public Set<Integer> getPrimeNumbers() {
        return primeNumbersVsPosition.keySet();
    }

    *//**
     * check given prime number is in store
     *
     * @param number
     *            any number
     * @return true if the prime number is in store
     *//*
    public boolean isPrimeNumberInStore(int number) {
        return primeNumbersVsPosition.containsKey(number);
    }

    *//**
     *
     * @param number
     *            prime number
     * @return position of prime number
     *//*
    public int getPositionOfPrime(int number) {
        return primeNumbersVsPosition.get(number);
    }*/
}

class Subset
{
    static int  number=2;
    static String[] array = new String[number+1];

    public static void main(String[] args)
    {
        for ( int i=0; i <= number; i++)
        {
            array[i]=((Integer)(i+1)).toString();
        }
        createSubset("", array);
    }


    private static void createSubset( String activeIndexes, String[] array )
    {
        for ( int i = 0; i<Math.pow(2,array.length); i++ )
        {
            printActiveIndex(appendZeroes(array.length, Integer.toBinaryString(i)),array);
        }

    }

    private static String appendZeroes( int totalLength, String binaryString )
    {
        int numberOfZeros = totalLength - binaryString.length();
        StringBuilder sb = new StringBuilder();
        for ( int i=0; i< numberOfZeros; i++)
        {
            sb.append("0");
        }
        return sb.append(binaryString).toString();
    }

    private static void  printActiveIndex( String binaryString, String[] array )
    {
        int counter=0;
        for ( char index : binaryString.toCharArray() )
        {
            if( (index+"").equals("1") )
            {
                System.out.print(array[counter]);
            }
            counter++;
        }
        System.out.println("");
    }
}


import java.util.Vector;
import static java.lang.Math.sqrt;

/**
 *
 * This program prints all prime factors for given input number
 *
 * @version 1.0 : Factorization.java, 2015/09/06
 * @author Abhishek Indukumar
 * 
 *
 */
public class Factorization {
    /**
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        int number = Integer.parseInt(args[0]);
        System.out.print(number + " = ");
        factorise(buildPrimeNumberList( number ), number);
    }


    /**
     * This build the prime number list upto
     * the square root of input number.
     *
     * @param number input number
     * @return vector of prime numbers
     */
    private static Vector<Integer> buildPrimeNumberList(int number) {
        Vector<Integer> primeNumber = new Vector<Integer>();
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
        return primeNumber;
    }

    /**
     * This method factorize the given input number
     * based on available primes in the Vector.
     *
     *
     * @param primeNumbers List of all prime numbers upto
     *                    square root of the input number
     * @param number Number to factorise
     */
    private static void factorise(Vector<Integer> primeNumbers, int number) {
        String tempString="";
        int copyNumber = number;
        for (int primeNumber : primeNumbers) {
            //Try to divide by same prime number
            //until possible, if not possible
            //increment prime number
            while (copyNumber % primeNumber == 0) {
                copyNumber = copyNumber / primeNumber;
                tempString+=primeNumber + " * ";
            }
        }
        System.out.println(tempString.substring(0,tempString.length()-2));
    }
}

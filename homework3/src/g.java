
import java.util.Vector;	// what does this line do?
//This line imports Vector class,
//which is present in the package
//java.util


/**
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : Calculator.java, 2015/09/11
 */
class ConstantOrNot {

    private final int aInt = 1;
    private final String aString = "abc";
    private final Vector aVector = new Vector();

    public void doTheJob() {
        // aInt = 3; why would this fail?
        //Ans: As aInt is declared as final
        //     you will not be allowed to change
        //     the value of the variable aInt
        //     as it has already been initialised with value 1.


        // aString = aString + "abc"; why would this fail?
        //Ans: As the object reference aString is final,
        //     java doesn't allow to change the reference
        //     after initial referencing.



        aVector.add("abc");		// why does this work?
        //Ans: This works because not changing object
        //     reference, instead using the object reference
        //     That is Vector Object reference to add string to
        //     Vector.
    }

    public static void main( String args[] ) {
        new ConstantOrNot().doTheJob();

    }
}

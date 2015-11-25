/**
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : T_2.java, 2015/10/16
 */
public class T_2 extends Thread    {
    int id = 1;
    static String  theValue  = "1";
    T_2(int id)       {
        this.id = id;
    }
    public void run () {
        if ( id == 1 )
                theValue = "3";
        else
                theValue = "2";
    }      
        
    public static void main (String args []) {
        new T_2(1).start();;
        new T_2(2).start();;
            
        System.out.println("theValue = " + theValue );
        System.out.println("theValue = " + theValue );
    }       
}


/*
Answer:
        theValue = 1
        theValue = 1
        If T_2("1") and T_2("2")  are not started yet and the Main Thread
        prints theValue.

        theValue = 1
        theValue = 2
        If T_2("1") and T_2("2")  are not started yet and the Main Thread prints
        theValue for first time and before the Main Thread printing theValue
        second time T_2(2) updates theValue.

        theValue = 2
        theValue = 2
        If only T_2("2") gets started before T_2("1") then theValue which is
        2 is printed.

        theValue = 1
        theValue = 3
        If T_2("1") and T_2("2")  are not started yet and the Main Thread prints
        theValue for first time and before the Main Thread printing theValue
        second time T_2(1) updates theValue.

        theValue = 3
        theValue = 3
        If only T_2("1") gets Started before T_2("2") then theValue which is
        3 is printed.

        theValue = 2
        theValue = 3
        if T_2("2") is Started before printing the first print statement and
        then T_2("1") is Started before printing second print statement.

        theValue = 3
        theValue = 2
        if T_2("1") is Started before printing the first print statement and
        then T_2("2") is Started before printing second print statement.

 */

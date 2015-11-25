class X {

    public static void main(String args[]) {

        int n = 0;

        here:

        // loop until break is executed
        while (true) {


            while (((n != 3) || (n != 5)) && (n < 4)) {


                /*First Run: ++n will not be equal to 0
                  as n is pre-incremented to 1
                  Second Run: ++n will not be equal to 0
                  as n is pre-incremented to 3
                */
                if (++n == 0)

                    /*First Run: N/A
                      Second Run: N/A
                     */
                    System.out.println("a/	n is " + n);


                /*First Run: "n++ == 1" will be true as n=1
                 and n will be incremented to 2
                 Second Run: "n++ == 1" will be false as
                 n=3 and n will now be increment to 4
                */
                else if (n++ == 1) {


                    /*
                       First Run:here "b/	n is 2" will be printed
                       as it was post incremented
                       Second Run: N/A
                     */
                    System.out.println("b/	n is " + n);
                }

                /*First Run: N/A
                  Second Run: "n++ == 1" will be false as
                  n=4 and n will now be increment to 5
                */
                else if (n++ == 2)

                    /*First Run: N/A
                      Second Run: N/A
                    */
                    System.out.println("c/	n is " + n);

                /*First Run: N/A
                  Second Run: Will execute as all other cases are false
                */
                else

                    /*First Run: N/A
                      Second Run: "d/ n is 5" will be printed
                    */
                    System.out.println("d/	n is " + n);

                /**
                 * First Run: This will be executed
                 * Second Run: This will be executed
                 */
                System.out.println("	executing break here");

            }

            /*
            If n =5
             */
            System.out.println(n % 2 == 0 ?
                    (n == 4 ? "=" : "!")
                    : (n % 3 != 0 ? "3" : "!3"));
            break here;
        }
    }
}

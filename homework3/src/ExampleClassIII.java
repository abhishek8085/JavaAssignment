
 class  XX	{
    int instanceV = 1;

    static XX bbbbb;
    static XX aaaaaa;

    public XX()	{
    }
    public void method(int i){
        instanceV = i;
    }

    public String toString() {
        return "instanceV = " + instanceV;
    }

    public void m2(int i){
        aaaaaa.method(-9);
        method(12);
        System.out.println("-----------------------------------------");
        System.out.println("print itself : " + this);
        System.out.println("print aaaaaa: " + aaaaaa);
        System.out.println("=========================================");
    }

    public static void main(String args[] )	{
        bbbbb = new XX();
        aaaaaa = new XX();

        bbbbb.m2(3);
        aaaaaa.m2(24);
    }
}




 class  XXX	{
     int oI = 1;

     XXX aXXX = new XXX();;

     public XXX() {
     }
     public XXX(XXX aXXX)	{
         this.aXXX = aXXX;
     }
     public void m1(int i){
         oI = i;
     }
     public void m2(int i){
         oI = i;
         m1(12);
         aXXX.m1(24);
         System.out.println("om: " + this);
         System.out.println("	oI " + oI);
         System.out.println("aXXX.om: " + aXXX);
         System.out.println("	aXXX.oI " + aXXX.oI);
     }

     public static void main(String args[] )	{
         XXX aaXXX = new XXX(new XXX() );

         aaXXX.m2(3);
     }
 }

























 /**
  * This class implements a point test program.
  *
  * @version   $Id$
  *
  * @author    hp bischof
  *
  * Revisions:
  *	$Log$
  */



 class TestPoint {
     private static Point aPoint;


     /**
      * The main program.
      *
      * @param    args    command line arguments (ignored)
      */
     public static void main(String args[])
     {
         System.out.println("Point.soManyPoints = " + Point.soManyPoints() );
         aPoint = new Point(2, 3);
         System.out.println("x = " + aPoint.getX() );
         System.out.println("y = " + aPoint.getY() );

         aPoint = new Point();
         aPoint.initPoint(4, 5);
         System.out.println("x = " + aPoint.getX() );
         System.out.println("y = " + aPoint.getY() );

         aPoint.move(6, 7);
         System.out.println("x = " + aPoint.getX() );
         System.out.println("y = " + aPoint.getY() );

         System.out.println("nPoints = " + aPoint.getNPoints() );
         System.out.println("aPoint.soManyPoints = " + aPoint.soManyPoints() );
     }
 }













 /**
  * This class implements a point in a two dimensional
  * area.
  * All method print when they are called.
  *
  * @version   $Id$
  *
  * @author    hp bischof
  *
  * Revisions:
  *	$Log$
  */

 class Point {
     // class variable
     static int nPoints;		// so many points were created.

     private int x;		// x coordinate of the point
     private int y;		// y cooridnate of the point

     /**
      * Default Constructor.
      * Increases the counter nPoints by 1.
      *
      * @return	      Point a Point object
      */
     public Point(){
         super();
         System.out.println("	in Point() constructor");
         nPoints ++;
     }

     /**
      * Constructor.
      * initialize x and y values of a point
      *
      * @param       x	x coordinate
      * @param       y	y coordinate
      *
      * @return	      Point a Point object
      */
     public Point(int x, int y){
         super();
         int i ;
         this.x = x;
         this.y = y;
         System.out.println("	in Point(int, int) constructor");
     }

     /**
      * So many points have been created.
      *
      * @return int So many points have been created
      */
     public static int soManyPoints(){
         return nPoints;
     }

     /**
      * initialzes x and y of a point.
      *
      * @param       x	int x coordinate
      * @param       y	int y coordinate
      *
      * @return	      Point a Point object
      */
     public Point initPoint(int x, int y){
         System.out.println("	in initPoint(int, int)");

         this.x = x;
         this.y = y;

         return this;
     }

     /**
      * move a point
      *
      * @param       x	int delta x value
      * @param       y	int delta y value
      *
      * @return	      Point a Point object
      */
     public Point move(int x, int y){
         System.out.println("	in move(int, int)");

         this.x += x;
         this.y += y;

         return this;
     }

     /**
      * Returns the x coordinate of a point
      *
      * @return	      int x value
      */
     public int getX(){
         System.out.println("	in getX()");
         return this.x;
     }

     /**
      * Returns the y coordinate of a point
      *
      * @return	      int x value
      */
     public int getY(){
         System.out.println("	in getY()");
         return this.y;
     }

     /**
      * Returns how many points are created so far.
      *
      * @return	      int nPoints
      */
     public int getNPoints(){
         System.out.println("	in getNPoints()");
         return this.nPoints;
     }
 }





 /**
  * This class shows how to override a method.
  * @version   $Id$
  *
  * @author    hp bischof
  *
  * Revisions:
  *	$Log$
  */

 class OverWriteTop {

     public static int var;

     public void both(int x)	{
         var = x;
         System.out.println("	in OverWriteTop!both");
     }

     public void notBoth(int x)	{
         var = x;
         System.out.println("	in OverWriteTop!notBoth");
     }

 }






 /**
  * This class shows how polymorphism can be used.
  * @version   $Id$
  *
  * @author    hp bischof
  *
  * Revisions:
  *	$Log$
  */

 class OverWrite extends OverWriteTop {

     public static int var;

     public void both(int x)	{
         var = x;
         System.out.println("	in OverWrite!both");
     }


     public static void main(String args[])
     {
         OverWrite aOverWrite = new OverWrite();
         aOverWrite.notBoth(42);
         aOverWrite.both(84);
         System.out.println("OverWrite.var    = " + OverWrite.var );
         System.out.println("OverWriteTop.var = " + OverWriteTop.var );

     }
 }



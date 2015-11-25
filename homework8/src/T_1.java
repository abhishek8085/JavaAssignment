/**
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : T_1.java, 2015/10/16
 */
public class T_1 extends Thread	{
	static int x = 1;
	String info = "";

	public T_1 (String info) {
		this.info = info;
		x++;
	}

	public void run () {
		x++;
		String output = x + " " + info + ": " + x;
		System.out.println(output);
	}

	public static void main (String args []) {
		new T_1("a").start();
		new T_1("b").start();
	}
}

/*
Answer:
		4 a: 4
		5 b: 5
		If T_1("a") and T_1("b")  are instantiated and T_1("a")
		runs through the --String output = x + " " + info + ": " + x;--
		without interruption from other threads And T_1("b")
		runs through the --String output = x + " " + info + ": " + x;--
		without interruption from other threads.


		4 a: 5
		5 b: 5
		If T_1("a") and T_1("b") are instantiated and T_1("a")
		runs through the --String output = x ;--
		but while executing second part i.e. --" " + info + ": " + x--
		T_1("b") increments x

		4 b: 4
		5 a: 5
		If T_1("a") and T_1("b") are instantiated and T_1("b") first and
		runs through the --String output = x + " " + info + ": " + x;--
		without interruption from other threads And T_1("a") next
		runs through the --String output = x + " " + info + ": " + x;--
		without interruption from other threads.


		4 b: 5
		5 a: 5
		If T_1("a") and T_1("b") are instantiated and T_1("b")
		runs through the --String output = x ;--
		but while executing second part i.e. --" " + info + ": " + x--
		T_1("a") increments x


		3 a: 3
		5 b: 5
		If T_1("a") is instantiated and T_1("a") first
		runs through the --String output = x + " " + info + ": " + x;--
		without interruption from other threads And then T_1("b") is instantiated
		runs through the --String output = x + " " + info + ": " + x;--
		without interruption from other threads.
 */
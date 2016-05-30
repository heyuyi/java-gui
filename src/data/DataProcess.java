/**
 * 
 */
package data;

/**
 * @author heyuyi
 *
 */
public class DataProcess {

	public static class Pair {
		public double x = 0;
		public double y = 0;
		public Pair() {
			// TODO Auto-generated constructor stub
		}
	}
	
	public static Pair calcLMS(Pair[] l) {
		double sumx = 0, sumy = 0, sumxy = 0, sumxx = 0;
		int n = l.length;
		Pair r = new Pair();
		for (int i = 0; i < n; ++i) {
			sumx += l[i].x;
			sumy += l[i].y;
			sumxx += l[i].x * l[i].x;
			sumxy += l[i].x * l[i].y;
		}
		r.x = (sumxy - (sumx*sumy/n)) / (sumxx - (sumx*sumx/n));
		r.y = (sumy - r.x*sumx) / n;
		return r;
	}
	
	public static double calcDelta(Pair[] l1, Pair[] l2) {
		Pair r1 = calcLMS(l1);
		Pair r2 = calcLMS(l2);
		double x = (l1[l1.length-1].x + l2[0].x) / 2;
		return (r1.x*x + r1.y) - (r2.x*x + r2.y);
	}
}

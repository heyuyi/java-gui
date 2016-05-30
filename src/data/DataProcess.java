/**
 * 
 */
package data;

/**
 * DataProcess类为数据处理基本算法
 * @author heyuyi
 *
 */
public class DataProcess {

	/**
	 * Pair类表示数据点
	 * @author heyuyi
	 *
	 */
	public static class Pair {
		public double x = 0;
		public double y = 0;
		public Pair() {
			// TODO Auto-generated constructor stub
		}
	}
	
	/**
	 * 最小二乘法直线拟合
	 * @param l 数据集
	 * @return 拟合结果
	 */
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
	
	/**
	 * 配平所需计算，两组数据点分别直线拟合，然后计算他们之间的差
	 * @param l1，l2 数据集
	 * @return 计算结果
	 */
	public static double calcDelta(Pair[] l1, Pair[] l2) {
		Pair r1 = calcLMS(l1);
		Pair r2 = calcLMS(l2);
		double x = (l1[l1.length-1].x + l2[0].x) / 2;
		return (r1.x*x + r1.y) - (r2.x*x + r2.y);
	}
}

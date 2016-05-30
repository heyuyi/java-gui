/**
 * 
 */
package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.InputEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;

/**
 * CurveChart类为显示曲线图，继承ChartPanel，包含一个JFreeChart，
 * 显示一副XYLineChart，根据得到的数据动态改变显示的数据。
 * 使用多个Crosshair，实现用户选取数据点的功能。
 * @author heyuyi
 *
 */
class CurveChart extends ChartPanel implements ChartMouseListener {
	
	/**
	 * CurveChart类成员
	 */
	private static final long serialVersionUID = 1L;
	private static final int SIZE = 200;
	private JFreeChart chart;
	private Crosshair[] xCrosshair = { 
			new Crosshair(Double.NaN, Color.WHITE, new BasicStroke(0f)),
			new Crosshair(Double.NaN, Color.BLACK, new BasicStroke(0f)),
			new Crosshair(Double.NaN, Color.YELLOW, new BasicStroke(0f)),
			new Crosshair(Double.NaN, Color.BLUE, new BasicStroke(0f)),
			new Crosshair(Double.NaN, Color.WHITE, new BasicStroke(0f)),
			new Crosshair(Double.NaN, Color.BLACK, new BasicStroke(0f)),
			new Crosshair(Double.NaN, Color.YELLOW, new BasicStroke(0f)),
			new Crosshair(Double.NaN, Color.BLUE, new BasicStroke(0f)),
	};
	private Crosshair[] yCrosshair = { 
			new Crosshair(Double.NaN, Color.WHITE, new BasicStroke(0f)),
			new Crosshair(Double.NaN, Color.BLACK, new BasicStroke(0f)),
			new Crosshair(Double.NaN, Color.YELLOW, new BasicStroke(0f)),
			new Crosshair(Double.NaN, Color.BLUE, new BasicStroke(0f)),
			new Crosshair(Double.NaN, Color.WHITE, new BasicStroke(0f)),
			new Crosshair(Double.NaN, Color.BLACK, new BasicStroke(0f)),
			new Crosshair(Double.NaN, Color.YELLOW, new BasicStroke(0f)),
			new Crosshair(Double.NaN, Color.BLUE, new BasicStroke(0f)),
	};
	private CrosshairOverlay[] overlay = { 
			new CrosshairOverlay(), new CrosshairOverlay(),
			new CrosshairOverlay(), new CrosshairOverlay(),
			new CrosshairOverlay(), new CrosshairOverlay(),
			new CrosshairOverlay(), new CrosshairOverlay() };
	private List<Double> data = new ArrayList<Double>();
	private double[] queue = new double[SIZE];
	private int overlayCnt = 0;
	private boolean IsCollecting = false;
	
	/**
	 * CurveChart类构造函数
	 */
	public CurveChart() {
		super(null, true);
		chart = createChart();
		setChart(chart);
//		setMouseZoomable(false);
		setPopupMenu(null);
		
		for (int i = 0; i < 8; ++i) {
			xCrosshair[i].setLabelAnchor(RectangleAnchor.BOTTOM_RIGHT);
			yCrosshair[i].setLabelAnchor(RectangleAnchor.TOP_RIGHT);
			xCrosshair[i].setLabelVisible(true);
			yCrosshair[i].setLabelVisible(true);
			overlay[i].addDomainCrosshair(xCrosshair[i]);
	        overlay[i].addRangeCrosshair(yCrosshair[i]);
		}
	}
	
	/**
	 * 创建XYLineChart
	 * @return 创建的JFreeChart
	 */
	private JFreeChart createChart() {
		JFreeChart chart = ChartFactory.createXYLineChart(null, null, null, 
				createCollectStatusDataset(), PlotOrientation.VERTICAL, false, false, false);
		XYPlot plot = chart.getXYPlot();
		plot.setDomainGridlinesVisible(false);
		plot.setRangeGridlinesVisible(false);
		
        ValueAxis xAxis = plot.getDomainAxis();
        xAxis.setVisible(false);
        xAxis.setLowerMargin(0.0);
		xAxis.setUpperMargin(0.0);
        
		NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
//		yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        yAxis.setAutoRangeIncludesZero(false);
		return chart;
	}
	
	/**
	 * 创建数据采集时的Dataset，其中仅200个点
	 * @return 创建的XYDataset
	 */
	private XYDataset createCollectStatusDataset() {
		XYSeries series = new XYSeries("");		
		for (int i = 0; i < SIZE; ++i)
			series.add(i, queue[i]);
		return new XYSeriesCollection(series);
	}
	
	/**
	 * 创建数据处理时的Dataset，包含了之前记录的所有的点
	 * @return 创建的XYDataset
	 */
	private XYDataset createProcessStatusDataset() {
		XYSeries series = new XYSeries("");
		for (int i = 0; i < data.size(); ++i)
			series.add(i, data.get(i));
		return new XYSeriesCollection(series);
	}
	
	/**
	 * 增加一条采集的数据
	 * @param y 采集到的数据
	 */
	public void addData(double y) {
		for (int i = 0; i < (SIZE-1); ++i)
			queue[i] = queue[i+1];
		queue[SIZE-1] = y;
		restoreAutoBounds();
		chart.getXYPlot().setDataset(createCollectStatusDataset());
		if (IsCollecting)
			data.add(y);
	}
	
	/**
	 * 清图函数
	 */
	public void clear() {
		IsCollecting = false;
		data.clear();
		for (int i = 0; i < SIZE; ++i)
			queue[i] = 0;
//		chart.getXYPlot().setDataset(createCollectStatusDataset());
	}
	
	/**
	 * 开始数据记录
	 */
	public void collect() {
		IsCollecting = true;
		for (int i = 0; i < SIZE; ++i)
			data.add(queue[i]);
	}
	
	/**
	 * 设置数据采集状态
	 */
	public void dataCollectStatus() {
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		removeChartMouseListener(this);
		for (int i = 0; i < 8; ++i)
			removeOverlay(overlay[i]);
		overlayCnt = 0;
	}
	
	/**
	 * 设置数据处理状态
	 */
	public void dataProcessStatus() {
		restoreAutoBounds();
		chart.getXYPlot().setDataset(createProcessStatusDataset());
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		
		XYPlot plot = chart.getXYPlot();
		double x = Double.NaN;
		double y = DatasetUtilities.findYValue(plot.getDataset(), 0, x);
		for (int i = 0; i < 8; ++i) {
			xCrosshair[i].setValue(x);
			yCrosshair[i].setValue(y);
			addOverlay(overlay[i]);
		}
        overlayCnt = 0;
        addChartMouseListener(this);
	}
	
	/**
	 * 返回数据处理时用户选取的点集
	 * @return 点集
	 */
	public int[] dataGroup() {
		int[] dg = new int[overlayCnt];
		for (int i = 0; i < overlayCnt; ++i)
			dg[i] = (int) xCrosshair[i].getValue();
		return dg;
	}
	
	/**
	 * 返回模块记录的所有数据
	 * @return 返回数据
	 */
	public List<Double> dataAll() {
		return data;
	}
	
	/**
	 * 清除用户所选取的点集
	 */
	public void clearOverlay() {
		XYPlot plot = chart.getXYPlot();
		double x = Double.NaN;
		double y = DatasetUtilities.findYValue(plot.getDataset(), 0, x);
		int n = Math.min(overlayCnt+1, 8);
		for (int i = 0; i < n; ++i) {
			xCrosshair[i].setValue(x);
			yCrosshair[i].setValue(y);
		}
        overlayCnt = 0;
	}
	
	/**
	 * 鼠标按键响应函数
	 */
	@Override
	public void chartMouseClicked(final ChartMouseEvent e) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int cnt = e.getTrigger().getClickCount();
				if (cnt == 1) {
					int mod = e.getTrigger().getModifiers();
					if ((mod & InputEvent.BUTTON1_MASK) != 0) {
						if (overlayCnt < 8)
							overlayCnt++;
					} else if ((mod & InputEvent.BUTTON3_MASK) != 0) {
						if (overlayCnt > 0) {
							xCrosshair[overlayCnt-1].setValue(xCrosshair[overlayCnt].getValue());
							yCrosshair[overlayCnt-1].setValue(yCrosshair[overlayCnt].getValue());
							double x = Double.NaN;
							double y = DatasetUtilities.findYValue(chart.getXYPlot().getDataset(), 0, x);
							xCrosshair[overlayCnt].setValue(x);
							yCrosshair[overlayCnt].setValue(y);
							overlayCnt--;
							
						}
					} else if((mod & InputEvent.BUTTON2_MASK) != 0)
						restoreAutoBounds();
				} 
			}
		});
	}

	/**
	 * 鼠标移动响应函数
	 */
	@Override
	public void chartMouseMoved(final ChartMouseEvent e) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (overlayCnt < 8) {
					Rectangle2D dataArea = getScreenDataArea();
					XYPlot plot = chart.getXYPlot();
					ValueAxis xAxis = plot.getDomainAxis();
					double x = xAxis.java2DToValue(e.getTrigger().getX(), 
							dataArea, RectangleEdge.BOTTOM);
					if (overlayCnt == 0) {
						if (!xAxis.getRange().contains(x)) { 
							x = Double.NaN;
						} else {
							x = Math.round(x);
						}
						double y = DatasetUtilities.findYValue(plot.getDataset(), 0, x);
						xCrosshair[overlayCnt].setValue(x);
						yCrosshair[overlayCnt].setValue(y);
					} else {
						if (!xAxis.getRange().contains(x)) { 
							x = Double.NaN;
						} else {
							x = Math.round(x);
							if (x < xCrosshair[overlayCnt-1].getValue())
								x = xCrosshair[overlayCnt-1].getValue();
						}
						double y = DatasetUtilities.findYValue(plot.getDataset(), 0, x);
						xCrosshair[overlayCnt].setValue(x);
						yCrosshair[overlayCnt].setValue(y);
					}
				}
			}
		});
	}
}

/**
 * 
 */
package gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import serialcom.SerialCom;

/**
 * DisplayModule类为数据显示模块
 * @author heyuyi
 *
 */
class DisplayModule extends JPanel {
	
	/**
	 * DisplayModule类成员
	 */
	private static final long serialVersionUID = 1L;
	private JLabel label = new JLabel();
	private JTextField txt = new JTextField("0.0");
	private JButton
		connectBtn = new JButton("连接"),
		restartBtn = new JButton("重启"),
		toZeroBtn  = new JButton("归零"),
		displayBtn = new JButton("采集");
	private volatile boolean
		connect = false,
//		restart = false,
//		toZero = false,
		display = false;
	private CurveChart chart;
	private SerialCom com;
	
	/**
	 * 从串口读取数据的线程任务
	 */
	private Runnable run = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (display) {
				com.sendCommand((byte) 0x50);
				try {
//					if (restart)
//						;
//					if (toZero)
//						;
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (com.dataValid() && display) {
							String str = com.read();
							txt.setText(str);
							String[] ss = str.split(" ");
							String s = ss[0];
							for (int i = 1; i < ss.length; ++i) {
								if (ss[i].length() > 1) {
									s += ss[i];
									break;
								}
							}
							try {
								chart.addData(Double.valueOf(s));
							} catch(NumberFormatException e) {
								System.err.println(str);
							}
						}
					}
				});	
			}
		}
	};
	
	/**
	 * DisplayModule类构造函数
	 * @param str 模块名
	 * @param comn 模块所用的串口名
	 */
	public DisplayModule(String str, String comn) {
		
		super();
		label.setText(str);
		label.setFont(FontStyle.font24);
		label.setHorizontalAlignment(JLabel.CENTER);
		txt.setEditable(false);
		txt.setFont(FontStyle.font24);
		txt.setHorizontalAlignment(JTextField.CENTER);
		txt.setBorder(null);
		
		connectBtn.setFont(FontStyle.font20);
		restartBtn.setFont(FontStyle.font20);
		toZeroBtn.setFont(FontStyle.font20);
		displayBtn.setFont(FontStyle.font20);
		connectBtn.setEnabled(false);
		restartBtn.setEnabled(false);
		toZeroBtn.setEnabled(false);
		displayBtn.setEnabled(false);
		
		JPanel
			titlePnl  = new JPanel(),
			buttonPnl = new JPanel();
		
		setLayout(new BorderLayout());
		titlePnl.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;
		gc.gridx = 0; gc.gridy = 0; gc.weightx = 1; gc.weighty = 1;
		titlePnl.add(label, gc);
		gc.gridx = 1; gc.gridy = 0; gc.weightx = 2;
		titlePnl.add(txt, gc);
		add(titlePnl, BorderLayout.NORTH);
		
		GridLayout layout = new GridLayout(1, 4);
		layout.setHgap(5);
		buttonPnl.setLayout(layout);
		buttonPnl.add(connectBtn);
		buttonPnl.add(restartBtn);
		buttonPnl.add(toZeroBtn);
		buttonPnl.add(displayBtn);
		add(buttonPnl, BorderLayout.SOUTH);
		
		com = new SerialCom(comn);
				
		chart = new CurveChart();
		add(chart, BorderLayout.CENTER);
		
		connectBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (connect) {
							try {
								com.close();
								connectBtn.setText("连接");
								displayBtn.setText("采集");
								restartBtn.setEnabled(false);
								toZeroBtn.setEnabled(false);
								displayBtn.setEnabled(false);
								connect = false;
								display = false;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, com.name()+": "+e);
							}
						} else {
							try {
								com.open();
								connectBtn.setText("断开");
								restartBtn.setEnabled(true);
								toZeroBtn.setEnabled(true);
								displayBtn.setEnabled(true);
								connect = true;
							} catch (Exception e) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, com.name()+": "+e);
							}
						}
					}
				});
			}
		});
		
		restartBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
//				if (display) {
//					restart = true;
//				} else {
//					com.sendCommand((byte) 'S');
//				}
			}
		});
		
		toZeroBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
//				if (display) {
//					toZero = true;
//				} else {
//					com.sendCommand((byte) 'T');//V
//				}
			}
		});
		
		displayBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (connect) {
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (display) {
								display = false;
								displayBtn.setText("采集");
							} else {
								chart.clear();
								displayBtn.setText("停止");
								display = true;
								Thread t = new Thread(run);
								t.start();
							}
						}
					});
				}
			}
		});		
	}
	
	/**
	 * 关闭模块串口
	 */
	public void closeSerialCom() {
		try {
			com.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 模块调用者判断是否可以从模块采集数据
	 * @return 判断结果
	 */
	public boolean canDataCollect() {
		return display;
	}
	
	/**
	 * 模块进入数据采集
	 */
	public void startDataCollect() {
		if (display) {
			connectBtn.setEnabled(false);
			restartBtn.setEnabled(false);
			toZeroBtn.setEnabled(false);
			displayBtn.setEnabled(false);
			chart.collect();
		}
	}
	
	/**
	 * 模块进入数据处理
	 */
	public void startDataProcess() {
		try {
			com.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connectBtn.setText("连接");
		displayBtn.setText("采集");
		connect = false;
		display = false;
		chart.dataProcessStatus();
	}
	
	/**
	 * 模块使能连接串口功能
	 */
	public void enableConnect() {
		connectBtn.setEnabled(true);
		chart.dataCollectStatus();
	}
	
	/**
	 * 返回模块采集到的所有数据
	 * @return 返回数据
	 */
	public List<Double> dataAll() {
		return chart.dataAll();
	}
	
	/**
	 * 返回数据处理时用户所选取的点集
	 * @return 点集
	 */
	public int[] dataGroup() {
		return chart.dataGroup();
	}
	
	/**
	 * 清楚用户所选取的点集
	 */
	public void clearSelectData() {
		chart.clearOverlay();
	}
}

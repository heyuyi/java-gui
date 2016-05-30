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
 * DisplayModule��Ϊ������ʾģ��
 * @author heyuyi
 *
 */
class DisplayModule extends JPanel {
	
	/**
	 * DisplayModule���Ա
	 */
	private static final long serialVersionUID = 1L;
	private JLabel label = new JLabel();
	private JTextField txt = new JTextField("0.0");
	private JButton
		connectBtn = new JButton("����"),
		restartBtn = new JButton("����"),
		toZeroBtn  = new JButton("����"),
		displayBtn = new JButton("�ɼ�");
	private volatile boolean
		connect = false,
//		restart = false,
//		toZero = false,
		display = false;
	private CurveChart chart;
	private SerialCom com;
	
	/**
	 * �Ӵ��ڶ�ȡ���ݵ��߳�����
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
	 * DisplayModule�๹�캯��
	 * @param str ģ����
	 * @param comn ģ�����õĴ�����
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
								connectBtn.setText("����");
								displayBtn.setText("�ɼ�");
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
								connectBtn.setText("�Ͽ�");
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
								displayBtn.setText("�ɼ�");
							} else {
								chart.clear();
								displayBtn.setText("ֹͣ");
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
	 * �ر�ģ�鴮��
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
	 * ģ��������ж��Ƿ���Դ�ģ��ɼ�����
	 * @return �жϽ��
	 */
	public boolean canDataCollect() {
		return display;
	}
	
	/**
	 * ģ��������ݲɼ�
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
	 * ģ��������ݴ���
	 */
	public void startDataProcess() {
		try {
			com.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connectBtn.setText("����");
		displayBtn.setText("�ɼ�");
		connect = false;
		display = false;
		chart.dataProcessStatus();
	}
	
	/**
	 * ģ��ʹ�����Ӵ��ڹ���
	 */
	public void enableConnect() {
		connectBtn.setEnabled(true);
		chart.dataCollectStatus();
	}
	
	/**
	 * ����ģ��ɼ�������������
	 * @return ��������
	 */
	public List<Double> dataAll() {
		return chart.dataAll();
	}
	
	/**
	 * �������ݴ���ʱ�û���ѡȡ�ĵ㼯
	 * @return �㼯
	 */
	public int[] dataGroup() {
		return chart.dataGroup();
	}
	
	/**
	 * ����û���ѡȡ�ĵ㼯
	 */
	public void clearSelectData() {
		chart.clearOverlay();
	}
}

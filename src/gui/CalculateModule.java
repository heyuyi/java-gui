/**
 * 
 */
package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import data.DataProcess;

/**
 * CalculateModule类为数据计算模块，包含三个基本功能
 * @author heyuyi
 *
 */
class CalculateModule {

	/**
	 * CalculateModule类成员
	 */
	TextPanel textPanel = new TextPanel();
	CalcPanel1 calcPanel1 = new CalcPanel1();
	CalcPanel2 calcPanel2 = new CalcPanel2();
	private DisplayModule dx, dy;
	private double lxx = 1799, lyy = 1289, rix = 42.25, riy = 27.05, 
			rox = 15.5, roz = 37.3, rrx = 47, rry = 47, theta = 30;
	
	/**
	 * CalcPanel1类为内外框配平功能
	 * @author heyuyi
	 *
	 */
	class CalcPanel1 extends JPanel {

		/**
		 * CalcPanel1类成员
		 */
		private static final long serialVersionUID = 1L;
		private double Fix = 0, Fox = 0, Fiy = 0, Foy = 0;
		private JButton FixBtn = new JButton("Fix");
		private JTextField FixTxt = new JTextField("0.0");
		private JButton FiyBtn = new JButton("Fiy");
		private JTextField FiyTxt = new JTextField("0.0");
		private JButton FoxBtn = new JButton("Fox");
		private JTextField FoxTxt = new JTextField("0.0");
		private JButton FoyBtn = new JButton("Foy");
		private JTextField FoyTxt = new JTextField("0.0");
		private JButton calcBtn = new JButton("计算");
		
		/**
		 * CalcPanel1类构造函数
		 */
		public CalcPanel1() {
			super();
			TitledBorder tb = BorderFactory.createTitledBorder("内外框配平");
		    tb.setTitleJustification(TitledBorder.LEFT);
		    this.setBorder(tb);
		    
		    JPanel panel = new JPanel();
		    panel.setLayout(new GridLayout(5, 2, 20, 5));
		    
		    panel.add(FixBtn);
			FixBtn.setFont(FontStyle.font18);
			panel.add(FixTxt);
			FixTxt.setHorizontalAlignment(JTextField.CENTER);
			FixTxt.setFont(FontStyle.font18);
			
			panel.add(FoxBtn);
			FoxBtn.setFont(FontStyle.font18);
			panel.add(FoxTxt);
			FoxTxt.setHorizontalAlignment(JTextField.CENTER);
			FoxTxt.setFont(FontStyle.font18);
			
			panel.add(FiyBtn);
			FiyBtn.setFont(FontStyle.font18);
			panel.add(FiyTxt);
			FiyTxt.setHorizontalAlignment(JTextField.CENTER);
			FiyTxt.setFont(FontStyle.font18);
			
			panel.add(FoyBtn);
			FoyBtn.setFont(FontStyle.font18);
			panel.add(FoyTxt);
			FoyTxt.setHorizontalAlignment(JTextField.CENTER);
			FoyTxt.setFont(FontStyle.font18);
			
			panel.add(calcBtn);
		    calcBtn.setFont(FontStyle.font18);
			
			setLayout(new GridBagLayout());
			add(panel, new GridBagConstraints(0, 0, 1, 1, 1, 1, 
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
					new Insets(5, 30, 10, 30), 0, 0));
			
			FixBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Fix = calcPart(dx);
							String str = String.format("%.5f", Fix);
							FixTxt.setText(str);
						}
					});
				}
			});
			
			FoxBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Fox = calcPart(dx);
							String str = String.format("%.5f", Fox);
							FoxTxt.setText(str);
						}
					});
				}
			});

			FiyBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub	
							Fiy = calcPart(dy);
							String str = String.format("%.5f", Fiy);
							FiyTxt.setText(str);
						}
					});
				}
			});
			
			FoyBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Foy = calcPart(dy);
							String str = String.format("%.5f", Foy);
							FoyTxt.setText(str);
						}
					});
				}
			});
			
			calcBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							double t = 2 * Math.sin(-theta * Math.PI / 180);
							double a = -Fix*lxx / (t * Math.cos(Math.PI / 4));
							double b = -Fiy*lyy / t;
							double c = Fox*lxx / (t * Math.cos(Math.PI / 4)) - a;
							double d = -Foy*lyy / t;
							double mix = a/rix;
							double miy = b/riy;
							double mox = -c/rox;
							double moz = -d/roz;
							String str1 = String.format("%.5f", mix);
							String str2 = String.format("%.5f", miy);
							String str3 = String.format("%.5f", mox);
							String str4 = String.format("%.5f", moz);
							textPanel.tArea.append("内外框配平结果：\n");
							textPanel.tArea.append("mix: " + str1 + "; miy: " + str2 + "; mox: " + str3 + "; moz: " + str4 + ";\n");
						}
					});
				}
			});
			
			FixTxt.addFocusListener(new FocusAdapter() {

				@Override
				public void focusLost(FocusEvent evt) {
					// TODO Auto-generated method stub
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							double temp  = 0;
							try {
								temp = Double.valueOf(FixTxt.getText());
							} catch (NumberFormatException e) {
								JOptionPane.showMessageDialog(null, "Fix输入数据格式有误！");
								FixTxt.setText(String.format("%.5f", Fix));
							}
							Fix = temp;
						}
						
					});
				}
			});
			
			FiyTxt.addFocusListener(new FocusAdapter() {

				@Override
				public void focusLost(FocusEvent evt) {
					// TODO Auto-generated method stub
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							double temp  = 0;
							try {
								temp = Double.valueOf(FiyTxt.getText());
							} catch (NumberFormatException e) {
								JOptionPane.showMessageDialog(null, "Fiy输入数据格式有误！");
								FiyTxt.setText(String.format("%.5f", Fiy));
							}
							Fiy = temp;
						}
						
					});
				}
			});
			
			FoxTxt.addFocusListener(new FocusAdapter() {

				@Override
				public void focusLost(FocusEvent evt) {
					// TODO Auto-generated method stub
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							double temp  = 0;
							try {
								temp = Double.valueOf(FoxTxt.getText());
							} catch (NumberFormatException e) {
								JOptionPane.showMessageDialog(null, "Fox输入数据格式有误！");
								FoxTxt.setText(String.format("%.5f", Fox));
							}
							Fox = temp;
						}
					});
				}
			});
			
			FoyTxt.addFocusListener(new FocusAdapter() {

				@Override
				public void focusLost(FocusEvent evt) {
					// TODO Auto-generated method stub
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							double temp  = 0;
							try {
								temp = Double.valueOf(FoyTxt.getText());
							} catch (NumberFormatException e) {
								JOptionPane.showMessageDialog(null, "Foy输入数据格式有误！");
								FoyTxt.setText(String.format("%.5f", Foy));
							}
							Foy = temp;
						}
					});
				}
			});
		}
	}
	
	/**
	 * CalcPanel2为消旋配平功能
	 * @author heyuyi
	 *
	 */
	class CalcPanel2 extends JPanel {

		/**
		 * CalcPanel2类成员
		 */
		private static final long serialVersionUID = 1L;
		private double Frx = 0, Fry = 0;
		private JButton FrxBtn = new JButton("Frx");
		private JTextField FrxTxt = new JTextField("0.0");
		private JButton FryBtn = new JButton("Fry");
		private JTextField FryTxt = new JTextField("0.0");
		private JButton calcBtn = new JButton("计算");
		
		/**
		 * CalcPanel2类构造函数
		 */
		public CalcPanel2() {
			super();
			TitledBorder tb = BorderFactory.createTitledBorder("消旋配平");
		    tb.setTitleJustification(TitledBorder.LEFT);
		    this.setBorder(tb);
		    
		    JPanel panel = new JPanel();
		    panel.setLayout(new GridLayout(5, 2, 20, 5));
		    
		    panel.add(FrxBtn);
			FrxBtn.setFont(FontStyle.font18);
			panel.add(FrxTxt);
			FrxTxt.setHorizontalAlignment(JTextField.CENTER);
			FrxTxt.setFont(FontStyle.font18);
		    
			panel.add(FryBtn);
			FryBtn.setFont(FontStyle.font18);
			panel.add(FryTxt);
			FryTxt.setHorizontalAlignment(JTextField.CENTER);
			FryTxt.setFont(FontStyle.font18);
			
			panel.add(new Panel());panel.add(new Panel());
			panel.add(new Panel());panel.add(new Panel());
			
			panel.add(calcBtn);
		    calcBtn.setFont(FontStyle.font18);
			
			setLayout(new GridBagLayout());
			add(panel, new GridBagConstraints(0, 0, 1, 1, 1, 1, 
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
					new Insets(5, 30, 10, 30), 0, 0));
			
			FrxBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub	
							Frx = calcPart(dx);
							String str = String.format("%.5f", Frx);
							FrxTxt.setText(str);
						}
					});
				}
			});
			
			FryBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Fry = calcPart(dy);
							String str = String.format("%.5f", Fry);
							FryTxt.setText(str);
						}
					});
				}
			});
			
			calcBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							double t = 2 * Math.sin(-theta * Math.PI / 180);
							double a = -Frx*lxx/t;
							double b = -Fry*lyy/t;
							double mrx = a/rrx;
							double mry = b/rry;
							String str1 = String.format("%.5f", mrx);
							String str2 = String.format("%.5f", mry);
							textPanel.tArea.append("消旋配平结果：\n");
							textPanel.tArea.append("mrx: " + str1 + "; mry: " + str2 + ";\n");
						}
					});
				}
			});
			
			FrxTxt.addFocusListener(new FocusAdapter() {

				@Override
				public void focusLost(FocusEvent evt) {
					// TODO Auto-generated method stub
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							double temp  = 0;
							try {
								temp = Double.valueOf(FrxTxt.getText());
							} catch (NumberFormatException e) {
								JOptionPane.showMessageDialog(null, "Frx输入数据格式有误！");
								FrxTxt.setText(String.format("%.5f", Frx));
							}
							Frx = temp;
						}
					});
				}
			});
			
			FryTxt.addFocusListener(new FocusAdapter() {

				@Override
				public void focusLost(FocusEvent evt) {
					// TODO Auto-generated method stub
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							double temp  = 0;
							try {
								temp = Double.valueOf(FryTxt.getText());
							} catch (NumberFormatException e) {
								JOptionPane.showMessageDialog(null, "Fry输入数据格式有误！");
								FryTxt.setText(String.format("%.5f", Fry));
							}
							Fry = temp;
						}
					});
				}
			});
		}
	}
	
	/**
	 * TextPanel类为计算结果文本显示功能
	 * @author heyuyi
	 *
	 */
	class TextPanel extends JPanel {

		/**
		 * TextPanel类成员
		 */
		private static final long serialVersionUID = 1L;
		private JTextArea tArea = new JTextArea();
		
		/**
		 * TextPanel类构造函数
		 */
		public TextPanel() {
			super();
			TitledBorder tb = BorderFactory.createTitledBorder("状态显示");
		    tb.setTitleJustification(TitledBorder.LEFT);
		    this.setBorder(tb);
		    
		    tArea.setEditable(false);
		    tArea.setFont(FontStyle.font14);
		    tArea.setLineWrap(true);
		    tArea.setWrapStyleWord(true);
		    
			setLayout(new GridBagLayout());
			add(new JScrollPane(tArea), new GridBagConstraints(0, 0, 1, 1, 1, 1, 
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
					new Insets(5, 10, 10, 10), 0, 0));
			
			tArea.addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub
					if (e.getKeyCode() == KeyEvent.VK_C) {
						tArea.setText(null);
					}
				}
			});
		}
	}
	
	/**
	 * CalculateModule类构造函数
	 * @param x,y DisplayModule模块的引用
	 */
	public CalculateModule(DisplayModule x, DisplayModule y) {
		dx = x;
		dy = y;
	}
	
	/**
	 * 将计算所需参数打包为double[]
	 * @return 打包结果
	 */
	public double[] packParam() {
		return new double[]{ lxx, lyy, rix, riy, rox, roz, rrx, rry, theta };
	}
	
	/**
	 * 保存计算参数
	 * @param param 参数
	 */
	public void storeParam(double[] param) {
		lxx = param[0];
		lyy = param[1];
		rix = param[2];
		riy = param[3];
		rox = param[4];
		roz = param[5];
		rrx = param[6];
		rry = param[7];
		theta = param[8];
		textPanel.tArea.append("参数设置成功：\n");
		textPanel.tArea.append("lxx: " + lxx + "; lyy: " + lyy + "; rix: " + rix + "; riy: " + riy
				+ "; rox: " + rox + "; roz: " + roz + "; rrx: " + rrx + "; rry: " + rry + "; theta: " + theta + ";\n");
	}
	
	/**
	 * 单个的配平计算
	 * @param d 对应的DisplayModule，即对应的x或y轴
	 * @return 计算结果
	 */
	private double calcPart(DisplayModule d) {
		List<Double> data = d.dataAll();
		int[] cnt = d.dataGroup();
		if (cnt.length < 4) {
			JOptionPane.showMessageDialog(null, "请先选取足够的数据！");
			return 0;
		} else if (cnt.length < 8) {
			int n = JOptionPane.showConfirmDialog(null, "按一组数据计算？", "提示", JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) {
				DataProcess.Pair[] l1 = new DataProcess.Pair[cnt[1]-cnt[0]+1];
				for (int i = 0; i < l1.length; i++) {
					l1[i] = new DataProcess.Pair();
					l1[i].x = i+cnt[0];
					l1[i].y = data.get(i+cnt[0]);
				}
				DataProcess.Pair[] l2 = new DataProcess.Pair[cnt[3]-cnt[2]+1];
				for (int i = 0; i < l2.length; i++) {
					l2[i] = new DataProcess.Pair();
					l2[i].x = i+cnt[2];
					l2[i].y = data.get(i+cnt[2]);
				}
				d.clearSelectData();
				return DataProcess.calcDelta(l1, l2);
			} else
				return 0;
		} else {
			DataProcess.Pair[] l1 = new DataProcess.Pair[cnt[1]-cnt[0]+1];
			for (int i = 0; i < l1.length; i++) {
				l1[i] = new DataProcess.Pair();
				l1[i].x = i+cnt[0];
				l1[i].y = data.get(i+cnt[0]);
			}
			DataProcess.Pair[] l2 = new DataProcess.Pair[cnt[3]-cnt[2]+1];
			for (int i = 0; i < l2.length; i++) {
				l2[i] = new DataProcess.Pair();
				l2[i].x = i+cnt[2];
				l2[i].y = data.get(i+cnt[2]);
			}
			DataProcess.Pair[] l3 = new DataProcess.Pair[cnt[5]-cnt[4]+1];
			for (int i = 0; i < l3.length; i++) {
				l3[i] = new DataProcess.Pair();
				l3[i].x = i+cnt[4];
				l3[i].y = data.get(i+cnt[4]);
			}
			DataProcess.Pair[] l4 = new DataProcess.Pair[cnt[7]-cnt[6]+1];
			for (int i = 0; i < l4.length; i++) {
				l4[i] = new DataProcess.Pair();
				l4[i].x = i+cnt[6];
				l4[i].y = data.get(i+cnt[6]);
			}
			d.clearSelectData();
			return DataProcess.calcDelta(l1, l2) + DataProcess.calcDelta(l3, l4);
		}
	}
}

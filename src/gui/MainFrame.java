/**
 * 
 */
package gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import data.ExportExcel;

/**
 * MainFrame类为应用程序主体
 * @author heyuyi
 *
 */
public class MainFrame extends JFrame {
	
	/**
	 * MainFrame类成员
	 */
	private static final long serialVersionUID = 1L;
	private final static int 
		HEIGHT = 800,
		WIDTH  = 1200;
	private DisplayModule 
		displayX = new DisplayModule("X 轴", "COM1"),
		displayY = new DisplayModule("Y 轴", "COM2");
	private CalculateModule
		calcModule = new CalculateModule(displayX, displayY);
	private ButtonPanel
		buttonPanel = new ButtonPanel();		
	
	/**
	 * ButtonPanel类为界面主要操作的按键组 
	 * @author heyuyi
	 *
	 */
	class ButtonPanel extends JPanel {
		
		/**
		 * ButtonPanel类成员
		 */
		private static final long serialVersionUID = 1L;
		private JButton
			switchBtn = new JButton("数据采集"),
			startBtn  = new JButton("开始记录"),
			paramBtn  = new JButton("参数设置"),
			saveBtn   = new JButton("保存结果");
		private boolean
			startFlag  = false;
		
		/**
		 * ButtonPanel类构造函数
		 */
		public ButtonPanel() {
			switchBtn.setFont(FontStyle.font18);
			startBtn.setFont(FontStyle.font18);
			paramBtn.setFont(FontStyle.font18);
			saveBtn.setFont(FontStyle.font18);
			startBtn.setEnabled(false);
			saveBtn.setEnabled(false);
			
			TitledBorder tb = BorderFactory.createTitledBorder("控制操作");
		    tb.setTitleJustification(TitledBorder.LEFT);
		    this.setBorder(tb);
		    
		    JPanel panel = new JPanel();
		    panel.setLayout(new GridLayout(4, 1, 0, 10));
		    panel.add(switchBtn);
		    panel.add(startBtn);
		    panel.add(paramBtn);
		    panel.add(saveBtn);
		    
		    setLayout(new GridBagLayout());
			add(panel, new GridBagConstraints(0, 0, 1, 1, 1, 1, 
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
					new Insets(5, 25, 10, 20), 0, 0));
			
			switchBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							switchBtn.setEnabled(false);
							startBtn.setEnabled(true);
							saveBtn.setEnabled(false);
							displayX.enableConnect();
							displayY.enableConnect();
						}
					});
				}
			});
			
			startBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (startFlag) {
								displayX.startDataProcess();
								displayY.startDataProcess();
								startBtn.setText("开始记录");
								startBtn.setEnabled(false);
								switchBtn.setEnabled(true);
								saveBtn.setEnabled(true);
								startFlag = false;
							} else {
								if (displayX.canDataCollect()/* && displayY.canDataCollect()*/) {
									displayX.startDataCollect();
									displayY.startDataCollect();
									startBtn.setText("停止记录");
									startFlag = true;
								} else 
									JOptionPane.showMessageDialog(null, "请保证串口正在正常采集！");
							}
						}
					});
				}
			});
			
			paramBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							ParamDialog dialog = new ParamDialog(MainFrame.this, calcModule.packParam());
							double[] param = dialog.showDialog();
							calcModule.storeParam(param);
						}
					});
				}
			});
			
			saveBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					ExportExcel excel = new ExportExcel();
					excel.export(displayX.dataAll(), displayY.dataAll());
				}
			});
		}
	}
	
	/**
	 * MainFrame类构造函数
	 */
	public MainFrame() {
		
		super("转子静平衡配平系统");
		pack();
		setSize(WIDTH, HEIGHT);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
//		setResizable(false);
		
		JPanel 
			framePanel = new JPanel(),
			mainPanel = new JPanel(),
			displayPanel = new JPanel(),
			controlPanel = new JPanel(),
			partPanel1 = new JPanel(),
			partPanel2 = new JPanel();
		
		displayPanel.setLayout(new GridLayout(1, 2, 10, 0));
		displayPanel.add(displayX);
		displayPanel.add(displayY);
		
		partPanel1.setLayout(new GridLayout(1, 2, 5, 0));
		partPanel1.add(calcModule.calcPanel1);
		partPanel1.add(calcModule.calcPanel2);
		
		partPanel2.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints(0, 0, 1, 1, 1, 1, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
				new Insets(0, 5, 0, 0), 0, 0);
		gc.gridx = 0; gc.gridy = 0; gc.weightx = 3; gc.weighty = 1;
		partPanel2.add(calcModule.textPanel, gc);
		gc.gridx = 1; gc.gridy = 0; gc.weightx = 1; gc.weighty = 1;
		partPanel2.add(buttonPanel, gc);
		
		controlPanel.setLayout(new GridLayout(1, 2));
		controlPanel.add(partPanel1);
		controlPanel.add(partPanel2);
		
		mainPanel.setLayout(new BorderLayout(0, 5));
		mainPanel.add(displayPanel, BorderLayout.CENTER);
		mainPanel.add(controlPanel, BorderLayout.SOUTH);
		
		framePanel.setLayout(new GridBagLayout());
		framePanel.add(mainPanel, new GridBagConstraints(0, 0, 1, 1, 1, 1, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
				new Insets(5, 15, 5, 15), 0, 0));
		
		add(framePanel);
		
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				displayX.closeSerialCom();
				displayY.closeSerialCom();
			}
		});
		setVisible(true);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new MainFrame();
			}
		});
	}
}

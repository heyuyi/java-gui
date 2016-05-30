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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * ParamDialog类为系统设置计算参数的对话框
 * @author heyuyi
 *
 */
class ParamDialog extends JDialog {

	/**
	 * ParamDialog类成员
	 */
	private static final long serialVersionUID = 1L;
	private JLabel labelLxx = new JLabel(" lxx(mm) ");
	private JTextField txtLxx = new JTextField();
	private JLabel labelLyy = new JLabel(" lyy(mm) ");
	private JTextField txtLyy = new JTextField();
	private JLabel labelRix = new JLabel(" rix(mm) ");
	private JTextField txtRix = new JTextField();
	private JLabel labelRiy = new JLabel(" riy(mm) ");
	private JTextField txtRiy = new JTextField();
	private JLabel labelRox = new JLabel(" rox(mm) ");
	private JTextField txtRox = new JTextField();
	private JLabel labelRoz = new JLabel(" roz(mm) ");
	private JTextField txtRoz = new JTextField();
	private JLabel labelRrx = new JLabel(" rrx(mm) ");
	private JTextField txtRrx = new JTextField();
	private JLabel labelRry = new JLabel(" rry(mm) ");
	private JTextField txtRry = new JTextField();
	private JLabel labelTheta = new JLabel(" theta(°)");
	private JTextField txtTheta = new JTextField();
	private JButton confirm = new JButton("确定");
	private JButton cancel = new JButton("取消");
	private double param[] = null;
	
	/**
	 * ParamDialog类构造函数
	 * @param father 父窗口
	 * @param param 参数数组
	 */
	public ParamDialog(JFrame father, double[] param) {
		super(father, "参数设置", true);
		this.param = param;
		
		JPanel paramPanel = new JPanel();
		JPanel mainPanel = new JPanel();
		JPanel controlPanel = new JPanel();
		
		paramPanel.setLayout(new GridLayout(5, 4, 20, 10));
		
		paramPanel.add(labelLxx);
		labelLxx.setHorizontalAlignment(JLabel.CENTER);
		labelLxx.setFont(FontStyle.font18);
		paramPanel.add(txtLxx);
		txtLxx.setHorizontalAlignment(JTextField.CENTER);
		txtLxx.setFont(FontStyle.font18);
		txtLxx.setText(Double.toString(param[0]));
		
		paramPanel.add(labelRox);
		labelRox.setHorizontalAlignment(JLabel.CENTER);
		labelRox.setFont(FontStyle.font18);
		paramPanel.add(txtRox);
		txtRox.setHorizontalAlignment(JTextField.CENTER);
		txtRox.setFont(FontStyle.font18);
		txtRox.setText(Double.toString(param[4]));
		
		paramPanel.add(labelLyy);
		labelLyy.setHorizontalAlignment(JLabel.CENTER);
		labelLyy.setFont(FontStyle.font18);
		paramPanel.add(txtLyy);
		txtLyy.setHorizontalAlignment(JTextField.CENTER);
		txtLyy.setFont(FontStyle.font18);
		txtLyy.setText(Double.toString(param[1]));
		
		paramPanel.add(labelRoz);
		labelRoz.setHorizontalAlignment(JLabel.CENTER);
		labelRoz.setFont(FontStyle.font18);
		paramPanel.add(txtRoz);
		txtRoz.setHorizontalAlignment(JTextField.CENTER);
		txtRoz.setFont(FontStyle.font18);
		txtRoz.setText(Double.toString(param[5]));
		
		paramPanel.add(labelRix);
		labelRix.setHorizontalAlignment(JLabel.CENTER);
		labelRix.setFont(FontStyle.font18);
		paramPanel.add(txtRix);
		txtRix.setHorizontalAlignment(JTextField.CENTER);
		txtRix.setFont(FontStyle.font18);
		txtRix.setText(Double.toString(param[2]));
		
		paramPanel.add(labelRrx);
		labelRrx.setHorizontalAlignment(JLabel.CENTER);
		labelRrx.setFont(FontStyle.font18);
		paramPanel.add(txtRrx);
		txtRrx.setHorizontalAlignment(JTextField.CENTER);
		txtRrx.setFont(FontStyle.font18);
		txtRrx.setText(Double.toString(param[6]));
		
		paramPanel.add(labelRiy);
		labelRiy.setHorizontalAlignment(JLabel.CENTER);
		labelRiy.setFont(FontStyle.font18);
		paramPanel.add(txtRiy);
		txtRiy.setHorizontalAlignment(JTextField.CENTER);
		txtRiy.setFont(FontStyle.font18);
		txtRiy.setText(Double.toString(param[3]));
		
		paramPanel.add(labelRry);
		labelRry.setHorizontalAlignment(JLabel.CENTER);
		labelRry.setFont(FontStyle.font18);
		paramPanel.add(txtRry);
		txtRry.setHorizontalAlignment(JTextField.CENTER);
		txtRry.setFont(FontStyle.font18);
		txtRry.setText(Double.toString(param[7]));
		
		paramPanel.add(labelTheta);
		labelTheta.setHorizontalAlignment(JLabel.CENTER);
		labelTheta.setFont(FontStyle.font18);
		paramPanel.add(txtTheta);
		txtTheta.setHorizontalAlignment(JTextField.CENTER);
		txtTheta.setFont(FontStyle.font18);
		txtTheta.setText(Double.toString(param[8]));
		
		controlPanel.setLayout(new GridLayout(1, 5, 20, 0));
		controlPanel.add(new JPanel());
		controlPanel.add(new JPanel());
		controlPanel.add(new JPanel());
		controlPanel.add(confirm);
		controlPanel.add(cancel);
		
		mainPanel.setLayout(new BorderLayout(0, 10));
		mainPanel.add(paramPanel);
		mainPanel.add(controlPanel, BorderLayout.SOUTH);
		
		setLayout(new GridBagLayout());
		add(mainPanel, new GridBagConstraints(0, 0, 1, 1, 1, 1, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
				new Insets(15, 20, 10, 20), 0, 0));
		
		confirm.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				// TODO Auto-generated method stub
				double[] temp = new double[9];
				try {
					temp[0] = Double.valueOf(txtLxx.getText());
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "lxx参数有误！");
					txtLxx.setText(Double.toString(ParamDialog.this.param[0]));
					return;
				}
				try {
					temp[1] = Double.valueOf(txtLyy.getText());
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "lyy参数有误！");
					txtLyy.setText(Double.toString(ParamDialog.this.param[1]));
					return;
				}
				try {
					temp[2] = Double.valueOf(txtRix.getText());
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "rix参数有误！");
					txtRix.setText(Double.toString(ParamDialog.this.param[2]));
					return;
				}
				try {
					temp[3] = Double.valueOf(txtRiy.getText());
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "riy参数有误！");
					txtRiy.setText(Double.toString(ParamDialog.this.param[3]));
					return;
				}
				try {
					temp[4] = Double.valueOf(txtRox.getText());
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "rox参数有误！");
					txtRox.setText(Double.toString(ParamDialog.this.param[4]));
					return;
				}
				try {
					temp[5] = Double.valueOf(txtRoz.getText());
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "roz参数有误！");
					txtRoz.setText(Double.toString(ParamDialog.this.param[5]));
					return;
				}
				try {
					temp[6] = Double.valueOf(txtRrx.getText());
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "rrx参数有误！");
					txtRrx.setText(Double.toString(ParamDialog.this.param[6]));
					return;
				}
				try {
					temp[7] = Double.valueOf(txtRry.getText());
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "rry参数有误！");
					txtRry.setText(Double.toString(ParamDialog.this.param[7]));
					return;
				}
				try {
					temp[8] = Double.valueOf(txtTheta.getText());
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Theta参数有误！");
					txtTheta.setText(Double.toString(ParamDialog.this.param[8]));
					return;
				}
				ParamDialog.this.param = temp;
				dispose();
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
			}
		});
		
		pack();
		setLocationRelativeTo(father);
	}
	
	/**
	 * 对话框显示，并且可以返回数据给对话框调用者
	 * @return 返回对话框设置参数
	 */
	public double[] showDialog() {
		setVisible(true);
		return param;
	}
}

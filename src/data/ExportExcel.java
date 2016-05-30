/**
 * 
 */
package data;

import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * ExportExcel类将记录的数据以excel文件的形式导出
 * @author heyuyi
 *
 */
public class ExportExcel {
	
	/**
	 * ExportExcel类成员
	 */
	private static String dirBuf = ".";
	private String dir = null;
	private boolean flag;
	
	/**
	 * ExportExcel类构造函数，产生一个用于选择保存路径和设置文件名的对话框，
	 * 默认文件名为当前时间，会自动记录上次的保存路径。
	 */
	public ExportExcel() {
		// TODO Auto-generated constructor stub
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String name = dateformat.format(new Date());
        
        JFileChooser fc = new JFileChooser(dirBuf);
//        JFileChooser fc = new JFileChooser("C:\\Users\\heyuyi\\");
		fc.setDialogTitle("导出Excel");
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setDialogType(JFileChooser.SAVE_DIALOG);
		fc.setMultiSelectionEnabled(false);
		fc.setAcceptAllFileFilterUsed(false);
		
		JTextField txt = getTextField(fc);
		txt.setText(name);
		
		fc.addChoosableFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return "Excel文件(*.xls)";
			}
			
			@Override
			public boolean accept(File f) {
				// TODO Auto-generated method stub
				String name = f.getName().toLowerCase();
				return f.isDirectory() || name.endsWith(".xls");
			}
		});
		int r = fc.showSaveDialog(null);
		if (r == JFileChooser.APPROVE_OPTION) {
			dir = fc.getSelectedFile().getAbsolutePath();
			String str = fc.getSelectedFile().getName();
			dirBuf = dir.substring(0, dir.length()-str.length());
			flag = true;
		} else if (r == JFileChooser.CANCEL_OPTION) {
			flag = false;
		}
	}
	
	/**
	 * 数据点导出数据
	 * @param l1，l2 数据点集
	 */
	public void export(List<Double> l1, List<Double> l2) {
		if (!flag)
			return;
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(dir+".xls");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("导出数据");
		
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("X轴");
		cell = row.createCell(1);
		cell.setCellValue("Y轴");
		
		for (int i = 1; i <= l1.size(); i++) {
			row = sheet.createRow(i);
			if (i <= l1.size()) {
				cell = row.createCell(0);
				cell.setCellValue(l1.get(i-1));
			}
			if (i <= l2.size()) {
				cell = row.createCell(1);
				cell.setCellValue(l2.get(i-1));
			}
		}
		
		try {
			workbook.write(out);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取TextFiel，用于设置保存对话框的默认文件名
	 * @param c
	 * @return 返回TextFiel
	 */
	private JTextField getTextField(Container c) {
        JTextField textField = null;
        for (int i = 0; i < c.getComponentCount(); i++) {
            Component cnt = c.getComponent(i);
            if (cnt instanceof JTextField) {
                return (JTextField) cnt;
            }
            if (cnt instanceof Container) {
                textField = getTextField((Container) cnt);
                if (textField != null) {
                    return textField;
                }
            }
        }
        return textField;
    }
}

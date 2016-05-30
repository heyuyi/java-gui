/**
 * 
 */
package serialcom;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

/**
 * SerialCom类为串口读取数据实现
 * @author heyuyi
 *
 */
public class SerialCom implements SerialPortEventListener {

	/**
	 * SerialCom类成员
	 */
	private OutputStream os;
    private InputStream is;
    private CommPortIdentifier portId;
    private SerialPort sPort;
    private String comName;
    private boolean isOpen = false;
    private StringBuffer sbuf = new StringBuffer();
    private String data;
    private volatile boolean valid = false;
    
    /**
	 * SerialCom类构造函数
	 * @param comn 串口名
	 */
	public SerialCom(String comn) {
		comName = comn;
	}
	
	/**
	 * 返回串口名
	 * @return 串口名
	 */
	public String name() {
		return comName;
	}
	
	/**
	 * 打开串口
	 */
	public void open() throws Exception {
		try {
			portId = CommPortIdentifier.getPortIdentifier(comName);
		} catch (NoSuchPortException e) {
			throw e;
		}
		try {
			sPort = (SerialPort) portId.open("Demo", 3000);
		} catch (PortInUseException e) {
			throw e;
		}
		try {
			sPort.setSerialPortParams(1200, SerialPort.DATABITS_7,
					SerialPort.STOPBITS_1, SerialPort.PARITY_ODD);
			sPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
		} catch (UnsupportedCommOperationException e) {
			close();
			throw e;
		}
		try {
			os = sPort.getOutputStream();
			is = sPort.getInputStream();
		}catch (IOException e) {
			close();
			throw e;
		}
		try {
			sPort.addEventListener(this);
		} catch (TooManyListenersException e) {
			close();
			throw e;
		}
		sPort.notifyOnDataAvailable(true);
		isOpen = true;
		valid = false;
	}

	/**
	 * 关闭串口
	 */
	public void close() throws IOException {
		if (!isOpen) {
			return;
		}
		try {
			if (os != null)
				os.close();
			if (is != null)
				is.close();
		} catch (IOException e) {
			throw e;
		}
		sPort.removeEventListener();
		sPort.close();
		isOpen = false;
	}
	
	/**
	 * 串口发送指令
	 * @param cmd 指令
	 */
	public void sendCommand(byte cmd){
    	byte[] temp = { 0x1b, cmd, 0x0d, 0x0a };
    	try {
    		os.write(temp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	/**
	 * 检查是否有了有效数据
	 * @param cmd 指令
	 */
	public boolean dataValid() {
		return valid;
//		valid = true;
//		return true;
	}
	
	/**
	 * 返回有效数据
	 * @return 返回数据
	 */
	public synchronized String readData() {
		if (valid) {
			valid = false;
//			Random random = new Random();
//			String str = "+   1"+"."+(random.nextInt(9))+(random.nextInt(9))
//					+(random.nextInt(9))+(random.nextInt(9))+" g  ";
//			return str; 
			return data;
		} else
			return null;
	}
	
	/**
	 * 写入有效数据
	 * @param str 写入数据
	 */
	public synchronized void writeData(String str) {
		data = str;
		valid = true;
	}
	
	/**
	 * 串口事件
	 */
	@Override
	public void serialEvent(SerialPortEvent e) {
		// TODO Auto-generated method stub
		switch (e.getEventType()) {
		
		case SerialPortEvent.BI:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.FE:
		case SerialPortEvent.OE:
		case SerialPortEvent.PE:
		case SerialPortEvent.RI:
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;
		case SerialPortEvent.DATA_AVAILABLE:
			int newData = 0;
			for (;;) {
				try {
					newData = is.read();
					if (newData == -1)
						break;
					else if (newData == '\n') {
//						sbuf.append((char)newData);
						writeData(sbuf.substring(0, sbuf.length()));
						sbuf.delete(0, sbuf.length());
					} else {
						if (newData != '\r'/* && newData != ' '*/)
							sbuf.append((char)newData);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			break;
		default:
			break;
		}
	}
}

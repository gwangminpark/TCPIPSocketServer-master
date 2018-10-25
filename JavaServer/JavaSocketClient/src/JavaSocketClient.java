import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/** TCP/IP Client Socket 
 *  Network Programming
 *	
 * @author Kitkat
 *
 */
public class JavaSocketClient {
	private static Socket socket;
	private static InputStream is;
	private static OutputStream os;
	
	public static void main(String[] args) {
		try {
			
			// socket = new Socket("localhost", 5001);
			
			socket = new Socket();
			System.out.println("Server Connecting..");
			socket.connect(new InetSocketAddress("localhost", 5001));
			System.out.println("Server Connection OK!");
			
			is = socket.getInputStream();
			os = socket.getOutputStream();
			
			byte[] byteArr = null;
			String msg = "Hello Server";
					
			byteArr = msg.getBytes("UTF-8");
			os.write(byteArr);
			os.flush();
			System.out.println("Data Transmitted OK!");
			
			byteArr = new byte[512];
			int readByteCount = is.read();
			
			if(readByteCount == -1)
				throw new IOException();
			
			msg = new String(byteArr, 0, readByteCount, "UTF-8");
			System.out.println("Data Received OK!");
			System.out.println("Message : " + msg);

			is.close();
			os.close();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			// IP Address�� �߸��Ǿ��� ���
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// �ش� Port�� Server�� ������ �� ���� ���
			e.printStackTrace();
			try { socket.close(); } catch (IOException e1) { e1.printStackTrace(); }
		}
		
		if(!socket.isClosed()) {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

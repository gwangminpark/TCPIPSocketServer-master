import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.lang.reflect.Member;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Base64;

/** TCP/IP Server Socket 
 *  Network Programming
 *	
 * @author Kitkat
 *
 */
public class JavaSocketServer {
	private static final String Naver = "www.naver.com";
	private static ServerSocket serverSocket;
	private static Socket socket;
	
	public static void main(String[] args) throws ClassNotFoundException {
		ipAddress();
		
		try {
			
			// Instantiate ServerSocket Class
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(9512));
			
						
			// Connection Wait for Multiple Client 
			while(true) {
	
				socket = serverSocket.accept();
				InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();
				System.out.println("Connection 연결성공 [" + isa.getHostName() + ":" + isa.getPort() + "]");
			
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				
	/*			 String base64Member = "...생략";
				    byte[] serializedMember = Base64.getDecoder().decode(base64Member);
				    try (ByteArrayInputStream bais = new ByteArrayInputStream(serializedMember)) {
				        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
				            // 역직렬화된 Member 객체를 읽어온다.
				            Object objectMember = ois.readObject();
				            Member member = (Member) objectMember;
				            System.out.println(member);
				        }
				    }*/
				
				byte[] byteArr = new byte[512];
				String msg = null;
				
				int readByteCount = is.read(byteArr);
				
				if(readByteCount == -1)
					throw new IOException();
				
				msg = new String(byteArr, 0, readByteCount, "UTF-8");

				System.out.println("Message : " + msg);
				
				msg = "socket test!!!!";
				byteArr = msg.getBytes("UTF-8");
				os.write(byteArr);
		
				
				os.flush();
				is.close();
				os.close();
				socket.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try { socket.close(); } catch (IOException e1) { e1.printStackTrace(); }
		}
		
		if(!serverSocket.isClosed()) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void ipAddress() {
		try {
			
			InetAddress inetAddress = InetAddress.getLocalHost();
			System.out.println("LocalHost IP Address : " + inetAddress.getHostAddress());
			
			InetAddress[] iaArr = InetAddress.getAllByName(Naver);
			
			for(InetAddress ia : iaArr) 
				System.out.println(Naver + " IP Address : " + ia.getHostAddress());
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientBackground extends Thread {

	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private Gui_Main gui;
	private String msg;
	private String nickName;
	private String ip;
	private int port;

	public final void setGui(Gui_Main gui) {
		this.gui = gui;
	}

	public void setNickname(String nickName) {
		this.nickName = nickName;
	}

	public void setServerIP(String ip) {
		this.ip = ip;
	}

	public void setServerPort(String port) {
		this.port = Integer.parseInt(port);
	}

	public void run() {
		try {
			socket = new Socket(ip, port);
			System.out.println("서버 연결됨.");

			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());

			out.writeUTF(nickName);
			System.out.println("클라이언트 : 메시지 전송완료");
			while (in != null) {
				msg = in.readUTF();
				gui.appendMsg(msg + "\n\r");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String msg) {
		try {
			out.writeUTF(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

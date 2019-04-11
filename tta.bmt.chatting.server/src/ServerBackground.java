import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServerBackground extends Thread{

	private ServerSocket serverSocket;
	private Socket socket;
	private static GUI_Main gui;
	private String msg;
	private int Socket;
	Receiver receiver;

	private Map<String, DataOutputStream> clientsMap = new HashMap<String, DataOutputStream>();

	public final void setGui(GUI_Main gui) {
		this.gui = gui;
	}

	public void setSocket(int Socket) {
		this.Socket = Socket;
	}

	public void run() {
		Collections.synchronizedMap(clientsMap);
		try {
			serverSocket = new ServerSocket(Socket);
			while (true) {
				gui.appendMsg("서버 대기중...\n");
				socket = serverSocket.accept();
				gui.appendMsg(socket.getInetAddress() + "에서 접속했습니다.\n");
				receiver = new Receiver(socket);
				receiver.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addClient(String nick, DataOutputStream out) throws IOException {
		sendMessage(nick + "님이 접속하셨습니다.\n");
		clientsMap.put(nick, out);
	}

	public void removeClient(String nick) {
		sendMessage(nick + "님이 나가셨습니다.\n");
		clientsMap.remove(nick);
	}

	public void sendMessage(String msg) {
		Iterator<String> it = clientsMap.keySet().iterator();
		String key = "";
		while (it.hasNext()) {
			key = it.next();
			try {
				clientsMap.get(key).writeUTF(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class Receiver extends Thread {
		private DataInputStream in;
		private DataOutputStream out;
		private String nick;

		public Receiver(Socket socket) throws IOException {
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			nick = in.readUTF();
			addClient(nick, out);
		}

		public void run() {
			try {
				while (in != null) {
					msg = in.readUTF();
					gui.appendMsg(msg);
					sendMessage(msg);
				}
			} catch (SocketException e) {
				removeClient(nick);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

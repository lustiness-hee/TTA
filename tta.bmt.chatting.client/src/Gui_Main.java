import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Button;

public class Gui_Main {
	private static Text text;
	private static Text textIP;
	private static Text textPort;
	private static Text textDisplay;
	private static ClientBackground client = new ClientBackground();
	private static String nickName;
	private static InetAddress inet;
	private static Display display;
	private static Shell shell;
	Button btnJoin;
	static Gui_Main gm;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 * @wbp.parser.entryPoint
	 */

	public Gui_Main() {
		// TODO Auto-generated constructor stub

		display = Display.getDefault();
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("kr.or.TTA.BMT.Chatting.Client");

		text = new Text(shell, SWT.BORDER);
		text.setBounds(0, 240, 434, 21);

		textIP = new Text(shell, SWT.BORDER);
		textIP.setBounds(42, 0, 152, 21);

		Label lblIp = new Label(shell, SWT.NONE);
		lblIp.setBounds(9, 3, 31, 15);
		lblIp.setText("IP : ");

		Label lblPort = new Label(shell, SWT.NONE);
		lblPort.setBounds(214, 3, 40, 15);
		lblPort.setText("PORT : ");

		textPort = new Text(shell, SWT.BORDER);
		textPort.setBounds(260, 0, 73, 21);

		Button btnJoin = new Button(shell, SWT.NONE);
		btnJoin.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				String port = textPort.getText();
				String IP = textIP.getText();
				if (!(port.equals("") || IP.equals(""))) {
					client.setNickname(nickName);
					client.setServerIP(IP);
					client.setServerPort(port);
					client.start();
				}
			}
		});

		btnJoin.setBounds(348, 0, 76, 25);
		btnJoin.setText("Join");

		textDisplay = new Text(shell, SWT.READ_ONLY | SWT.V_SCROLL);
		textDisplay.setEditable(false);
		textDisplay.setBounds(0, 27, 434, 207);

		text.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.keyCode == SWT.CR) {
					client.sendMessage(text.getText());
					text.setText("");
				}
			}
		});
	}

	public static void main(String[] args) {
		try {
			gm = new Gui_Main();
			inet = InetAddress.getLocalHost();
			nickName = inet.getHostAddress();
			client.setGui(new Gui_Main());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public void appendMsg(String msg) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				textDisplay.append(msg + "\n");
			}
		});
	}
}

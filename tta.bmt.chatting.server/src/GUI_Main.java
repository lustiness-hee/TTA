import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

public class GUI_Main {
	private static Display display;
	private static Shell shell;
	private Text textPort;
	private Text textField;
	private Button btnStart;
	private Label IP;
	private static ServerBackground server;
	private static GUI_Main gm;
	private boolean serverFlag = false;
	private static InetAddress socket;
	private Text text;
	String buffer;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 * @wbp.parser.entryPoint
	 */

	public static void main(String[] args) {
		gm = new GUI_Main();

		server = new ServerBackground();
		server.setGui(gm);
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public GUI_Main() {

		try {
			socket = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		display = Display.getDefault();
		shell = new Shell();
		IP = new Label(shell, SWT.NONE);
		textField = new Text(shell, SWT.BORDER);
		shell.setSize(450, 300);
		shell.setText("kr.or.tta.bmt.chating.server");

		btnStart = new Button(shell, SWT.NONE);
		btnStart.setBounds(358, 0, 76, 25);
		btnStart.setText("Start");

		textPort = new Text(shell, SWT.BORDER);
		textPort.setBounds(279, 4, 73, 21);

		IP.setBounds(132, 7, 141, 15);
		IP.setText("IP : " + socket.getHostAddress());

		textField.setBounds(0, 240, 434, 21);

		text = new Text(shell, SWT.READ_ONLY | SWT.V_SCROLL);
		text.setBounds(0, 30, 434, 206);

		textField.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.keyCode == SWT.CR) {
					appendMsg(textField.getText());
					textField.setText("");
				}
			}
		});
		btnStart.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				// TODO Auto-generated method stub
				if (serverFlag == false) {
					server.setSocket(Integer.parseInt(textPort.getText()));
					btnStart.setText("Stop");
					serverFlag = true;
					server.start();
				} else if (serverFlag == true) {
					btnStart.setText("Start");
					serverFlag = false;
					server.stop();
				}
			}
		});
	}

	public void appendMsg(String msg) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				text.append("\n" + msg);
			}
		});
	}
}

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import File.FileExtractor;
import FingerPrint.MD5Hash;
import FingerPrint.SHA256Hash;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;

public class Main {
	private static Text text;

	public static void main(String[] args) {
		init();
	}

	private static void init() {
		Display display = Display.getDefault();
		Shell shell = new Shell();
		shell.setSize(450, 351);
		shell.setText("FFPE");

		Button btnNewButton = new Button(shell, SWT.NONE);

		btnNewButton.setBounds(348, 8, 76, 25);
		btnNewButton.setText("Select File..");

		text = new Text(shell, SWT.BORDER);
		text.setBounds(10, 10, 332, 21);

		Label label = new Label(shell, SWT.NONE);
		label.setTouchEnabled(true);
		label.setBounds(10, 37, 414, 214);

		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fileNameRead(shell, btnNewButton, label);
			}
		});
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		shell.open();
		shell.layout();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private static void fileNameRead(Shell shell, Button btn1, Label label) {
		FileDialog fileload = new FileDialog(shell);
		fileload.setText("Select file.....");
		FileExtractor fe = new FileExtractor();
		btn1.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				fileload.open();
				setfile();
			}

			private void setfile() {
				// TODO Auto-generated method stub
				String filepath = fileload.getFilterPath() + "\\" + fileload.getFileName().toString();

				if (!filepath.equals("\\")) {
					text.setText(filepath);
					MD5Hash md5 = new MD5Hash();
					md5.setFile(filepath);
					SHA256Hash sha256Hash = new SHA256Hash();
					sha256Hash.setFile(filepath);
					fe.add(filepath + " : \n MD5 : \n" + md5.getMD5() + "\n SHA-256 : \n" + sha256Hash.getSHA256());
					label.setText(fe.getContents());
				}
			}
		});
	}
}

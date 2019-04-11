package kr.or.tta.DummyDocGenerator;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class main_ {

	protected Shell shlKrorttadummydocgenerator;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			main_ window = new main_();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlKrorttadummydocgenerator.open();
		shlKrorttadummydocgenerator.layout();
		while (!shlKrorttadummydocgenerator.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlKrorttadummydocgenerator = new Shell();
		shlKrorttadummydocgenerator.setSize(533, 361);
		shlKrorttadummydocgenerator.setText("kr.or.tta.DummyDocGenerator");

	}
}

//*************************************************************************************************
package pgem;
//*************************************************************************************************

import pgem.gui.Frame;
import pgem.gui.GUI;
import pgem.host.Host;
import pgem.msg.Msg;
import pgem.msg.MsgBox;
import pgem.msg.MsgType;

//*************************************************************************************************
public final class App {

	//=============================================================================================
	private final GUI gui = new GUI();
	private final MsgBox msgBox = new MsgBox();
	private final Host host = Host.create(Host.TYPE_JOGL, msgBox);
	//=============================================================================================

	//=============================================================================================
	private boolean quit = false;
	//=============================================================================================
	
	//=============================================================================================
	public void config(String[] args) {
		
	}
	//=============================================================================================

	//=============================================================================================
	public void run() {
		init();
		loop();
		done();
	}
	//=============================================================================================

	//=============================================================================================
	public void quit() {
		quit = true;
	}
	//=============================================================================================
	
	//=============================================================================================
	private void init() {

		gui.root().size(1200, 800);
		
		for (int i=0; i<5; i++) {
			Frame frame = new Frame();
			frame.title("WINDOW " + (i+1));
			frame.position(40 + i*20, 760 - frame.size().y - i * 30);
			frame.parent(gui.root());
		}
		
		host.init();
		host.plug(gui);

		msgBox.plug(MsgType.APPLICATION_QUIT, this::handleQuit);
		msgBox.plug(MsgType.WINDOW_CLOSE, this::handleQuit);
		
		msgBox.plug(MsgType.KEY_PRESSED, gui::handleInput);
		msgBox.plug(MsgType.KEY_RELEASED, gui::handleInput);
		msgBox.plug(MsgType.KEY_TYPED, gui::handleInput);
		msgBox.plug(MsgType.POINTER_MOVED, gui::handleInput);
		msgBox.plug(MsgType.POINTER_PRESSED, gui::handleInput);
		msgBox.plug(MsgType.POINTER_RELEASED, gui::handleInput);
		msgBox.plug(MsgType.POINTER_CLICKED, gui::handleInput);
		msgBox.plug(MsgType.POINTER_SCROLLED, gui::handleInput);
		
		msgBox.plug(MsgType.WINDOW_RESIZED, gui::handleResize);
		
	}
	//=============================================================================================

	//=============================================================================================
	private void loop() {
		quit = false;
		host.visible(true);
		while (!quit) {
			msgBox.update();
			host.update();
			Thread.yield();			
		}
	}
	//=============================================================================================
	
	//=============================================================================================
	private void done() {
		msgBox.unplug(this::handleQuit);
		host.done();
	}
	//=============================================================================================

	//=============================================================================================
	private void handleQuit(Msg msg) {
		quit();
	}
	//=============================================================================================
	
	//=============================================================================================
	public static void main(String[] args) {
		var app = new App();
		app.config(args);
		app.run();
	}
	//=============================================================================================
	
}
//*************************************************************************************************

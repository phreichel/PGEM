//*************************************************************************************************
package pgem;
//*************************************************************************************************

import pgem.host.Host;
import pgem.msg.Msg;
import pgem.msg.MsgBox;
import pgem.msg.MsgType;

//*************************************************************************************************
public final class App {

	//=============================================================================================
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
		host.init();
		msgBox.plug(MsgType.APPLICATION_QUIT, this::handleQuit);
		msgBox.plug(MsgType.WINDOW_CLOSE, this::handleQuit);
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
	public static void main(String[] args) {
		var app = new App();
		app.config(args);
		app.run();
	}
	//=============================================================================================

	//=============================================================================================
	private void handleQuit(Msg msg) {
		quit();
	}
	//=============================================================================================
	
}
//*************************************************************************************************
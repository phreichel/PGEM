//*************************************************************************************************
package pgem.app;
//*************************************************************************************************

import pgem.msg.MsgHub;
import pgem.gui.GUI;
import pgem.gui.RenderFlag;
import pgem.gui.Widget;
import pgem.log.Level;
import pgem.log.Log;
import pgem.msg.Msg;
import pgem.msg.MsgType;
import pgem.port.Port;
import pgem.port.jogl.JOGLPort;

//*************************************************************************************************
public class App {

	//=============================================================================================
	private final Port port = new JOGLPort();
	private final MsgHub msgHub = new MsgHub();
	private final GUI gui = new GUI();
	//=============================================================================================

	//=============================================================================================
	private boolean terminate = false;
	//=============================================================================================
	
	//=============================================================================================
	private void init() {
		terminate = false;
		Log.getDefault().enable(
			Level.INFO,
			Level.WARNING,
			Level.ERROR,
			Level.DEBUG);
		msgHub.link(
			MsgType.WND_CLOSE,
			this::handleQuit);
		
		Widget w = new Widget();
		w.renderFlags.add(RenderFlag.BACKGROUND);
		w.renderFlags.add(RenderFlag.BORDER);
		w.dock.set(0, 0, 1, 1);
		w.position(100, 200);
		w.size(300, 200);
		gui.root.size(800, 600);
		w.parent(gui.root);

		gui.hook(msgHub);
		gui.hook(port);
		port.link(msgHub);
		port.window()
			.position(50, 50)
			.size(800, 600)
			.fullscreen(true)
			.title("PGEM Example Window 01")
			.visible(true);
	
	}
	//=============================================================================================

	//=============================================================================================
	private void loop() {
		while (!terminate) {
			msgHub.dispatch();
			port.render();
			Thread.yield();
		}
	}
	//=============================================================================================

	//=============================================================================================
	private void done() {
		msgHub.unlink(
			MsgType.WND_CLOSE,
			this::handleQuit);
		port.window()
			.visible(false);
		gui.unhook(msgHub);
		gui.unhook(port);
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
	private void handleQuit(Msg msg) {
		assert msg.type.equals(MsgType.WND_CLOSE);
		terminate = true;
	}
	//=============================================================================================
	
	//=============================================================================================
	public static void main(String[] args) {
		App app = new App();
		app.run();
	}
	//=============================================================================================
	
}
//*************************************************************************************************

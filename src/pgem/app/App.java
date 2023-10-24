//*************************************************************************************************
package pgem.app;
//*************************************************************************************************

import pgem.msg.MsgHub;
import pgem.gui.GUI;
import pgem.gui.Render;
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

		Widget panel = gui.factory().panel(400, 300);
		panel.position(50, 50);
		panel.dock.set(0, 0, 1, 1);
		panel.borderColor.set(1, 0, 0, 1);
		panel.parent(gui.root);

		Widget image = gui.factory().image("DECO", 400, 300);
		image.renders.add(Render.TRANSPARENT);
		image.imageData.color.set(1, 0.6f, 0, 1);
		image.imageData.size.set(20, 20);
		image.position(0, 0);
		image.dock.set(0, 0, 1, 1);
		image.parent(panel);

		Widget subpanel = gui.factory().panel(388, 250);
		subpanel.renders.add(Render.TRANSPARENT);
		subpanel.backgroundColor.set(.6f, .6f, 1f, .5f);
		subpanel.borderColor.set(0, 0, 1, 1);
		subpanel.position(10, 25);
		subpanel.dock.set(0, 0, 1, 1);
		subpanel.parent(panel);
		
		Widget label = gui.factory().label("Hello, World!", 100, 18);
		label.position(2, 2);
		label.parent(panel);

		Widget textbox = gui.factory().textBox("Hello, World!", 294, 18);
		textbox.position(2 + 100 + 2, 2);
		textbox.dock.set(0, 0, 0, 1);
		textbox.parent(panel);

		Widget apply = gui.factory().button("APPLY", 60, 18, "quit");
		apply.position(139, 300 - 20);
		apply.dock.set(1, .5f, 1, .5f);
		apply.parent(panel);

		Widget cancel = gui.factory().button("CANCEL", 60, 18, null);
		cancel.position(201, 300 - 20);
		cancel.dock.set(1, .5f, 1, .5f);
		cancel.parent(panel);
		
		gui.hook(msgHub);
		gui.hook(port);
		port.link(msgHub);	
		port.window()
			.position(50, 50)
			.size(800, 600)
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

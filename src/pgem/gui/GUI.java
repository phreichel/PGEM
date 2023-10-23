//*************************************************************************************************
package pgem.gui;
//*************************************************************************************************

import pgem.msg.Msg;
import pgem.msg.MsgHub;
import pgem.port.Port;

import static pgem.msg.MsgType.*;

import javax.vecmath.Vector2f;

//*************************************************************************************************
public class GUI {

	//=============================================================================================
	private final GUIFactory factory = new GUIFactory();
	//=============================================================================================
	
	//=============================================================================================
	public final Widget root = factory.root();
	//=============================================================================================

	//=============================================================================================
	private final GUILayouter layouter = new GUILayouter();
	private final GUIRenderer renderer = new GUIRenderer(this);
	//=============================================================================================

	//=============================================================================================
	public GUIFactory factory() {
		return this.factory;
	}
	//=============================================================================================

	//=============================================================================================
	private void handleMessage(Msg msg) {
		Vector2f offset = new Vector2f(0, 0);
		root.handle(offset, msg);
	}
	//=============================================================================================
	
	//=============================================================================================
	private void handleResize(Msg msg) {
		root.size(msg.size);
	}
	//=============================================================================================

	//=============================================================================================
	public void hook(MsgHub msgHub) {
		msgHub.link(KBD_PRESSED, this::handleMessage);
		msgHub.link(KBD_RELEASED, this::handleMessage);
		msgHub.link(KBD_TYPED, this::handleMessage);
		msgHub.link(PTR_CLICKED, this::handleMessage);
		msgHub.link(PTR_PRESSED, this::handleMessage);
		msgHub.link(PTR_RELEASED, this::handleMessage);
		msgHub.link(PTR_MOVED, this::handleMessage);
		msgHub.link(WND_RESIZE, this::handleResize);
	}
	//=============================================================================================

	//=============================================================================================
	public void unhook(MsgHub msgHub) {
		msgHub.unlink(KBD_PRESSED, this::handleMessage);
		msgHub.unlink(KBD_RELEASED, this::handleMessage);
		msgHub.unlink(KBD_TYPED, this::handleMessage);
		msgHub.unlink(PTR_CLICKED, this::handleMessage);
		msgHub.unlink(PTR_PRESSED, this::handleMessage);
		msgHub.unlink(PTR_RELEASED, this::handleMessage);
		msgHub.unlink(PTR_MOVED, this::handleMessage);
		msgHub.unlink(WND_RESIZE, this::handleResize);
	}
	//=============================================================================================

	//=============================================================================================
	public void hook(Port port) {
		port.link(renderer);
	}
	//=============================================================================================

	//=============================================================================================
	public void unhook(Port port) {
		port.unlink(renderer);
	}
	//=============================================================================================

	//=============================================================================================
	public void update() {
		layouter.update();
	}
	//=============================================================================================
	
}
//*************************************************************************************************

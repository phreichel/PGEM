//*************************************************************************************************
package pgem.gui;
//*************************************************************************************************

import pgem.msg.Msg;
import pgem.msg.MsgHub;
import pgem.port.Port;

import static pgem.msg.MsgType.*;

//*************************************************************************************************
public class GUI {

	//=============================================================================================
	public final Widget root = new Widget();
	//=============================================================================================

	//=============================================================================================
	private final GUILayouter layouter = new GUILayouter();
	private final GUIRenderer renderer = new GUIRenderer(this);
	//=============================================================================================
	
	//=============================================================================================
	private void handleKeyboard(Msg msg) {
		
	}
	//=============================================================================================
	
	//=============================================================================================
	private void handlePointer(Msg msg) {
		
	}
	//=============================================================================================
	
	//=============================================================================================
	private void handleResize(Msg msg) {
		root.size(msg.size);
	}
	//=============================================================================================

	//=============================================================================================
	public void hook(MsgHub msgHub) {
		msgHub.link(KBD_PRESSED, this::handleKeyboard);
		msgHub.link(KBD_RELEASED, this::handleKeyboard);
		msgHub.link(KBD_TYPED, this::handleKeyboard);
		msgHub.link(PTR_CLICKED, this::handlePointer);
		msgHub.link(PTR_PRESSED, this::handlePointer);
		msgHub.link(PTR_RELEASED, this::handlePointer);
		msgHub.link(PTR_MOVED, this::handlePointer);
		msgHub.link(WND_RESIZE, this::handleResize);
	}
	//=============================================================================================

	//=============================================================================================
	public void unhook(MsgHub msgHub) {
		msgHub.unlink(KBD_PRESSED, this::handleKeyboard);
		msgHub.unlink(KBD_RELEASED, this::handleKeyboard);
		msgHub.unlink(KBD_TYPED, this::handleKeyboard);
		msgHub.unlink(PTR_CLICKED, this::handlePointer);
		msgHub.unlink(PTR_PRESSED, this::handlePointer);
		msgHub.unlink(PTR_RELEASED, this::handlePointer);
		msgHub.unlink(PTR_MOVED, this::handlePointer);
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

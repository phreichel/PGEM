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
	public final Widget root = new Widget();
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
	private void handleKeyboard(Msg msg) {
		
	}
	//=============================================================================================

	//=============================================================================================
	private void handlePointer(Msg msg) {
		var hovering = hovering(msg.pointer);
	}
	//=============================================================================================

	//=============================================================================================
	private Widget hovered = null;
	//=============================================================================================
	
	//=============================================================================================
	public Widget hovering(Vector2f parentPointer) {
		var hovering = hovering(root, parentPointer);
		if (hovering != hovered) {
			if (hovered != null) {
				hovered.interactData.hover = false;
			}
			if (hovering != null) {
				hovering.interactData.hover = true;
			}
			hovered = hovering;
		}
		return hovering;
	}
	//=============================================================================================
	
	//=============================================================================================
	public Widget hovering(Widget widget, Vector2f parentPointer) {
		Vector2f localPointer = new Vector2f(parentPointer);
		localPointer.sub(widget.position());
		Widget hovering = null;
		boolean match =
			localPointer.x >= 0f &&
			localPointer.y >= 0f &&
			localPointer.x <= widget.size().x &&
			localPointer.y <= widget.size().y;
		if (match) {
			hovering = widget;
			for (var i=widget.children().size()-1; i>=0; i--) {
				var child = widget.children().get(i);
				var result = hovering(child, localPointer);
				if (result != null) {
					hovering = result;
					break;
				}
			}
		}
		return hovering;
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

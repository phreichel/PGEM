//*************************************************************************************************
package pgem.gui;
//*************************************************************************************************

import pgem.msg.Msg;
import pgem.msg.MsgHub;
import pgem.msg.MsgType;
import pgem.port.Button;
import pgem.port.Port;

import static pgem.msg.MsgType.*;

import java.util.ArrayList;
import java.util.List;

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
<<<<<<< HEAD
	private void handlePointer(Msg msg) {
		
		var hovering = hovering(msg.pointer);
		var focused = focused(msg, hovering);
		if (focused == null) return;
		clicked(msg, focused);
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
=======
	private void handleMessage(Msg msg) {
		Vector2f offset = new Vector2f(0, 0);
		root.handle(offset, msg);
>>>>>>> branch 'main' of https://github.com/phreichel/PGEM
	}
	//=============================================================================================

	//=============================================================================================
	private Widget focused = null;
	//=============================================================================================
	
	//=============================================================================================
	public Widget focused(Msg msg, Widget hovering) {
		if (msg.type.equals(PTR_PRESSED)) {
			if (hovering.renders.contains(Render.FOCUS)) {
				if (focused != hovering) {
					if (focused != null) {
						focused.interactData.focus = false;
					}
					focused = hovering;
					if (focused != null) {
						focused.interactData.focus = true;
					}
				}
			}
		}
		return focused;
	}
	//=============================================================================================

	//=============================================================================================
	private void clicked(Msg msg, Widget focused) {
		if (!msg.type.equals(PTR_PRESSED)) return;
		if (!msg.button.equals(Button.PTR_BTN1)) return;
		if (focused.interactData.onClick == null) return;
		switch (focused.interactData.onClick) {
			case "quit" -> postQuit();
		}
	}
	//=============================================================================================

	//=============================================================================================
	private void postQuit() {
		for (var hub : msghubs) {
			Msg msg = hub.allocate();
			msg.type = MsgType.WND_CLOSE;
			hub.post(msg);
		}
	}
	//=============================================================================================
	
	//=============================================================================================
	private void handleResize(Msg msg) {
		root.size(msg.size);
	}
	//=============================================================================================

	//=============================================================================================
	private List<MsgHub> msghubs = new ArrayList<>();
	//=============================================================================================

	//=============================================================================================
	public void hook(MsgHub msgHub) {
<<<<<<< HEAD
		msghubs.add(msgHub);
		msgHub.link(KBD_PRESSED, this::handleKeyboard);
		msgHub.link(KBD_RELEASED, this::handleKeyboard);
		msgHub.link(KBD_TYPED, this::handleKeyboard);
		msgHub.link(PTR_CLICKED, this::handlePointer);
		msgHub.link(PTR_PRESSED, this::handlePointer);
		msgHub.link(PTR_RELEASED, this::handlePointer);
		msgHub.link(PTR_MOVED, this::handlePointer);
=======
		msgHub.link(KBD_PRESSED, this::handleMessage);
		msgHub.link(KBD_RELEASED, this::handleMessage);
		msgHub.link(KBD_TYPED, this::handleMessage);
		msgHub.link(PTR_CLICKED, this::handleMessage);
		msgHub.link(PTR_PRESSED, this::handleMessage);
		msgHub.link(PTR_RELEASED, this::handleMessage);
		msgHub.link(PTR_MOVED, this::handleMessage);
>>>>>>> branch 'main' of https://github.com/phreichel/PGEM
		msgHub.link(WND_RESIZE, this::handleResize);
	}
	//=============================================================================================

	//=============================================================================================
	public void unhook(MsgHub msgHub) {
<<<<<<< HEAD
		msghubs.remove(msgHub);
		msgHub.unlink(KBD_PRESSED, this::handleKeyboard);
		msgHub.unlink(KBD_RELEASED, this::handleKeyboard);
		msgHub.unlink(KBD_TYPED, this::handleKeyboard);
		msgHub.unlink(PTR_CLICKED, this::handlePointer);
		msgHub.unlink(PTR_PRESSED, this::handlePointer);
		msgHub.unlink(PTR_RELEASED, this::handlePointer);
		msgHub.unlink(PTR_MOVED, this::handlePointer);
=======
		msgHub.unlink(KBD_PRESSED, this::handleMessage);
		msgHub.unlink(KBD_RELEASED, this::handleMessage);
		msgHub.unlink(KBD_TYPED, this::handleMessage);
		msgHub.unlink(PTR_CLICKED, this::handleMessage);
		msgHub.unlink(PTR_PRESSED, this::handleMessage);
		msgHub.unlink(PTR_RELEASED, this::handleMessage);
		msgHub.unlink(PTR_MOVED, this::handleMessage);
>>>>>>> branch 'main' of https://github.com/phreichel/PGEM
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

//*************************************************************************************************
package pgem.gui;
//*************************************************************************************************

import java.util.ArrayList;
import java.util.List;

import pgem.msg.Msg;
import pgem.msg.WindowData;
import pgem.paint.Graphics;
import pgem.paint.Painter;

//*************************************************************************************************
public class GUI implements Painter {

	//=============================================================================================
	private final List<Shortcut> shortcuts = new ArrayList<>();
	private final Style style = new Style();	
	private final Root root = Root.createRoot(style, this);
	private Widget<?> focused = null;
	//=============================================================================================

	//=============================================================================================
	public Widget<?> root() {
		return root;
	}
	//=============================================================================================

	//=============================================================================================
	public Widget<?> focused() {
		return focused;
	}
	//=============================================================================================

	//=============================================================================================
	public void focused(Widget<?> focused) {
		if (this.focused == focused) return;
		if (this.focused != null) this.focused.flag(Flag.FOCUSED, false);
		this.focused = focused;
		if (this.focused != null) this.focused.flag(Flag.FOCUSED, true);
	}
	//=============================================================================================
	
	//=============================================================================================
	public Style style() {
		return style;
	}
	//=============================================================================================

	//=============================================================================================
	public void paint(Graphics g) {

		init(g);
		
		g.surface();
		root.paint(g);
		
	}
	//=============================================================================================

	//=============================================================================================
	private boolean initialized = false;
	//=============================================================================================

	//=============================================================================================
	private void init(Graphics g) {
		
		if (initialized) return;
		initialized = true;
		
		g.fontInit(Font.DEFAULT_16.name(), "Aptos Black PLAIN 16");
		g.fontInit(Font.DEFAULT_18.name(), "Aptos Black PLAIN 18");
		g.fontInit(Font.DEFAULT_20.name(), "Aptos Black PLAIN 20");
		g.fontInit(Font.GARAMOND_18.name(), "Garamond PLAIN 20");
		
		g.imageInit(Icon.ARROW_DOWN.name(), "data/icons/arrowdown.png");
		g.imageInit(Icon.ARROW_UP.name(), "data/icons/arrowup.png");
		g.imageInit(Icon.CLOSE.name(), "data/icons/close.png");
		g.imageInit(Icon.DESK.name(), "data/icons/desk_inv.png");
		g.imageInit(Icon.FULL_SCREEN.name(), "data/icons/fullscreen.png");
		g.imageInit(Icon.PLUS.name(), "data/icons/plus.png");
		g.imageInit(Icon.SHUT_DOWN.name(), "data/icons/shutdown_inv.png");
		g.imageInit(Icon.SIZE.name(), "data/icons/size.png");
		
	}
	//=============================================================================================
	
	//=============================================================================================
	public void handleShortcuts(Msg msg) {
		for (var shortcut : shortcuts) {
			shortcut.update(msg);
		}
	}
	//=============================================================================================

	//=============================================================================================
	public void shortcut(Shortcut sc) {
		shortcuts.add(sc);
	}
	//=============================================================================================

	//=============================================================================================
	public void shortcut(Action action, pgem.msg.Button button, pgem.msg.Button ... modifiers) {
		var sc = new Shortcut(action, button, modifiers);
		shortcuts.add(sc);
	}
	//=============================================================================================

	//=============================================================================================
	public List<Shortcut> shortcuts() {
		return shortcuts;
	}
	//=============================================================================================
	
	//=============================================================================================
	public void handleInput(Msg msg) {
		root.handle(msg);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void handleResize(Msg msg) {
		WindowData data = (WindowData) msg.data;
		root.size(data.size);
	}
	//=============================================================================================
	
}
//*************************************************************************************************

//*************************************************************************************************
package pgem.gui;
//*************************************************************************************************

import javax.vecmath.Vector2f;

import pgem.msg.Msg;
import pgem.msg.WindowData;
import pgem.paint.Graphics;
import pgem.paint.Painter;

//*************************************************************************************************
public class GUI implements Painter {

	//=============================================================================================
	private final Style style = new Style();
	private final Root  root  = new Root(this);
	//=============================================================================================

	//=============================================================================================
	public Widget root() {
		return root;
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
		root.style(style);
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
		g.imageInit(Icon.DESK.name(), "data/icons/desk.png");
		g.imageInit(Icon.FULL_SCREEN.name(), "data/icons/fullscreen.png");
		g.imageInit(Icon.PLUS.name(), "data/icons/plus.png");
		g.imageInit(Icon.SHUT_DOWN.name(), "data/icons/shutdown.png");
		g.imageInit(Icon.SIZE.name(), "data/icons/size.png");
		
	}
	//=============================================================================================

	//=============================================================================================
	public void handleInput(Msg msg) {
		var offset = new Vector2f(0, 0);
		root.handle(msg, offset);
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

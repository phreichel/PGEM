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
	public static final String TITLE_FONT = "TITLE_FONT";
	//=============================================================================================

	//=============================================================================================
	public static final String ARROW_DOWN_IMAGE = "ARROW_DOWN";
	public static final String ARROW_UP_IMAGE = "ARROW_UP";
	public static final String CLOSE_IMAGE = "CLOSE";
	public static final String DESK_IMAGE = "DESK";
	public static final String FULL_SCREEN_IMAGE = "FULL_SCREEN";
	public static final String PLUS_IMAGE = "PLUS";
	public static final String SHUT_DOWN_IMAGE = "SHUT_DOWN";
	public static final String SIZE_IMAGE = "SIZE";
	//=============================================================================================
	
	//=============================================================================================
	private final Widget root = new Widget(true);
	//=============================================================================================

	//=============================================================================================
	public Widget root() {
		return root;
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
		
		g.fontInit(TITLE_FONT, "Aptos Black PLAIN 18");
		
		g.imageInit(ARROW_DOWN_IMAGE, "data/icons/arrowdown.png");
		g.imageInit(ARROW_UP_IMAGE, "data/icons/arrowup.png");
		g.imageInit(CLOSE_IMAGE, "data/icons/close.png");
		g.imageInit(DESK_IMAGE, "data/icons/desk.png");
		g.imageInit(FULL_SCREEN_IMAGE, "data/icons/fullscreen.png");
		g.imageInit(PLUS_IMAGE, "data/icons/plus.png");
		g.imageInit(SHUT_DOWN_IMAGE, "data/icons/shutdown.png");
		g.imageInit(SIZE_IMAGE, "data/icons/size.png");
		
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

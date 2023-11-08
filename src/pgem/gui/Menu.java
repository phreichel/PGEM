//*************************************************************************************************
package pgem.gui;
//*************************************************************************************************

import pgem.msg.Axis;
import pgem.msg.InputData;
import pgem.msg.Msg;
import pgem.msg.MsgType;
import pgem.paint.Graphics;

//*************************************************************************************************
public class Menu extends Widget<Menu> {

	//=============================================================================================
	public static final Menu createMenu(Style style) {
		return new Menu(style);
	}
	//=============================================================================================
	
	//=============================================================================================
	public Menu(Style style) {
		super(style, true);
	}
	//=============================================================================================

	//=============================================================================================
	public Menu add(MenuItem item) {
		item.parent(this);
		layout();
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public Menu add(Icon icon, String label) {
		var item = MenuItem.createMenuItem(style(), icon, label);
		add(item);
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public Menu add(Icon icon, String label, Action action) {
		var item = MenuItem
			.createMenuItem(style(), icon, label)
			.action(action);
		add(item);
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public SubMenu addSubMenu(Icon icon, String label) {
		var item = SubMenu.createSubMenu(style(), icon, label);
		add(item);
		return item;
	}
	//=============================================================================================
	
	//=============================================================================================
	public Menu layout() {
		float w = 0;
		float h = 0;
		for (int i=0; i<children().size(); i++) {
			var child = children().get(i);
			if (child instanceof MenuItem mi) mi.layout();
			if (!child.flag(Flag.HIDDEN)) {
				w = Math.max(w, child.size().x);
			}
		}
		for (int i=0; i<children().size(); i++) {
			var child = children().get(i);
			if (!child.flag(Flag.HIDDEN)) {
				child.position(0, h);
				child.size(w, child.size().y);
				h += child.size().y;
			}
		}
		size(w, h);
		return this;
	}
	//=============================================================================================
	
	//=============================================================================================
	public void handleWidget(Msg msg) {

		if (msg.consumed) return;
		
		if (MsgType.POINTER_MASK.contains(msg.type)) {
			var data = msg.data(InputData.class);
			float px = data.axes.get(Axis.POINTER_HORIZONTAL);
			float py = data.axes.get(Axis.POINTER_VERTICAL);
			if (containsScreen(px, py)) {
				msg.consumed = true;
			}
		}
	}
	//=============================================================================================
	
	//=============================================================================================
	protected void paintWidget(Graphics g) {
		var bg = style().get(StyleColor.MENU_BACKGROUND); 
		var bd = style().get(StyleColor.MENU_BORDER);
		g.color(bg);
		g.box(true, ORIGIN, size());
		g.color(bd);
		g.box(false, ORIGIN, size());
	}
	//=============================================================================================

	//=============================================================================================
	public void closeSubMenus() {
		for (var c : children()) {
			if (c instanceof SubMenu sm) {
				sm.menu().flag(Flag.HIDDEN, true);
			}
		}
	}
	//=============================================================================================
	
}
//*************************************************************************************************

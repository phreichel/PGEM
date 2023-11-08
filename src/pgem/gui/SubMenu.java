//*************************************************************************************************
package pgem.gui;
//*************************************************************************************************

import pgem.msg.Axis;
import pgem.msg.InputData;
import pgem.msg.Msg;
import pgem.msg.MsgType;

//*************************************************************************************************
public class SubMenu extends MenuItem {

	//=============================================================================================
	public static final SubMenu createSubMenu(Style style, Icon icon, String label) {
		return new SubMenu(style, icon, label);
	}
	//=============================================================================================
	
	//=============================================================================================
	public static final SubMenu createSubMenu(Style style, Icon icon, String label, Menu menu) {
		return new SubMenu(style, icon, label, menu);
	}
	//=============================================================================================
	
	//=============================================================================================
	private Menu menu;
	//=============================================================================================
	
	//=============================================================================================
	public SubMenu(Style style, Icon icon, String label, Menu menu) {
		super(style, icon, label);
		this.menu = menu;
		this.menu.flag(Flag.HIDDEN, true);
		this.menu.parent(this);
	}
	//=============================================================================================

	//=============================================================================================
	public SubMenu(Style style, Icon icon, String label) {
		super(style, icon, label);
		this.menu = Menu.createMenu(style);
		this.menu.flag(Flag.HIDDEN, true);
		this.menu.parent(this);
	}
	//=============================================================================================
	
	//=============================================================================================
	public Menu menu() {
		return this.menu;
	}
	//=============================================================================================
	
	//=============================================================================================
	public void handleWidget(Msg msg) {

		if (MsgType.POINTER_MASK.contains(msg.type)) {

			var data = msg.data(InputData.class);
			float px = data.axes.get(Axis.POINTER_HORIZONTAL);
			float py = data.axes.get(Axis.POINTER_VERTICAL);
			
			if (msg.type.equals(MsgType.POINTER_PRESSED)) {
				menu.flag(Flag.HIDDEN, true);
			}
			
			if (!msg.consumed && containsScreen(px, py)) {
				msg.consumed = true;
				flag(Flag.ARMED, true);
				if (parent() instanceof Menu m) {
					m.closeSubMenus();
				}
				menu.position(size().x - 5, 15-menu.size().y);
				menu.flag(Flag.HIDDEN, false);
			} else {
				flag(Flag.ARMED, false);
			}
			
		}

	}
	//=============================================================================================
	
}
//*************************************************************************************************

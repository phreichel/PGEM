//*************************************************************************************************
package pgem.gui;
//*************************************************************************************************

import pgem.msg.Axis;
import pgem.msg.Button;
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
		if (flag(Flag.FOCUSED) && msg.type.equals(MsgType.KEY_PRESSED)) {
			if (msg.consumed) return;
			var data = msg.data(InputData.class);
			if (parent() != null && parent() instanceof Menu m) {
				if (data.button.equals(Button.KEY_UP)) {
					msg.consumed = true;
					int idx = m.children().indexOf(this);
					if (idx < m.children().size()-1) {
						m.children().get(idx+1).focus();
					}
				} else if (data.button.equals(Button.KEY_DOWN)) {
					msg.consumed = true;
					int idx = m.children().indexOf(this);
					if (idx > 0) {
						m.children().get(idx-1).focus();
					}
				} else if (data.button.equals(Button.KEY_LEFT)) {
					if (m.parent() != null && m.parent() instanceof SubMenu psm) {
						psm.focus();
						m.closeSubMenus();
						m.flag(Flag.HIDDEN, true);
					}
				}
			}
			if (data.button.equals(Button.KEY_RIGHT) || data.button.equals(Button.KEY_ENTER)) {
				msg.consumed = true;
				if (parent() instanceof Menu m) {
					m.closeSubMenus();
				}
				menu.position(size().x - 5, 15-menu.size().y);
				menu.flag(Flag.HIDDEN, false);
				menu.children().get(menu.children().size()-1).focus();
			}
		} else if (MsgType.POINTER_MASK.contains(msg.type)) {
			var data = msg.data(InputData.class);
			float px = data.axes.get(Axis.POINTER_HORIZONTAL);
			float py = data.axes.get(Axis.POINTER_VERTICAL);
			if (msg.type.equals(MsgType.POINTER_PRESSED)) {
				menu.flag(Flag.HIDDEN, true);
			}
			if (!msg.consumed && containsScreen(px, py)) {
				focus();
				msg.consumed = true;
				if (parent() instanceof Menu m) {
					m.closeSubMenus();
				}
				menu.position(size().x - 5, 15-menu.size().y);
				menu.flag(Flag.HIDDEN, false);
			}
		}
	}
	//=============================================================================================
	
}
//*************************************************************************************************

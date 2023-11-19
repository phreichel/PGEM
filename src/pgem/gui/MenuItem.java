//*************************************************************************************************
package pgem.gui;
//*************************************************************************************************

import pgem.msg.Axis;
import pgem.msg.Button;
import pgem.msg.InputData;
import pgem.msg.Msg;
import pgem.msg.MsgType;
import pgem.paint.Graphics;

//*************************************************************************************************
public class MenuItem extends Widget<MenuItem> {

	//=============================================================================================
	public static final MenuItem createMenuItem(Style style, Icon icon, String label) {
		return new MenuItem(style, icon, label);
	}
	//=============================================================================================
	
	//=============================================================================================
	private Image icon;
	private Label label;
	//=============================================================================================

	//=============================================================================================
	private Action action = null;
	//=============================================================================================
	
	//=============================================================================================
	public MenuItem(Style style, Icon icon, String label) {
		super(style, true);
		action = this::action;
		size(120, 20);
		this.icon = Image
			.createImage(style, icon)
			.color(style().get(StyleColor.MENU_ITEM_ICON_COLOR))
			.flag(Flag.HIDDEN, icon == null)
			.position(0, 0)
			.size(20, 20)
			.parent(this);		
		this.label = Label
			.createLabel(style, label)
			.align(Align.START)
			.font(style().get(StyleFont.MENU_ITEM_LABEL).name())
			.color(style().get(StyleColor.MENU_ITEM_LABEL_COLOR))
			.flag(Flag.HIDDEN, label == null)
			.position(25, 0)
			.size(95, 20)
			.parent(this);
	}
	//=============================================================================================

	//=============================================================================================
	public MenuItem icon(Icon icon) {
		if (icon != null) {
			this.icon.image(icon.name());
			this.label.flag(Flag.HIDDEN, false);
		} else {
			this.icon.image(null);
			this.label.flag(Flag.HIDDEN, true);
		}
		return this;
	}
	//=============================================================================================
	
	//=============================================================================================
	public MenuItem label(String label) {
		this.label.text(label);
		if (label != null) {
			this.label.flag(Flag.HIDDEN, false);
		} else {
			this.label.flag(Flag.HIDDEN, true);
		}
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public MenuItem layout() {
		float w = 0;
		float h = 0;
		if (!this.icon.flag(Flag.HIDDEN)) {
			w += this.icon.size().x;
			h = Math.max(h, this.icon.size().y);
		}
		if (!this.label.flag(Flag.HIDDEN)) {
			w += this.label.size().x;
			h = Math.max(h, this.label.size().y);
		}
		this.size(w, h);
		return this;
	}
	//=============================================================================================
	
	//=============================================================================================
	public void handleWidget(Msg msg) {
		if (MsgType.POINTER_MASK.contains(msg.type)) {
			var data = msg.data(InputData.class);
			float px = data.axes.get(Axis.POINTER_HORIZONTAL);
			float py = data.axes.get(Axis.POINTER_VERTICAL);
			if (!msg.consumed && containsScreen(px, py)) {
				msg.consumed = true;
				focus();
				if (parent() instanceof Menu m) {
					m.closeSubMenus();
				}
				if (msg.type.equals(MsgType.POINTER_PRESSED)) {
					if (data.button.equals(pgem.msg.Button.POINTER_1)) {
						triggerAction(msg);
					}
				}
			}
		} else if (flag(Flag.FOCUSED) && msg.type.equals(MsgType.KEY_PRESSED)) {
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
					msg.consumed = true;
					if (m.parent() != null && m.parent() instanceof SubMenu psm) {
						psm.focus();
						m.closeSubMenus();
						m.flag(Flag.HIDDEN, true);
					}
				} else if (data.button.equals(Button.KEY_ENTER)) {
					msg.consumed = true;
					triggerAction(msg);
				}
			}
		}
	}
	//=============================================================================================
	
	//=============================================================================================
	protected void paintWidget(Graphics g) {
		var bg = style().get(StyleColor.MENU_ITEM_BACKGROUND); 
		var ba = style().get(StyleColor.MENU_ITEM_BACKGROUND_FOCUSED);
		var bd = style().get(StyleColor.MENU_ITEM_BORDER);
		g.color(flag(Flag.FOCUSED) ? ba : bg);
		g.rectangle(true, ORIGIN, size());
		g.color(bd);
		g.rectangle(false, ORIGIN, size());
	}
	//=============================================================================================

	//=============================================================================================
	public Action action() {
		return action;
	}
	//=============================================================================================

	//=============================================================================================
	public MenuItem action(Action action) {
		this.action = action;
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	protected void action(Widget<?> widget, Msg msg) {}
	//=============================================================================================
	
	//=============================================================================================
	private void triggerAction(Msg msg) {
		this.action.perform(this, msg);
	}
	//=============================================================================================
	
}
//*************************************************************************************************

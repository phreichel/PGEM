//*************************************************************************************************
package pgem.gui;
//*************************************************************************************************

import pgem.msg.Axis;
import pgem.msg.InputData;
import pgem.msg.Msg;
import pgem.msg.MsgType;
import pgem.paint.Graphics;

//*************************************************************************************************
public class Button extends Widget<Button> {

	//=============================================================================================
	public static final Button createButton(Style style) {
		return new Button(style);
	}
	//=============================================================================================
	
	//=============================================================================================
	public static final Button createButton(Style style, String label) {
		return new Button(style, label);
	}
	//=============================================================================================

	//=============================================================================================
	private Action action = null;
	//=============================================================================================
	
	//=============================================================================================
	public Button(Style style) {
		super(style, true);
		action = this::action;
	}
	//=============================================================================================

	//=============================================================================================
	public Button(Style style, String label) {
		super(style, true);
		action = this::action;
		Label labelWidget = Label.createLabel(style);
		labelWidget.text(label);
		labelWidget.align(Align.CENTER);
		labelWidget.dock(Dock.SCALE);
		labelWidget.parent(this);
	}
	//=============================================================================================
	
	//=============================================================================================
	public Action action() {
		return action;
	}
	//=============================================================================================

	//=============================================================================================
	public Button action(Action action) {
		this.action = action;
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	protected void action(Widget<?> widget, Msg msg) {}
	//=============================================================================================
	
	//=============================================================================================
	protected void paintWidget(Graphics g) {
		var st = style();
		var bg = st.get(StyleColor.BUTTON_BACKGROUND);
		var bl = st.get(StyleColor.BUTTON_BORDER_LIGHT);
		var bd = st.get(StyleColor.BUTTON_BORDER_DARK);
		g.color(bg);
		g.rectangle(true, ORIGIN, size());
		var s = size(); 
		if (flag(Flag.ARMED)) {
			g.color(bl);
			g.lines(false, 0, 0, 0, s.y, s.x, s.y);
			g.color(bd);
			g.lines(false, 0, 0, s.x, 0, s.x, s.y);
		} else {
			g.color(bd);
			g.lines(false, 0, 0, 0, s.y, s.x, s.y);
			g.color(bl);
			g.lines(false, 0, 0, s.x, 0, s.x, s.y);
		}
	}
	//=============================================================================================

	//=============================================================================================
	public void handleWidget(Msg msg) {

		if (msg.type.equals(MsgType.POINTER_RELEASED)) {
			var data = msg.data(InputData.class);
			if (data.button.equals(pgem.msg.Button.POINTER_1)) {
				flag(Flag.ARMED, false);				
			}
		}

		if (msg.consumed) return;
		
		if (MsgType.POINTER_MASK.contains(msg.type)) {
			var data = msg.data(InputData.class);
			float px = data.axes.get(Axis.POINTER_HORIZONTAL);
			float py = data.axes.get(Axis.POINTER_VERTICAL);
			if (containsScreen(px, py)) {
				msg.consumed = true;
				if (msg.type.equals(MsgType.POINTER_PRESSED)) {
					focus();
					if (data.button.equals(pgem.msg.Button.POINTER_1)) {
						flag(Flag.ARMED, true);				
					}
				}
				else if (msg.type.equals(MsgType.POINTER_RELEASED)) {
					if (data.button.equals(pgem.msg.Button.POINTER_1)) {
							triggerAction(msg);
					}
				}
			}
		}
	}
	//=============================================================================================

	//=============================================================================================
	private void triggerAction(Msg msg) {
		this.action.perform(this, msg);
	}
	//=============================================================================================
	
}
//*************************************************************************************************

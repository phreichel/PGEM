//*************************************************************************************************
package pgem.gui;
//*************************************************************************************************

import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Vector2f;

import pgem.msg.Axis;
import pgem.msg.InputData;
import pgem.msg.Msg;
import pgem.msg.MsgType;
import pgem.paint.Graphics;

//*************************************************************************************************
public class Button extends Widget<Button> {

	//=============================================================================================
	private static final Vector2f ORIGIN = new Vector2f(0, 0);
	//=============================================================================================

	//=============================================================================================
	public static final Button createButton() {
		return new Button();
	}
	//=============================================================================================
	
	//=============================================================================================
	public static final Button createButton(String label) {
		return new Button(label);
	}
	//=============================================================================================

	//=============================================================================================
	private Color4f borderLight = new Color4f();
	private Color4f borderDark = new Color4f();
	private Color4f background = new Color4f();
	//=============================================================================================

	//=============================================================================================
	private Action action = null;
	//=============================================================================================
	
	//=============================================================================================
	public Button() {
		super(true);
		action = this::action;
	}
	//=============================================================================================

	//=============================================================================================
	public Button(String label) {
		super(true);
		action = this::action;
		Label labelWidget = new Label();
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
	public Color4f background() {
		return background;
	}
	//=============================================================================================

	//=============================================================================================
	public Button background(Color3f background) {
		this.background.set(
			background.x,
			background.y,
			background.z,
			1);
		return this; 
	}
	//=============================================================================================
	
	//=============================================================================================
	public Button background(Color4f background) {
		this.background.set(
			background.x,
			background.y,
			background.z,
			background.w);
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public Button background(float r, float g, float b) {
		this.background.set(r, g, b, 1);
		return this;
	}
	//=============================================================================================
	
	//=============================================================================================
	public Button background(float r, float g, float b, float a) {
		this.background.set(r, g, b, a);
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public Color4f borderLight() {
		return borderLight;
	}
	//=============================================================================================

	//=============================================================================================
	public Button borderLight(Color3f borderLight) {
		this.borderLight.set(
			borderLight.x,
			borderLight.y,
			borderLight.z,
			1);
		return this;
	}
	//=============================================================================================
	
	//=============================================================================================
	public Button borderLight(Color4f borderLight) {
		this.borderLight.set(
			borderLight.x,
			borderLight.y,
			borderLight.z,
			borderLight.w);
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public Button borderLight(float r, float g, float b) {
		this.borderLight.set(r, g, b, 1);
		return this;
	}
	//=============================================================================================
	
	//=============================================================================================
	public Button borderLight(float r, float g, float b, float a) {
		this.borderLight.set(r, g, b, a);
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public Color4f borderDark() {
		return borderDark;
	}
	//=============================================================================================

	//=============================================================================================
	public Button borderDark(Color3f borderDark) {
		this.borderDark.set(
			borderDark.x,
			borderDark.y,
			borderDark.z,
			1);
		return this;
	}
	//=============================================================================================
	
	//=============================================================================================
	public Button borderDark(Color4f borderDark) {
		this.borderDark.set(
			borderDark.x,
			borderDark.y,
			borderDark.z,
			borderDark.w);
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public Button borderDark(float r, float g, float b) {
		this.borderDark.set(r, g, b, 1);
		return this;
	}
	//=============================================================================================
	
	//=============================================================================================
	public Button borderDark(float r, float g, float b, float a) {
		this.borderDark.set(r, g, b, a);
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	protected void styleWidget(Style style) {
		background(style.get(StyleColor.BUTTON_BACKGROUND));
		borderDark(style.get(StyleColor.BUTTON_BORDER_DARK));
		borderLight(style.get(StyleColor.BUTTON_BORDER_LIGHT));
	}
	//=============================================================================================
	
	//=============================================================================================
	protected void paintWidget(Graphics g) {
		g.color(background);
		g.box(true, ORIGIN, size());
		var s = size(); 
		if (flag(GUIFlag.ARMED)) {
			g.color(borderLight);
			g.lines(false, 0, 0, 0, s.y, s.x, s.y);
			g.color(borderDark);
			g.lines(false, 0, 0, s.x, 0, s.x, s.y);
		} else {
			g.color(borderDark);
			g.lines(false, 0, 0, 0, s.y, s.x, s.y);
			g.color(borderLight);
			g.lines(false, 0, 0, s.x, 0, s.x, s.y);
		}
	}
	//=============================================================================================

	//=============================================================================================
	public void handleWidget(Msg msg) {

		if (msg.type.equals(MsgType.POINTER_RELEASED)) {
			var data = msg.data(InputData.class);
			if (data.button.equals(pgem.msg.Button.POINTER_1)) {
				flag(GUIFlag.ARMED, false);				
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
						flag(GUIFlag.ARMED, true);				
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

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
public class Button extends Widget {

	//=============================================================================================
	private static final Vector2f ORIGIN = new Vector2f(0, 0);
	//=============================================================================================
	
	//=============================================================================================
	private Color4f borderLight = new Color4f();
	private Color4f borderDark = new Color4f();
	private Color4f background = new Color4f();
	//=============================================================================================

	//=============================================================================================
	private boolean armed = false;
	private Action action = null;
	//=============================================================================================
	
	//=============================================================================================
	public Button() {
		super(true);
		action = this::action;
	}
	//=============================================================================================

	//=============================================================================================
	public Action action() {
		return action;
	}
	//=============================================================================================

	//=============================================================================================
	public void action(Action action) {
		this.action = action;
	}
	//=============================================================================================

	//=============================================================================================
	protected void action(Widget widget, Msg msg) {}
	//=============================================================================================
	
	//=============================================================================================
	public Color4f background() {
		return background;
	}
	//=============================================================================================

	//=============================================================================================
	public void background(Color3f background) {
		this.background.set(
			background.x,
			background.y,
			background.z,
			1);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void background(Color4f background) {
		this.background.set(
			background.x,
			background.y,
			background.z,
			background.w);
	}
	//=============================================================================================

	//=============================================================================================
	public void background(float r, float g, float b) {
		this.background.set(r, g, b, 1);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void background(float r, float g, float b, float a) {
		this.background.set(r, g, b, a);
	}
	//=============================================================================================

	//=============================================================================================
	public Color4f borderLight() {
		return borderLight;
	}
	//=============================================================================================

	//=============================================================================================
	public void borderLight(Color3f borderLight) {
		this.borderLight.set(
			borderLight.x,
			borderLight.y,
			borderLight.z,
			1);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void borderLight(Color4f borderLight) {
		this.borderLight.set(
			borderLight.x,
			borderLight.y,
			borderLight.z,
			borderLight.w);
	}
	//=============================================================================================

	//=============================================================================================
	public void borderLight(float r, float g, float b) {
		this.borderLight.set(r, g, b, 1);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void borderLight(float r, float g, float b, float a) {
		this.borderLight.set(r, g, b, a);
	}
	//=============================================================================================

	//=============================================================================================
	public Color4f borderDark() {
		return borderDark;
	}
	//=============================================================================================

	//=============================================================================================
	public void borderDark(Color3f borderDark) {
		this.borderDark.set(
			borderDark.x,
			borderDark.y,
			borderDark.z,
			1);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void borderDark(Color4f borderDark) {
		this.borderDark.set(
			borderDark.x,
			borderDark.y,
			borderDark.z,
			borderDark.w);
	}
	//=============================================================================================

	//=============================================================================================
	public void borderDark(float r, float g, float b) {
		this.borderDark.set(r, g, b, 1);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void borderDark(float r, float g, float b, float a) {
		this.borderDark.set(r, g, b, a);
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
		if (!armed) {
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
				armed = false;
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
						armed = true;
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

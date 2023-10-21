//*************************************************************************************************
package pgem.gui;
//*************************************************************************************************

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.vecmath.Color4f;
import javax.vecmath.Vector2f;

//*************************************************************************************************
public class Widget {

	//=============================================================================================
	private Widget parent = null;
	private final List<Widget> children = new ArrayList<>();
	//=============================================================================================
	
	//=============================================================================================
	private final Vector2f position = new Vector2f();
	private final Vector2f size = new Vector2f(800, 600);
	//=============================================================================================

	//=============================================================================================
	public final Set<Render> renders = EnumSet.noneOf(Render.class);
	public final Dock dock = new Dock();
	public final Color4f textColor = new Color4f(0f, 0f, 0f, 1f);
	public final Color4f borderColor = new Color4f(.8f, .8f, .8f, 1f);
	public final Color4f backgroundColor = new Color4f(.9f, .9f, .9f, 1f);
	public final Color4f imageColor = new Color4f(1f, 1f, 1f, 1f);
	public Layout layout = Layout.NONE;
	//=============================================================================================

	//=============================================================================================
	private String font = null;
	private String text = null;
	private String image = null;
	//=============================================================================================
	
	//=============================================================================================
	public Widget parent() {
		return parent;
	}
	//=============================================================================================

	//=============================================================================================
	public void parent(Widget parent) {
		if (this.parent == parent) return;
		if (this.parent != null) this.parent.children.remove(this);
		this.parent = parent;
		if (this.parent != null) this.parent.children.add(this);
	}
	//=============================================================================================
	
	//=============================================================================================
	public List<Widget> children() {
		return children;
	}
	//=============================================================================================
	
	//=============================================================================================
	public Vector2f position() {
		return position;
	}
	//=============================================================================================

	//=============================================================================================
	public void position(Vector2f position) {
		this.position.set(position);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void position(float x, float y) {
		position.set(x, y);
	}
	//=============================================================================================
	
	//=============================================================================================
	public Vector2f size() {
		return size;
	}
	//=============================================================================================

	//=============================================================================================
	public void size(Vector2f size) {
		this.size(size.x, size.y);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void size(float width, float height) {
		float dw = width - this.size.x;
		float dh = height - this.size.y;
		this.size.set(width, height);
		for (var c : this.children) {
			c.adjustSize(dw, dh);
		}
	}
	//=============================================================================================

	//=============================================================================================
	private void adjustSize(float dw, float dh) {
		float nx = this.position.x + dw * dock.left;
		float ny = this.position.y + dh * dock.top;
		float nw = size.x + dw * (dock.right-dock.left);
		float nh = size.y + dh * (dock.bottom-dock.top);
		position(nx, ny);
		size(nw, nh);
	}
	//=============================================================================================

	//=============================================================================================
	public String font() {
		return (font==null) ? "normal" : font;
	}
	//=============================================================================================

	//=============================================================================================
	public void font(String font) {
		this.font = font;
	}
	//=============================================================================================
	
	//=============================================================================================
	public String text() {
		return text;
	}
	//=============================================================================================
	
	//=============================================================================================
	public void text(String font, String text) {
		font(font);
		text(text);
	}
	//=============================================================================================

	//=============================================================================================
	public void text(String text) {
		switch ((text == null) ? 0 : 1) {
			case 0 -> renders.remove(Render.TEXT); 
			case 1 -> renders.add(Render.TEXT);
		}
		this.text = text;
	}
	//=============================================================================================

	//=============================================================================================
	public String image() {
		return image;
	}
	//=============================================================================================

	//=============================================================================================
	public void image(String image) {
		switch ((image == null) ? 0 : 1) {
			case 0 -> renders.remove(Render.IMAGE); 
			case 1 -> renders.add(Render.IMAGE);
		}
		this.image = image;
	}
	//=============================================================================================
	
}
//*************************************************************************************************

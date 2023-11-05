//*************************************************************************************************
package pgem.gui;
//*************************************************************************************************

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.vecmath.Vector2f;

import pgem.msg.Msg;
import pgem.paint.Graphics;

//*************************************************************************************************
public class Widget {

	//=============================================================================================
	private static final List<Widget> EMPTY = Collections.unmodifiableList(new ArrayList<>(0));
	//=============================================================================================
	
	//=============================================================================================
	private final Vector2f position = new Vector2f();
	private final Vector2f size = new Vector2f();
	private final Dock     dock = new Dock(Dock.BOTTOM_LEFT);
	//=============================================================================================

	//=============================================================================================
	private Widget parent = null;
	private final List<Widget> children;
	private final List<Widget> _children;
	//=============================================================================================

	//=============================================================================================
	public Widget() {
		this(false);
	}
	//=============================================================================================

	//=============================================================================================
	public Widget(boolean group) {
		children = group ? new ArrayList<>() : EMPTY;
		_children = group ? Collections.unmodifiableList(children) : EMPTY;
	}
	//=============================================================================================

	//=============================================================================================
	public GUI gui() {
		if (parent == null) return null;
		return parent.gui();
	}
	//=============================================================================================
	
	//=============================================================================================
	public Vector2f position() {
		return this.position;
	}
	//=============================================================================================

	//=============================================================================================
	public void position(Vector2f p) {
		position(p.x, p.y);
	}
	//=============================================================================================

	//=============================================================================================
	public void position(float x, float y) {
		this.position.set(x, y);
	}
	//=============================================================================================

	//=============================================================================================
	public Vector2f screenPosition() {
		return screenPosition(null);
	}
	//=============================================================================================
	
	//=============================================================================================
	public Vector2f screenPosition(Vector2f dst) {
		if (dst == null) dst = new Vector2f(0, 0);
		if (parent != null) parent.screenPosition(dst);
		dst.add(position);
		return dst;
	}
	//=============================================================================================
	
	//=============================================================================================
	public Vector2f size() {
		return this.size;
	}
	//=============================================================================================

	//=============================================================================================
	public void size(Vector2f s) {
		size(s.x, s.y);
	}
	//=============================================================================================

	//=============================================================================================
	public void size(float w, float h) {
		float dx = w - size.x;
		float dy = h - size.y;
		for (int i=0; i<children.size(); i++) {
			var child = children.get(i);
			child.position(
				child.position.x + child.dock.left() * dx,
				child.position.y + child.dock.bottom() * dy);
			child.size(
				child.size.x + (child.dock.right() - child.dock.left()) * dx,
				child.size.y + (child.dock.top() - child.dock.bottom()) * dy);
		}
		this.size.set(w, h);
	}
	//=============================================================================================

	//=============================================================================================
	public Dock dock() {
		return dock;
	}
	//=============================================================================================

	//=============================================================================================
	public void dock(Dock dock) {
		this.dock.set(dock);
	}
	//=============================================================================================

	//=============================================================================================
	public void dock(float left, float right, float top, float bottom) {
		this.dock.set(left, right, top, bottom);
	}
	//=============================================================================================
	
	//=============================================================================================
	public Widget parent() {
		return this.parent;
	}
	//=============================================================================================

	//=============================================================================================
	public void parent(Widget p) {
		if (this.parent == p) return;
		if (this.parent != null) this.parent.children.remove(this);
		this.parent = p;
		if (this.parent != null) this.parent.children.add(0, this);
	}
	//=============================================================================================

	//=============================================================================================
	public List<Widget> children() {
		return _children;
	}
	//=============================================================================================

	//=============================================================================================
	public void style(Style style) {
		for (int i=0; i<children.size(); i++) {
			var child = children.get(i);
			child.style(style);
		}
		styleWidget(style);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void paint(Graphics g) {
		g.push();
		g.translate(position.x, position.y);
		paintWidget(g);
		paintChildren(g);
		g.pop();
	}
	//=============================================================================================

	//=============================================================================================
	protected void styleWidget(Style style) {};
	protected void paintWidget(Graphics g) {};
	//=============================================================================================
	
	//=============================================================================================
	protected void paintChildren(Graphics g) {
		for (int i = children.size()-1; i>=0; i--) {
			var child = children.get(i);
			child.paint(g);
		}
	}
	//=============================================================================================

	//=============================================================================================
	public void handle(Msg msg) {		
		for (int i = 0; i<children.size(); i++) {
			var child = children.get(i);
			child.handle(msg);
		}
		handleWidget(msg);
	}
	//=============================================================================================

	//=============================================================================================
	protected void handleWidget(Msg msg) {}
	//=============================================================================================

	//=============================================================================================
	public boolean contains(Vector2f p) {
		return contains(p.x, p.y);
	}
	//=============================================================================================
	
	//=============================================================================================
	public boolean contains(float x, float y) {
		return
			x >= 0 &&
			y >= 0 &&
			x <= size.x &&
			y <= size.y;
	}
	//=============================================================================================

	//=============================================================================================
	public boolean containsScreen(Vector2f p) {
		return containsScreen(p.x, p.y);
	}
	//=============================================================================================

	//=============================================================================================
	public boolean containsScreen(float screenx, float screeny) {
		Vector2f pos = screenPosition();
		return contains(screenx - pos.x, screeny - pos.y);
	}
	//=============================================================================================

	//=============================================================================================
	public void toTop() {
		if (parent != null) {
			parent.children.remove(this);
			parent.children.add(0, this);
		}
	}
	//=============================================================================================
	
	//=============================================================================================
	public void focus() {
		if (parent != null) {
			parent.focus();
		}
	}
	//=============================================================================================
	
}
//*************************************************************************************************

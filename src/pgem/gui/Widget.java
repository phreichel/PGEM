//*************************************************************************************************
package pgem.gui;
//*************************************************************************************************

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.vecmath.Color4f;
import javax.vecmath.Vector2f;

import pgem.msg.Msg;
import pgem.msg.MsgType;

//*************************************************************************************************
public class Widget {

	//=============================================================================================
	private static final List<Widget> EMPTY = Collections.unmodifiableList(new ArrayList<>(0));
	//=============================================================================================

	//=============================================================================================
	public final Set<GUICap> caps = EnumSet.noneOf(GUICap.class);
	public final Set<GUIFlag> flags = EnumSet.noneOf(GUIFlag.class);
	public final Set<Render> renders = EnumSet.noneOf(Render.class);
	//=============================================================================================
	
	//=============================================================================================
	private Widget parent = null;
	private final List<Widget> children;
	//=============================================================================================

	//=============================================================================================
	private final Vector2f position = new Vector2f();
	private final Vector2f size = new Vector2f(800, 600);
	public final Dock dock = new Dock();
	//=============================================================================================

	//=============================================================================================
	public final Color4f borderColor = new Color4f(.8f, .8f, .8f, 1f);
	public final Color4f backgroundColor = new Color4f(.9f, .9f, .9f, 1f);
	//=============================================================================================
	
	//=============================================================================================
	public final TextData textData = new TextData();
	public final TextEditor textEditor = new TextEditor();
	public final ImageData imageData = new ImageData();
	public final InteractData interactData = new InteractData();
	//=============================================================================================

	//=============================================================================================
	public Widget() {
		this.children = EMPTY;
	}
	//=============================================================================================

	//=============================================================================================
	public Widget(boolean group) {
		this.children = group ? new ArrayList<>(5) : EMPTY;
	}
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
		if (!caps.contains(GUICap.MOVABLE)) return;
		this.position.set(position);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void position(float x, float y) {
		if (!caps.contains(GUICap.MOVABLE)) return;
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
		if (!caps.contains(GUICap.RESIZABLE)) return;
		this.size(size.x, size.y);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void size(float width, float height) {
		if (!caps.contains(GUICap.RESIZABLE)) return;
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
	protected void handle(Vector2f ofs, Msg msg) {
		Vector2f offset = new Vector2f(position());
		offset.add(ofs);
		if (MsgType.PTR_MASK.contains(msg.type)) {
			boolean hover = contains(ofs, size(), msg.pointer);
			flag(GUIFlag.HOVERED, hover);
		}
		if (renders.contains(Render.TEXT) && renders.contains(Render.INTERACT)) {
			textEditor.handle(msg);
			textData.text = textEditor.text();
		}
		for (var child : children()) {
			child.handle(ofs, msg);
		}
	}
	//=============================================================================================

	//=============================================================================================
	private boolean contains(Vector2f offset, Vector2f size, Vector2f pointer) {
		return
			(offset.x <= pointer.x) &&
			(offset.y <= pointer.y) &&
			(offset.x + size.x >= pointer.x) &&
			(offset.y + size.y >= pointer.y);
	}
	//=============================================================================================
	
	//=============================================================================================
	private Widget flag(GUIFlag flag, boolean set) {
		if (set) flags.add(flag);
		else flags.remove(flag);
		return this;
	}
	//=============================================================================================
	
}
//*************************************************************************************************

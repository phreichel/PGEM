//*************************************************************************************************
package pgem.gui2;
//*************************************************************************************************

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import javax.vecmath.Vector2f;

import pgem.X;
import pgem.msg.Msg;

//*************************************************************************************************
public class Element {

	//=============================================================================================
	public static final int FLAG_DISPLAYED  = 0x0001;
	public static final int FLAG_FOCUSABLE  = 0x0002;
	public static final int FLAG_FOCUSED    = 0x0003;
	public static final int FLAG_SELECTABLE = 0x0004;
	public static final int FLAG_SELECTED   = 0x0005;
	//=============================================================================================
	
	//=============================================================================================
	public Handler onPointerEntered   = Element::handle;
	public Handler onPointerExited    = Element::handle;
	public Handler onPointerPressed   = Element::handle;
	public Handler onPointerReleased  = Element::handle;
	public Handler onPointerClicked   = Element::handle;
	//=============================================================================================

	//=============================================================================================
	public Handler onKeyPressed       = Element::handle;
	public Handler onKeyReleased      = Element::handle;
	public Handler onKeyTyped         = Element::handle;
	//=============================================================================================

	//=============================================================================================
	public Handler onParentChange     = Element::handle;
	public Handler onParentResize     = Element::handle;
	public Handler onComponentsChange = Element::handle;
	public Handler onDisplayChange    = Element::handle;
	public Handler onFocusChange      = Element::handle;
	//=============================================================================================

	//=============================================================================================
	public Vector2f position          = new Vector2f();
	public Vector2f extent            = new Vector2f();
	public BitSet   flags             = new BitSet();
	//=============================================================================================

	//=============================================================================================
	public Element parent = null;
	public List<Element> children = new ArrayList<Element>(0);
	//=============================================================================================
	
	//=============================================================================================
	public boolean isDisplayed() {
		return flags.get(FLAG_DISPLAYED);
	}
	//=============================================================================================

	//=============================================================================================
	public void setDisplayed(boolean displayed) {
		setFlag(FLAG_DISPLAYED, displayed);
	}
	//=============================================================================================

	//=============================================================================================
	public boolean isFocusable() {
		return flags.get(FLAG_FOCUSABLE);
	}
	//=============================================================================================

	//=============================================================================================
	public void setFocusable(boolean focusable) {
		setFlag(FLAG_FOCUSABLE, focusable);
	}
	//=============================================================================================
	
	//=============================================================================================
	public boolean isFocused() {
		return flags.get(FLAG_FOCUSED);
	}
	//=============================================================================================

	//=============================================================================================
	public void setFocused(boolean focused) {
		if (!isFocusable()) throw new X("This Element is not focusable");
		setFlag(FLAG_FOCUSED, focused);
	}
	//=============================================================================================

	//=============================================================================================
	public boolean isSelectable() {
		return flags.get(FLAG_SELECTABLE);
	}
	//=============================================================================================

	//=============================================================================================
	public void setSelectable(boolean selectable) {
		setFlag(FLAG_SELECTABLE, selectable);
	}
	//=============================================================================================
	
	//=============================================================================================
	public boolean isSelected() {
		return flags.get(FLAG_SELECTED);
	}
	//=============================================================================================

	//=============================================================================================
	public void setSelected(boolean selected) {
		if (!isSelectable()) throw new X("This Element is not selectable");
		setFlag(FLAG_SELECTED, selected);
	}
	//=============================================================================================

	//=============================================================================================
	public Element getParent() {
		return this.parent;
	}
	//=============================================================================================

	//=============================================================================================
	public void setParent(Element parent) {
		if (this.parent == parent) return;
		if (this.parent != null) {
			this.parent.children.remove(this);
			this.parent.fireHandler(this.parent.onComponentsChange, null);
		}
		this.parent = parent;		
		if (this.parent != null) {
			this.parent.children.add(this);
			this.parent.fireHandler(this.parent.onComponentsChange, null);
		}
		this.parent.fireHandler(onParentChange, null);;
	}
	//=============================================================================================
	
	//=============================================================================================
	protected void fireHandler(Handler handler, Msg msg) {
		handler.handle(this, msg);
	}
	//=============================================================================================
	
	//=============================================================================================
	protected void setFlag(int flag, boolean value) {
		if (value)
			flags.set(flag);
		else
			flags.clear(flag);
	}
	//=============================================================================================

	//=============================================================================================
	private static void handle(Element element, Msg msg) {} 
	//=============================================================================================
	
}
//*************************************************************************************************

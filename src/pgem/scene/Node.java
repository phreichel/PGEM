//*************************************************************************************************
package pgem.scene;
//*************************************************************************************************

import java.util.ArrayList;
import java.util.List;

import pgem.paint.Graphics;

//*************************************************************************************************
public class Node {

	//=============================================================================================
	private Node parent = null;
	public final List<Node> children = new ArrayList<>();
	//=============================================================================================

	//=============================================================================================
	public Node() {}
	//=============================================================================================

	//=============================================================================================
	public Node(Node parent) {
		this.parent(parent);
	}
	//=============================================================================================
	
	//=============================================================================================
	public Scene scene() {
		if (parent == null) return null;
		return parent.scene();
	}
	//=============================================================================================
	
	//=============================================================================================
	public Node parent() {
		return parent;
	}
	//=============================================================================================

	//=============================================================================================
	public void parent(Node parent) {
		if (this.parent == parent) return;
		if (this.parent != null) this.parent.children.remove(this);
		this.parent = parent;
		if (this.parent != null) this.parent.children.add(this);
	}
	//=============================================================================================

	//=============================================================================================
	public List<Node> children() {
		return children;
	}
	//=============================================================================================

	//=============================================================================================
	public void paint(Graphics g) {
		paintNode(g);
		paintChildren(g);
	}
	//=============================================================================================

	//=============================================================================================
	protected void paintNode(Graphics g) {}
	//=============================================================================================

	//=============================================================================================
	protected void paintChildren(Graphics g) {
		for (int i=0; i<children.size(); i++) {
			var child = children.get(i);
			child.paint(g);
		}
	}
	//=============================================================================================
	
}
//*************************************************************************************************

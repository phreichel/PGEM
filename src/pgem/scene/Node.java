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
		g.push();
		paintNode(g);
		paintChildren(g);
		g.pop();
	}
	//=============================================================================================

	//=============================================================================================
	protected void paintNode(Graphics g) {}
	//=============================================================================================

	//=============================================================================================
	protected void paintChildren(Graphics g) {
		for (int i=0; i<children.size(); i++) {
			var child = children.get(0);
			child.paint(g);
		}
	}
	//=============================================================================================
	
}
//*************************************************************************************************

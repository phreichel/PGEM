//*************************************************************************************************
package pgem.qtree;
//*************************************************************************************************

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector2f;

//*************************************************************************************************
public class QNode {

	//=============================================================================================
	public static final int MAX_DEPTH   = 10;
	public static final int MAX_OBJECTS = 10;
	//=============================================================================================

	//=============================================================================================
	private final Vector2f lo = new Vector2f();
	private final Vector2f hi = new Vector2f();
	//=============================================================================================
	
	//=============================================================================================
	private int     depth;
	private QNode   parent;
	private QNode[] children;
	//=============================================================================================	

	//=============================================================================================	
	private final List<QObject> objects = new ArrayList<>();
	//=============================================================================================	
	
	//=============================================================================================	
	public QNode(QNode parent, float ax, float ay, float bx, float by) {
		set(parent, ax, ay, bx, by);
	}
	//=============================================================================================	

	//=============================================================================================
	private void set(QNode parent, float ax, float ay, float bx, float by) {
		this.parent = parent;
		this.depth = parent == null ? 0 : parent.depth+1;
		this.lo.x = Math.min(ax, bx);
		this.lo.y = Math.min(ay, by);
		this.hi.x = Math.max(ax, bx);
		this.hi.y = Math.max(ay, by);
	}
	//=============================================================================================	

	//=============================================================================================	
	public boolean insert(QObject object) {
		if (this.encloses(object)) {
			if (children != null) {
				for (QNode child : children) {
					if (child.insert(object)) {
						return true;
					}
				}
			}
			object.node = this;
			this.objects.add(object);
			split();
			return true;
		}
		return false;
	}
	//=============================================================================================	

	//=============================================================================================	
	public void remove(QObject object) {
		object.node = null;
		objects.remove(object);
		join();
	}
	//=============================================================================================	
	
	//=============================================================================================	
	private void split() {

		if (children != null) return;
		if (this.depth >= MAX_DEPTH) return;
		if (objects.size() < MAX_OBJECTS) return;
		
		final Vector2f md = new Vector2f();
		md.x = lo.x + (hi.x - lo.x) * .5f;
		md.y = lo.y + (hi.y - lo.y) * .5f;
		children = new QNode[4];
		children[0] = new QNode(this, lo.x, lo.y, md.x, md.y);
		children[1] = new QNode(this, md.x, lo.y, hi.x, md.y);
		children[2] = new QNode(this, lo.x, md.y, md.x, hi.y);
		children[3] = new QNode(this, md.x, md.y, hi.x, hi.y);

		for (int i=0; i<objects.size(); i++) {
			QObject obj = objects.get(i);
			for (QNode child : children) {
				if (child.encloses(obj)) {
					obj.node = child;
					child.objects.add(obj);
					objects.remove(i--);
					continue;
				}
			}
		}

	}
	//=============================================================================================	

	//=============================================================================================	
	private void join() {

		if (children == null) return;
		if (count() < MAX_OBJECTS) return;
		
		for (QNode node : children) {
			node.join();
			objects.addAll(node.objects);
		}
		children = null;
		
	}
	//=============================================================================================	

	//=============================================================================================	
	private int count() {
		int n = objects.size();
		if (children != null) {
			for (QNode child :children) {
				n += child.count();
			}
		}
		return n;
	}
	//=============================================================================================	
	
	//=============================================================================================	
	private boolean contains(QObject object) {
		return
			(lo.x <= object.hi.x || hi.x >= object.lo.x) &&
			(lo.y <= object.hi.y || hi.y >= object.lo.y);
	}
	//=============================================================================================	
	
	//=============================================================================================	
	private boolean encloses(QObject object) {
		return
			lo.x <= object.lo.x &&
			lo.y <= object.lo.y &&
			hi.x >= object.hi.x &&
			hi.y >= object.hi.y;
	}
	//=============================================================================================	
	
}
//*************************************************************************************************

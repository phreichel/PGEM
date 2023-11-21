//*************************************************************************************************
package pgem.model.physics;
//*************************************************************************************************

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3f;

//*************************************************************************************************
public class SpaceNode {

	//=============================================================================================
	public final AlignedBox boundary = new AlignedBox();
	//=============================================================================================

	//=============================================================================================
	public SpaceNode parent = null;
	public final SpaceNode[] children = new SpaceNode[8];
	//=============================================================================================

	//=============================================================================================
	public final List<Body> bodies = new ArrayList<>();
	//=============================================================================================

	//=============================================================================================
	public boolean contains(Body body) {
		return boundary.contains(body.boundingBox);
	}
	//=============================================================================================

	//=============================================================================================
	public boolean insert(Body body) {
		if (contains(body)) {
			for (int i=0; i<children.length; i++) {
				var nd = children[i];
				if (nd == null) break;
				if (nd.insert(body)) {
					return true;
				}
			}
			bodies.add(body);
			split();
			return true;
		}
		return false;
	}
	//=============================================================================================

	//=============================================================================================
	public boolean delete(Body body) {
		boolean state = false;
		if (contains(body)) {
			if (bodies.remove(body))
				state = true;
			else {
				int n = 0;
				for (int i=0; i<children.length; i++) {
					var nd = children[i];
					if (nd == null) break;
					if (!state && nd.delete(body)) {
						state = true;
					}
					n += nd.nbodies();
				}
				if (n == 0) {
					join();
				}
			}
		}
		return state;
	}
	//=============================================================================================

	//=============================================================================================
	public int nbodies() {
		int n = bodies.size();
		for (int i=0; i<children.length; i++) {
			var nd = children[i];
			if (nd == null) break;
			n += nd.nbodies();
		}
		return n;
 	}
	//=============================================================================================
	
	//=============================================================================================
	private void split() {
		
		if (children[0] != null) return;
		
		final Vector3f l = boundary.lo;
		final Vector3f u = boundary.up;
 		final Vector3f m = new Vector3f();
		boundary.getmid(m);
		
		for (int i=0; i<8; i++) {
			children[i] = new SpaceNode();
		}
		
		children[0].boundary.set(l.x, l.y, l.z, m.x, m.y, m.z);
		children[1].boundary.set(l.x, l.y, m.z, m.x, m.y, u.z);
		children[2].boundary.set(l.x, m.y, l.z, m.x, u.y, m.z);
		children[3].boundary.set(l.x, m.y, m.z, m.x, u.y, u.z);
		children[4].boundary.set(m.x, l.y, l.z, u.x, m.y, m.z);
		children[5].boundary.set(m.x, l.y, m.z, u.x, m.y, u.z);
		children[6].boundary.set(m.x, m.y, l.z, u.x, u.y, m.z);
		children[7].boundary.set(m.x, m.y, m.z, u.x, u.y, u.z);

		for (int i=0; i<children.length; i++) {
			for (int j=0; j<bodies.size(); j++) {
				Body b = bodies.get(j);
				if (children[i].contains(b)) {
					children[i].bodies.add(b);
					bodies.remove(j);
					j--;
				}
			}
		}
		
	}
	//=============================================================================================

	//=============================================================================================
	private void join() {
		for (int i=0; i<8; i++) {
			var nd = children[i];
			children[i] = null;
			if (nd == null) break;
			nd.join();
			this.bodies.addAll(nd.bodies);
		}
	}
	//=============================================================================================
	
}
//*************************************************************************************************

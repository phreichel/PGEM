//*************************************************************************************************
package pgem.model.physics;
//*************************************************************************************************

import javax.vecmath.Vector3f;

//*************************************************************************************************
public class Sphere {

	//=============================================================================================
	public float radius = 0;
	public final Vector3f location = new Vector3f();
	//=============================================================================================

	//=============================================================================================
	public void set(Vector3f location, float r) {
		this.location.set(location);
		this.radius = r;
	}
	//=============================================================================================

	//=============================================================================================
	public void set(float x, float y, float z, float r) {
		this.location.set(location);
		this.radius = r;
	}
	//=============================================================================================

	//=============================================================================================
	public void move(Vector3f d) {
		this.location.add(d);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void move(float dx, float dy, float dz) {
		this.move(new Vector3f(dx, dy, dz));
	}
	//=============================================================================================
	
}
//*************************************************************************************************
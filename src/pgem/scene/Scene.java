//*************************************************************************************************
package pgem.scene;
//*************************************************************************************************

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Vector3f;

import pgem.paint.Graphics;
import pgem.paint.Painter;
import pgem.terrain.Terrain;

//*************************************************************************************************
public class Scene implements Painter {

	//=============================================================================================
	private Camera camera = null;
	private final Node root = new Root(this);
	//=============================================================================================

	//=============================================================================================
	public Node root() {
		return root;
	}
	//=============================================================================================

	//=============================================================================================
	public Camera camera() {
		return camera;
	}
	//=============================================================================================

	//=============================================================================================
	public void camera(Camera camera) {
		this.camera = camera;
	}
	//=============================================================================================

	//=============================================================================================
	public void init(Terrain terrain) {

		Transform camTransform = new Transform(root());
		var n = new Vector3f(-1, 1,0);
		n.normalize();
		camTransform.matrix.setRotation(new AxisAngle4f(n, (float) Math.toRadians(30)));
		camTransform.matrix.setTranslation(new Vector3f(5f, 5f, 10f));
		
		Camera camera = new Camera(camTransform);
		camera(camera);
		
		new TerrainNode(root(), terrain);

	}
	//=============================================================================================
	
	//=============================================================================================
	public void paint(Graphics g) {
		
		float fovy = (camera == null) ? 70f : camera.fovy;
		float near = (camera == null) ? .4f : camera.near;
		float far  = (camera == null) ? 1000f : camera.far;
		g.perspective(fovy, near, far);

		if (camera != null) g.apply(camera.pov());

		g.color(1, 0, 0);
		g.box(false, new Vector3f(-1, -1, -1), new Vector3f(2, 2, 2));
		
		root.paint(g);

	}
	//=============================================================================================

}
//*************************************************************************************************

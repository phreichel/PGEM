//*************************************************************************************************
package pgem.scene;
//*************************************************************************************************

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import pgem.paint.Graphics;
import pgem.terrain.Chunk;
import pgem.terrain.Terrain;

//*************************************************************************************************
public class TerrainNode extends Node {

	//=============================================================================================
	private final Terrain terrain;
	//=============================================================================================

	//=============================================================================================
	public TerrainNode(Node parent, Terrain terrain) {
		super(parent);
		this.terrain = terrain;
	}
	//=============================================================================================

	//=============================================================================================
	protected void paintNode(Graphics g) {
		
		Point3f  cpos = new Point3f();
		Vector3f cfwd = new Vector3f(0, 0, -1);
		Camera cam = this.scene().camera();
		cam.toWorld(cpos, cpos);
		cam.toWorld(cfwd, cfwd);
		
		Point3f tpos = new Point3f();
		this.toWorld(tpos, tpos);
		
		g.color(.7f, 1, 0);
		
		final List<Chunk> chunks = new ArrayList<>();
		terrain.load(cpos.x, cpos.z, 5000, chunks);
		for (int i=0; i<chunks.size(); i++) {

			Chunk chunk = chunks.get(i);
			
			if (!chunk.loaded()) continue;
			
			float h = (chunk.lo + chunk.hi) * .5f;
			float r = (float)
				Math.sqrt(
					(h*2)*(h*2) +
					chunk.w*chunk.w +
					chunk.h*chunk.h
				) * .5f;
			
			Point3f chpos = new Point3f(chunk.x, h, chunk.y);
			chpos.add(tpos);			
			
			final Vector3f dst = new Vector3f();
			dst.sub(chpos, cpos);
			
			if (cfwd.dot(dst) > -r*2) {
				float d = Math.max(0, dst.length() - r);
				var chr = terrain.chunk(chunk.x + chunk.w + 1, chunk.y, chunk.distance);
				var chb = terrain.chunk(chunk.x, chunk.y + chunk.h + 1, chunk.distance);
				var chbr = terrain.chunk(chunk.x + chunk.w + 1, chunk.y + chunk.h + 1, chunk.distance);
				g.chunk(chunk, chr, chb, chbr, d/2500f, false);
			}
		}

	}
	//=============================================================================================

}
//*************************************************************************************************

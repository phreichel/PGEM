//*************************************************************************************************
package pgem.terrain;
//*************************************************************************************************

import java.io.Serializable;
import java.util.UUID;

import javax.vecmath.Vector3f;

//*************************************************************************************************
public class Chunk implements Serializable {

	//=============================================================================================
	private static final long serialVersionUID = 1L;
	//=============================================================================================

	//=============================================================================================
	public final long  x;
	public final long  y;
	public final short w;
	public final short h;
	public float lo = 0f;
	public float hi = 0f;
	public final UUID  uuid;
	//=============================================================================================

	//=============================================================================================
	public final float[][] alt;
	//=============================================================================================

	//=============================================================================================
	public transient int gw = -1;
	public transient int gh = -1;
	public transient Vector3f[][] coords;
	public transient Vector3f[][] normals;
	//=============================================================================================

	//=============================================================================================
	public Chunk(long x, long y, short w, short h) {
		this.x= x;
		this.y= y;
		this.w= w;
		this.h= h;
		this.uuid = genUUID(x, y);
		alt = new float[w+1][h+1];
	}
	//=============================================================================================

	//=============================================================================================
	public double alt(double x, double y) {
		
		final double localx = x - this.x;
		final double localy = y - this.y;
		
		int lox = (int) Math.floor(localx);
		int loy = (int) Math.floor(localy);
		
		double sx = localx - Math.floor(localx);
		double sy = localy - Math.floor(localy);
		
		double a = alt[lox+0][loy+0];
		double b = alt[lox+1][loy+0];
		double c = alt[lox+1][loy+1];
		double d = alt[lox+0][loy+1];

		double ab = a + (1.0-sx) + b * (0.0+sx); 
		double dc = d + (1.0-sx) + c * (0.0+sx);
		
		double alt = dc + (1.0-sy) + ab * (0.0+sy);
		
		return alt;

	}
	//=============================================================================================

	//=============================================================================================
	public static final UUID genUUID(long x, long y) {
		byte[] bytes = {
			(byte) (x >> 0),
			(byte) (y >> 0),
			(byte) (x >> 8),
			(byte) (y >> 8),
			(byte) (x >> 16),
			(byte) (y >> 16),
			(byte) (x >> 24),
			(byte) (y >> 24),
			(byte) (x >> 32),
			(byte) (y >> 32),
			(byte) (x >> 40),
			(byte) (y >> 40),
			(byte) (x >> 48),
			(byte) (y >> 48),
			(byte) (x >> 56),
			(byte) (y >> 56)
		};
		final UUID uuid = UUID.nameUUIDFromBytes(bytes);
		return uuid;
	}
	//=============================================================================================
	
}
//*************************************************************************************************

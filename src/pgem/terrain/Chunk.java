//*************************************************************************************************
package pgem.terrain;
//*************************************************************************************************

import java.io.Serializable;
import java.util.UUID;

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
	public final UUID  uuid;
	//=============================================================================================

	//=============================================================================================
	public final float[][] alt;
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
		final String sgnx = (x < 0) ? "N" : "P"; 
		final String sgny = (y < 0) ? "N" : "P"; 
		final String str  = String.format("%s%08d:%s%08d", sgnx, Math.abs(x), sgny, Math.abs(y));
		int hc = str.hashCode();
		byte[] bytes = {
			(byte) (hc >> 0),
			(byte) (hc >> 8),
			(byte) (hc >> 16),
			(byte) (hc >> 32),
		};
		final UUID uuid = UUID.nameUUIDFromBytes(bytes);
		return uuid;
	}
	//=============================================================================================
	
}
//*************************************************************************************************

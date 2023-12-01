//*************************************************************************************************
package pgem.terrain;
//*************************************************************************************************

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

//*************************************************************************************************
public class TerrainStorage {

	//=============================================================================================
	public static final String TERRAIN_DATA_CACHE = "./data/terrain";
	//=============================================================================================
	
	//=============================================================================================
	public Chunk load(long x, long y) {
		File file = new File(path(x, y));
		if (!file.exists()) return null;
		try {
			InputStream in = new FileInputStream(file);
			BufferedInputStream bin = new BufferedInputStream(in);
			ObjectInputStream oin = new ObjectInputStream(bin);
			Chunk chunk = (Chunk) oin.readObject();
			oin.close();
			return chunk;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	//=============================================================================================

	//=============================================================================================
	public void store(Chunk chunk) {
		File file = new File(path(chunk.x, chunk.y));
		file.getParentFile().mkdirs();
		try {
			OutputStream out = new FileOutputStream(file, false);
			BufferedOutputStream bout = new BufferedOutputStream(out);
			ObjectOutputStream oout = new ObjectOutputStream(bout);
			oout.writeObject(chunk);
			oout.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	//=============================================================================================
	
	//=============================================================================================
	private String path(long x, long y) {
		String signx = (x < 0) ? "NX" : "PX";
		String signy = (y < 0) ? "NY" : "PY";
		String namx  = String.format("%s%08d", signx, Math.abs(x));
		String namy  = String.format("%s%08d", signy, Math.abs(y));
		String path  = String.format("./data/terrain/%s/%s.DAT", namx, namy);
		return path;
	}
	//=============================================================================================
	
}
//*************************************************************************************************

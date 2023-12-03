//*************************************************************************************************
package pgem.terrain;
//*************************************************************************************************

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

//*************************************************************************************************
public class Terrain {

	//=============================================================================================
	public static final short CHUNK_WIDTH  = (short) 256;
	public static final short CHUNK_HEIGHT = (short) 256;
	//=============================================================================================
	
	//=============================================================================================
	public  final long seed = 0L; 
	private final TerrainGenerator     generator;
	private final TerrainStorage       storage;
	private final HashMap<UUID, Chunk> cache = new HashMap<>();
	//=============================================================================================

	//=============================================================================================
	public Terrain() {
		storage   = new TerrainStorage();
		generator = new TerrainGenerator(seed, CHUNK_WIDTH, CHUNK_HEIGHT);
	}
	//=============================================================================================

	//=============================================================================================
	public void load(double x, double y, double r, List<Chunk> chunks) {
		final int nx = (int) Math.ceil((r*2) / CHUNK_WIDTH); 
		final int ny = (int) Math.ceil((r*2) / CHUNK_HEIGHT);
		for (int i=0; i<nx; i++) {
			for (int j=0; j<ny; j++) {
				Chunk chunk = chunk(
					x - r + (i*CHUNK_WIDTH),
					y - r + (j*CHUNK_HEIGHT));
				if (chunks != null) chunks.add(chunk);
			}
		}
	}
	//=============================================================================================
	
	//=============================================================================================
	public Chunk chunk(double x, double y) {

		long cx = chunkX(x);
		long cy = chunkY(y);
		UUID uuid = Chunk.genUUID(cx, cy);
		
		Chunk chunk = cache.get(uuid);
		if (chunk != null) return chunk;
		
		chunk = storage.load(cx, cy);
		if (chunk != null) {
			cache.put(uuid, chunk);
			return chunk;
		}
		
		chunk = generator.generate(cx, cy);
		cache.put(uuid, chunk);
		storage.store(chunk);
		return chunk;

	}
	//=============================================================================================

	//=============================================================================================
	private long chunkX(double x) {
		long ix = (long) Math.floor(x);
		ix /= CHUNK_WIDTH;
		ix *= CHUNK_WIDTH;
		return ix;
	}
	//=============================================================================================

	//=============================================================================================
	private long chunkY(double y) {
		long iy = (long) Math.floor(y);
		iy /= CHUNK_HEIGHT;
		iy *= CHUNK_HEIGHT;
		return iy;
	}
	//=============================================================================================

	//=============================================================================================
	public void done() {
		generator.done();
		storage.done();
	}
	//=============================================================================================
	
}
//*************************************************************************************************

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
import java.util.ArrayDeque;
import java.util.Deque;

//*************************************************************************************************
public class TerrainStorage {

	//=============================================================================================
	public static final String TERRAIN_DATA_CACHE = "./data/terrain";
	//=============================================================================================
	
	//=============================================================================================
	private boolean stop = false;
	//=============================================================================================
	
	//=============================================================================================
	private Deque<Chunk> loading = new ArrayDeque<>();
	private Thread loadThread = new Thread(this::runLoad);
	//=============================================================================================

	//=============================================================================================
	private Deque<Chunk> storing = new ArrayDeque<>();
	private Thread storeThread = new Thread(this::runStore);
	//=============================================================================================
	
	//=============================================================================================
	public TerrainStorage() {
		init();
	}
	//=============================================================================================
	
	//=============================================================================================
	public void init() {
		loadThread.setDaemon(false);
		storeThread.setDaemon(false);
		loadThread.start();
		storeThread.start();
	}
	//=============================================================================================
	
	//=============================================================================================
	public Chunk load(long x, long y) {
		File file = new File(path(x, y));
		if (!file.exists()) return null;
		Chunk chunk = new Chunk(x, y, Terrain.CHUNK_WIDTH, Terrain.CHUNK_WIDTH);
		synchronized (this) {
			loading.offer(chunk);
		}
		return chunk;
	}
	//=============================================================================================

	//=============================================================================================
	private void runLoad() {
		while (true) {
			if (updateLoad() && stop) break;
			try {
				Thread.sleep(1L);
			} catch (InterruptedException e) {}
		}
	}
	//=============================================================================================
	
	//=============================================================================================
	private boolean updateLoad() {
		Chunk chunk = null;
		synchronized (this) {
			chunk = loading.poll();
		}
		if (chunk != null) {
			loadChunk(chunk);
			chunk.loaded(true);
			return false;
		} else {
			return true;
		}
	}
	//=============================================================================================

	//=============================================================================================
	private void loadChunk(Chunk chunk) {
		try {
			File file = new File(path(chunk.x, chunk.y));
			InputStream in = new FileInputStream(file);
			BufferedInputStream bin = new BufferedInputStream(in);
			ObjectInputStream oin = new ObjectInputStream(bin);
			Chunk loaded = (Chunk) oin.readObject();
			oin.close();
			chunk.set(loaded);
			chunk.loaded(true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	//=============================================================================================

	//=============================================================================================
	public void store(Chunk chunk) {
		if (!chunk.modified()) return;
		synchronized (this) {
			storing.offer(chunk);
		}
	}
	//=============================================================================================
	
	//=============================================================================================
	private void runStore() {
		while (true) {
			if (updateStore() && stop) break;
			try {
				Thread.sleep(1L);
			} catch (InterruptedException e) {}
		}
	}
	//=============================================================================================

	//=============================================================================================
	private boolean updateStore() {
		Chunk chunk = null;
		synchronized (this) {
			chunk = storing.poll();
		}
		if (chunk != null) {
			if (chunk.loaded()) {
				storeChunk(chunk);
				chunk.modified(false);
			} else {
				store(chunk);
			}
			return false;
		} else {
			return true;
		}
	}
	//=============================================================================================

	//=============================================================================================
	private void storeChunk(Chunk chunk) {
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

	//=============================================================================================
	public void done() {
		stop = true;
		boolean joined = false;
		while (!joined) {
			try {
				loadThread.join();
				joined = true;
			} catch (InterruptedException e) {}			
		}
		joined = false;
		while (!joined) {
			try {
				storeThread.join();
				joined = true;
			} catch (InterruptedException e) {}			
		}
	}
	//=============================================================================================
	
}
//*************************************************************************************************

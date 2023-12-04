//*************************************************************************************************
package pgem.terrain;
//*************************************************************************************************

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import pgem.noise.Add;
import pgem.noise.Octave;
import pgem.noise.Perlin;
import pgem.noise.Scale;
import pgem.noise.Amplify;

//*************************************************************************************************
public class TerrainGenerator {

	//=============================================================================================
	public final long seed;
	//=============================================================================================

	//=============================================================================================
	private pgem.noise.Module alt;
	//=============================================================================================

	//=============================================================================================
	private boolean stop = false;
	//=============================================================================================
	
	//=============================================================================================
	private Queue<Chunk> chunks = new PriorityQueue<>((a, b) -> Double.compare(a.distance, b.distance));
	private Thread worker = new Thread(this::run);
	//=============================================================================================
	
	//=============================================================================================
	public TerrainGenerator(long seed) {
		this.seed = seed;
		initALT();
	}
	//=============================================================================================

	//=============================================================================================
	public Chunk generate(long x, long y, double d) {		
		x = x - x % Terrain.CHUNK_WIDTH;
		y = y - y % Terrain.CHUNK_HEIGHT;
		Chunk chunk = new Chunk(x, y);
		chunk.modified(true);
		chunk.distance = d;
		push(chunk);
		return chunk;
	}
	//=============================================================================================

	//=============================================================================================
	private synchronized void push(Chunk chunk) {
		chunks.offer(chunk);
	}
	//=============================================================================================

	//=============================================================================================
	public void run() {
		while (true) {
			if (update() && stop) break;
			try {
				Thread.sleep(1L);
			} catch (InterruptedException e) {}
		}
	}
	//=============================================================================================
	
	//=============================================================================================
	public boolean update() {
		Chunk chunk = null;
		synchronized (this) {
			chunk = chunks.poll();
		}
		if (chunk != null) {
			genALT(chunk);
			chunk.loaded(true);
			return false;
		} else {
			return true;
		}
	}
	//=============================================================================================
	
	//=============================================================================================
	private void initALT() {

		Random rnd = new Random(seed);
		
		Perlin  shape  = new Perlin(rnd.nextLong());
		Octave  soct   = new Octave(shape, 5);
		Amplify samp   = new Amplify(soct, 200);
		Scale   sscale = new Scale(samp, .002, 1, .002); 
		
		Perlin  detail = new Perlin(rnd.nextLong());
		Octave  octave = new Octave(detail, 4);
		Amplify damp   = new Amplify(octave, 5);
		Scale   dscale = new Scale(damp, .05, 1, .05); 

		Add add = new Add(sscale, dscale);
		
		alt = add;

		worker.setDaemon(true);
		worker.start();
		
	}
	//=============================================================================================
	
	//=============================================================================================
	private void genALT(Chunk chunk) {
		chunk.lo =  Float.MAX_VALUE;
		chunk.hi = -Float.MAX_VALUE;
		for (int i=0; i<=Terrain.CHUNK_WIDTH; i++) {
			for (int j=0; j<=Terrain.CHUNK_HEIGHT; j++) {
				float value = (float) alt.noise((chunk.x+i), 0, (chunk.y+j));				
				chunk.lo = Math.min(chunk.lo, value); 
				chunk.hi = Math.max(chunk.hi, value); 
				chunk.alt[i][j] = value;
			}
		}
	}
	//=============================================================================================

	//=============================================================================================
	public void done() {
		stop = true;
		boolean joined = false;
		while (!joined) {
			try {
				worker.join();
				joined = true;
			} catch (InterruptedException e) {}			
		}
	}
	//=============================================================================================
	
}
//*************************************************************************************************

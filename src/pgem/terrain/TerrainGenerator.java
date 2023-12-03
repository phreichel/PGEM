//*************************************************************************************************
package pgem.terrain;
//*************************************************************************************************

import java.util.ArrayDeque;
import java.util.Deque;
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
	public final short w; 
	public final short h; 
	//=============================================================================================

	//=============================================================================================
	private pgem.noise.Module alt;
	//=============================================================================================

	//=============================================================================================
	private boolean stop = false;
	//=============================================================================================
	
	//=============================================================================================
	private Deque<Chunk> chunks = new ArrayDeque<>();
	private Thread worker = new Thread(this::run);
	//=============================================================================================
	
	//=============================================================================================
	public TerrainGenerator(long seed, short w, short h) {
		this.seed = seed;
		this.w = w;
		this.h = h;
		initALT();
	}
	//=============================================================================================

	//=============================================================================================
	public Chunk generate(long x, long y) {		
		x = x - x % w;
		y = y - y % h;
		Chunk chunk = new Chunk(x, y, w, h);
		chunk.modified(true);
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
		Amplify samp   = new Amplify(shape, 200);
		Scale   sscale = new Scale(samp, .002, 1, .002); 
		
		Perlin  detail = new Perlin(rnd.nextLong());
		Octave  octave = new Octave(detail, 4);
		Amplify damp   = new Amplify(octave, 10);
		Scale   dscale = new Scale(damp, .03, 1, .03); 

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
		for (int i=0; i<=w; i++) {
			for (int j=0; j<=h; j++) {
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

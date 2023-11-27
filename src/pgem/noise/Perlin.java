//*************************************************************************************************
package pgem.noise;
//*************************************************************************************************

import java.util.Random;

//*************************************************************************************************
public class Perlin implements Module {

    //=============================================================================================
	private final int[] p;
    //=============================================================================================

    //=============================================================================================
    public Perlin(long seed) {

    	p = new int[512];
    	for (int i = 0; i < 256; i++) {
    		p[i] = i;
        }

        Random r = new Random(seed);
        for (int i = 0; i < 256; i++) {
        	int j = r.nextInt(256 - i) + i;
        	int temp = p[i];
        	p[i] = p[j];
        	p[j] = temp;
        	p[i + 256] = p[i];
        }

    }
    //=============================================================================================

    //=============================================================================================
    private double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }
    //=============================================================================================

    //=============================================================================================
    private double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }
    //=============================================================================================

    //=============================================================================================
    private double grad(int hash, double x, double y, double z) {
        int h = hash & 15; // Convert low 4 bits of hash code into 12 simple gradient directions, each rotated 45 degrees
        double u = h < 8 ? x : y;
        double v = h < 4 ? y : h == 12 || h == 14 ? x : z;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }
    //=============================================================================================
    
    //=============================================================================================
    public double noise(double x, double y, double z) {

    	// Calculate the "unit cube" that the point asked will be located in
        int X = (int) Math.floor(x) & 255;
        int Y = (int) Math.floor(y) & 255;
        int Z = (int) Math.floor(z) & 255;

        // Relative x, y, z of point in cube
        x -= Math.floor(x);
        y -= Math.floor(y);
        z -= Math.floor(z);

        // Compute fade curves for each of x, y, z
        double u = fade(x);
        double v = fade(y);
        double w = fade(z);

        // Hash coordinates of the 8 cube corners
        int a  = p[X] + Y;
        int aa = p[a] + Z;
        int ab = p[a + 1] + Z;
        int b  = p[X + 1] + Y;
        int ba = p[b] + Z;
        int bb = p[b + 1] + Z;

        // Add blended results from 8 corners of cube
        return lerp(w,
        	lerp(
        		v,
        		lerp(u,
        			grad(p[aa], x, y, z),
        			grad(p[ba], x - 1, y, z)),
        		lerp(u,
        			grad(p[ab], x, y - 1, z),
        			grad(p[bb], x - 1, y - 1, z))
        	),
        	lerp(v,
        		lerp(u,
        			grad(p[aa + 1], x, y, z - 1),
        			grad(p[ba + 1], x - 1, y, z - 1)),
        		lerp(u,
        			grad(p[ab + 1], x, y - 1, z - 1),
        			grad(p[bb + 1], x - 1, y - 1, z - 1))
        	)
        );
    }
    //=============================================================================================

}
//*************************************************************************************************

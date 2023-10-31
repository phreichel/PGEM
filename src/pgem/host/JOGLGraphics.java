//*************************************************************************************************
package pgem.host;
//*************************************************************************************************

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Vector2f;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;

import pgem.paint.Painter;
import pgem.paint.Fn;
import pgem.paint.Graphics;

//*************************************************************************************************
public class JOGLGraphics implements GLEventListener, Graphics {

	//=============================================================================================
	private GLAutoDrawable wnd = null;
	private GL2 gl = null;
	private GLU glu = null;
	//=============================================================================================

	//=============================================================================================
	private final List<Painter> list = new ArrayList<>();
	//=============================================================================================
	
	//=============================================================================================
	public void plug(Painter painter) {
		list.add(painter);
	}
	//=============================================================================================

	//=============================================================================================
	public void unplug(Painter painter) {
		list.remove(painter);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void init(GLAutoDrawable drawable) {
		
		wnd = drawable;
		gl = wnd.getGL().getGL2();
		glu = GLU.createGLU(gl);
		
		gl.glHint(GL2.GL_POINT_SMOOTH_HINT, GL2.GL_NICEST);
		gl.glHint(GL2.GL_LINE_SMOOTH_HINT, GL2.GL_NICEST);
		gl.glHint(GL2.GL_POLYGON_SMOOTH_HINT, GL2.GL_NICEST);
		gl.glHint(GL2.GL_FOG_HINT, GL2.GL_NICEST);
		gl.glHint(GL2.GL_GENERATE_MIPMAP_HINT, GL2.GL_NICEST);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);
		
		gl.glDisable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		
	}
	//=============================================================================================

	//=============================================================================================
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
	}
	//=============================================================================================
	
	//=============================================================================================
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		for (int i=0; i<list.size(); i++) {
			var canvas = list.get(i);
			canvas.paint(this);
		}
	}
	//=============================================================================================

	//=============================================================================================
	public void dispose(GLAutoDrawable drawable) {
	}
	//=============================================================================================

	//=============================================================================================
	public void surface() {
		surface(wnd.getSurfaceWidth(), wnd.getSurfaceHeight());
	}
	//=============================================================================================
	
	//=============================================================================================
	public void surface(float width, float height) {
		gl.glDisable(GL2.GL_LIGHTING);
		gl.glDisable(GL2.GL_DEPTH_TEST);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluOrtho2D(-1, width, -1, height);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	//=============================================================================================

	//=============================================================================================
	public void perspective(float fovy, float aspect, float near, float far) {
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(fovy, aspect, near, far);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	//=============================================================================================
	
	//=============================================================================================
	public void push() {
		gl.glPushMatrix();
	}
	//=============================================================================================

	//=============================================================================================
	public void pop() {
		gl.glPopMatrix();
	}
	//=============================================================================================

	//=============================================================================================
	public void scale(float s) {
		gl.glScalef(s, s, 1);
	}
	//=============================================================================================

	//=============================================================================================
	public void rotate(float a) {
		gl.glRotatef(a, 0, 0, 1);
	}
	//=============================================================================================

	//=============================================================================================
	public void translate(float dx, float dy) {
		gl.glTranslatef(dx, dy, 0);
	}
	//=============================================================================================

	//=============================================================================================
	public void color(Color3f color) {
		color(color.x, color.y, color.z);
	}
	//=============================================================================================

	//=============================================================================================
	public void color(Color4f color) {
		color(color.x, color.y, color.z, color.w);
	}
	//=============================================================================================

	//=============================================================================================
	public void color(float r, float g, float b) {
		color(r, g, b, 1);
	}
	//=============================================================================================

	//=============================================================================================
	public void color(float r, float g, float b, float a) {
		if (a == 1) gl.glDisable(GL2.GL_BLEND);
		else gl.glEnable(GL2.GL_BLEND);
		gl.glColor4f(r, g, b, a);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void points(float ... coords) {
		gl.glBegin(GL2.GL_POINTS);
		for (int i=0; i<coords.length/2; i++) {
			var x = coords[i*2+0];
			var y = coords[i*2+1];
			gl.glVertex2f(x, y);
		}
		gl.glEnd();
	}
	//=============================================================================================

	//=============================================================================================
	public void points(Vector2f ... coords) {
		gl.glBegin(GL2.GL_POINTS);
		for (int i=0; i<coords.length; i++) {
			var v = coords[i];
			gl.glVertex2f(v.x, v.y);
		}
		gl.glEnd();
	};
	//=============================================================================================

	//=============================================================================================
	public void lines(boolean closed, float ... coords) {
		int type = closed ? GL2.GL_LINE_LOOP : GL2.GL_LINE_STRIP;  
		gl.glBegin(type);
		for (int i=0; i<coords.length/2; i++) {
			var x = coords[i*2+0];
			var y = coords[i*2+1];
			gl.glVertex2f(x, y);
		}
		gl.glEnd();
	}
	//=============================================================================================

	//=============================================================================================
	public void lines(boolean closed, Vector2f ... coords) {
		int type = closed ? GL2.GL_LINE_LOOP : GL2.GL_LINE_STRIP;  
		gl.glBegin(type);
		for (int i=0; i<coords.length; i++) {
			var v = coords[i];
			gl.glVertex2f(v.x, v.y);
		}
		gl.glEnd();
	}
	//=============================================================================================

	//=============================================================================================
	public void spline(float precision, float ... coords) {
		
		var idx = 0;

		gl.glBegin(GL2.GL_LINE_STRIP);

		var cx = 0f;
		var cy = 0f;
	
		while (idx <= coords.length-4) {
	
			var ax = (idx < 2) ? coords[idx++] : cx;
			var ay = (idx < 2) ? coords[idx++] : cy;
	
			var bx = coords[idx++];
			var by = coords[idx++];
			
			cx = coords[idx++];
			cy = coords[idx++];
			
			var dax = bx-ax;
			var day = by-ay;
			var dbx = cx-bx;
			var dby = cy-by;

			var lena = Math.sqrt(dax*dax + day*day);
			var lenb = Math.sqrt(dbx*dbx + dby*dby);
			
			var steps = (int) Math.rint((lena+lenb) / precision);
			
			for (int i=0; i<=steps; i++) {
				
				var a = 1f - (1f / steps) * i;
				
				var abx = ax*a + bx*(1f-a);
				var aby = ay*a + by*(1f-a);

				var bcx = bx*a + cx*(1f-a);
				var bcy = by*a + cy*(1f-a);

				var abcx = abx*a + bcx*(1f-a);
				var abcy = aby*a + bcy*(1f-a);
				
				gl.glVertex2f(abcx, abcy);
	
			}
			
			ax = cx;
			ay = cy;
			
		}

		gl.glEnd();
		
	}
	//=============================================================================================

	//=============================================================================================
	public void bezier(float precision, float ... coords) {
		
		var idx = 0;

		gl.glBegin(GL2.GL_LINE_STRIP);
	
		var dx = 0f;
		var dy = 0f;
		
		while (idx <= coords.length-6) {
			
			var ax = (idx < 2) ? coords[idx++] : dx;
			var ay = (idx < 2) ? coords[idx++] : dy;

			var bx = coords[idx++];
			var by = coords[idx++];
			
			var cx = coords[idx++];
			var cy = coords[idx++];

			dx = coords[idx++];
			dy = coords[idx++];
	
			var dax = bx-ax;
			var day = by-ay;

			var dbx = cx-bx;
			var dby = cy-by;

			var dcx = dx-cx;
			var dcy = dy-cy;
	
			var lena = Math.sqrt(dax*dax + day*day);
			var lenb = Math.sqrt(dbx*dbx + dby*dby);
			var lenc = Math.sqrt(dcx*dcx + dcy*dcy);
			
			var steps = (int) Math.rint((lena+lenb+lenc) / precision);
			
			for (int i=0; i<=steps; i++) {
				
				var a = 1f - (1f / steps) * i;
				
				var abx = ax*a + bx*(1f-a);
				var aby = ay*a + by*(1f-a);

				var bcx = bx*a + cx*(1f-a);
				var bcy = by*a + cy*(1f-a);

				var cdx = cx*a + dx*(1f-a);
				var cdy = cy*a + dy*(1f-a);
				
				var abcx = abx*a + bcx*(1f-a);
				var abcy = aby*a + bcy*(1f-a);

				var bcdx = bcx*a + cdx*(1f-a);
				var bcdy = bcy*a + cdy*(1f-a);

				var abcdx = abcx*a + bcdx*(1f-a);
				var abcdy = abcy*a + bcdy*(1f-a);
	
				gl.glVertex2f(abcdx, abcdy);
	
			}
						
		}

		gl.glEnd();
	
	}
	//=============================================================================================
	
	//=============================================================================================
	public void fn(float x, float y, float precision, float from, float to, Fn fn) {

		int steps = (int) Math.rint((to-from) / precision);

		gl.glBegin(GL2.GL_LINE_STRIP);
	
		for (int i=0; i<= steps; i++) {
			
			float a = (float) i / (float) steps;
			float fx = from*a + to*(1f-a);
			float dx = (to - from) * a;
			float dy = fn.fn(fx);
			
			gl.glVertex2f(x+dx, x+dy);
			
		}

		gl.glEnd();
		
	}
	//=============================================================================================
	
	//=============================================================================================
	public void box(boolean filled, Vector2f origin, Vector2f size) {
		box(filled, origin.x, origin.y, size.x, size.y);
	}
	//=============================================================================================

	//=============================================================================================
	public void box(boolean filled, float x, float y, float w, float h) {
		if (filled) gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		else gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
		gl.glBegin(GL2.GL_QUADS);
		gl.glVertex2f(x+0, y+0);
		gl.glVertex2f(x+w, y+0);
		gl.glVertex2f(x+w, y+h);
		gl.glVertex2f(x+0, y+h);
		gl.glEnd();
	}
	//=============================================================================================

}
//*************************************************************************************************

//*************************************************************************************************
package pgem.render.jogl;
//*************************************************************************************************

import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Color3f;
import javax.vecmath.Vector2f;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import pgem.render.Graphics;
import pgem.render.Renderer;
import pgem.util.X;

import static com.jogamp.opengl.GL2.*;

//*************************************************************************************************
public class JOGLGraphics implements Graphics, GLEventListener {

	//=============================================================================================
	private final List<Renderer> init = new ArrayList<>();
	private final List<Renderer> renderer = new ArrayList<>();
	private final List<Renderer> done = new ArrayList<>();
	//=============================================================================================

	//=============================================================================================
	private final Map<String, Texture> textures = new HashMap<>();
	private final Map<String, TextRenderer> fonts = new HashMap<>();
	//=============================================================================================

	//=============================================================================================
	private final Color3f color = new Color3f();
	//=============================================================================================
	
	//=============================================================================================
	private GLAutoDrawable wnd = null;
	private GL2 gl = null;
	private GLU glu = null;
	//=============================================================================================
	
	//=============================================================================================
	public void link(Renderer r) {
		init.add(r);
		renderer.add(r);
	}
	//=============================================================================================

	//=============================================================================================
	public void unlink(Renderer r) {
		if (renderer.remove(r)) {
			done.add(r);
		}		
	}
	//=============================================================================================
	
	//=============================================================================================
	public void init(GLAutoDrawable drawable) {
		wnd = drawable;
		gl = wnd.getGL().getGL2();
		glu = GLU.createGLU(gl);
		
		// default blend function is Alpha-Blending.
		gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
	}
	//=============================================================================================

	//=============================================================================================
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}
	//=============================================================================================

	//=============================================================================================
	public void display(GLAutoDrawable drawable) {

		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		while (init.size() > 0) { 
			var r = init.remove(0);
			r.init(this);
		}
		
		for (var r : renderer) {
			r.render(this);
		}
		
		while (done.size() > 0) { 
			var r = done.remove(0);
			r.done(this);
		}
		
	}
	//=============================================================================================

	//=============================================================================================
	public void dispose(GLAutoDrawable drawable) {

		done.addAll(renderer);
		renderer.clear();
		while (done.size() > 0) { 
			var r = done.remove(0);
			r.done(this);
		}

		wnd = null;
		gl = null;
		glu = null;

	}
	//=============================================================================================

	//=============================================================================================
	public void gui() {

		gl.glDisable(GL_LIGHTING);
		gl.glDisable(GL_CULL_FACE);
		
		float w = wnd.getSurfaceWidth();
		float h = wnd.getSurfaceHeight();

		gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluOrtho2D(-1f, w, h+1, 0f);
		
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();
		
	}
	//=============================================================================================

	//=============================================================================================
	public void translate(Vector2f position) {
		gl.glTranslatef(position.x, position.y, 0f);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void pushTransform() {
		gl.glPushMatrix();
	}
	//=============================================================================================

	//=============================================================================================
	public void blend(boolean enable) {
		switch (enable ? 1 : 0) {
			case 0 -> gl.glDisable(GL_BLEND);
			case 1 -> gl.glEnable(GL_BLEND);				
		};
	}
	//=============================================================================================
	
	//=============================================================================================
	public void texture(String name, boolean enable) {
		var texture = textures.get(name);
		if (texture != null) {
			texture.bind(gl);
			switch (enable ? 1 : 0) {
				case 0 -> texture.disable(gl);
				case 1 -> texture.enable(gl);
			}
		}
	}
	//=============================================================================================
	
	//=============================================================================================
	public void popTransform() {
		gl.glPopMatrix();
	}
	//=============================================================================================

	//=============================================================================================
	public void color(float r, float g, float b) {
		color.set(r, g, b);
		gl.glColor3f(r, g, b);
	}
	//=============================================================================================

	//=============================================================================================
	public void rectangle(float x, float y, float w, float h, boolean fill) {
		int type = fill ? GL_QUADS : GL_LINE_LOOP;
		gl.glBegin(type);
		gl.glVertex2f(x+0, y+0);
		gl.glVertex2f(x+w, y+0);
		gl.glVertex2f(x+w, y+h);
		gl.glVertex2f(x+0, y+h);
		gl.glEnd();
	}
	//=============================================================================================

	//=============================================================================================
	public void image(String name, float x, float y, float w, float h) {
		texture(name, true);
		gl.glBegin(GL_QUADS);
		gl.glTexCoord2f(0f, 1f);
		gl.glVertex2f(x+0, y+0);
		gl.glTexCoord2f(1f, 1f);
		gl.glVertex2f(x+w, y+0);
		gl.glTexCoord2f(1f, 0f);
		gl.glVertex2f(x+w, y+h);
		gl.glTexCoord2f(0f, 0f);
		gl.glVertex2f(x+0, y+h);
		gl.glEnd();
		texture(name, false);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void loadTexture(String name, File path) {
		try {
			var texture = textures.get(name);
			if (texture != null) return;
			texture = TextureIO.newTexture(path, true);
			textures.put(name, texture);
		} catch (Exception e) {
			throw new X(e);
		}
	}
	//=============================================================================================

	//=============================================================================================
	public void unloadTexture(String name) {
		var texture = textures.remove(name);
		if (texture != null) {
			texture.bind(gl);
			texture.destroy(gl);
		}
	}
	//=============================================================================================
	
	//=============================================================================================
	public void loadFont(String name, String definition) {
		var font = Font.decode(definition);
		var tr = new TextRenderer(font, true, false, null, true);
		fonts.put(name, tr);
	}
	//=============================================================================================

	//=============================================================================================
	public void unloadFont(String name) {
		var tr = fonts.remove(name);
		if (tr != null) tr.dispose();
	}
	//=============================================================================================

	//=============================================================================================
	public void write(String font, String text, float x, float y) {
		var tr = fonts.get(font);
		if (tr == null) throw new X("Font with name %s is not loaded.", font);
		tr.setColor(color.x, color.y, color.z, 1f);
		gl.glPushMatrix();
		tr.begin3DRendering();
		tr.draw3D(text, 0, 0, 0f, 1f);
		gl.glTranslatef(x, y, 0f);
		gl.glScalef(1f, -1f, 1f);
		tr.end3DRendering();
		gl.glPopMatrix();
	}
	//=============================================================================================
	
}
//*************************************************************************************************

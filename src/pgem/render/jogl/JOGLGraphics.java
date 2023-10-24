//*************************************************************************************************
package pgem.render.jogl;
//*************************************************************************************************

import java.awt.Font;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Vector2f;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import pgem.gui.Align;
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
	private final Color4f color = new Color4f(0, 0, 0, 1);
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

		// enable hints
		gl.glHint(GL_FOG_HINT, GL_NICEST);
		gl.glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
		gl.glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);
		gl.glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
		gl.glHint(GL_GENERATE_MIPMAP_HINT, GL_NICEST);
		gl.glHint(GL_TEXTURE_COMPRESSION_HINT, GL_NICEST);
		gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		gl.glHint(GL_FRAGMENT_SHADER_DERIVATIVE_HINT, GL_NICEST);

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
	public void pushTransform() {
		gl.glPushMatrix();
	}
	//=============================================================================================

	//=============================================================================================
	public void popTransform() {
		gl.glPopMatrix();
	}
	//=============================================================================================
	
	//=============================================================================================
	public void translate(Vector2f position) {
		gl.glTranslatef(position.x, position.y, 0f);
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
	public void color(Color3f color) {
		color(color.x, color.y, color.z, 1);
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
		color.set(r, g, b, a);
		gl.glColor4f(r, g, b, a);
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
	public void lines(boolean closed, float ... coords) {
		int type = closed ? GL_LINE_STRIP : GL_LINE_LOOP;
		gl.glBegin(type);
		for (var i=0; i<coords.length; ) {
			gl.glVertex2f(coords[i++], coords[i++]);
		}
		gl.glEnd();
	}
	//=============================================================================================

	//=============================================================================================
	public void lines(boolean closed, Vector2f ... coords) {
		int type = closed ? GL_LINE_STRIP : GL_LINE_LOOP;
		gl.glBegin(type);
		for (var i=0; i<coords.length; i++) {
			Vector2f v = coords[i];
			gl.glVertex2f(v.x, v.y);
		}
		gl.glEnd();
	}
	//=============================================================================================

	//=============================================================================================
	public void rectangle(boolean fill, Vector2f pos, Vector2f size) {
		rectangle(fill, pos.x, pos.y, size.x, size.y);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void rectangle(boolean fill, float x, float y, float w, float h) {
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
	public void text(String font, String text, Vector2f pos, Vector2f size, Align horz, Align vert) {
		text(font, text, pos.x, pos.y, size.x, size.y, horz, vert);
	}
	//=============================================================================================

	//=============================================================================================
	public void text(String font, String text, Vector2f pos, Vector2f size, Align horz, Align vert, int caret, int mark) {
		text(font, text, pos.x, pos.y, size.x, size.y, horz, vert, caret, mark);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void text(String font, String text, float x, float y, float w, float h, Align horz, Align vert) {
		
		font = font != null ? font : "normal";
		var tr = fonts.get(font);
		if (tr == null) throw new X("Font with name %s is not loaded.", font);
		tr.setColor(color.x, color.y, color.z, 1f);		
		
		Rectangle2D r = tr.getBounds(text);

		LineMetrics lineMetrics = tr.getFont().getLineMetrics(text, tr.getFontRenderContext());
		float asc = lineMetrics.getAscent();
		float desc = lineMetrics.getDescent();
		float lead = lineMetrics.getLeading();
		
		float cy = x + h*.5f;
		float oy = switch (vert) {
			case START -> cy + asc;	
			case CENTER -> cy + ((float) r.getHeight()-desc-lead) * .5f;
			case END -> cy + h - 2;	
		};
		
		float ox = switch (horz) {
			case START -> x + 2;
			case CENTER -> x + (w - (float) r.getWidth()) * .5f;
			case END -> x + w - (float) r.getWidth() - 2;
		};

		gl.glPushMatrix();
		tr.begin3DRendering();
		gl.glTranslatef(ox, oy, 0f);
		gl.glScalef(1f, -1f, 1f);
		tr.draw3D(text, 0, 0, 0f, 1f);
		tr.end3DRendering();
		gl.glPopMatrix();
		
	}
	//=============================================================================================

	//=============================================================================================
	public void text(
			String font,
			String text,
			float x, float y,
			float w, float h,
			Align horz, Align vert,
			int caret,
			int mark) {
		
		caret = Math.max(caret, 0);
		caret = Math.min(caret, text.length());
		
		font = font != null ? font : "normal";
		var tr = fonts.get(font);
		if (tr == null) throw new X("Font with name %s is not loaded.", font);
		tr.setColor(color.x, color.y, color.z, 1f);		
		
		final String STR = "Âp"; 
		
		Rectangle2D rr = tr.getBounds(STR);
		Rectangle2D r = tr.getBounds(text);

		LineMetrics lineMetrics = tr.getFont().getLineMetrics(STR, tr.getFontRenderContext());
		float asc = lineMetrics.getAscent();
		float desc = lineMetrics.getDescent();
		float lead = lineMetrics.getLeading();
		
		float cy = x + h*.5f;
		float oy = switch (vert) {
			case START -> cy + asc;	
			case CENTER -> cy + ((float) rr.getHeight()-desc-lead) * .5f;
			case END -> cy + h - 2;	
		};
		
		float ox = switch (horz) {
			case START -> x + 2;
			case CENTER -> x + (w - (float) r.getWidth()) * .5f;
			case END -> x + w - (float) r.getWidth() - 2;
		};

		var rcaret = tr.getBounds(text.substring(0, caret) + STR);
		var rmark  = tr.getBounds(text.substring(0, mark) + STR);
		var markx  = ox + (float) (rmark.getWidth() - rr.getWidth());
		var caretx = ox + (float) (rcaret.getWidth() - rr.getWidth());
		var a = Math.min(markx, caretx);
		var b = Math.max(markx, caretx);
		this.color(.8f, .8f, .8f);
		this.rectangle(true, a, oy, b-a, -asc);
		
		gl.glPushMatrix();
		tr.begin3DRendering();
		gl.glTranslatef(ox, oy, 0f);
		gl.glScalef(1f, -1f, 1f);
		tr.draw3D(text, 0, 0, 0f, 1f);
		tr.end3DRendering();
		gl.glPopMatrix();

		this.color(1, 0, 0);
		this.rectangle(true, caretx, oy, 2, -asc);
		
	}
	//=============================================================================================
	
	//=============================================================================================
	public void image(String texture, Vector2f pos, Vector2f size) {
		image(texture, pos.x, pos.y, size.x, size.y);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void image(String texture, float x, float y, float w, float h) {
		texture(texture, true);
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
		texture(texture, false);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void image(String texture, Vector2f pos, Vector2f size, Vector2f txorg, Vector2f txext, boolean scaled) {

		float x = pos.x;
		float y = pos.y;
		
		float w = size.x;
		float h = size.y;

		float ax = txorg.x;
		float ay = txorg.y;

		float bx = txorg.x + (scaled ? (size.x / txext.x) : txext.x);
		float by = txorg.y + (scaled ? (size.y / txext.y) : txext.y);
		
		texture(texture, true);
		gl.glBegin(GL_QUADS);
		gl.glTexCoord2f(ax, by);
		gl.glVertex2f(x+0, y+0);
		gl.glTexCoord2f(bx, by);
		gl.glVertex2f(x+w, y+0);
		gl.glTexCoord2f(bx, ay);
		gl.glVertex2f(x+w, y+h);
		gl.glTexCoord2f(ax, ay);
		gl.glVertex2f(x+0, y+h);
		gl.glEnd();
		texture(texture, false);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void loadTexture(String name, File path) {
		try {
			var texture = textures.get(name);
			if (texture != null) return;
			texture = TextureIO.newTexture(path, true);

			texture.bind(gl);
			texture.setTexParameteri(gl, GL_TEXTURE_WRAP_S, GL_REPEAT);
			texture.setTexParameteri(gl, GL_TEXTURE_WRAP_T, GL_REPEAT);
			
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
	
}
//*************************************************************************************************

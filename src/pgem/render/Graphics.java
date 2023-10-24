//*************************************************************************************************
package pgem.render;
//*************************************************************************************************

import java.io.File;

import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Vector2f;

import pgem.gui.Align;

//*************************************************************************************************
public interface Graphics {

	//=============================================================================================
	public void gui();
	//=============================================================================================

	//=============================================================================================
	public void pushTransform();
	public void popTransform();
	public void translate(Vector2f position);
	//=============================================================================================

	//=============================================================================================
	public void blend(boolean enable);
	public void color(Color3f color);
	public void color(Color4f color);
	public void color(float r, float g, float b);
	public void color(float r, float g, float b, float a);
	public void texture(String name, boolean enable);
	//=============================================================================================

	//=============================================================================================
	public void lines(boolean closed, float ... coords);
	public void lines(boolean closed, Vector2f ... coords);
	public void rectangle(boolean fill, Vector2f pos, Vector2f size);
	public void rectangle(boolean fill, float x, float y, float w, float h);
	public void text(String font, String text, Vector2f pos, Vector2f size, Align horz, Align vert);
	public void text(String font, String text, Vector2f pos, Vector2f size, Align horz, Align vert, int caret, int mark);
	public void text(String font, String text, float x, float y, float w, float h, Align horz, Align vert);
	public void text(String font, String text, float x, float y, float w, float h, Align horz, Align vert, int caret, int mark);
	public void image(String texture, Vector2f pos, Vector2f size);
	public void image(String texture, float x, float y, float w, float h);
	public void image(String texture, Vector2f pos, Vector2f size, Vector2f txorg, Vector2f txext, boolean scaled);
	//=============================================================================================

	//=============================================================================================
	public void loadTexture(String name, File path);
	public void unloadTexture(String name);
	//=============================================================================================

	//=============================================================================================
	public void loadFont(String name, String definition);
	public void unloadFont(String name);
	//=============================================================================================
	
}
//*************************************************************************************************

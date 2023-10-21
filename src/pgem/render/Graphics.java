//*************************************************************************************************
package pgem.render;
//*************************************************************************************************

import java.io.File;

import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Vector2f;

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
	public void color(Color3f color);
	public void color(Color4f color);
	public void color(float r, float g, float b);
	public void color(float r, float g, float b, float a);
	public void blend(boolean enable);
	public void texture(String name, boolean enable);
	//=============================================================================================

	//=============================================================================================
	public void lines(boolean closed, float ... xycoords);
	public void rectangle(boolean fill, float x, float y, float w, float h);
	public void write(String font, String text, float x, float y);
	public void write(String font, String text, float x, float y, float w, float h);
	public void image(String texture, float x, float y, float w, float h);
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

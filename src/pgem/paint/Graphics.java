//*************************************************************************************************
package pgem.paint;
//*************************************************************************************************

import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import pgem.terrain.Chunk;

//*************************************************************************************************
public interface Graphics {

	//=============================================================================================
	public void surface();
	public void surface(float width, float height);
	public void perspective(float fovy, float near, float far);
	public void perspective(float fovy, float aspect, float near, float far);
	//=============================================================================================
	
	//=============================================================================================
	public void push();
	public void pop();
	//=============================================================================================

	//=============================================================================================
	public void scale(float s);
	public void rotate(float a);
	public void translate(float dx, float dy);
	public void translate(float dx, float dy, float dz);
	public void apply(Matrix4f m);
	//=============================================================================================

	//=============================================================================================
	public void color(Color3f color);
	public void color(Color4f color);
	public void color(float r, float g, float b);
	public void color(float r, float g, float b, float a);
	//=============================================================================================
	
	//=============================================================================================
	public void points(float ... coords);
	public void points(Vector2f ... coords);
	public void lines(boolean closed, float ... coords);
	public void lines(boolean closed, Vector2f ... coords);
	public void rectangle(boolean filled, Vector2f origin, Vector2f size);
	public void rectangle(boolean filled, float x, float y, float width, float height);
	public void spline(float precision, float ... coords);
	public void bezier(float precision, float ... coords);
	public void function(float x, float y, float precision, float from, float to, Fn fn);
	//=============================================================================================

	//=============================================================================================
	public void fontInit(String name, String fontdef);
	public void fontDone(String name);
	public void text(float x, float y, String text, String font);
	public TextData textMetrics(String text, String font, TextData data);
	//=============================================================================================

	//=============================================================================================
	public void imageInit(String name, String filePath);
	public void imageDone(String name);
	public void image(String name, float x, float y, float w, float h);
	//=============================================================================================

	//=============================================================================================
	public void box(boolean filled, Vector3f origin, Vector3f size);
	public void box(boolean filled, float x, float y, float z, float w, float h, float d);
	//=============================================================================================

	//=============================================================================================
	public void chunk(Chunk chunk);
	//=============================================================================================
	
}
//*************************************************************************************************

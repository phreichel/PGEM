//*************************************************************************************************
package pgem.paint;

import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Vector2f;

//*************************************************************************************************

//*************************************************************************************************
public interface Graphics {

	//=============================================================================================
	public void surface();
	public void surface(float width, float height);
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
	public void spline(float precision, float ... coords);
	public void bezier(float precision, float ... coords);
	public void fn(float x, float y, float precision, float from, float to, Fn fn);
	public void box(boolean filled, Vector2f origin, Vector2f size);
	public void box(boolean filled, float x, float y, float width, float height);
	//=============================================================================================

	//=============================================================================================
	public void fontInit(String name, String fontdef);
	public void fontDone(String name);
	public void text(float x, float y, String text, String font);
	public TextData textMetrics(String text, String font, TextData data);
	//=============================================================================================

}
//*************************************************************************************************

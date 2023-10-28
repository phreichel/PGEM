//*************************************************************************************************
package pgem.host;
//*************************************************************************************************

import javax.vecmath.Vector2f;

import com.jogamp.openal.sound3d.AudioSystem3D;

import pgem.graphics.Canvas;
import pgem.msg.MsgBox;

//*************************************************************************************************
final class JOGLHost implements Host {

	//=============================================================================================
	private final JOGLInput input;
	private final JOGLGraphics graphics;
	private final JOGLWindow window;
	//=============================================================================================
	
	//=============================================================================================
	public JOGLHost(MsgBox msgBox) {
		input = new JOGLInput(msgBox);
		graphics = new JOGLGraphics();
		window = new JOGLWindow(msgBox);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void init() {
		AudioSystem3D.init();
		window.init(input, graphics);
	}
	//=============================================================================================

	//=============================================================================================
	public void update() {
		window.update();
	}
	//=============================================================================================

	//=============================================================================================
	public void done() {
		window.done();
	}
	//=============================================================================================

	//=============================================================================================
	public boolean visible() {
		return window.visible();
	}
	//=============================================================================================
	
	//=============================================================================================
	public Host visible(boolean visible) {
		window.visible(visible);
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public boolean fullscreen() {
		return window.fullscreen();
	}
	//=============================================================================================
	
	//=============================================================================================
	public Host fullscreen(boolean fullscreen) {
		window.fullscreen(fullscreen);
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public boolean maximized() {
		return window.maximized();
	}
	//=============================================================================================
	
	//=============================================================================================
	public Host maximized(boolean maximized) {
		window.maximized(maximized);
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public String title() {
		return window.title();
	}
	//=============================================================================================

	//=============================================================================================
	public Host title(String title) {
		window.title(title);
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public Vector2f size() {
		return size();
	}
	//=============================================================================================

	//=============================================================================================
	public Host size(Vector2f size) {
		window.size(size);
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public Host size(float width, float height) {
		window.size(width, height);
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public Host plug(Canvas canvas) {
		graphics.plug(canvas);
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public Host unplug(Canvas canvas) {
		graphics.unplug(canvas);
		return this;
	}
	//=============================================================================================
	
}
//*************************************************************************************************

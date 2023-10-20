//*************************************************************************************************
package pgem.port.jogl;
//*************************************************************************************************

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector2f;

import com.jogamp.nativewindow.WindowClosingProtocol.WindowClosingMode;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.event.WindowListener;
import com.jogamp.newt.event.WindowUpdateEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;

import pgem.msg.MsgHub;
import pgem.msg.MsgType;
import pgem.port.Window;
import pgem.render.jogl.JOGLGraphics;

//*************************************************************************************************
class JOGLWindow implements Window, WindowListener {

	//=============================================================================================
	private final GLWindow wnd;
	//=============================================================================================

	//=============================================================================================
	private final List<MsgHub> msgHubs = new ArrayList<>();
	//=============================================================================================
	
	//=============================================================================================
	JOGLWindow(JOGLInput input, JOGLGraphics graphics) {
		GLProfile glProfile = GLProfile.getDefault();
		GLCapabilities glCapabilities = new GLCapabilities(glProfile);
		wnd = GLWindow.create(glCapabilities);
		wnd.setDefaultCloseOperation(WindowClosingMode.DO_NOTHING_ON_CLOSE);
		wnd.addWindowListener(this);
		wnd.addKeyListener(input);
		wnd.addMouseListener(input);
		wnd.addGLEventListener(graphics);
	}
	//=============================================================================================

	//=============================================================================================
	public String title() {
		return wnd.getTitle();
	}
	//=============================================================================================

	//=============================================================================================
	public Window title(String title) {
		wnd.setTitle(title);
		return this;
	}
	//=============================================================================================
	
	//=============================================================================================
	public boolean visible() {
		return wnd.isVisible();
	}
	//=============================================================================================

	//=============================================================================================
	public Window visible(boolean visible) {
		wnd.setVisible(visible);
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public boolean fullscreen() {
		return wnd.isFullscreen();
	}
	//=============================================================================================

	//=============================================================================================
	public Window fullscreen(boolean fullscreen) {
		wnd.setFullscreen(fullscreen);
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public boolean decorated() {
		return !wnd.isUndecorated();
	}
	//=============================================================================================

	//=============================================================================================
	public Window decorated(boolean decorated) {
		wnd.setUndecorated(!decorated);
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public float x() {
		return wnd.getX();
	}
	//=============================================================================================

	//=============================================================================================
	public float y() {
		return wnd.getY();
	}
	//=============================================================================================

	//=============================================================================================
	public Window position(float x, float y) {
		int ix = (int) Math.rint(x);
		int iy = (int) Math.rint(y);
		wnd.setPosition(ix, iy);
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public float width() {
		return wnd.getWidth();
	}
	//=============================================================================================

	//=============================================================================================
	public float height() {
		return wnd.getHeight();
	}
	//=============================================================================================

	//=============================================================================================
	public Window size(float width, float height) {
		int iw = (int) Math.rint(width);
		int ih = (int) Math.rint(height);
		wnd.setSize(iw, ih);
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public void render() {
		wnd.display();
	}
	//=============================================================================================

	//=============================================================================================
	public void link(MsgHub msgHub) {
		this.msgHubs.add(msgHub);
	}
	//=============================================================================================

	//=============================================================================================
	public void unlink(MsgHub msgHub) {
		this.msgHubs.remove(msgHub);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void windowDestroyNotify(WindowEvent e) {
		for (int i=0; i<msgHubs.size(); i++) {
			var msgHub = msgHubs.get(i);
			var msg = msgHub.allocate();
			msg.type = MsgType.WND_CLOSE;
			msgHub.post(msg);
		}
	}
	//=============================================================================================

	//=============================================================================================
	public void windowResized(WindowEvent e) {
		com.jogamp.newt.Window w = (com.jogamp.newt.Window) e.getSource();
		Vector2f size = new Vector2f(w.getSurfaceWidth(), w.getSurfaceHeight());
		for (int i=0; i<msgHubs.size(); i++) {
			var msgHub = msgHubs.get(i);
			var msg = msgHub.allocate();
			msg.type = MsgType.WND_RESIZE;
			msg.size.set(size);
			msgHub.post(msg);
		}
	}
	//=============================================================================================

	//=============================================================================================
	public void windowMoved(WindowEvent e) {}
	public void windowDestroyed(WindowEvent e) {}
	public void windowGainedFocus(WindowEvent e) {}
	public void windowLostFocus(WindowEvent e) {}
	public void windowRepaint(WindowUpdateEvent e) {}
	//=============================================================================================
	
}
//*************************************************************************************************

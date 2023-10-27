//*************************************************************************************************
package pgem.host;
//*************************************************************************************************

import javax.vecmath.Vector2f;

import com.jogamp.nativewindow.WindowClosingProtocol.WindowClosingMode;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.event.WindowListener;
import com.jogamp.newt.event.WindowUpdateEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;

import pgem.msg.Msg;
import pgem.msg.MsgBox;
import pgem.msg.MsgType;
import pgem.msg.WindowData;

//*************************************************************************************************
final class JOGLWindow implements WindowListener {

	//=============================================================================================
	private static final String DEFAULT_TITLE = "PGEM Window";
	private static final int DEFAULT_WIDTH = 1024;
	private static final int DEFAULT_HEIGHT = 768;
	//=============================================================================================

	//=============================================================================================
	private final MsgBox msgBox;
	//=============================================================================================
	
	//=============================================================================================
	private final GLWindow window;
	//=============================================================================================

	//=============================================================================================
	public JOGLWindow(MsgBox msgBox) {
		this.msgBox = msgBox;
		GLProfile profile = GLProfile.getDefault();
		GLCapabilities capabilities = new GLCapabilities(profile);
		window = GLWindow.create(capabilities);
	}
	//=============================================================================================

	//=============================================================================================
	public void init(JOGLInput input) {
		window.setTitle(DEFAULT_TITLE);
		window.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		window.setDefaultCloseOperation(WindowClosingMode.DO_NOTHING_ON_CLOSE);
		window.addKeyListener(input);
		window.addMouseListener(input);
		window.addWindowListener(this);
		msgBox.plug(MsgType.WINDOW_TITLE, this::handle);
		msgBox.plug(MsgType.WINDOW_SIZE, this::handle);
		msgBox.plug(MsgType.WINDOW_VISIBLE, this::handle);
		msgBox.plug(MsgType.WINDOW_MAXIMIZED, this::handle);
		msgBox.plug(MsgType.WINDOW_FULLSCREEN, this::handle);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void update() {
		msgBox.unplug(this::handle);
		window.display();
	}
	//=============================================================================================

	//=============================================================================================
	public void done() {
		window.setVisible(false);
		window.destroy();
	}
	//=============================================================================================

	//=============================================================================================
	public boolean visible() {
		return window.isVisible();
	}
	//=============================================================================================
	
	//=============================================================================================
	public void visible(boolean visible) {
		window.setVisible(visible);
	}
	//=============================================================================================

	//=============================================================================================
	public boolean fullscreen() {
		return window.isFullscreen();
	}
	//=============================================================================================
	
	//=============================================================================================
	public void fullscreen(boolean fullscreen) {
		window.setFullscreen(fullscreen);
	}
	//=============================================================================================

	//=============================================================================================
	public boolean maximized() {
		return window.isMaximizedHorz() && window.isMaximizedVert();
	}
	//=============================================================================================
	
	//=============================================================================================
	public void maximized(boolean maximized) {
		window.setMaximized(maximized, maximized);
	}
	//=============================================================================================

	//=============================================================================================
	public String title() {
		return window.getTitle();
	}
	//=============================================================================================

	//=============================================================================================
	public void title(String title) {
		window.setTitle(title);
	}
	//=============================================================================================

	//=============================================================================================
	public Vector2f size() {
		return new Vector2f(window.getWidth(), window.getHeight());
	}
	//=============================================================================================

	//=============================================================================================
	public void size(Vector2f size) {
		size(size.x, size.y);
	}
	//=============================================================================================

	//=============================================================================================
	public void size(float width, float height) {
		int w = (int) Math.rint(width);
		int h = (int) Math.rint(height);
		window.setSize(w, h);
	}
	//=============================================================================================

	//=============================================================================================
	public void windowDestroyNotify(WindowEvent e) {
		var msg = msgBox.alloc(MsgType.WINDOW_CLOSE);
		msgBox.post(msg);
	}
	//=============================================================================================

	//=============================================================================================
	public void windowResized(WindowEvent e) {}
	public void windowMoved(WindowEvent e) {}
	public void windowDestroyed(WindowEvent e) {}
	public void windowGainedFocus(WindowEvent e) {}
	public void windowLostFocus(WindowEvent e) {}
	public void windowRepaint(WindowUpdateEvent e) {}
	//=============================================================================================

	//=============================================================================================
	private void handle(Msg msg) {
		var data = msg.data(WindowData.class);
		switch (msg.type) {
		case WINDOW_TITLE -> title(data.title);
		case WINDOW_SIZE -> size(data.size);
		case WINDOW_VISIBLE -> visible(data.state);
		case WINDOW_MAXIMIZED -> maximized(data.state);
		case WINDOW_FULLSCREEN -> fullscreen(data.state);
		default -> {}
		}
	}
	//=============================================================================================
	
}
//*************************************************************************************************

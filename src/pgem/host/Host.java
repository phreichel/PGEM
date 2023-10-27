//*************************************************************************************************
package pgem.host;
//*************************************************************************************************

import javax.vecmath.Vector2f;

import pgem.msg.MsgBox;

//*************************************************************************************************
public interface Host {

	//=============================================================================================
	public static final int TYPE_JOGL = 1;
	//=============================================================================================
	
	//=============================================================================================
	public static Host create(int type, MsgBox msgBox) {
		return switch (type) {
			case TYPE_JOGL -> new JOGLHost(msgBox);	
			default -> new JOGLHost(msgBox);
		};
	}
	//=============================================================================================
	
	//=============================================================================================
	public void init();
	public void update();
	public void done();
	//=============================================================================================
	
	//=============================================================================================
	public boolean visible();
	public Host visible(boolean visible);
	//=============================================================================================

	//=============================================================================================
	public boolean fullscreen();
	public Host fullscreen(boolean fullscreen);
	//=============================================================================================

	//=============================================================================================
	public boolean maximized();
	public Host maximized(boolean maximized);
	//=============================================================================================

	//=============================================================================================
	public String title();
	public Host title(String title);
	//=============================================================================================

	//=============================================================================================
	public Vector2f size();
	public Host size(Vector2f size);
	public Host size(float width, float height);
	//=============================================================================================
	
}
//*************************************************************************************************

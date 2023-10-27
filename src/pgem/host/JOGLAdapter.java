//*************************************************************************************************
package pgem.host;
//*************************************************************************************************

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.event.WindowListener;
import com.jogamp.newt.event.WindowUpdateEvent;

//*************************************************************************************************
class JOGLAdapter implements WindowListener, KeyListener, MouseListener {

	//=============================================================================================
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
	public void mouseWheelMoved(MouseEvent e) {}
	//=============================================================================================

	//=============================================================================================
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	//=============================================================================================

	//=============================================================================================
	public void windowResized(WindowEvent e) {}
	public void windowMoved(WindowEvent e) {}
	public void windowDestroyNotify(WindowEvent e) {}
	public void windowDestroyed(WindowEvent e) {}
	public void windowGainedFocus(WindowEvent e) {}
	public void windowLostFocus(WindowEvent e) {}
	public void windowRepaint(WindowUpdateEvent e) {}
	//=============================================================================================
	
}
//*************************************************************************************************

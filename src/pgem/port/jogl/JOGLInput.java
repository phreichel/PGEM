//*************************************************************************************************
package pgem.port.jogl;
//*************************************************************************************************

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

import pgem.msg.MsgHub;
import pgem.msg.MsgType;
import pgem.port.Button;

import static com.jogamp.newt.event.KeyEvent.*;
import static com.jogamp.newt.event.MouseEvent.*;

import static pgem.port.Button.*;

//*************************************************************************************************
class JOGLInput implements KeyListener, MouseListener {

	//=============================================================================================
	private final List<MsgHub> msgHubs = new ArrayList<>();
	private final EnumSet<Button> buttons = EnumSet.noneOf(Button.class);
	//=============================================================================================
	
	//=============================================================================================
	public void mouseClicked(MouseEvent e) {
		Button button = translateButton(e.getButton());
		for (int i=0; i<msgHubs.size(); i++) {
			var msgHub = msgHubs.get(i);
			var msg = msgHub.allocate();
			msg.type = MsgType.PTR_CLICKED;
			msg.button = button;
			msg.buttons.addAll(buttons);
			msg.repeat = e.getClickCount();
			msg.pointer.set(e.getX(), e.getY());
			msgHub.post(msg);
		}
	}
	//=============================================================================================

	//=============================================================================================
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	//=============================================================================================

	//=============================================================================================
	public void mousePressed(MouseEvent e) {
		if (!e.isAutoRepeat()) {
			Button button = translateButton(e.getButton());
			buttons.add(button);
			for (int i=0; i<msgHubs.size(); i++) {
				var msgHub = msgHubs.get(i);
				var msg = msgHub.allocate();
				msg.type = MsgType.PTR_PRESSED;
				msg.button = button;
				msg.buttons.addAll(buttons);
				msg.pointer.set(e.getX(), e.getY());
				msgHub.post(msg);
			}
		}
	}
	//=============================================================================================

	//=============================================================================================
	public void mouseReleased(MouseEvent e) {
		if (!e.isAutoRepeat()) {
			Button button = translateButton(e.getButton());
			buttons.remove(button);
			for (int i=0; i<msgHubs.size(); i++) {
				var msgHub = msgHubs.get(i);
				var msg = msgHub.allocate();
				msg.type = MsgType.PTR_RELEASED;
				msg.button = button;
				msg.buttons.addAll(buttons);
				msg.pointer.set(e.getX(), e.getY());
				msgHub.post(msg);
			}
		}
	}
	//=============================================================================================

	//=============================================================================================
	public void mouseMoved(MouseEvent e) {
		if (!e.isAutoRepeat()) {
			for (int i=0; i<msgHubs.size(); i++) {
				var msgHub = msgHubs.get(i);
				var msg = msgHub.allocate();
				msg.type = MsgType.PTR_MOVED;
				msg.buttons.addAll(buttons);
				msg.pointer.set(e.getX(), e.getY());
				msgHub.post(msg);
			}
		}
	}
	//=============================================================================================

	//=============================================================================================
	public void mouseDragged(MouseEvent e) {
		if (!e.isAutoRepeat()) {
			for (int i=0; i<msgHubs.size(); i++) {
				var msgHub = msgHubs.get(i);
				var msg = msgHub.allocate();
				msg.type = MsgType.PTR_MOVED;
				msg.buttons.addAll(buttons);
				msg.pointer.set(e.getX(), e.getY());
				msgHub.post(msg);
			}
		}
	}
	//=============================================================================================

	//=============================================================================================
	public void mouseWheelMoved(MouseEvent e) {
	}
	//=============================================================================================

	//=============================================================================================
	public void keyPressed(KeyEvent e) {
		Button key = translateKey(e.getKeySymbol());
		if (!e.isAutoRepeat()) {
			buttons.add(key);
			for (int i=0; i<msgHubs.size(); i++) {
				var msgHub = msgHubs.get(i);
				var msg = msgHub.allocate();
				msg.type = MsgType.KBD_PRESSED;
				msg.button = key;
				msg.character = e.getKeyChar();
				msg.buttons.addAll(buttons);
				msgHub.post(msg);
			}
		}
		for (int i=0; i<msgHubs.size(); i++) {
			var msgHub = msgHubs.get(i);
			var msg = msgHub.allocate();
			msg.type = MsgType.KBD_TYPED;
			msg.button = key;
			msg.character = e.getKeyChar();
			msg.buttons.addAll(buttons);
			msgHub.post(msg);
		}
	}
	//=============================================================================================

	//=============================================================================================
	public void keyReleased(KeyEvent e) {
		if (!e.isAutoRepeat()) {
			Button key = translateKey(e.getKeySymbol());
			buttons.remove(key);
			for (int i=0; i<msgHubs.size(); i++) {
				var msgHub = msgHubs.get(i);
				var msg = msgHub.allocate();
				msg.type = MsgType.KBD_RELEASED;
				msg.button = key;
				msg.buttons.addAll(buttons);
				msgHub.post(msg);
			}
		}
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
	private Button translateButton(short button) {
		switch (button) {
			case BUTTON1: return PTR_BTN1;
			case BUTTON2: return PTR_BTN2;
			case BUTTON3: return PTR_BTN3;
			case BUTTON4: return PTR_BTN4;
			case BUTTON5: return PTR_BTN5;
			case BUTTON6: return PTR_BTN6;
			case BUTTON7: return PTR_BTN7;
			case BUTTON8: return PTR_BTN8;
			default: return NONE;
		}
	}
	//=============================================================================================

	//=============================================================================================
	private Button translateKey(short key) {
		switch (key) {
			case VK_ESCAPE: return ESCAPE;
			case VK_F1: return F1;
			case VK_F2: return F2;
			case VK_F3: return F3;
			case VK_F4: return F4;
			case VK_F5: return F5;
			case VK_F6: return F6;
			case VK_F7: return F7;
			case VK_F8: return F8;
			case VK_F9: return F9;
			case VK_F10: return F10;
			case VK_F11: return F11;
			case VK_F12: return F12;
			case VK_PRINTSCREEN: return PRINT_SCREEN;
			case VK_SCROLL_LOCK: return SCROLL_LOCK;
			case VK_PAUSE: return PAUSE;
			case VK_1: return _1;
			case VK_2: return _2;
			case VK_3: return _3;
			case VK_4: return _4;
			case VK_5: return _5;
			case VK_6: return _6;
			case VK_7: return _7;
			case VK_8: return _8;
			case VK_9: return _9;
			case VK_0: return _0;
			case VK_Q: return Q;
			case VK_W: return W;
			case VK_E: return E;
			case VK_R: return R;
			case VK_T: return T;
			case VK_Z: return Z;
			case VK_U: return U;
			case VK_I: return I;
			case VK_O: return O;
			case VK_P: return P;
			case VK_A: return A;
			case VK_S: return S;
			case VK_D: return D;
			case VK_F: return F;
			case VK_G: return G;
			case VK_H: return H;
			case VK_J: return J;
			case VK_K: return K;
			case VK_L: return L;
			case VK_Y: return Y;
			case VK_X: return X;
			case VK_C: return C;
			case VK_V: return V;
			case VK_B: return B;
			case VK_N: return N;
			case VK_M: return M;
			case VK_PLUS: return PLUS;
			case VK_MINUS: return MINUS;
			case VK_BACK_SPACE: return BACK_SPACE;
			case VK_TAB: return TAB;
			case VK_CAPS_LOCK: return CAPS_LOCK;
			case VK_CONTROL: return CONTROL;
			case VK_SHIFT: return SHIFT;
			case VK_WINDOWS: return SYSTEM;
			case VK_ALT: return ALT;
			case VK_CONTEXT_MENU: return MENU;
			case VK_ENTER: return ENTER;
			case VK_SPACE: return SPACE;
			case VK_INSERT: return INSERT;
			case VK_DELETE: return DELETE;
			case VK_HOME: return POS1;
			case VK_END: return END;
			case VK_PAGE_UP: return PAGE_UP;
			case VK_PAGE_DOWN: return PAGE_DOWN;
			case VK_UP: return UP;
			case VK_DOWN: return DOWN;
			case VK_LEFT: return LEFT;
			case VK_RIGHT: return RIGHT;
			case VK_NUM_LOCK: return NUM_LOCK;
			case VK_DIVIDE: return DIVIDE;
			case VK_MULTIPLY: return MULTIPLY;
			case VK_SUBTRACT: return SUBTRACT;
			case VK_ADD: return ADD;
			case VK_DECIMAL: return DECIMAL;
			case VK_NUMPAD0: return NP_0;
			case VK_NUMPAD1: return NP_1;
			case VK_NUMPAD2: return NP_2;
			case VK_NUMPAD3: return NP_3;
			case VK_NUMPAD4: return NP_4;
			case VK_NUMPAD5: return NP_5;
			case VK_NUMPAD6: return NP_6;
			case VK_NUMPAD7: return NP_7;
			case VK_NUMPAD8: return NP_8;
			case VK_NUMPAD9: return NP_9;
			default: return NONE;
		}
	}
	//=============================================================================================

}
//*************************************************************************************************

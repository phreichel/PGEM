//*************************************************************************************************
package pgem.host;
//*************************************************************************************************

import java.util.EnumSet;
import java.util.Set;

import javax.vecmath.Vector2f;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

import pgem.msg.Axis;
import pgem.msg.Button;
import pgem.msg.InputData;
import pgem.msg.MsgBox;
import pgem.msg.MsgType;

//*************************************************************************************************
final class JOGLInput implements KeyListener, MouseListener {

	//=============================================================================================
	private final MsgBox msgBox;
	//=============================================================================================
	
	//=============================================================================================
	private final Set<Button> buttons = EnumSet.noneOf(Button.class);
	private final Vector2f pointer = new Vector2f();
	//=============================================================================================

	//=============================================================================================
	public JOGLInput(MsgBox msgBox) {
		this.msgBox = msgBox;
	}
	//=============================================================================================
	
	//=============================================================================================
	public void keyPressed(KeyEvent e) {

		if (!e.isAutoRepeat()) {
			var msg = msgBox.alloc(MsgType.KEY_PRESSED);
			var data = msg.data(InputData.class);
			data.button = keyButton(e.getKeySymbol());
			buttons.add(data.button);
			data.buttons.addAll(buttons);
			if (e.isPrintableKey()) data.keyCharacter = e.getKeyChar();
			data.axes.put(Axis.POINTER_HORIZONTAL, pointer.x);
			data.axes.put(Axis.POINTER_VERTICAL, pointer.y);
			msgBox.post(msg);
		}

		if (e.isPrintableKey()) {
			var msg = msgBox.alloc(MsgType.KEY_TYPED);
			var data = msg.data(InputData.class);
			data.button = keyButton(e.getKeySymbol());
			buttons.add(data.button);
			data.buttons.addAll(buttons);
			data.keyCharacter = e.getKeyChar();
			data.axes.put(Axis.POINTER_HORIZONTAL, pointer.x);
			data.axes.put(Axis.POINTER_VERTICAL, pointer.y);
			msgBox.post(msg);
		}
	
	}
	//=============================================================================================

	//=============================================================================================
	public void keyReleased(KeyEvent e) {
		if (e.isAutoRepeat()) return;
		var msg = msgBox.alloc(MsgType.KEY_RELEASED);
		var data = msg.data(InputData.class);
		data.button = keyButton(e.getKeySymbol());
		buttons.remove(data.button);
		data.buttons.addAll(buttons);
		if (e.isPrintableKey()) data.keyCharacter = e.getKeyChar();
		data.axes.put(Axis.POINTER_HORIZONTAL, pointer.x);
		data.axes.put(Axis.POINTER_VERTICAL, pointer.y);
		msgBox.post(msg);
	}
	//=============================================================================================

	//=============================================================================================
	public void mouseClicked(MouseEvent e) {
		if (e.isAutoRepeat()) return;
		pointer.x = e.getX();
		pointer.y = e.getY();
		var msg = msgBox.alloc(MsgType.POINTER_CLICKED);
		var data = msg.data(InputData.class);
		data.button = pointerButton(e.getButton());
		data.buttons.addAll(buttons);
		data.clickCount = e.getClickCount();
		data.axes.put(Axis.POINTER_HORIZONTAL, pointer.x);
		data.axes.put(Axis.POINTER_VERTICAL, pointer.y);
		msgBox.post(msg);
	}
	//=============================================================================================

	//=============================================================================================
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	//=============================================================================================
	
	//=============================================================================================
	public void mousePressed(MouseEvent e) {
		if (e.isAutoRepeat()) return;
		pointer.x = e.getX();
		pointer.y = e.getY();
		var msg = msgBox.alloc(MsgType.POINTER_PRESSED);
		var data = msg.data(InputData.class);
		data.button = pointerButton(e.getButton());
		buttons.add(data.button);
		data.buttons.addAll(buttons);
		data.axes.put(Axis.POINTER_HORIZONTAL, pointer.x);
		data.axes.put(Axis.POINTER_VERTICAL, pointer.y);
		msgBox.post(msg);
	}
	//=============================================================================================

	//=============================================================================================
	public void mouseReleased(MouseEvent e) {
		if (e.isAutoRepeat()) return;
		pointer.x = e.getX();
		pointer.y = e.getY();
		var msg = msgBox.alloc(MsgType.POINTER_RELEASED);
		var data = msg.data(InputData.class);
		data.button = pointerButton(e.getButton());
		buttons.remove(data.button);
		data.buttons.addAll(buttons);
		data.axes.put(Axis.POINTER_HORIZONTAL, pointer.x);
		data.axes.put(Axis.POINTER_VERTICAL, pointer.y);
		msgBox.post(msg);
	}
	//=============================================================================================

	//=============================================================================================
	public void mouseMoved(MouseEvent e) {
		var msg = msgBox.alloc(MsgType.POINTER_MOVED);
		pointer.x = e.getX();
		pointer.y = e.getY();
		var data = msg.data(InputData.class);
		data.buttons.addAll(buttons);
		data.axes.put(Axis.POINTER_HORIZONTAL, pointer.x);
		data.axes.put(Axis.POINTER_VERTICAL, pointer.y);
		msgBox.post(msg);
	}
	//=============================================================================================

	//=============================================================================================
	public void mouseDragged(MouseEvent e) {
		var msg = msgBox.alloc(MsgType.POINTER_MOVED);
		pointer.x = e.getX();
		pointer.y = e.getY();
		var data = msg.data(InputData.class);
		data.buttons.addAll(buttons);
		data.axes.put(Axis.POINTER_HORIZONTAL, pointer.x);
		data.axes.put(Axis.POINTER_VERTICAL, pointer.y);
		msgBox.post(msg);
	}
	//=============================================================================================

	//=============================================================================================
	public void mouseWheelMoved(MouseEvent e) {
		var msg = msgBox.alloc(MsgType.POINTER_SCROLLED);
		pointer.x = e.getX();
		pointer.y = e.getY();
		var data = msg.data(InputData.class);
		data.buttons.addAll(buttons);
		data.axes.put(Axis.POINTER_HORIZONTAL, pointer.x);
		data.axes.put(Axis.POINTER_VERTICAL, pointer.y);
		data.axes.put(Axis.POINTER_SCROLL_HORIZONTAL, e.getRotation()[0]);
		data.axes.put(Axis.POINTER_SCROLL_VERTICAL, e.getRotation()[1]);
		msgBox.post(msg);
	}
	//=============================================================================================

	//=============================================================================================
	private Button keyButton(short key) {
		return switch(key) {
		case KeyEvent.VK_ESCAPE -> Button.KEY_ESCAPE;
		case KeyEvent.VK_F1 -> Button.KEY_F1;
		case KeyEvent.VK_F2 -> Button.KEY_F2;
		case KeyEvent.VK_F3 -> Button.KEY_F3;
		case KeyEvent.VK_F4 -> Button.KEY_F4;
		case KeyEvent.VK_F5 -> Button.KEY_F5;
		case KeyEvent.VK_F6 -> Button.KEY_F6;
		case KeyEvent.VK_F7 -> Button.KEY_F7;
		case KeyEvent.VK_F8 -> Button.KEY_F8;
		case KeyEvent.VK_F9 -> Button.KEY_F9;
		case KeyEvent.VK_F10 -> Button.KEY_F10;
		case KeyEvent.VK_F11 -> Button.KEY_F11;
		case KeyEvent.VK_F12 -> Button.KEY_F12;
		case KeyEvent.VK_PRINTSCREEN -> Button.KEY_PRINT_SCREEN;
		case KeyEvent.VK_SCROLL_LOCK -> Button.KEY_SCROLL_LOCK;
		case KeyEvent.VK_PAUSE -> Button.KEY_PAUSE;
		case KeyEvent.VK_1 -> Button.KEY_1;
		case KeyEvent.VK_2 -> Button.KEY_2;
		case KeyEvent.VK_3 -> Button.KEY_3;
		case KeyEvent.VK_4 -> Button.KEY_4;
		case KeyEvent.VK_5 -> Button.KEY_5;
		case KeyEvent.VK_6 -> Button.KEY_6;
		case KeyEvent.VK_7 -> Button.KEY_7;
		case KeyEvent.VK_8 -> Button.KEY_8;
		case KeyEvent.VK_9 -> Button.KEY_9;
		case KeyEvent.VK_0 -> Button.KEY_0;
		case KeyEvent.VK_BACK_SPACE -> Button.KEY_BACK_SPACE;
		case KeyEvent.VK_TAB -> Button.KEY_TAB;
		case KeyEvent.VK_ENTER -> Button.KEY_ENTER;
		case KeyEvent.VK_SPACE -> Button.KEY_SPACE;
		case KeyEvent.VK_Q -> Button.KEY_Q;
		case KeyEvent.VK_W -> Button.KEY_W;
		case KeyEvent.VK_E -> Button.KEY_E;
		case KeyEvent.VK_R -> Button.KEY_R;
		case KeyEvent.VK_T -> Button.KEY_T;
		case KeyEvent.VK_Z -> Button.KEY_Z;
		case KeyEvent.VK_U -> Button.KEY_U;
		case KeyEvent.VK_I -> Button.KEY_I;
		case KeyEvent.VK_O -> Button.KEY_O;
		case KeyEvent.VK_P -> Button.KEY_P;
		case KeyEvent.VK_A -> Button.KEY_A;
		case KeyEvent.VK_S -> Button.KEY_S;
		case KeyEvent.VK_D -> Button.KEY_D;
		case KeyEvent.VK_F -> Button.KEY_F;
		case KeyEvent.VK_G -> Button.KEY_G;
		case KeyEvent.VK_H -> Button.KEY_H;
		case KeyEvent.VK_J -> Button.KEY_J;
		case KeyEvent.VK_K -> Button.KEY_K;
		case KeyEvent.VK_L -> Button.KEY_L;
		case KeyEvent.VK_Y -> Button.KEY_Y;
		case KeyEvent.VK_X -> Button.KEY_X;
		case KeyEvent.VK_C-> Button.KEY_C;
		case KeyEvent.VK_V -> Button.KEY_V;
		case KeyEvent.VK_B -> Button.KEY_B;
		case KeyEvent.VK_N -> Button.KEY_N;
		case KeyEvent.VK_M -> Button.KEY_M;
		case KeyEvent.VK_PLUS -> Button.KEY_PLUS;
		case KeyEvent.VK_MINUS -> Button.KEY_MINUS;
		case KeyEvent.VK_CAPS_LOCK -> Button.KEY_CAPS_LOCK;
		case KeyEvent.VK_SHIFT -> Button.KEY_SHIFT;
		case KeyEvent.VK_CONTROL -> Button.KEY_CONTROL;
		case KeyEvent.VK_WINDOWS -> Button.KEY_SYSTEM;
		case KeyEvent.VK_ALT -> Button.KEY_ALT;
		case KeyEvent.VK_CONTEXT_MENU -> Button.KEY_MENU;
		case KeyEvent.VK_INSERT -> Button.KEY_INSERT;
		case KeyEvent.VK_DELETE -> Button.KEY_DELETE;
		case KeyEvent.VK_HOME -> Button.KEY_POS1;
		case KeyEvent.VK_END -> Button.KEY_END;
		case KeyEvent.VK_PAGE_UP -> Button.KEY_PAGE_UP;
		case KeyEvent.VK_PAGE_DOWN -> Button.KEY_PAGE_DOWN;
		case KeyEvent.VK_UP -> Button.KEY_UP;
		case KeyEvent.VK_DOWN -> Button.KEY_DOWN;
		case KeyEvent.VK_LEFT -> Button.KEY_LEFT;
		case KeyEvent.VK_RIGHT -> Button.KEY_RIGHT;
		case KeyEvent.VK_NUM_LOCK -> Button.KEY_NUM_LOCK;
		case KeyEvent.VK_DIVIDE -> Button.KEY_DIVIDE;
		case KeyEvent.VK_MULTIPLY -> Button.KEY_MULTIPLY;
		case KeyEvent.VK_SUBTRACT -> Button.KEY_SUBTRACT;
		case KeyEvent.VK_ADD -> Button.KEY_ADD;
		case KeyEvent.VK_DECIMAL -> Button.KEY_DECIMAL;
		case KeyEvent.VK_NUMPAD7 -> Button.KEY_NP7;
		case KeyEvent.VK_NUMPAD8 -> Button.KEY_NP8;
		case KeyEvent.VK_NUMPAD9 -> Button.KEY_NP9;
		case KeyEvent.VK_NUMPAD4 -> Button.KEY_NP4;
		case KeyEvent.VK_NUMPAD5 -> Button.KEY_NP5;
		case KeyEvent.VK_NUMPAD6 -> Button.KEY_NP6;
		case KeyEvent.VK_NUMPAD1 -> Button.KEY_NP1;
		case KeyEvent.VK_NUMPAD2 -> Button.KEY_NP2;
		case KeyEvent.VK_NUMPAD3 -> Button.KEY_NP3;
		case KeyEvent.VK_NUMPAD0 -> Button.KEY_NP0;
		default -> Button.NONE;
		};
	}
	//=============================================================================================
	
	//=============================================================================================
	private Button pointerButton(short button) {
		return switch(button) {
		case MouseEvent.BUTTON1 -> Button.POINTER_1;
		case MouseEvent.BUTTON2 -> Button.POINTER_2;
		case MouseEvent.BUTTON3 -> Button.POINTER_3;
		case MouseEvent.BUTTON4 -> Button.POINTER_4;
		case MouseEvent.BUTTON5 -> Button.POINTER_5;
		case MouseEvent.BUTTON6 -> Button.POINTER_6;
		case MouseEvent.BUTTON7 -> Button.POINTER_7;
		case MouseEvent.BUTTON8 -> Button.POINTER_8;
		case MouseEvent.BUTTON9 -> Button.POINTER_9;
		default -> Button.NONE;
		};
	}
	//=============================================================================================

}
//*************************************************************************************************

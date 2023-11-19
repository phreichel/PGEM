//*************************************************************************************************
package pgem.model;
//*************************************************************************************************

import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import pgem.gui.Flag;
import pgem.gui.GUI;
import pgem.gui.Menu;
import pgem.msg.Button;
import pgem.msg.InputData;
import pgem.msg.Msg;
import pgem.msg.MsgBox;
import pgem.msg.MsgType;
import pgem.msg.WindowData;
import pgem.scene.Node;
import pgem.scene.Scene;
import pgem.scene.Transform;

//*************************************************************************************************
public class Controller {

	//=============================================================================================
	private final GUI   gui; 
	private final Scene scene;
	//=============================================================================================

	//=============================================================================================
	private boolean   guiMode = false;
	private long      mark    = -1L;
	private Transform camtx   = null;
	//=============================================================================================
	
	//=============================================================================================
	public Controller(GUI gui, Scene scene) {
		this.gui = gui;
		this.scene = scene;
	}
	//=============================================================================================
	
	//=============================================================================================
	public void hook(MsgBox msgBox) {
		msgBox.plug(MsgType.WINDOW_RESIZED, gui::handleResize);
		msgBox.plug(MsgType.BUTTON_PRESSED, gui::handleShortcuts);
		msgBox.plug(MsgType.KEY_PRESSED, this::handleInput);
		msgBox.plug(MsgType.KEY_RELEASED, this::handleInput);
		msgBox.plug(MsgType.KEY_TYPED, this::handleInput);
		msgBox.plug(MsgType.POINTER_MOVED, this::handleInput);
		msgBox.plug(MsgType.POINTER_PRESSED, this::handleInput);
		msgBox.plug(MsgType.POINTER_RELEASED, this::handleInput);
		msgBox.plug(MsgType.POINTER_CLICKED, this::handleInput);
		msgBox.plug(MsgType.POINTER_SCROLLED, this::handleInput);
		msgBox.plug(MsgType.BUTTON_PRESSED, this::handleInput);
		msgBox.plug(MsgType.BUTTON_RELEASED, this::handleInput);
	}
	//=============================================================================================

	//=============================================================================================
	public void init() {
		mark = System.nanoTime();
		Node node = scene.camera();
		while (node != null) {
			if (node instanceof Transform t) {
				camtx = t;
				break;
			}
			node = node.parent();
		}
	}
	//=============================================================================================
	
	//=============================================================================================
	public void update() {
		if (!guiMode) {
			var current = System.nanoTime();
			var delta = current - mark;
			var dT = (float) delta / 1000000000L;
			mark = current;
			update(dT);
		} else {
			mark = System.nanoTime();
		}
	}
	//=============================================================================================

	//=============================================================================================
	private void handleInput(Msg msg) {

		if (msg.type.equals(MsgType.KEY_PRESSED) &&
			msg.data(InputData.class).button.equals(Button.KEY_ESCAPE)
		) {
			
			guiMode = !guiMode;
			if (guiMode && gui.root().children().get(0) instanceof Menu m) {
				m.closeSubMenus();
				m.children().get(0).focus();
			}
			gui.root().flag(Flag.HIDDEN, !guiMode);
			
			Msg ptrmsg = msg.msgbox.alloc(MsgType.WINDOW_POINTER);
			ptrmsg.data(WindowData.class).state = guiMode;
			msg.msgbox.post(ptrmsg);
			
			return;
		}
		
		if (guiMode) {
			gui.handleInput(msg);
		} else {
			gui.focused(null);
			handleFreeMovement(msg);
		}
		
	}
	//=============================================================================================

	//=============================================================================================
	private float lat   = 0f;
	private float hor   = 0f;
	private float ver   = 0f;
	private float pitch = 0f;
	private float yaw   = 0f;
	private float roll  = 0f;
	//=============================================================================================
	
	//=============================================================================================
	private void handleFreeMovement(Msg msg) {
		if (msg.type.equals(MsgType.BUTTON_PRESSED) || msg.type.equals(MsgType.BUTTON_RELEASED) ) {
			var data = msg.data(InputData.class);
			var factor = msg.type.equals(MsgType.BUTTON_PRESSED) ? 1f : -1f;
			switch (data.button) {
				case KEY_W     -> lat   += factor;
				case KEY_S     -> lat   -= factor;
				case KEY_A     -> hor   -= factor;
				case KEY_D     -> hor   += factor;
				case KEY_R     -> ver   += factor;
				case KEY_F     -> ver   -= factor;
				case KEY_UP    -> pitch -= factor;
				case KEY_DOWN  -> pitch += factor;
				case KEY_LEFT  -> yaw   += factor; 
				case KEY_RIGHT -> yaw   -= factor;
				case KEY_Q     -> roll  += factor;
				case KEY_E     -> roll  -= factor;
				default -> {}
			}
		}
	}
	//=============================================================================================

	//=============================================================================================
	private void updateFreeMovement(float dT) {
		
		final float SCALE_TRS = .1f;
		final float SCALE_ROT = 1f;
		
		Vector3f tl = new Vector3f();
		tl.set(
			 hor * SCALE_TRS,
			 ver * SCALE_TRS,
			-lat * SCALE_TRS);

		Matrix3f rp = new Matrix3f();
		rp.rotX((float) Math.toRadians(pitch * SCALE_ROT));
		
		Matrix3f ry = new Matrix3f();
		ry.rotY((float) Math.toRadians(yaw * SCALE_ROT));

		Matrix3f rr = new Matrix3f();
		rr.rotZ((float) Math.toRadians(roll * SCALE_ROT));

		Matrix3f rt = new Matrix3f();
		rt.set(rp);
		rt.mul(ry);
		rt.mul(rr);

		Matrix4f tx = new Matrix4f();
		tx.setIdentity();
		tx.setTranslation(tl);
		tx.setRotation(rt);

		if (camtx != null) {			
			camtx.matrix.mul(tx);
		}
		
	}
	//=============================================================================================
	
	//=============================================================================================
	private void update(float dT) {
		updateFreeMovement(dT);
		scene.update(dT);
	}
	//=============================================================================================
	
}
//*************************************************************************************************

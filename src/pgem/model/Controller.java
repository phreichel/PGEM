//*************************************************************************************************
package pgem.model;
//*************************************************************************************************

import pgem.gui.Flag;
import pgem.gui.GUI;
import pgem.msg.Button;
import pgem.msg.InputData;
import pgem.msg.Msg;
import pgem.msg.MsgBox;
import pgem.msg.MsgType;
import pgem.msg.WindowData;

//*************************************************************************************************
public class Controller {

	//=============================================================================================
	private final GUI gui; 
	//=============================================================================================

	//=============================================================================================
	private boolean guiMode = true;
	//=============================================================================================
	
	//=============================================================================================
	public Controller(GUI gui) {
		this.gui = gui;
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
	}
	//=============================================================================================

	//=============================================================================================
	private void handleInput(Msg msg) {

		if (msg.type.equals(MsgType.KEY_PRESSED) &&
			msg.data(InputData.class).button.equals(Button.KEY_ESCAPE)
		) {
			
			guiMode = !guiMode;
			gui.root().flag(Flag.HIDDEN, !guiMode);
			
			Msg ptrmsg = msg.msgbox.alloc(MsgType.WINDOW_POINTER);
			ptrmsg.data(WindowData.class).state = guiMode;
			msg.msgbox.post(ptrmsg);
			
			return;
		}
		
		if (guiMode) {
			gui.handleInput(msg);
		} else {
			
		}
		
	}
	//=============================================================================================
	
}
//*************************************************************************************************

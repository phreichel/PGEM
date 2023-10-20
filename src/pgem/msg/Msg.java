//*************************************************************************************************
package pgem.msg;
//*************************************************************************************************

import java.util.EnumSet;

import javax.vecmath.Vector2f;

import pgem.port.Button;

//*************************************************************************************************
public class Msg {

	//=============================================================================================
	public MsgType type = MsgType.NONE;
	public Button button = Button.NONE;
	public char character = '\0';
	public int repeat = 0;
	public final EnumSet<Button> buttons = EnumSet.noneOf(Button.class);
	public final Vector2f pointer = new Vector2f();
	public final Vector2f size = new Vector2f();
	//=============================================================================================

	//=============================================================================================
	public void clear() {
		type = MsgType.NONE;
		button = Button.NONE;
		character = '\0';
		repeat = 0;
		buttons.clear();
		pointer.set(0, 0);
		size.set(0, 0);
	}
	//=============================================================================================
	
}
//*************************************************************************************************

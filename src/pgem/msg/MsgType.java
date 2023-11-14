//*************************************************************************************************
package pgem.msg;

import java.util.EnumSet;
import java.util.Set;

//*************************************************************************************************

//*************************************************************************************************
public enum MsgType {


	//=============================================================================================
	
	NONE(null),

	APPLICATION_QUIT(null),

	WINDOW_TITLE(WindowData.class),
	WINDOW_SIZE(WindowData.class),
	WINDOW_VISIBLE(WindowData.class),
	WINDOW_MAXIMIZED(WindowData.class),
	WINDOW_FULLSCREEN(WindowData.class),
	WINDOW_RESIZED(WindowData.class),
	WINDOW_POINTER(WindowData.class),
	WINDOW_CLOSE(null),
	
	BUTTON_PRESSED(InputData.class),
	BUTTON_RELEASED(InputData.class),
	
	KEY_TYPED(InputData.class),
	KEY_PRESSED(InputData.class),
	KEY_RELEASED(InputData.class),
	
	POINTER_MOVED(InputData.class),
	POINTER_CLICKED(InputData.class),
	POINTER_PRESSED(InputData.class),
	POINTER_RELEASED(InputData.class),
	POINTER_SCROLLED(InputData.class),

	;
	
	//=============================================================================================

	//=============================================================================================
	public final Class<? extends MsgData> dataClass;
	//=============================================================================================

	//=============================================================================================
	private MsgType(Class<? extends MsgData> dataClass) {
		this.dataClass = dataClass;
	}
	//=============================================================================================

	//=============================================================================================
	public static final Set<MsgType> WINDOW_MASK = EnumSet.of(
		WINDOW_TITLE,
		WINDOW_SIZE,
		WINDOW_MAXIMIZED,
		WINDOW_FULLSCREEN
	);
	//=============================================================================================
	
	//=============================================================================================
	public static final Set<MsgType> INPUT_MASK = EnumSet.of(
		KEY_TYPED,
		KEY_PRESSED,
		KEY_RELEASED,
		POINTER_MOVED,
		POINTER_CLICKED,
		POINTER_PRESSED,
		POINTER_RELEASED,
		POINTER_MOVED,
		POINTER_CLICKED,
		POINTER_PRESSED,
		POINTER_RELEASED,
		BUTTON_PRESSED,
		BUTTON_RELEASED
	);
	//=============================================================================================
	
	//=============================================================================================
	public static final Set<MsgType> KEY_MASK = EnumSet.of(
		KEY_TYPED,
		KEY_PRESSED,
		KEY_RELEASED
	);
	//=============================================================================================

	//=============================================================================================
	public static final Set<MsgType> POINTER_MASK = EnumSet.of(
		POINTER_MOVED,
		POINTER_CLICKED,
		POINTER_PRESSED,
		POINTER_RELEASED
	);
	//=============================================================================================

	//=============================================================================================
	public static final Set<MsgType> BUTTON_MASK = EnumSet.of(
		BUTTON_PRESSED,
		BUTTON_RELEASED
	);
	//=============================================================================================
	
}
//*************************************************************************************************

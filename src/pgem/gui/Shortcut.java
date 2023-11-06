//*************************************************************************************************
package pgem.gui;
//*************************************************************************************************

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import pgem.msg.InputData;
import pgem.msg.Msg;
import pgem.msg.MsgType;

//*************************************************************************************************
public class Shortcut {

	//=============================================================================================
	private Action action;
	private boolean enabled = true;
	private pgem.msg.Button trigger = null;   
	private Set<pgem.msg.Button> modifiers = EnumSet.noneOf(pgem.msg.Button.class);
	//=============================================================================================

	//=============================================================================================
	public Shortcut(pgem.msg.Button trigger, pgem.msg.Button ... modifiers) {
		this.action = this::action;
		this.trigger = trigger;
		this.modifiers.addAll(List.of(modifiers));
	}
	//=============================================================================================

	//=============================================================================================
	public Shortcut(Action action, pgem.msg.Button trigger, pgem.msg.Button ... modifiers) {
		this.action = action;
		this.trigger = trigger;
		this.modifiers.addAll(List.of(modifiers));
	}
	//=============================================================================================
	
	//=============================================================================================
	public boolean enabled() {
		return enabled;
	}
	//=============================================================================================

	//=============================================================================================
	public void enabled(boolean enabled) {
		this.enabled = enabled;
	}
	//=============================================================================================

	//=============================================================================================
	public Action action() {
		return action;
	}
	//=============================================================================================

	//=============================================================================================
	public void action(Action action) {
		this.action = action;
	}
	//=============================================================================================
	
	//=============================================================================================
	public boolean match(Msg msg) {
		if (!msg.type.equals(MsgType.BUTTON_PRESSED)) return false;
		var data = msg.data(InputData.class);
		return
			enabled &&
			data.button.equals(this.trigger) &&
			data.buttons.containsAll(modifiers);
	}
	//=============================================================================================

	//=============================================================================================
	public void update(Msg msg) {
		if (match(msg)) {		
			action.perform(null, msg);
			msg.consumed = true;
		}
	}
	//=============================================================================================

	//=============================================================================================
	private void action(Widget<?> w, Msg msg) {}
	//=============================================================================================
	
}
//*************************************************************************************************

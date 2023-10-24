//*************************************************************************************************
package pgem.gui;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import pgem.log.Log;
import pgem.msg.Msg;
import pgem.msg.MsgType;
import pgem.port.Button;
import pgem.util.X;

//*************************************************************************************************

//*************************************************************************************************
public class TextEditor {

	//=============================================================================================
	private boolean focused = false;
	private int caret = 0;
	private int mark = caret;
	private StringBuffer text;
	//=============================================================================================

	//=============================================================================================
	public TextEditor() {
		text = new StringBuffer(128);
	}
	//=============================================================================================
	
	//=============================================================================================
	public TextEditor(int capacity) {
		text = new StringBuffer(capacity);
	}
	//=============================================================================================
	
	//=============================================================================================
	public boolean focused() {
		return focused;
	}
	//=============================================================================================

	//=============================================================================================
	public void focused(boolean b) {
		focused = b;
	}
	//=============================================================================================
	
	//=============================================================================================
	public int caret() {
		return caret;
	}
	//=============================================================================================

	//=============================================================================================
	public String text() {
		return text.toString();
	}
	//=============================================================================================

	//=============================================================================================
	public void text(String text) {
		this.text.setLength(0);
		this.text.append(text);
		caret = this.text.length();
	}
	//=============================================================================================
	
	//=============================================================================================
	public String selection() {
		var a = Math.min(mark, caret);
		var b = Math.max(mark, caret);
		return text.substring(a, b);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void move(int idx, boolean select) {
		caret = idx;
		if (!select) mark = caret;
	}
	//=============================================================================================

	//=============================================================================================
	public void moveRelative(int rel, boolean select) {
		caret = Math.max(0 , Math.min(text.length(), caret + rel));
		if (!select) mark = caret;
	}
	//=============================================================================================

	//=============================================================================================
	public void moveToBegin(boolean select) {
		caret = 0;
		if (!select) mark = caret;
	}
	//=============================================================================================

	//=============================================================================================
	public void moveToEnd(boolean select) {
		caret = text.length();
		if (!select) mark = caret;
	}
	//=============================================================================================
	
	//=============================================================================================
	public void insert(String s) {
		text.insert(caret, s);
		moveRelative(s.length(), false);
	}
	//=============================================================================================

	//=============================================================================================
	public void delete() {
		if (mark == caret) {
			if (caret < text.length()) {
				text.deleteCharAt(caret);
			}
		} else {
			deleteSelection();
		}
	}
	//=============================================================================================

	//=============================================================================================
	public void backspace() {
		if (mark == caret) {
			if (caret > 0) {
				moveRelative(-1, false);
				text.deleteCharAt(caret);
			}
		} else {
			deleteSelection();
		}
	}
	//=============================================================================================

	//=============================================================================================
	private void deleteSelection() {
		var a = Math.min(mark, caret);
		var b = Math.max(mark, caret);
		text.delete(a, b);
		caret = a;
		mark = a;
	}
	//=============================================================================================

	//=============================================================================================
	public void handle(Msg msg) {
		
		//if (!focused) return;
		if (!msg.type.equals(MsgType.KBD_TYPED)) return;
		
		var select  = msg.buttons.contains(Button.SHIFT);
		var control = msg.buttons.contains(Button.CONTROL);
		switch (msg.button) {
		
			case C -> {
				if (control) {
					if (mark != caret) {
						Transferable tx = new StringSelection(selection());
						Toolkit
							.getDefaultToolkit()
							.getSystemClipboard()
							.setContents(tx, null);
					}
				} else {
					insert("" + msg.character);
				}
			}
			
			case V -> {
				if (control) {
					Transferable tx = Toolkit
						.getDefaultToolkit()
						.getSystemClipboard()
						.getContents(null);
					try {
						String s = (String) tx.getTransferData(DataFlavor.stringFlavor);
						if (s != null && !s.isEmpty()) {
							insert(s);
						}
					} catch (Exception e) {
						new X(e);
					}
				} else {
					insert("" + msg.character);
				}
			}
			
			case X -> {
				if (control) {
					if (mark != caret) {
						Transferable tx = new StringSelection(selection());
						Toolkit
							.getDefaultToolkit()
							.getSystemClipboard()
							.setContents(tx, null);
						deleteSelection();
					}
				} else {
					insert("" + msg.character);
				}
			}

			case POS1 -> moveToBegin(select);
			case END -> moveToEnd(select);
			case LEFT -> moveRelative(-1, select);
			case RIGHT -> moveRelative(1, select);
			case DELETE -> delete();
			case BACK_SPACE -> backspace();			
			default -> { if (msg.character != '\0') insert("" + msg.character); }
			
		}
	}
	//=============================================================================================
	
}
//*************************************************************************************************

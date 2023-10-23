//*************************************************************************************************
package pgem.gui;
//*************************************************************************************************

//*************************************************************************************************
public class GUIFactory {

	//=============================================================================================
	public Widget panel(float w, float h) {
		Widget panel = new Widget();
		panel.renders.add(Render.BORDER);
		panel.renders.add(Render.BACKGROUND);
		panel.size(w, h);
		return panel;
	}
	//=============================================================================================
	
	//=============================================================================================
	public Widget label(String text, float w, float h) {
		Widget label = new Widget();
		label.renders.add(Render.TEXT);
		label.textData.horizontalAlign = Align.END;
		label.textData.text = text;
		label.size(w, h);
		return label;
	}
	//=============================================================================================

	//=============================================================================================
	public Widget image(String texture, float w, float h) {
		Widget image = new Widget();
		image.renders.add(Render.IMAGE);
		image.imageData.name = texture;
		image.size(w, h);
		return image;
	}
	//=============================================================================================
	
	//=============================================================================================
	public Widget textBox(String text, float w, float h) {
		Widget textBox = new Widget();
		textBox.renders.add(Render.BORDER);
		textBox.renders.add(Render.BACKGROUND);
		textBox.renders.add(Render.TEXT);
		textBox.renders.add(Render.INTERACT);
		textBox.renders.add(Render.FOCUS);
		textBox.borderColor.set(0, 0, 0, 1);
		textBox.backgroundColor.set(1, 1, 1, 1);
		textBox.interactData.hoverColor.set(textBox.backgroundColor);
		textBox.textData.horizontalAlign = Align.START;
		textBox.textData.text = text;
		textBox.size(w, h);
		return textBox;
	}
	//=============================================================================================

	//=============================================================================================
	public Widget button(String label, float w, float h, String onClick) {
		Widget button = new Widget();
		button.renders.add(Render.BORDER);
		button.renders.add(Render.BACKGROUND);
		button.renders.add(Render.TEXT);
		button.renders.add(Render.INTERACT);
		button.renders.add(Render.FOCUS);
		button.borderColor.set(0, 0, 0, 1);
		button.backgroundColor.set(.7f, .7f, .7f, 1);
		button.textData.text = label;
		button.interactData.onClick = onClick;
		button.size(w, h);
		return button;
	}
	//=============================================================================================
	
}
//*************************************************************************************************

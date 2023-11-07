//*************************************************************************************************
package pgem.gui;
//*************************************************************************************************

import javax.vecmath.Color4f;

import pgem.paint.Graphics;

//*************************************************************************************************
public class Label extends Widget<Label> {

	//=============================================================================================
	public static final Label createLabel(Style style) {
		return new Label(style);
	}
	//=============================================================================================

	//=============================================================================================
	public static final Label createLabel(Style style, String text) {
		var label = new Label(style);
		label.text(text);
		return label;
	}
	//=============================================================================================
	
	//=============================================================================================
	private String  font;
	private Color4f color;
	private String  text  = "TEXT";
	private Align   align = Align.CENTER;
	//=============================================================================================
	
	//=============================================================================================
	public Label(Style style) {
		super(style);
		font = style().get(StyleFont.LABEL).name();
		color = style().get(StyleColor.LABEL_COLOR);
	}
	//=============================================================================================

	//=============================================================================================
	public Label font(String font) {
		this.font = font;
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public Label color(Color4f color) {
		this.color = color;
		return this;
	}
	//=============================================================================================
	
	//=============================================================================================
	public String text() {
		return text;
	}
	//=============================================================================================

	//=============================================================================================
	public void text(String text) {
		this.text = text;
	}
	//=============================================================================================

	//=============================================================================================
	public Align align() {
		return align;
	}
	//=============================================================================================
	
	//=============================================================================================
	public Label align(Align align) {
		this.align = align;
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	protected void paintWidget(Graphics g) {
		g.color(color);
		var data = g.textMetrics(text, font, null);
		var x = switch (align) {
			case START -> 0;
			case CENTER -> (size().x - data.width()) * .5f;
			case END -> size().x - data.width();
		};
		var y = (size().y - data.height()) * .5f + data.descent();		
		g.text(x, y, text, font);
	}
	//=============================================================================================
	
}
//*************************************************************************************************

//*************************************************************************************************
package pgem.gui;
//*************************************************************************************************

import javax.vecmath.Color4f;

import pgem.paint.Graphics;

//*************************************************************************************************
public class Panel extends Widget<Panel> {

	//=============================================================================================
	private Color4f background;
	private Color4f border;
	//=============================================================================================
	
	//=============================================================================================
	public static final Panel createPanel(Style style) {
		return new Panel(style);
	}
	//=============================================================================================

	//=============================================================================================
	public Panel(Style style) {
		super(style, true);
		background = style().get(StyleColor.PANEL_BACKGROUND);
		border = style().get(StyleColor.PANEL_BORDER);
	}
	//=============================================================================================

	//=============================================================================================
	public Panel background(Color4f background) {
		this.background = background;
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public Panel border(Color4f border) {
		this.border = border;
		return this;
	}
	//=============================================================================================
	
	//=============================================================================================
	protected void paintWidget(Graphics g) {
		g.color(background);
		g.box(true, ORIGIN, size());
		g.color(border);
		g.box(false, ORIGIN, size());
	}
	//=============================================================================================

}
//*************************************************************************************************

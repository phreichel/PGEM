//*************************************************************************************************
package pgem.gui;
//*************************************************************************************************

import java.io.File;

import pgem.render.Graphics;
import pgem.render.Renderer;

//*************************************************************************************************
public class GUIRenderer implements Renderer {

	//=============================================================================================
	private final GUI gui;
	//=============================================================================================

	//=============================================================================================
	private Graphics g;
	//=============================================================================================
	
	//=============================================================================================
	public GUIRenderer(GUI gui) {
		this.gui = gui;
	}
	//=============================================================================================
	
	//=============================================================================================
	public void init(Graphics g) {
		this.g = g;
		g.loadFont("caption", "Terminal BOLD 14");
		g.loadFont("normal", "Terminal BOLD 12");
		g.loadTexture("PLUS", new File("data/gui/plus.png"));
		g.loadTexture("TEST", new File("data/gui/test.png"));
	}
	//=============================================================================================

	//=============================================================================================
	public void render(Graphics g) {
		this.g = g;
		g.gui();
		render(gui.root);
	}
	//=============================================================================================
	
	//=============================================================================================
	public void done(Graphics g) {
		this.g = g;
		g.unloadFont("normal");
		g.unloadTexture("PLUS");
	}
	//=============================================================================================

	//=============================================================================================
	private void render(Widget widget) {
		g.pushTransform();
		g.translate(widget.position());
		startTransparent(widget);
		renderBackground(widget);
		renderImage(widget);
		renderText(widget);
		renderBorder(widget);
		stopTransparent(widget);
		var children = widget.children();
		for (int i=0; i<children.size(); i++) {
			Widget child = children.get(i);
			render(child);
		}
		g.popTransform();
	}
	//=============================================================================================

	//=============================================================================================
	private void startTransparent(Widget widget) {
		if (!widget.renders.contains(Render.TRANSPARENT)) return;
		g.blend(true);
	}
	//=============================================================================================

	//=============================================================================================
	private void stopTransparent(Widget widget) {
		if (!widget.renders.contains(Render.TRANSPARENT)) return;
		g.blend(false);
	}
	//=============================================================================================

	//=============================================================================================
	private void renderBackground(Widget widget) {
		if (!widget.renders.contains(Render.BACKGROUND)) return;
		g.color(widget.backgroundColor);
		g.rectangle(true, 0f, 0f, widget.size().x, widget.size().y);
	}
	//=============================================================================================

	//=============================================================================================
	private void renderImage(Widget widget) {
		if (!widget.renders.contains(Render.IMAGE)) return;
		g.color(widget.imageColor);
		g.image(widget.image(), 0, 0, widget.size().x, widget.size().y);
	}
	//=============================================================================================

	//=============================================================================================
	private void renderText(Widget widget) {
		if (!widget.renders.contains(Render.TEXT)) return;
		g.color(widget.textColor);
		g.write(widget.font(), widget.text(), 0, 0, widget.size().x, widget.size().y);
	}
	//=============================================================================================
	
	//=============================================================================================
	private void renderBorder(Widget widget) {
		if (!widget.renders.contains(Render.BORDER)) return;
		g.color(widget.borderColor);
		g.rectangle(false, 0f, 0f, widget.size().x, widget.size().y);
	}
	//=============================================================================================

}
//*************************************************************************************************

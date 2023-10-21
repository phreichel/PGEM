//*************************************************************************************************
package pgem.gui;
//*************************************************************************************************

import java.io.File;

import javax.vecmath.Vector2f;

import pgem.render.Graphics;
import pgem.render.Renderer;

//*************************************************************************************************
public class GUIRenderer implements Renderer {

	//=============================================================================================
	private static final Vector2f ZERO = new Vector2f();
	//=============================================================================================
	
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
		g.loadTexture("DECO", new File("data/gui/deco.png"));
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
		if (widget.renders.contains(Render.HIDDEN)) return;
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
		if (widget.renders.contains(Render.INTERACT) && widget.interactData.hover) {
			g.color(widget.interactData.color);
		}
		g.rectangle(true, ZERO, widget.size());
	}
	//=============================================================================================

	//=============================================================================================
	private void renderImage(Widget widget) {
		if (!widget.renders.contains(Render.IMAGE)) return;
		g.color(widget.imageData.color);
		g.image(
			widget.imageData.name,
			ZERO,
			widget.size(),
			widget.imageData.position,
			widget.imageData.size,
			true
		);
	}
	//=============================================================================================

	//=============================================================================================
	private void renderText(Widget widget) {
		if (!widget.renders.contains(Render.TEXT)) return;
		g.color(widget.textData.color);
		g.text(
			widget.textData.font,
			widget.textData.text,
			ZERO,
			widget.size(),
			widget.textData.horizontalAlign,
			widget.textData.verticalAlign
		);
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

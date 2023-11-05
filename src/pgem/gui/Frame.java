//*************************************************************************************************
package pgem.gui;
//*************************************************************************************************

import javax.vecmath.Vector2f;

import pgem.msg.Axis;
import pgem.msg.InputData;
import pgem.msg.Msg;
import pgem.msg.MsgType;

//*************************************************************************************************
public class Frame extends Widget {

	//=============================================================================================
	private Panel framePanel = new Panel();
	private Panel titlePanel = new Panel();
	private Label titleLabel = new Label();
	private Button closeButton = new Button();
	private Image closeButtonImage = new Image();
	private Panel contentPanel = new Panel();
	//=============================================================================================

	//=============================================================================================
	private boolean  dragging = false;
	private final Vector2f pointerBefore = new Vector2f();
	//=============================================================================================

	//=============================================================================================
	private boolean maximized = false;
	private final Vector2f normalPosition = new Vector2f();
	private final Vector2f normalSize = new Vector2f();
	//=============================================================================================
	
	//=============================================================================================
	public Frame() {

		super(true);

		size(800, 600);
		dock(Dock.TOP_LEFT);
		
		framePanel.size(800, 600);
		framePanel.dock(Dock.SCALE);
		framePanel.background(0, 0, 0, 0);
		
		titlePanel.position(3, 577);
		titlePanel.size(771, 20);
		titlePanel.dock(Dock.SCALE_TOP);
		titlePanel.border(0, 0, 1, 1);
		titlePanel.background(0, 0, .8f, 1);

		titleLabel.position(2, 2);
		titleLabel.size(767, 16);
		titleLabel.dock(Dock.SCALE);
		titleLabel.align(Align.START);
		titleLabel.color(0, 1, 1, 1);

		closeButton.position(777, 577);
		closeButton.size(20, 20);
		closeButton.dock(Dock.TOP_RIGHT);

		closeButtonImage.position(0, 0);
		closeButtonImage.size(20, 20);
		closeButtonImage.color(0, 1, 1, 1);
		closeButtonImage.dock(Dock.SCALE);		
		
		contentPanel.position(3, 3);
		contentPanel.size(794, 571);
		contentPanel.dock(Dock.SCALE);
		
		titleLabel.parent(titlePanel);
		titlePanel.parent(framePanel);
		closeButton.parent(framePanel);
		closeButtonImage.parent(closeButton);
		contentPanel.parent(framePanel);
		
		framePanel.parent(this);

	}
	//=============================================================================================

	//=============================================================================================
	public String title() {
		return titleLabel.text();
	}
	//=============================================================================================

	//=============================================================================================
	public void title(String title) {
		titleLabel.text(title);
	}
	//=============================================================================================

	//=============================================================================================
	public Widget content() {
		return contentPanel;
	}
	//=============================================================================================

	//=============================================================================================
	protected void styleWidget(Style style) {
		framePanel.border(style.get(dragging ? StyleColor.FRAME_DRAG_BORDER : StyleColor.FRAME_BORDER));
		framePanel.background(style.get(StyleColor.FRAME_BACKGROUND));
		titlePanel.background(style.get(StyleColor.FRAME_TITLE_BACKGROUND));		
		titlePanel.border(style.get(StyleColor.FRAME_TITLE_BORDER));		
		titleLabel.color(style.get(StyleColor.FRAME_TITLE_COLOR));
		titleLabel.font(style.get(StyleFont.FRAME_TITLE).name());
		closeButtonImage.color(style.get(StyleColor.FRAME_ICON_COLOR));
		closeButtonImage.image(style.get(StyleIcon.FRAME_CLOSE).name());
	}
	//=============================================================================================
	
	//=============================================================================================
	protected void handleWidget(Msg msg, Vector2f offset) {

		switch (msg.type) {
			case POINTER_PRESSED -> {
				var data = msg.data(InputData.class);
				if (data.button.equals(pgem.msg.Button.POINTER_1)) {
					float px = data.axes.get(Axis.POINTER_HORIZONTAL);
					float py = data.axes.get(Axis.POINTER_VERTICAL);
					if (
						!msg.consumed &&
						px >= offset.x + framePanel.position().x + titlePanel.position().x &&
						py >= offset.y + framePanel.position().y + titlePanel.position().y &&
						px <= offset.x + framePanel.position().x + titlePanel.position().x + titlePanel.size().x &&
						py <= offset.y + framePanel.position().y + titlePanel.position().y + titlePanel.size().y
					) {
						dragging = true;
						framePanel.border(0, 1, 1, 1);
						pointerBefore.set(px, py);
					} else {
						dragging = false;
						framePanel.border(0, 0, 1, 1);
						pointerBefore.set(-1, -1);
					}
				}
			}
			case POINTER_CLICKED -> {
				var data = msg.data(InputData.class);
				float px = data.axes.get(Axis.POINTER_HORIZONTAL);
				float py = data.axes.get(Axis.POINTER_VERTICAL);
				if (
					!msg.consumed &&
					px >= offset.x + framePanel.position().x + titlePanel.position().x &&
					py >= offset.y + framePanel.position().y + titlePanel.position().y &&
					px <= offset.x + framePanel.position().x + titlePanel.position().x + titlePanel.size().x &&
					py <= offset.y + framePanel.position().y + titlePanel.position().y + titlePanel.size().y
				) {
					if (data.button.equals(pgem.msg.Button.POINTER_1) && data.clickCount >= 2) {
						if (!maximized) {
							maximized = true;
							normalPosition.set(position());
							normalSize.set(size());
							position(0, 0);
							size(parent().size());
						} else {
							maximized = false;
							position(normalPosition);
							size(normalSize);
						}
					}
				}
			}
			case POINTER_RELEASED -> {
				var data = msg.data(InputData.class);
				if (data.button.equals(pgem.msg.Button.POINTER_1)) {
					dragging = false;
					framePanel.border(0, 0, 1, 1);
				}
			}
			case POINTER_MOVED -> {
				if (dragging) {
					var data = msg.data(InputData.class);
					float px = data.axes.get(Axis.POINTER_HORIZONTAL);
					float py = data.axes.get(Axis.POINTER_VERTICAL);
					float dx = px- pointerBefore.x;
					float dy = py- pointerBefore.y;
					position(
						position().x + dx,
						position().y + dy);
					pointerBefore.set(px, py);
				}
			}
			default -> {}
		}
		
		if (!msg.consumed && MsgType.POINTER_MASK.contains(msg.type)) {
			var data = msg.data(InputData.class);
			float px = data.axes.get(Axis.POINTER_HORIZONTAL);
			float py = data.axes.get(Axis.POINTER_VERTICAL);
			if (contains(offset, px, py)) {
				if (msg.type.equals(MsgType.POINTER_PRESSED)) {
					Widget p = parent();
					parent(null);
					parent(p);
				}
				msg.consumed = true;
			}
		}
		
	}
	//=============================================================================================

	//=============================================================================================
	private boolean contains(Vector2f offset, float px, float py) {
		return (px >= offset.x && py >= offset.y && px <= offset.x + size().x && py <= offset.y + size().y); 
	}
	//=============================================================================================
	
}
//*************************************************************************************************

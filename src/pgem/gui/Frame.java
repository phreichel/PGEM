//*************************************************************************************************
package pgem.gui;
//*************************************************************************************************

import javax.vecmath.Vector2f;

import pgem.msg.Axis;
import pgem.msg.InputData;
import pgem.msg.Msg;
import pgem.msg.MsgType;

//*************************************************************************************************
public class Frame extends Widget<Frame> {

	//=============================================================================================
	public static final Frame createFrame() {
		return new Frame();
	}
	//=============================================================================================
	
	//=============================================================================================
	private Panel framePanel = null;
	private Panel titlePanel = null;
	private Label titleLabel = null;
	private Button closeButton = null;
	private Image closeButtonImage = null;
	private Panel contentPanel = null;
	//=============================================================================================

	//=============================================================================================
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
		
		framePanel = Panel
			.createPanel()
			.size(800, 600)
			.dock(Dock.SCALE)
			.background(0, 0, 0, 0);
		
		titlePanel = Panel
			.createPanel()
			.position(3, 577)
			.size(771, 20)
			.dock(Dock.SCALE_TOP)
			.border(0, 0, 1, 1)
			.background(0, 0, .8f, 1);

		titleLabel = Label
			.createLabel()
			.position(2, 2)
			.size(767, 16)
			.dock(Dock.SCALE)
			.align(Align.START)
			.color(0, 1, 1, 1);

		closeButton = Button
			.createButton()
			.position(777, 577)
			.size(20, 20)
			.dock(Dock.TOP_RIGHT)
			.action(this::handleClose);

		closeButtonImage = Image
			.createImage()
			.position(0, 0)
			.size(20, 20)
			.color(0, 1, 1, 1)
			.dock(Dock.SCALE);
		
		contentPanel = Panel
			.createPanel()
			.position(3, 3)
			.size(794, 571)
			.dock(Dock.SCALE);
		
		titleLabel.parent(titlePanel);
		titlePanel.parent(framePanel);
		closeButton.parent(framePanel);
		closeButtonImage.parent(closeButton);
		contentPanel.parent(framePanel);
		
		framePanel.parent(this);
		
	}
	//=============================================================================================

	//=============================================================================================
	public Frame focus() {
		if (parent() != null) {
			parent().focus();
			toTop();
		}
		return this;
	}
	//=============================================================================================
	
	//=============================================================================================
	public String title() {
		return titleLabel.text();
	}
	//=============================================================================================

	//=============================================================================================
	public Frame title(String title) {
		titleLabel.text(title);
		return this;
	}
	//=============================================================================================

	//=============================================================================================
	public Widget<?> content() {
		return contentPanel;
	}
	//=============================================================================================

	//=============================================================================================
	private void handleClose(Widget<?> widget, Msg msg) {
		if (flag(Flag.HIDABLE)) {
			flag(Flag.HIDDEN, true);
		}
	}
	//=============================================================================================
	
	//=============================================================================================
	protected void styleWidget(Style style) {
		framePanel.border(style.get(flag(Flag.ARMED) ? StyleColor.FRAME_DRAG_BORDER : StyleColor.FRAME_BORDER));
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
	protected void handleWidget(Msg msg) {

		switch (msg.type) {

			case POINTER_RELEASED -> {
				var data = msg.data(InputData.class);
				if (data.button.equals(pgem.msg.Button.POINTER_1)) {
					flag(Flag.ARMED, false);
				}
			}
			
			case POINTER_MOVED -> {
				if (flag(Flag.ARMED)) {
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
		
		if (msg.consumed) return;
		
		if (MsgType.POINTER_MASK.contains(msg.type)) {
			var data = msg.data(InputData.class);
			float px = data.axes.get(Axis.POINTER_HORIZONTAL);
			float py = data.axes.get(Axis.POINTER_VERTICAL);
			if (containsScreen(px, py)) {
				msg.consumed = true;
			}
		}
		
		switch (msg.type) {

			case POINTER_PRESSED -> {
				
				var data = msg.data(InputData.class);
					
				float px = data.axes.get(Axis.POINTER_HORIZONTAL);
				float py = data.axes.get(Axis.POINTER_VERTICAL);

				if (containsScreen(px, py)) {
					focus();
				}
				
				if (
					!maximized &&
					titlePanel.containsScreen(px, py) &&
					data.button.equals(pgem.msg.Button.POINTER_1)
				) {
					flag(Flag.ARMED, true);
					pointerBefore.set(px, py);
				}

			}

			case POINTER_CLICKED -> {

				var data = msg.data(InputData.class);

				float px = data.axes.get(Axis.POINTER_HORIZONTAL);
				float py = data.axes.get(Axis.POINTER_VERTICAL);
				
				if (titlePanel.containsScreen(px, py)) {
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

			default -> {}

		}
		
	}
	//=============================================================================================
	
}
//*************************************************************************************************

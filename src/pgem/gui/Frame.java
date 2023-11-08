//*************************************************************************************************
package pgem.gui;
//*************************************************************************************************

import javax.vecmath.Vector2f;

import pgem.msg.Axis;
import pgem.msg.InputData;
import pgem.msg.Msg;
import pgem.msg.MsgType;
import pgem.paint.Graphics;

//*************************************************************************************************
public class Frame extends Widget<Frame> {

	//=============================================================================================
	public static final Frame createFrame(Style style) {
		return new Frame(style);
	}
	//=============================================================================================
	
	//=============================================================================================
	private Panel titlePanel = null;
	private Label titleLabel = null;
	private Button closeButton = null;
	private Image closeButtonImage = null;
	private Panel scalePanel = null;
	private Panel contentPanel = null;
	//=============================================================================================

	//=============================================================================================
	private final Vector2f pointerOld = new Vector2f();
	private int action = 0; 
	//=============================================================================================

	//=============================================================================================
	private boolean maximized = false;
	private final Vector2f normalPosition = new Vector2f();
	private final Vector2f normalSize = new Vector2f();
	//=============================================================================================
	
	//=============================================================================================
	public Frame(Style style) {

		super(style, true);

		size(800, 600);
		dock(Dock.TOP_LEFT);
		
		titlePanel = Panel
			.createPanel(style)
			.border(style.get(StyleColor.FRAME_TITLE_BORDER))
			.background(style.get(StyleColor.FRAME_TITLE_BACKGROUND))
			.position(3, 577)
			.size(771, 20)
			.dock(Dock.SCALE_TOP);

		titleLabel = Label
			.createLabel(style)
			.font(style.get(StyleFont.FRAME_TITLE).name())
			.color(style.get(StyleColor.FRAME_TITLE_COLOR))
			.position(2, 2)
			.size(767, 16)
			.dock(Dock.SCALE)
			.align(Align.START);

		closeButton = Button
			.createButton(style)
			.position(777, 577)
			.size(20, 20)
			.dock(Dock.TOP_RIGHT)
			.action(this::handleClose);

		closeButtonImage = Image
			.createImage(style, style.get(StyleIcon.FRAME_CLOSE))
			.position(0, 0)
			.size(20, 20)
			.dock(Dock.SCALE);

		contentPanel = Panel
			.createPanel(style)
			.position(3, 3)
			.size(794, 571)
			.dock(Dock.SCALE);

		scalePanel = Panel
				.createPanel(style)
				.border(style.get(StyleColor.FRAME_SCALE_BORDER))
				.background(style.get(StyleColor.FRAME_SCALE_BACKGROUND))
				.position(796, 0)
				.size(4, 4)
				.dock(Dock.BOTTOM_RIGHT);
		
		titleLabel.parent(titlePanel);
		titlePanel.parent(this);
		closeButton.parent(this);
		closeButtonImage.parent(closeButton);
		contentPanel.parent(this);
		scalePanel.parent(this);
		
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
	protected void paintWidget(Graphics g) {
		var st = style();
		var bg = st.get(StyleColor.FRAME_BACKGROUND);
		var bc = st.get(StyleColor.FRAME_BORDER);
		var dc = st.get(StyleColor.FRAME_BORDER_ARMED);
		var rc = flag(Flag.ARMED) ? dc : bc;
		g.color(bg);
		g.box(true, ORIGIN, size());
		g.color(rc);
		g.box(false, ORIGIN, size());
	}
	//=============================================================================================
	
	//=============================================================================================
	private void handleClose(Widget<?> widget, Msg msg) {
		if (flag(Flag.HIDABLE)) flag(Flag.HIDDEN, true);
	}
	//=============================================================================================
	
	//=============================================================================================
	protected void handleWidget(Msg msg) {

		switch (msg.type) {

			case POINTER_RELEASED -> {
				var data = msg.data(InputData.class);
				if (data.button.equals(pgem.msg.Button.POINTER_1)) {
					flag(Flag.ARMED, false);
					action = 0;
				}
			}
			
			case POINTER_MOVED -> {
				if (flag(Flag.ARMED)) {
					var data = msg.data(InputData.class);
					float px = data.axes.get(Axis.POINTER_HORIZONTAL);
					float py = data.axes.get(Axis.POINTER_VERTICAL);
					float dx = px- pointerOld.x;
					float dy = py- pointerOld.y;
					if (action == 1) {
						position(
							position().x + dx,
							position().y + dy);
					}
					else if (action == 2) {
						position(
							position().x,
							position().y + dy);
						size(
							size().x + dx,
							size().y - dy);
					}
					pointerOld.set(px, py);
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
					data.button.equals(pgem.msg.Button.POINTER_1) && (
						titlePanel.containsScreen(px, py) ||
						scalePanel.containsScreen(px, py)
					)
				) {
					flag(Flag.ARMED, true);
					pointerOld.set(px, py);
					if (titlePanel.containsScreen(px, py))
						action = 1;
					else if (scalePanel.containsScreen(px, py))
						action = 2;
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

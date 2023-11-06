//*************************************************************************************************
package pgem;
//*************************************************************************************************

import pgem.gui.Button;
import pgem.gui.Dock;
import pgem.gui.Frame;
import pgem.gui.GUI;
import pgem.gui.Flag;
import pgem.gui.Panel;
import pgem.gui.Widget;
import pgem.host.Host;
import pgem.msg.Msg;
import pgem.msg.MsgBox;
import pgem.msg.MsgType;

//*************************************************************************************************
public final class App {

	//=============================================================================================
	private final GUI gui = new GUI();
	private final MsgBox msgBox = new MsgBox();
	private final Host host = Host.create(Host.TYPE_JOGL, msgBox);
	//=============================================================================================

	//=============================================================================================
	private boolean quit = false;
	//=============================================================================================
	
	//=============================================================================================
	public void config(String[] args) {
		
	}
	//=============================================================================================

	//=============================================================================================
	public void run() {
		init();
		loop();
		done();
	}
	//=============================================================================================

	//=============================================================================================
	public void quit() {
		quit = true;
	}
	//=============================================================================================
	
	//=============================================================================================
	private void init() {

		gui.root().size(1200, 800);
		
		final Widget<?> container = Widget
			.createWidget(gui.style(), true)
			.position(0, 0)
			.size(1200, 800)
			.dock(Dock.SCALE)
			.parent(gui.root());
		
		for (int i=0; i<5; i++) {
			Frame frame = Frame.createFrame(gui.style());
			frame.title("WINDOW " + (i+1));
			frame.position(40 + i*20, 760 - frame.size().y - i * 30);
			frame.parent(container);
		}

		Button menuButton = Button
			.createButton(gui.style(), "MENU")
			.position(10, 770)
			.size(60, 20)
			.dock(Dock.TOP_LEFT)
			.parent(gui.root());

		final Panel menuPanel = Panel
			.createPanel(gui.style())
			.size(100, 70)
			.position(10, 700)
			.dock(Dock.TOP_LEFT)
			.flag(Flag.HIDDEN, true)
			.flag(Flag.REACTIVE, true)
			.parent(gui.root());
		
		menuButton.action((w, m) -> { menuPanel.flag(Flag.HIDDEN, !menuPanel.flag(Flag.HIDDEN)); }); 
		
		host.init();
		host.plug(gui);

		msgBox.plug(MsgType.APPLICATION_QUIT, this::handleQuit);
		msgBox.plug(MsgType.WINDOW_CLOSE, this::handleQuit);
		gui.hook(msgBox);
		
	}
	//=============================================================================================

	//=============================================================================================
	private void loop() {
		quit = false;
		host.visible(true);
		while (!quit) {
			msgBox.update();
			host.update();
			Thread.yield();			
		}
	}
	//=============================================================================================
	
	//=============================================================================================
	private void done() {
		msgBox.unplug(this::handleQuit);
		host.done();
	}
	//=============================================================================================

	//=============================================================================================
	private void handleQuit(Msg msg) {
		quit();
	}
	//=============================================================================================
	
	//=============================================================================================
	public static void main(String[] args) {
		var app = new App();
		app.config(args);
		app.run();
	}
	//=============================================================================================
	
}
//*************************************************************************************************


//*************************************************************************************************
package pgem;
//*************************************************************************************************

import pgem.gui.Dock;
import pgem.gui.GUI;
import pgem.gui.Icon;
import pgem.gui.Menu;
import pgem.host.Host;
import pgem.model.Controller;
import pgem.msg.Msg;
import pgem.msg.MsgBox;
import pgem.msg.MsgType;
import pgem.msg.WindowData;
import pgem.scene.Scene;
import pgem.terrain.Terrain;

//*************************************************************************************************
public final class App {

	//=============================================================================================
	private final MsgBox     msgbox = new MsgBox();
	private final Host       host = Host.create(Host.TYPE_JOGL, msgbox);
	private final GUI        gui = new GUI();
	private final Scene      scene = new Scene();
	private final Terrain    terrain = new Terrain();
	private final Controller controller = new Controller(gui, scene);
	//=============================================================================================

	//=============================================================================================
	private boolean quit = false;
	//=============================================================================================
	
	//=============================================================================================
	public void config(String[] args) {}
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
		
		var menu = Menu.createMenu(gui.style());
		menu.dock(Dock.TOP_LEFT);
		menu.position(5, 800 - 25);
		menu.size(20, 20);
		menu.parent(gui.root());
		var sub = menu.addSubMenu(Icon.DESK, null);
		sub.size(20, 20);
		sub.menu()
			.add(Icon.FULL_SCREEN, "Fullscreen", (w, m) -> handleFullscreen())
			.add(Icon.SHUT_DOWN, "Quit", (w, m) -> handleQuit(m));

		msgbox.plug(MsgType.APPLICATION_QUIT, this::handleQuit);
		msgbox.plug(MsgType.WINDOW_CLOSE, this::handleQuit);

		controller.hook(msgbox);

		host.init();
		host.plug(scene);
		host.plug(gui);

		scene.init(terrain);
		controller.init();
		
	}
	//=============================================================================================

	//=============================================================================================
	private void loop() {
		quit = false;
		host.visible(true);
		host.fullscreen(true);
		while (!quit) {
			msgbox.update();
			controller.update();
			host.update();
			Thread.yield();
		}
	}
	//=============================================================================================
	
	//=============================================================================================
	private void done() {
		msgbox.unplug(this::handleQuit);
		host.done();
	}
	//=============================================================================================

	//=============================================================================================
	private void handleQuit(Msg msg) {
		quit();
	}
	//=============================================================================================

	//=============================================================================================
	private boolean fsState = true;
	//=============================================================================================
	
	//=============================================================================================
	private void handleFullscreen() {
		var msg = msgbox.alloc(MsgType.WINDOW_FULLSCREEN);
		var data = msg.data(WindowData.class);
		fsState = !fsState;
		data.state = fsState;
		msgbox.post(msg);
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


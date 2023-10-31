//*************************************************************************************************
package pgem.paint;
//*************************************************************************************************

//*************************************************************************************************
public class TestPainter implements Painter {

	//=============================================================================================
	public void paint(Graphics g) {
		
		g.surface();
		
		g.color(1, 0, 0);
		g.box(true, 50, 50, 200, 25);

		g.color(0, 1, 0);
		g.box(false, 50, 50, 200, 25);
		
		g.color(0, 0, 1);
		g.lines(
			true,
			150, 150,
			300, 150,
			300, 400,
			50, 400);
		
		g.color(0, 1, 1);
		g.points(
			150, 150,
			300, 150,
			300, 400,
			50, 400);

		g.push();

		g.translate(400, 400);
		g.rotate(45);
		g.scale(.1f)	;

		g.color(1, 1, 0);
		g.box(true, -50, -50, 400, 400);
		
		g.pop();
		
		g.color(1f, .5f, .8f);
		g.spline(
			10f,
			200, 200,
			400,   0,
			600, 200,
			800, 400,
			600, 600,
			400, 800,
			200, 200);

		g.color(0, .5f, 1f);
		g.bezier(
			10f,
			500, 900,
			700, 900,
			700, 500,
			500, 500,
			300, 500,
			300, 900,
			500, 900);
	
		g.color(1f, 0f, 1f);
		g.push();
		g.rotate(45);
		g.fn(100, 500, 10f, 0f, 1000f, this::sin);
		g.pop();
	}
	//=============================================================================================

	//=============================================================================================
	private float sin(float a) {
		return (float) Math.sin(a/100) * 100;
	}
	//=============================================================================================
	
}
//*************************************************************************************************

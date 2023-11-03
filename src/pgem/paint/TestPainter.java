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
			200, 600,
			  0, 400,
			200, 200);

		g.color(0, .5f, 1f);
		g.bezier(
			10f,
			500, 900,
			750, 900,
			750, 500,
			500, 500,
			250, 500,
			250, 900,
			500, 900);
	
		g.push();
		g.rotate(35);
		g.color(1f, 0f, 1f);
		g.fn(100, 500, 1f, 0f, 800f, this::sin);
		g.pop();
		
		//g.fontInit("default", "Old English Text MT PLAIN 20");
		g.fontInit("default", "Aptos Black PLAIN 20");

		final var text  = "Peter the Cheetah playing Quiddich!";
		g.color(.8f, .8f, .7f);
		g.text(1000, 600, text, "default");

		/*
		var metrics = g.textMetrics(text, "default", null);

		g.color(1, 0, 0);
		g.box(
			false,
			100 + metrics.x,
			100 + metrics.baseline - metrics.descent,
			metrics.w,
			metrics.descent + metrics.ascent);

		g.color(1, 1, 0);
		g.lines(
			false,
			100 + metrics.x,
			100 + metrics.baseline,
			100 + metrics.w,
			100 + metrics.baseline);

		g.color(1, .5f, 0);
		g.lines(
			false,
			100 + metrics.x,
			100 + metrics.baseline - metrics.descent,
			100 + metrics.w,
			100 + metrics.baseline - metrics.descent);

		g.color(.5f, 1f, 0);
		g.lines(
			false,
			100 + metrics.x,
			100 + metrics.baseline + metrics.ascent,
			100 + metrics.w,
			100 + metrics.baseline + metrics.ascent);
		*/

		final var text2 = "oooooze";
		g.color(.8f, .8f, .7f);
		g.text(1000, 580, text2, "default");
		/*
		metrics = g.textMetrics(text2, "default", metrics);
		g.color(1, 0, 0);
		g.box(
			false,
			400 + metrics.x,
			100 + metrics.baseline - metrics.descent,
			metrics.w,
			metrics.descent + metrics.ascent);
		*/
	}
	//=============================================================================================

	//=============================================================================================
	private float sin(float a) {
		return (float) Math.sin(a/20) * 25 - 100000 / (a*a) ;
	}
	//=============================================================================================
	
}
//*************************************************************************************************

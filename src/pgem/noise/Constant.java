//*************************************************************************************************
package pgem.noise;
//*************************************************************************************************

//*************************************************************************************************
public class Constant implements Module {

	//=============================================================================================
	public double value = 0f;
	//=============================================================================================

	//=============================================================================================
	public Constant() {
		value = 0f;
	}
	//=============================================================================================

	//=============================================================================================
	public Constant(double value) {
		this.value = value;
	}
	//=============================================================================================
	
	//=============================================================================================
	public double noise(double x, double y, double z) {
		return value;
	}
	//=============================================================================================

}
//*************************************************************************************************

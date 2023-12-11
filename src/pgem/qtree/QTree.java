//*************************************************************************************************
package pgem.qtree;
//*************************************************************************************************

//*************************************************************************************************
public class QTree {

	//=============================================================================================
	private QNode root;
	//=============================================================================================
	
	//=============================================================================================
	public QTree() {
		root = new QNode(
			null,
			-Float.MAX_VALUE,
			-Float.MAX_VALUE,
			+Float.MAX_VALUE,
			+Float.MAX_VALUE);
	}
	//=============================================================================================

	//=============================================================================================
	public void insert(QObject object) {
		root.insert(object);
	}
	//=============================================================================================

	//=============================================================================================
	public void remove(QObject object) {
		object.node.remove(object);
	}
	//=============================================================================================
	
}
//*************************************************************************************************
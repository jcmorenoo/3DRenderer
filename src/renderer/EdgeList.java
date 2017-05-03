package renderer;

import java.util.HashMap;
import java.util.Map;

/**
 * EdgeList should store the data for the edge list of a single polygon in your
 * scene. A few method stubs have been provided so that it can be tested, but
 * you'll need to fill in all the details.
 *
 * You'll probably want to add some setters as well as getters or, for example,
 * an addRow(y, xLeft, xRight, zLeft, zRight) method.
 */
public class EdgeList {
	
	private int startY;
	private int endY;
	private Map<Integer, Row> right;
	private Map<Integer, Row> left;
	
	
	
	public EdgeList(int startY, int endY) {
		// TODO fill this in.
		this.startY = startY;
		this.endY = endY;
		this.right = new HashMap<Integer, Row>();
		this.left = new HashMap<Integer, Row>();
	}
	
	public void addLeft(int y, float x, float z){
		this.left.put(y, new Row(x,z));
	}
	
	public void addRight(int y, float x, float z){
		this.right.put(y, new Row(x,z));
	}

	public int getStartY() {
		// TODO fill this in.
		return this.startY;
	}

	public int getEndY() {
		// TODO fill this in.
		return this.endY;
	}

	public float getLeftX(int y) {
		// TODO fill this in.
		Row row = this.left.get(y);
		return row.getX();
	}

	public float getRightX(int y) {
		// TODO fill this in.
		
		Row row = this.right.get(y);
		return row.getX();
	}

	public float getLeftZ(int y) {
		// TODO fill this in.
		Row row = this.left.get(y);
		return row.getZ();
	}

	public float getRightZ(int y) {
		// TODO fill this in.
		
		Row row = this.right.get(y);
		return row.getZ();
	}
	
	public static class Row{
		float x;
		float z;
	
		
		public Row(float x, float z){
			
			this.x = x;
			this.z = z;
		}
		
		
		public float getX(){
			return this.x;
		}
		
		public float getZ(){
			return this.z;
		}
		
		
		
	}
}

// code for comp261 assignments

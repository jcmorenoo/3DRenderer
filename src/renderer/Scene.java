package renderer;

import java.awt.Color;
import java.util.List;

/**
 * The Scene class is where we store data about a 3D model and light source
 * inside our renderer. It also contains a static inner class that represents one
 * single polygon.
 * 
 * Method stubs have been provided, but you'll need to fill them in.
 * 
 * If you were to implement more fancy rendering, e.g. Phong shading, you'd want
 * to store more information in this class.
 */
public class Scene {
	private List<Polygon> polygons;
	private Vector3D lightPos;
	private Color lightCol;
	private Color ambientLight;
	private float height = 0;
	private float width = 0;
	private float depth = 0;
	
	public Vector3D viewingDirection = new Vector3D(0f,0f,-10f);
	public Scene(List<Polygon> polygons, Vector3D lightPos) {
		// TODO fill this in.
		this.polygons = polygons;
		this.lightPos = lightPos;
		this.lightCol = new Color(255, 255, 255);
		this.ambientLight = new Color(Renderer.red.getValue(),Renderer.green.getValue(),Renderer.blue.getValue());
          
	}
	
	public Scene(List<Polygon> polygons, Vector3D lightPos, Color ambientLight) {
		// TODO fill this in.
		this.polygons = polygons;
		this.lightPos = lightPos;
		this.lightCol = new Color(255, 255, 255);
		this.ambientLight = ambientLight;
          
	}

	public void setAmbientCol(Color col){
		this.ambientLight = col;
	}
	public Color getAmbientLight(){
		return this.ambientLight;
	}
	
	public Vector3D getLight() {
          // TODO fill this in.
          return this.lightPos;
	}
	
	public Color getLightCol(){
		return this.lightCol;
	}

	public List<Polygon> getPolygons() {
          // TODO fill this in.
          return this.polygons;
	}
	
	/**
	 *  Get the scale for the shape depending on the bounding box
	 * @return
	 */
	public Transform getScale(){
		float height = (float)Renderer.CANVAS_HEIGHT;
		float width = (float)Renderer.CANVAS_WIDTH;
		float divideBy = Math.max(this.depth, Math.max(this.height, this.width));
//		float scaleHeight = Renderer.CANVAS_HEIGHT/Math.max(Math.round(this.depth),Math.max(Math.round(this.height), Math.round(this.width)));
//		float scaleWidth = Renderer.CANVAS_WIDTH/Math.max(Math.round(this.depth),Math.max(Math.round(this.height), Math.round(this.width)));
		
		float scaleHeight = (height / divideBy) * 0.7f ;
		float scaleWidth = (width / divideBy) * 0.7f ;
		
		float minScale = Math.min(scaleHeight, scaleWidth);
		
		Transform scale = Transform.newScale(minScale, minScale, minScale);
		
		return scale;
		
	}
	
	/**
	 * get bounding box for the shape.
	 * @return
	 */
	public Transform getBoundingBox(){
		
		float minX = Float.POSITIVE_INFINITY;
		float maxX = Float.NEGATIVE_INFINITY;
		float minY = Float.POSITIVE_INFINITY;
		float maxY = Float.NEGATIVE_INFINITY;
		float minZ = Float.POSITIVE_INFINITY;
		float maxZ = Float.NEGATIVE_INFINITY;
		Transform translation = null;
		for(Polygon polygon : this.polygons){
			Vector3D[] verts = polygon.getVertices();
			
			for(Vector3D vector : verts){
				minX = Math.min(minX, vector.x);
				maxX = Math.max(maxX, vector.x);
				minY = Math.min(minY, vector.y);
				maxY = Math.max(maxY, vector.y);
				minZ = Math.min(minZ, vector.z);
				maxZ = Math.max(maxZ, vector.z);
			}
			
			
		}
		
		float diffY = maxY - minY;
		float diffX = maxX - minX;
		float diffZ = maxZ - minZ;
		float midX = (diffX/2) + minX;
		float midY = (diffY/2) + minY;
		float midZ = (diffZ/2) + minZ;
		
		this.height = diffY;
		this.width = diffX;
		this.depth = diffZ;
		
		
	
		

		
		
		
		translation = Transform.newTranslation(-minX,-minY, minZ);
		//translation = Transform.newTranslation(minX-(minX),minY-(minY),minZ);
		
		return translation;
	}
	
	public Transform midPoint(){
			
			float minX = Float.POSITIVE_INFINITY;
			float maxX = Float.NEGATIVE_INFINITY;
			float minY = Float.POSITIVE_INFINITY;
			float maxY = Float.NEGATIVE_INFINITY;
			float minZ = Float.POSITIVE_INFINITY;
			float maxZ = Float.NEGATIVE_INFINITY;
			Transform translation = null;
			for(Polygon polygon : this.polygons){
				Vector3D[] verts = polygon.getVertices();
				
				for(Vector3D vector : verts){
					minX = Math.min(minX, vector.x);
					maxX = Math.max(maxX, vector.x);
					minY = Math.min(minY, vector.y);
					maxY = Math.max(maxY, vector.y);
					minZ = Math.min(minZ, vector.z);
					maxZ = Math.max(maxZ, vector.z);
				}
				
				
			}
			
			float diffY = maxY - minY;
			float diffX = maxX - minX;
			float diffZ = maxZ - minZ;
			float midX = (diffX/2) + minX;
			float midY = (diffY/2) + minY;
			float midZ = (diffZ/2) + minZ;
			
			this.height = diffY;
			this.width = diffX;
			this.depth = diffZ;
			
			
			
			
			translation = Transform.newTranslation(-midX,-midY,-midZ);
			//translation = Transform.newTranslation(0,0,0);
			
			return translation;
	}
	
	public Transform backFromMidPoint(){
		
		float minX = Float.POSITIVE_INFINITY;
		float maxX = Float.NEGATIVE_INFINITY;
		float minY = Float.POSITIVE_INFINITY;
		float maxY = Float.NEGATIVE_INFINITY;
		float minZ = Float.POSITIVE_INFINITY;
		float maxZ = Float.NEGATIVE_INFINITY;
		Transform translation = null;
		for(Polygon polygon : this.polygons){
			Vector3D[] verts = polygon.getVertices();
			
			for(Vector3D vector : verts){
				minX = Math.min(minX, vector.x);
				maxX = Math.max(maxX, vector.x);
				minY = Math.min(minY, vector.y);
				maxY = Math.max(maxY, vector.y);
				minZ = Math.min(minZ, vector.z);
				maxZ = Math.max(maxZ, vector.z);
			}
			
			
		}
		
		float diffY = maxY - minY;
		float diffX = maxX - minX;
		float diffZ = maxZ - minZ;
		float midX = (diffX/2) + minX;
		float midY = (diffY/2) + minY;
		float midZ = (diffZ/2) + minZ;
		
		this.height = diffY;
		this.width = diffX;
		this.depth = diffZ;
		
		
		
		
		translation = Transform.newTranslation(midX,midY,midZ);
		//translation = Transform.newTranslation(0,0,0);
		
		return translation;
}

	/**
	 * Polygon stores data about a single polygon in a scene, keeping track of
	 * (at least!) its three vertices and its reflectance.
         *
         * This class has been done for you.
	 */
	public static class Polygon {
		Vector3D[] vertices;
		Color reflectance;

		/**
		 * @param points
		 *            An array of floats with 9 elements, corresponding to the
		 *            (x,y,z) coordinates of the three vertices that make up
		 *            this polygon. If the three vertices are A, B, C then the
		 *            array should be [A_x, A_y, A_z, B_x, B_y, B_z, C_x, C_y,
		 *            C_z].
		 * @param color
		 *            An array of three ints corresponding to the RGB values of
		 *            the polygon, i.e. [r, g, b] where all values are between 0
		 *            and 255.
		 */
		public Polygon(float[] points, int[] color) {
			this.vertices = new Vector3D[3];

			float x, y, z;
			for (int i = 0; i < 3; i++) {
				x = points[i * 3];
				y = points[i * 3 + 1];
				z = points[i * 3 + 2];
				this.vertices[i] = new Vector3D(x, y, z);
			}

			int r = color[0];
			int g = color[1];
			int b = color[2];
			this.reflectance = new Color(r, g, b);
		}

		/**
		 * An alternative constructor that directly takes three Vector3D objects
		 * and a Color object.
		 */
		public Polygon(Vector3D a, Vector3D b, Vector3D c, Color color) {
			this.vertices = new Vector3D[] { a, b, c };
			this.reflectance = color;
		}

		public Vector3D[] getVertices() {
			return vertices;
		}

		public Color getReflectance() {
			return reflectance;
		}

		@Override
		public String toString() {
			String str = "polygon:";

			for (Vector3D p : vertices)
				str += "\n  " + p.toString();

			str += "\n  " + reflectance.toString();

			return str;
		}
		
		
	}
}

// code for COMP261 assignments

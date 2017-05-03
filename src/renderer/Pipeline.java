package renderer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import renderer.Scene.Polygon;

/**
 * The Pipeline class has method stubs for all the major components of the
 * rendering pipeline, for you to fill in.
 * 
 * Some of these methods can get quite long, in which case you should strongly
 * consider moving them out into their own file. You'll need to update the
 * imports in the test suite if you do.
 */
public class Pipeline {

	/**
	 * Returns true if the given polygon is facing away from the camera (and so
	 * should be hidden), and false otherwise.
	 */
	public static boolean isHidden(Polygon poly) {
		// TODO fill this in.
		
		Vector3D[] verts = poly.getVertices();
		Vector3D v1 = verts[0];
		Vector3D v2 = verts[1];
		Vector3D v3 = verts[2];
		
		Vector3D firstDiff = v2.minus(v1);
		Vector3D secondDiff = v3.minus(v2);
		
		Vector3D normal = firstDiff.crossProduct(secondDiff);
		if(normal.z > 0){
			return true;
		}
		
		return false;
	}

	/**
	 * Computes the colour of a polygon on the screen, once the lights, their
	 * angles relative to the polygon's face, and the reflectance of the polygon
	 * have been accounted for.
	 * 
	 * @param lightDirection
	 *            The Vector3D pointing to the directional light read in from
	 *            the file.
	 * @param lightColor
	 *            The color of that directional light.
	 * @param ambientLight
	 *            The ambient light in the scene, i.e. light that doesn't depend
	 *            on the direction.
	 */
	public static Color getShading(Polygon poly, Vector3D lightDirection, Color lightColor, Color ambientLight) {
		// TODO fill this in.
		
		Vector3D normal;
		//normal = poly.getVertices()[0].plus(poly.getVertices()[1].plus(poly.getVertices()[2]));
		normal = getNormal(poly);
		
//		float x = 0;
//		float y = 0;
//		float z = 0;
//		for(Vector3D vector : poly.getVertices()){
//			x = x + vector.unitVector().x;
//			y = y + vector.unitVector().y;
//			z = z + vector.unitVector().z;
//		}
//		
//		normal = new Vector3D(x,y,z);
//		normal = normal.unitVector();
		//Vector3D normal = getNormal(poly);
		Vector3D l = lightDirection.unitVector();
		float cosTheta = Math.max(0, normal.cosTheta(l));
		//float cosTheta = normal.cosTheta(l);
		
		int r = 0;
		int g = 0;
		int b = 0;
		
//		if(lightColor.getRed() == 0 && lightColor.getBlue() == 0 && lightColor.getGreen() == 0){
//			return new Color(ambientLight.getRed(), ambientLight.getGreen(), ambientLight.getBlue());
//		}
		
		r = (int)((ambientLight.getRed()/255 + (lightColor.getRed() * cosTheta)/255) * poly.getReflectance().getRed());
		g = (int)((ambientLight.getGreen()/255 + (lightColor.getGreen() * cosTheta)/255) * poly.getReflectance().getGreen());
		b = (int)((ambientLight.getBlue()/255 + (lightColor.getBlue() * cosTheta)/255) * poly.getReflectance().getBlue());
		
		if(r>255){
			r = 255;
		}
		if(g > 255){
			g = 255;
		}
		if(b>255){
			b = 255;
		}
		
		if(r == 0 && g == 0 && b == 0){
			//null
			return new Color(ambientLight.getRed(), ambientLight.getGreen(), ambientLight.getBlue());
		}
		
		return new Color(r,g,b);
	}

	/**
	 * Returns a Vector3D Normal.
	 * @param poly
	 * @return
	 */
	private static Vector3D getNormal(Polygon poly) {
		// TODO Auto-generated method stub
		
		Vector3D[] verts = poly.getVertices();
		Vector3D v1 = verts[0];
		Vector3D v2 = verts[1];
		Vector3D v3 = verts[2];
		
		Vector3D a = v2.minus(v1);
		Vector3D b = v3.minus(v2);
		
		float nX = (a.y * b.z) - (a.z * b.y);
		float nY = (a.z * b.x) - (a.x * b.z);
		float nZ = (a.x * b.y) - (a.y * b.x);
		
		Vector3D normal = a.crossProduct(b);
		
		float norm = (float) Math.sqrt(Math.pow(normal.x, 2) + Math.pow(normal.y, 2) + Math.pow(normal.z,2));
		
		
		return normal.divide(norm);
	}

	/**
	 * This method should rotate the polygons and light such that the viewer is
	 * looking down the Z-axis. The idea is that it returns an entirely new
	 * Scene object, filled with new Polygons, that have been rotated.
	 * 
	 * @param scene
	 *            The original Scene.
	 * @param xRot
	 *            An angle describing the viewer's rotation in the YZ-plane (i.e
	 *            around the X-axis).
	 * @param yRot
	 *            An angle describing the viewer's rotation in the XZ-plane (i.e
	 *            around the Y-axis).
	 * @return A new Scene where all the polygons and the light source have been
	 *         rotated accordingly.
	 */
	public static Scene rotateScene(Scene scene, float xRot, float yRot) {
		// TODO fill this in.
		Vector3D lightPos = scene.getLight();
		List<Polygon> polygons = new ArrayList<Polygon>();
		
		Transform x;
		Transform y;
		Transform mid = scene.midPoint();
		Transform back = scene.backFromMidPoint();
		Vector3D light = lightPos;
		x = Transform.newXRotation(xRot);
		y = Transform.newYRotation(yRot);
	
		Transform finalRot = x.compose(y);
		Transform translation = back.compose(finalRot.compose(mid));
		for(Polygon polygon : scene.getPolygons()){
			Vector3D[] verts = polygon.getVertices();
			Vector3D v1 = verts[0];
			Vector3D v2 = verts[1];
			Vector3D v3 = verts[2];
			
			Color col = polygon.getReflectance();
			 
			Vector3D newV1 = translation.multiply(v1);
			Vector3D newV2 = translation.multiply(v2);
			Vector3D newV3 = translation.multiply(v3);

			Polygon newPoly = new Polygon(newV1, newV2, newV3, col);
			polygons.add(newPoly);
			
		}
		light = finalRot.multiply(lightPos);
		Scene rotatedS = new Scene(polygons, light);
		//Transform translate = rotatedS.midPoint();
		//System.out.println(rotatedS.getBoundingBox());
		//return applyTransformation(scene.backFromMidPoint(), rotatedS);
		//Scene finalScene = Pipeline.applyTransformation(translate, rotatedS);
		return rotatedS;
		
	}

	/**
	 * This should translate the scene by the appropriate amount.
	 * 
	 * @param scene
	 * @return
	 */
	public static Scene translateScene(Scene scene) {
		// TODO fill this in.
		
		List<Polygon> polygons = new ArrayList<Polygon>();
		Vector3D lightPos = scene.getLight();
		Vector3D light = lightPos;
		Transform translation = scene.getBoundingBox();
		
		Transform scale = scene.getScale();
		
//		Scene s = scaleScene(scene);
		//Vector3D newLight = scale.compose(translation).multiply(lightPos);
		Vector3D viewing = scene.viewingDirection;
		Transform viewingDir = Transform.newTranslation(viewing);
		
		
		
		//	Transform finalTransform = scale.compose(translation.compose(viewingDir));
		Transform finalTransform = scale.compose(translation);
		for(Polygon polygon : scene.getPolygons()){
			Color col = polygon.getReflectance();
			Vector3D[] verts = polygon.getVertices();
			
			Vector3D v1 = verts[0];
			Vector3D v2 = verts[1];
			Vector3D v3 = verts[2];
			
			
			
			
			
			//Transform finalTransform = translation;
		
			
			Vector3D newV1 = finalTransform.multiply(v1);
			Vector3D newV2 = finalTransform.multiply(v2);
			Vector3D newV3 = finalTransform.multiply(v3);
			
			
			polygons.add(new Polygon(newV1, newV2, newV3, col));
			
		}
		light = finalTransform.multiply(lightPos);
		
		return new Scene(polygons, light);
	}

	/**
	 * This should scale the scene.
	 * 
	 * @param scene
	 * @return
	 */
	public static Scene scaleScene(Scene scene) {
		// TODO fill this in.
		List<Polygon> polygons = new ArrayList<Polygon>();
		Vector3D lightPos = scene.getLight();
		Vector3D light = lightPos;
		Transform scale = scene.getScale();
		Transform translate = scene.getBoundingBox();
		Transform finalTransform = scale.compose(translate);
		for(Polygon polygon : scene.getPolygons()){
			Color col = polygon.getReflectance();
			Vector3D[] verts = polygon.getVertices();
			
			Vector3D v1 = verts[0];
			Vector3D v2 = verts[1];
			Vector3D v3 = verts[2];
			
			
			
			
			
			Vector3D newV1 = finalTransform.multiply(v1);
			Vector3D newV2 = finalTransform.multiply(v2);
			Vector3D newV3 = finalTransform.multiply(v3);
			
			polygons.add(new Polygon(newV1, newV2, newV3, col));
			
		}
		light = finalTransform.multiply(light);
		Scene s = new Scene(polygons, light);
		
		return s;
	}

	/**
	 * Computes the edgelist of a single provided polygon, as per the lecture
	 * slides.
	 */
	public static EdgeList computeEdgeList(Polygon poly) {
		// TODO fill this in.
		
		
		Vector3D[] verts = poly.getVertices();
		
		Vector3D v1 = verts[0];
		Vector3D v2 = verts[1];
		Vector3D v3 = verts[2];
		
		List<Edge> edges = new ArrayList<Edge>();
		
		edges.add(new Edge(v1,v2));
		edges.add(new Edge(v2,v3));
		edges.add(new Edge(v3,v1));
		
		int minY = Math.round(Math.min(v1.y, Math.min(v2.y, v3.y)));
		int maxY = Math.round(Math.max(v1.y, Math.max(v2.y, v3.y)));
		
		EdgeList eL = new EdgeList(minY, maxY);
		
		
		
		for(Edge edge : edges){
			Vector3D a = edge.v1;
			Vector3D b = edge.v2;
			
			float slopeX = ((b.x - a.x)/(b.y-a.y));
			float slopeZ = ((b.z - a.z)/(b.y - a.y));
			
			float x = a.x;
			float z = a.z;
			int y = Math.round(a.y);
			
			if(a.y < b.y){
				while(y <= Math.round(b.y)){
					if(y > Math.round(b.y)){
						break;
					}
					//go left going down.
					//System.out.println("Going down");
					float xLeft = x;
					float zLeft = z;
					eL.addLeft(y, xLeft, zLeft);
					
					x = x + slopeX;
					z = z + slopeZ;
					//System.out.println(y);
					//System.out.println(Math.round(b.y));
					y++;
				}
			}
			else{
				while(y >= Math.round(b.y)){
					//go right going up..
					if(y < Math.round(b.y)){
						break;
					}
					//System.out.println("Going up");
					float xRight = x;
					float zRight = z;
					eL.addRight(y, xRight, zRight);
					
					x = x - slopeX;
					z = z - slopeZ;
					//System.out.println(y);
					//System.out.println(Math.round(b.y));
					y--;
					
					
				}
				
			}
			
		}
		
		
		return eL;
	}
	
	public static Scene applyTransformation(Transform transformation, Scene scene){
		
		List<Polygon> polygons = new ArrayList<Polygon>();
		for(Polygon polygon : scene.getPolygons()){
			Vector3D v1 = polygon.getVertices()[0];
			Vector3D v2 = polygon.getVertices()[1];
			Vector3D v3 = polygon.getVertices()[2];
			
			Vector3D newV1 = transformation.multiply(v1);
			Vector3D newV2 = transformation.multiply(v2);
			Vector3D newV3 = transformation.multiply(v3);
			
			Polygon pol = new Polygon(newV1, newV2, newV3, polygon.getReflectance());
			polygons.add(pol);
			
		}
		
		Scene s = new Scene(polygons, scene.getLight());
		
		return s;
	}

	/**
	 * Fills a zbuffer with the contents of a single edge list according to the
	 * lecture slides.
	 * 
	 * The idea here is to make zbuffer and zdepth arrays in your main loop, and
	 * pass them into the method to be modified.
	 * 
	 * @param zbuffer
	 *            A double array of colours representing the Color at each pixel
	 *            so far.
	 * @param zdepth
	 *            A double array of floats storing the z-value of each pixel
	 *            that has been coloured in so far.
	 * @param polyEdgeList
	 *            The edgelist of the polygon to add into the zbuffer.
	 * @param polyColor
	 *            The colour of the polygon to add into the zbuffer.
	 */
	public static void computeZBuffer(Color[][] zbuffer, float[][] zdepth, EdgeList eL, Color polyColor) {
		// TODO fill this in.
		
		Integer y = Math.min(eL.getStartY(),eL.getEndY());
		int yMax = Math.max(eL.getStartY(), eL.getEndY());
		
		
		while(y < yMax){
		
			
			float zLeft = eL.getLeftZ(y);
			float zRight = eL.getRightZ(y);
			float xLeft = eL.getLeftX(y);
			float xRight = eL.getRightX(y);
			
			float slope = (zRight - zLeft)/(xRight - xLeft);
			
			float z = eL.getLeftZ(y);
			int x = Math.round(eL.getLeftX(y));
			
			while(x < Math.round(eL.getRightX(y))){

				if(x >= 0 && y >= 0){
					if(z < zdepth[x][y]){
						if(y < Renderer.CANVAS_HEIGHT || x < Renderer.CANVAS_WIDTH){
							
							zbuffer[x][y] = polyColor;
							zdepth[x][y] = z;
						}
					}
				}
				
				z = z + slope;
				x = x + 1;
			}
			
			y = y + 1;
		}
		
	
	}
}

// code for comp261 assignments

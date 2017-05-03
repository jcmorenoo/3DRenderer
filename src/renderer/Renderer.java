package renderer;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import renderer.Scene.Polygon;

public class Renderer extends GUI {
	private Scene scene;
	private Transform transformation = Transform.identity();
	private Vector3D viewingDirection;
	
	public static Transform boundingBox;
	
	@Override
	protected void onLoad(File file) {
		// TODO fill this in.

		/*
		 * This method should parse the given file into a Scene object, which
		 * you store and use to render an image.
		 */
		
		this.viewingDirection = new Vector3D(0f,0f,-10f);
		List<Polygon> polygons = new ArrayList<Polygon>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			Vector3D light = null;
			//first line = light source
			if((line = br.readLine()) != null){
				String[] value = line.split(" ");
				
				light = new Vector3D(Float.parseFloat(value[0]), Float.parseFloat(value[1]), Float.parseFloat(value[2]));
			}
			
			
			//second - last line
			while((line = br.readLine()) != null){
				String[] values = line.split(" ");
				float[] verts = new float[9];
				//store the values into vertices
				for(int i = 0; i < verts.length; i++){
					verts[i] = Float.parseFloat(values[i]);
				}
				Vector3D v1 = new Vector3D(verts[0], verts[1], verts[2]);
				Vector3D v2 = new Vector3D(verts[3], verts[4], verts[5]);
				Vector3D v3 = new Vector3D(verts[6], verts[7], verts[8]);
				int[] col = new int[] {Integer.parseInt(values[9]), Integer.parseInt(values[10]), Integer.parseInt(values[11])};
				Color color = new Color(col[0], col[1], col[2]);
				
				//Scene.Polygon....
				Polygon polygon = new Polygon(v1, v2, v3, color);
				
				polygons.add(polygon);
				
				
				//				Polygon p = new Polygon();
			}
			
			Scene s = new Scene(polygons, light);
			//this.scene = Pipeline.translateScene(s);
			
			this.scene = Pipeline.translateScene(s);
			
			render();
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	protected void onKeyPress(KeyEvent ev) {
		// TODO fill this in.

		/*
		 * This method should be used to rotate the user's viewpoint.
		 */
		
		
		
		if(ev.getKeyChar() == 'a' || ev.getKeyCode() == 65 || ev.getKeyCode() == KeyEvent.VK_LEFT){
			

			
			Scene s = Pipeline.rotateScene(this.scene, 0, 0.1f);
			//Transform yRot = Transform.newYRotation(0.5f);
			
			//Scene finalScene = Pipeline.translateScene(s);
			this.scene = s;
		
			
		}
		if(ev.getKeyChar() == 's' || ev.getKeyCode() == 83 || ev.getKeyCode() == KeyEvent.VK_DOWN){
		
			//Transform down = Transform.newXRotation(0.5f);
			//this.transformation = down;
			Scene s = Pipeline.rotateScene(this.scene, 0.1f, 0);
			//Scene finalScene = Pipeline.translateScene(s);
			this.scene = s;
			
		}
		if(ev.getKeyChar() == 'd' || ev.getKeyCode() == 68 || ev.getKeyCode() == KeyEvent.VK_RIGHT){
			
//			Transform right = Transform.newYRotation(-0.5f);
//			this.transformation = right;
			Scene s = Pipeline.rotateScene(this.scene, 0, -0.1f);
			//Scene finalScene = Pipeline.translateScene(s);
			this.scene = s;
			
		}
		if(ev.getKeyChar() == 'w' || ev.getKeyCode() == 87 || ev.getKeyCode() == KeyEvent.VK_UP){
		
			
			//Transform up = Transform.newXRotation(-0.5f);
			
			//this.transformation = up;
			
			Scene s = Pipeline.rotateScene(this.scene, -0.1f, 0);
			
			//Scene finalScene = Pipeline.translateScene(s);
			this.scene = s;
		
		}
		
		
	}

	@Override
	protected BufferedImage render() {
		// TODO fill this in.

		
		/*
		 * This method should put together the pieces of your renderer, as
		 * described in the lecture. This will involve calling each of the
		 * static method stubs in the Pipeline class, which you also need to
		 * fill in.
		 */
		
		
		if(this.scene != null){
			applyTransformation();
			
			Color[][] zCol = new Color[CANVAS_WIDTH+30][CANVAS_HEIGHT+30];
			
			for(int i = 0; i < CANVAS_WIDTH; i++){
				for(int j = 0; j < CANVAS_HEIGHT; j++){
					zCol[i][j] = Color.white;
				}
			}
			float[][] zDepth = new float[CANVAS_WIDTH+30][CANVAS_HEIGHT+30];
			
			
			for(int i = 0; i < CANVAS_WIDTH; i++){
				for(int j = 0; j < CANVAS_HEIGHT; j++){
					zDepth[i][j] = Float.POSITIVE_INFINITY;
				}
			}
			
			Vector3D lightDirection = this.scene.getLight();
			Color lightCol = this.scene.getLightCol();
			Color ambientLight = this.scene.getAmbientLight();
			
			
			
			
//			for(Polygon polygon : this.scene.getPolygons()){
//				Vector3D v1 = polygon.getVertices()[0];
//				Vector3D v2 = polygon.getVertices()[1];
//				Vector3D v3 = polygon.getVertices()[2];
//				
//				Vector3D newV1 = this.transformation.multiply(v1);
//				Vector3D newV2 = this.transformation.multiply(v2);
//				Vector3D newV3 = this.transformation.multiply(v3);
//				Color col = polygon.getReflectance();
//				
//				Polygon pol = new Polygon(newV1, newV2, newV3, col);
//				newPoly.add(pol);
//			}
//			this.scene = new Scene(newPoly, lightDirection);
			
			for(Polygon polygon : this.scene.getPolygons()){
				
				if(!Pipeline.isHidden(polygon)){
					
					EdgeList eL = Pipeline.computeEdgeList(polygon);
					Color color = Pipeline.getShading(polygon, lightDirection, lightCol, ambientLight);
					
					Pipeline.computeZBuffer(zCol, zDepth, eL, color);
					
				}
				
			}
	
			// make the bitmap of smoothly changing colors;
			// Your program should render a model
	//		for (int x = 0; x < CANVAS_WIDTH; x++) {
	//			for (int y = 0; y < CANVAS_HEIGHT; y++) {
	//				float hue = (float) Math.sin((x + Math.pow(y, 1))
	//						/ (CANVAS_WIDTH));
	//				bitmap[x][y] = Color.getHSBColor(hue, 1.0f, 1.0f);
	//			}
	//		}
	
			// render the bitmap to the image so it can be displayed (and saved)
	//		return convertBitmapToImage(bitmap);
			
			
			return convertBitmapToImage(zCol);
		}
		return null;
		
	}

	private void applyTransformation() {
		// TODO Auto-generated method stub
		this.scene = Pipeline.applyTransformation(this.transformation, this.scene);
		
		
	}

	/**
	 * Converts a 2D array of Colors to a BufferedImage. Assumes that bitmap is
	 * indexed by column then row and has imageHeight rows and imageWidth
	 * columns. Note that image.setRGB requires x (col) and y (row) are given in
	 * that order.
	 */
	private BufferedImage convertBitmapToImage(Color[][] bitmap) {
		BufferedImage image = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < CANVAS_WIDTH; x++) {
			for (int y = 0; y < CANVAS_HEIGHT; y++) {
				image.setRGB(x, y, bitmap[x][y].getRGB());
			}
		}
		return image;
	}

	public static void main(String[] args) {
		new Renderer();
	}

	@Override
	protected void setAmbientLight(int col, int value) {
		// TODO Auto-generated method stub
		
	}

	
	
}

// code for comp261 assignments

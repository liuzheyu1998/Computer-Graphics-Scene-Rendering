package comp557.a1;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import mintools.parameters.DoubleParameter;
import comp557.a1.geom.*;

import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

public class Geometry extends GraphNode{
	Tuple3d trans;
	Tuple3d sca;
	Vector3f color =  new Vector3f(0, 0, 0);
	String type;
	public Geometry( String name, Tuple3d translation, Tuple3d scaling, String type) {
		super(name);
		this.trans = translation;
		this.sca = scaling;
		this.type = type;
		
		
	}
	public Geometry( String name, String type) {
		super(name);
		this.type = type;
		
	}
	
	public void setCentre(Tuple3d center) {
		this.trans = center;
		
	}
	
	public void setScale(Tuple3d scale) {
		this.sca = scale;
		
	}
	
	public void setColor(Tuple3d  color) {
		this.color = new Vector3f((float)color.x, (float)color.y, (float)color.z);
		
	}
	
	@Override
	public void display( GLAutoDrawable drawable, BasicPipeline pipeline ) {
		pipeline.push();
		
		pipeline.translate(trans.x,trans.y, trans.z);
		pipeline.scale(sca.x,sca.y,sca.z);

		GL4 gl = drawable.getGL().getGL4();
		
		pipeline.setModelingMatrixUniform(gl);
//		pipeline.setColorUniform(gl,this.color);
        gl.glUniform3f( pipeline.kdID, this.color.x, this.color.y, this.color.z);
		if (type.equals("box")) {
			Cube.draw(drawable, pipeline);
		}else if(type.equals("sphere")) {
			Sphere.draw(drawable, pipeline);
		}

		pipeline.setModelingMatrixUniform(gl);


		super.display( drawable, pipeline );		
		pipeline.pop();
	}


}

package comp557.a1;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;

import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import mintools.parameters.DoubleParameter;

public class RotaryJoint extends GraphNode  {
	DoubleParameter r;

	Tuple3d axis;
	Tuple3d trans;
	
	public RotaryJoint(String name, double min, double max, Tuple3d axis, Tuple3d translation) {
		super(name);
		dofs.add(r = new DoubleParameter(name + "rotation",0.1, min, max));
		this.axis = axis;
		this.trans = translation;

	}
	public RotaryJoint(String name) {
		super(name);
		dofs.add(r = new DoubleParameter(name + "rotation",0, -150, 150));

	}
	
	public void setAxis(Tuple3d axis) {
		this.axis = axis;
	}
	
	public void setPosition(Tuple3d translation) {
		this.trans = translation;
	}
	@Override
	public void display( GLAutoDrawable drawable, BasicPipeline pipeline ) {
		pipeline.push();
		
		pipeline.translate(trans.x,trans.y, trans.z);
		pipeline.rotate(r.getValue()*Math.PI/180,axis.x,axis.y,axis.z);

		GL4 gl = drawable.getGL().getGL4();
		pipeline.setModelingMatrixUniform(gl);
		


		super.display( drawable, pipeline );		
		pipeline.pop();
	}

}

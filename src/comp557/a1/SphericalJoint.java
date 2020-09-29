package comp557.a1;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;

import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import mintools.parameters.DoubleParameter;

public class SphericalJoint extends GraphNode{
	DoubleParameter rx;
	DoubleParameter ry;
	DoubleParameter rz;
	Tuple3d trans;

	
	public SphericalJoint(String name,Tuple3d translation) {
		super(name);
		dofs.add( this.rx = new DoubleParameter( name+" rx", 0, -180, 180 ) );		
		dofs.add( this.ry = new DoubleParameter( name+" ry", 0, -180, 180 ) );
		dofs.add( this.rz = new DoubleParameter( name+" rz", 0, -180, 180 ) );
		this.trans = translation;
		
	}
	
	public SphericalJoint(String name) {
		super(name);
		dofs.add( this.rx = new DoubleParameter( name+" rx", 0, -180, 180 ) );		
		dofs.add( this.ry = new DoubleParameter( name+" ry", 0, -180, 180 ) );
		dofs.add( this.rz = new DoubleParameter( name+" rz", 0, -180, 180 ) );
	}
	
	public void setPosition(Tuple3d translation) {
		this.trans = translation;
	}
	@Override
	public void display( GLAutoDrawable drawable, BasicPipeline pipeline ) {
		pipeline.push();
		
		pipeline.translate(trans.x, trans.y, trans.z);
		pipeline.rotate(rx.getValue()*Math.PI/180,1,0,0);
		pipeline.rotate(ry.getValue()*Math.PI/180,0,1,0);
		pipeline.rotate(rz.getValue()*Math.PI/180,0,0,1);
		GL4 gl = drawable.getGL().getGL4();
		pipeline.setModelingMatrixUniform(gl);

		super.display( drawable, pipeline );		
		pipeline.pop();
	}
	
}

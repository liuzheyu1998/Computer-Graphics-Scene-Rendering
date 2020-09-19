package comp557.a1;

import com.jogamp.opengl.GLAutoDrawable;
import javax.vecmath.Vector3d;

import mintools.parameters.DoubleParameter;

public class SphericalJoint extends GraphNode{
	DoubleParameter rx;
	DoubleParameter ry;
	DoubleParameter rz;

	
	public SphericalJoint(String name, double rx, double ry, double rz) {
		super(name);
		dofs.add( this.rx = new DoubleParameter( name+" rx", rx, -180, 180 ) );		
		dofs.add( this.ry = new DoubleParameter( name+" ry", ry, -180, 180 ) );
		dofs.add( this.rz = new DoubleParameter( name+" rz", rz, -180, 180 ) );
		
	}
	
	@Override
	public void display( GLAutoDrawable drawable, BasicPipeline pipeline ) {
		pipeline.push();
		
		
		pipeline.rotate(rx.getValue()*Math.PI/180,1,0,0);
		pipeline.rotate(ry.getValue()*Math.PI/180,0,1,0);
		pipeline.rotate(rz.getValue()*Math.PI/180,0,0,1);

		super.display( drawable, pipeline );		
		pipeline.pop();
	}
	
}

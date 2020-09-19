package comp557.a1;

import com.jogamp.opengl.GLAutoDrawable;
import javax.vecmath.Vector3d;

import mintools.parameters.DoubleParameter;

public class RotaryJoint extends GraphNode  {
	DoubleParameter r;
	DoubleParameter tx;
	DoubleParameter ty;
	DoubleParameter tz;
	Vector3d axis;
	
	public RotaryJoint(String name, double min, double max, Vector3d axis, Vector3d translation) {
		super(name);
		dofs.add(r = new DoubleParameter(name + " r",0, min, max));
		dofs.add( tx = new DoubleParameter( name+" tx", 0, -2, 2 ) );		
		dofs.add( ty = new DoubleParameter( name+" ty", 0, -2, 2 ) );
		dofs.add( tz = new DoubleParameter( name+" tz", 0, -2, 2 ) );
		this.axis.set(axis);
		this.tx.setValue(translation.x);
		this.ty.setValue(translation.y);
		this.tz.setValue(translation.z);
		
		
	}
	
	@Override
	public void display( GLAutoDrawable drawable, BasicPipeline pipeline ) {
		pipeline.push();
		
		pipeline.translate(tx.getValue(), ty.getValue(), tz.getValue());
		pipeline.rotate(r.getValue()*Math.PI/180,axis.x,axis.y,axis.z);


		super.display( drawable, pipeline );		
		pipeline.pop();
	}

}

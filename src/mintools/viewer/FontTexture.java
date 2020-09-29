package mintools.viewer;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jogamp.opengl.DebugGL4;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import mintools.parameters.DoubleParameter;
import mintools.swing.ControlFrame;
import mintools.swing.VerticalFlowPanel;

/**
 * Bitmap fonts, using Consolas generated with http://www.codehead.co.uk/cbfg/
 * @author kry
 */
public class FontTexture implements GLEventListener {

    Texture texture; 

    int glslProgramID;
    int texUnitID;
	int posID;
	int sizeID;
	int colourID;
	int charID;
	int positionAttributeID;
	int texCoordAttributeID;
	int positionBufferID;
	int texCoordBufferID;
	int elementBufferID;

    @Override
    public void init(GLAutoDrawable drawable) {
        drawable.setGL(new DebugGL4(drawable.getGL().getGL4()));
        GL4 gl = drawable.getGL().getGL4();
        gl.glEnable( GL4.GL_BLEND );
      	gl.glBlendFunc( GL4.GL_SRC_ALPHA, GL4.GL_ONE_MINUS_SRC_ALPHA );

    	URL urlCollapsed = getClass().getResource( "fontTexture/ExportedFont.tga" );
    	try {
	    	texture = TextureIO.newTexture( urlCollapsed, false, "tga" );
	    	texture.enable(gl);
	    	texture.bind(gl);
//	    	gl.glGenerateTextureMipmap( texture.getTextureObject() );
    	} catch (Exception e) {
			e.printStackTrace();
    	}
	    
    	// Create the GLSL program 
		glslProgramID = createProgram( drawable, "fontTexture" );
		// Get the IDs of the parameters (i.e., uniforms and attributes)
        gl.glUseProgram( glslProgramID );
        texUnitID = gl.glGetUniformLocation( glslProgramID, "tex" );
        posID = gl.glGetUniformLocation( glslProgramID, "pos" );
        sizeID = gl.glGetUniformLocation( glslProgramID, "size" );
        colourID = gl.glGetUniformLocation( glslProgramID, "colour" );
        charID = gl.glGetUniformLocation( glslProgramID, "character" );
        positionAttributeID = gl.glGetAttribLocation( glslProgramID, "position" );
        texCoordAttributeID = gl.glGetAttribLocation( glslProgramID, "texCoord" );
        
        // Initialize the vertex and index buffers
        FloatBuffer vertexBuffer = GLBuffers.newDirectFloatBuffer( new float[] {0,0,0,1,1,0,1,1} );
        FloatBuffer texCoordBuffer = GLBuffers.newDirectFloatBuffer( new float[] {0,0,0,1,1,0,1,1} );
        ShortBuffer elementBuffer = GLBuffers.newDirectShortBuffer( new short[] {0,1,2,3} );
        int[] bufferIDs = new int[3];
        gl.glGenBuffers( 3, bufferIDs, 0 );
        positionBufferID = bufferIDs[0];
        texCoordBufferID = bufferIDs[1];
        elementBufferID = bufferIDs[2];
        gl.glBindBuffer( GL4.GL_ARRAY_BUFFER, positionBufferID );
        gl.glBufferData( GL4.GL_ARRAY_BUFFER, vertexBuffer.capacity() * Float.BYTES, vertexBuffer, GL4.GL_STATIC_DRAW );
        gl.glBindBuffer( GL4.GL_ARRAY_BUFFER, texCoordBufferID );
        gl.glBufferData( GL4.GL_ARRAY_BUFFER, texCoordBuffer.capacity() * Float.BYTES, texCoordBuffer, GL4.GL_STATIC_DRAW );
        gl.glBindBuffer( GL4.GL_ELEMENT_ARRAY_BUFFER, elementBufferID );
        gl.glBufferData( GL4.GL_ELEMENT_ARRAY_BUFFER, elementBuffer.capacity() * Short.BYTES, elementBuffer, GL4.GL_STATIC_DRAW );
        
        // need to set these every time if other people are using textures... 
        gl.glActiveTexture( GL4.GL_TEXTURE0 );
      	gl.glTexParameterf( GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MAG_FILTER, GL4.GL_LINEAR ); // will be set again elsewhere for demo        
      	gl.glTexParameterf( GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_LINEAR ); // will be set again elsewhere for demo 
        float[] fa= new float[1];
        gl.glGetFloatv(GL.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, fa, 0 );
        gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAX_ANISOTROPY_EXT, fa[0]);        
    }
    
    /**
	 * Creates a GLSL program from the .vp and .fp code provided in the shader directory 
	 * @param drawable
	 * @param name
	 * @return
	 */
	private int createProgram( GLAutoDrawable drawable, String name ) {
		GL4 gl = drawable.getGL().getGL4();
		ShaderCode vsCode = ShaderCode.create( gl, GL4.GL_VERTEX_SHADER, this.getClass(), "fontTexture", "fontTexture/bin", name, false );
		ShaderCode fsCode = ShaderCode.create( gl, GL4.GL_FRAGMENT_SHADER, this.getClass(), "fontTexture", "fontTexture/bin", name, false );
		ShaderProgram shaderProgram = new ShaderProgram();
		shaderProgram.add( vsCode );
		shaderProgram.add( fsCode );
		if ( !shaderProgram.link( gl, System.err ) ) {
			throw new GLException( "Couldn't link program: " + shaderProgram );
		}	
		shaderProgram.init(gl);
        return shaderProgram.program();
	}
    
	public void drawTextLines( GLAutoDrawable drawable, String txt, float x, float y, float size, float r, float g, float b ) {
        int w = drawable.getSurfaceWidth();
        int h = drawable.getSurfaceHeight();
    	GL4 gl = drawable.getGL().getGL4();        

        gl.glUseProgram( glslProgramID );

        gl.glActiveTexture( GL4.GL_TEXTURE0 );
        texture.enable(gl);
        texture.bind(gl);
             	
        gl.glEnableVertexAttribArray( positionAttributeID );
        gl.glEnableVertexAttribArray( texCoordAttributeID );
        gl.glBindBuffer( GL4.GL_ARRAY_BUFFER, positionBufferID );		
		gl.glVertexAttribPointer( positionAttributeID, 2, GL4.GL_FLOAT, false, 2*Float.BYTES, 0 );
		gl.glBindBuffer( GL4.GL_ARRAY_BUFFER, texCoordBufferID );		
	    gl.glVertexAttribPointer( texCoordAttributeID, 2, GL4.GL_FLOAT, false, 2*Float.BYTES, 0 );			
		gl.glBindBuffer( GL4.GL_ELEMENT_ARRAY_BUFFER, elementBufferID );
		
		gl.glUniform1i( texUnitID, 0 );
 
		// 64x64 pixel cells in 1024x1024 texture, actual width is 30 texels
		float sx = size*2f/w; 
		float sy = size*2f/h;
        gl.glUniform2f( sizeID, sx, sy );  // need to factor viewport size into this...
        gl.glUniform3f( colourID, r, g, b );
		
        int xoff = 0;
        for ( int i = 0; i < txt.length(); i++ ) {
        	char c = txt.charAt(i);
        	if ( c == '\n' ) {
        		y = y + size;
        		xoff = 0;
        	} else {
	        	float cvvx = -1f + (x+xoff*30/64f*size)*2f/w; 
	        	float cvvy = 1 - y*2f/h;
	            gl.glUniform2f( posID, cvvx, cvvy );
	            gl.glUniform1i( charID, c );    // ASCII
	    		gl.glDrawElements( GL4.GL_TRIANGLE_STRIP, 4, GL4.GL_UNSIGNED_SHORT, 0 );
	    		xoff++;
        	}
        }
	}
    
    @Override
    public void display(GLAutoDrawable drawable) {
    	GL4 gl = drawable.getGL().getGL4();        
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); 
        gl.glClear( GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT );

        String txt = "Tgyj is a test of multiple lines... \nThis is the second line!\nThis is the 3rd.";
		float size = fontSize.getFloatValue(); // size in pixels
		float x = pxx.getFloatValue();     // pixel location
		float y = pxy.getFloatValue(); 
		drawTextLines( drawable, txt, x, y, size, 1, 1, 1 );
    }
    
    @Override
    public void dispose(GLAutoDrawable drawable) {}
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}
    
    public FontTexture() {
        // do nothing
    }
       
    DoubleParameter fontSize = new DoubleParameter( "size", 32, 0, 512 );
    DoubleParameter pxx = new DoubleParameter( "pxx", 0, 0, 512 );
    DoubleParameter pxy = new DoubleParameter( "pxy", 32, 0, 512 );
    
    public JPanel getControls() {
    	VerticalFlowPanel vfp = new VerticalFlowPanel();
    	vfp.add( fontSize.getControls() );
    	vfp.add( pxx.getControls() );
    	vfp.add( pxy.getControls() );
    	return vfp.getPanel();
    }
    
    /** For testing */
    public static void main(String[] args) {
        FontTexture fontTexture = new FontTexture();
        GLProfile glprofile = GLProfile.get( GLProfile.GL4 );
        GLCapabilities glcapabilities = new GLCapabilities( glprofile );
        GLCanvas glcanvas = new GLCanvas( glcapabilities );
        glcanvas.addGLEventListener( fontTexture );
        FPSAnimator animator; 
        animator = new FPSAnimator(glcanvas, 60);
        animator.start();
        final JFrame jframe = new JFrame( "JOGL Texture Font Test" ); 
        jframe.addWindowListener( new WindowAdapter() {
            public void windowClosing( WindowEvent windowevent ) {
                jframe.dispose();
                System.exit( 0 );
            }
        });
        jframe.getContentPane().add( glcanvas, BorderLayout.CENTER );
        jframe.setSize( 500, 500 );
        jframe.setVisible( true );
        ControlFrame controls = new ControlFrame("Controls");
        controls.add("Font Pos", fontTexture.getControls());
        controls.setSize( 500, 500 );
        controls.setLocation(500 + 20, 0);
        controls.setVisible(true);    
    }

}

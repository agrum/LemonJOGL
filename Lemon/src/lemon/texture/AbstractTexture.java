package lemon.texture;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import lemon.Lemon;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.glu.GLU;

public abstract class AbstractTexture extends Lemon{
	protected String m_name;
	protected int m_id;
	protected int m_internalFormat;
	protected int m_internalFormatBase;
	protected boolean m_signed;
	protected boolean m_integer;
	protected Sampler m_sampler = null;
	protected boolean m_updated;

	public AbstractTexture(String p_name, int p_internalFormat) throws Exception
	{
		m_id = GL11.glGenTextures();
		m_name = p_name;
		m_internalFormat = p_internalFormat;

		//Define the base format
		if(p_internalFormat == GL30.GL_R32I || p_internalFormat == GL30.GL_R32UI || p_internalFormat == GL30.GL_R8UI)
			m_internalFormatBase = GL30.GL_RED_INTEGER;
		else if(p_internalFormat == GL11.GL_RED || p_internalFormat == GL30.GL_R32F || p_internalFormat == GL30.GL_R16F || p_internalFormat == GL30.GL_R8)
			m_internalFormatBase = GL11.GL_RED;
		else if(p_internalFormat == GL11.GL_RGB8 || p_internalFormat == GL11.GL_RGB)
			m_internalFormatBase = GL11.GL_RGB;
		else
			throw new Exception("Texture internal format not supported");
		
		//Define the byte siganture
		if(p_internalFormat == GL30.GL_R32I || p_internalFormat == GL30.GL_R8)
			m_signed = true;
		else if(p_internalFormat == GL30.GL_R32UI || p_internalFormat == GL11.GL_RGB8)
			m_signed = false;
		
		//Define the data type
		if(p_internalFormat == GL30.GL_R32I || p_internalFormat == GL30.GL_R32UI || p_internalFormat == GL30.GL_R8UI)
			m_integer = true;
		else
			m_integer = false;
	}
	
	public void finalize()
	{
		GL11.glDeleteTextures(m_id);
	}
	
	public int getId()
	{
		return m_id;
	}
	
	public boolean isInteger()
	{
		return m_integer;
	}
	
	public boolean isSigned()
	{
		return m_signed;
	}
	
	protected int formatSize(int p_format)
	{
		if(p_format == GL30.GL_RED_INTEGER || p_format == GL11.GL_RED || p_format == GL11.GL_DEPTH_COMPONENT || p_format == GL11.GL_STENCIL_INDEX)
			return 1;
		if(p_format == GL30.GL_RG_INTEGER || p_format == GL30.GL_RG)
			return 2;
		else if(p_format == GL30.GL_RGB_INTEGER || p_format == GL30.GL_BGR_INTEGER || p_format == GL11.GL_RGB || p_format == GL12.GL_BGR)
			return 3;
		else if(p_format == GL30.GL_RGBA_INTEGER || p_format == GL11.GL_RGBA || p_format == GL12.GL_BGRA)
			return 4;
		
		return 0;
	}
	
	protected void exitOnGLError(String errorMessage) {
		int errorValue = GL11.glGetError();
		
		if (errorValue != GL11.GL_NO_ERROR) {
			String errorString = GLU.gluErrorString(errorValue);
			System.err.println("ERROR - " + errorMessage + ": " + errorString);
			
			if (Display.isCreated()) Display.destroy();
			System.exit(-1);
		}
	}
	
	abstract public IntBuffer size();

	abstract public void bindTexture(int p_unit);
	abstract public void bindImage(int p_unit, int p_access);
	abstract public void setSampler(Sampler p_sampler);
	abstract public void setSize(IntBuffer p_data) throws Exception;
	abstract public void setData(int p_format, boolean p_signed, ByteBuffer p_data) throws Exception;
	abstract public void setData(int p_format, boolean p_signed, IntBuffer p_data) throws Exception;
	abstract public void setData(int p_format, FloatBuffer p_data) throws Exception;
	abstract public void setImmutable();
}

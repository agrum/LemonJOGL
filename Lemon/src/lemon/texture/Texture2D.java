package lemon.texture;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL42;


public class Texture2D extends AbstractTexture {
	private IntBuffer m_size = null;

	public Texture2D(String p_name, int p_internalFormat) throws Exception {
		super(p_name, p_internalFormat);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, m_id);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

		// TODO Auto-generated constructor stub
		m_size = IntBuffer.allocate(2);
		m_size.put(0, 0);
		m_size.put(1, 0);
		
		this.exitOnGLError("Texture");
	}
	
	@Override
	public IntBuffer size()
	{
		return m_size;
	}
	
	@Override
	public void bindTexture(int p_unit)
	{
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + p_unit);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, m_id);
		if(m_sampler != null)
			m_sampler.bind(p_unit);
	}
	
	@Override
	public void bindImage(int p_unit, int p_access)
	{
		GL42.glBindImageTexture(p_unit, m_id, 0, false, 0, p_access, m_internalFormat);
		if(m_sampler != null)
			m_sampler.bind(p_unit);
	}

	@Override
	public 	void setSampler(Sampler p_sampler)
	{
		m_sampler = p_sampler;
	}
	
	@Override
	public void setSize(IntBuffer p_sizeBuf) throws Exception
	{
		if(p_sizeBuf.limit() != 2)
			throw new Exception("1D Texture can only have two input sizes");

		m_size.put(0, p_sizeBuf.get(0));
		m_size.put(1, p_sizeBuf.get(1));

		this.exitOnGLError("setSize");
	}

	@Override
	public void setData(int p_format, boolean p_signed, ByteBuffer p_data) throws Exception
	{
		int type;
		if(p_signed)
			type = GL11.GL_BYTE;
		else
			type = GL11.GL_UNSIGNED_BYTE;

		/*if(p_format != m_internalFormatBase)
			throw new Exception("Invalid buffer type for texture");
		if(p_data.limit() != formatSize(p_format)*m_size.get(0)*m_size.get(1))
			throw new Exception("Invalid buffer size for texture");*/
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, m_id);
		//GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, m_internalFormat, m_size.get(0), m_size.get(1), 0, p_format, type, p_data);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, m_internalFormat, m_size.get(0), m_size.get(1), 0, GL11.GL_RED, GL11.GL_UNSIGNED_BYTE, p_data);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		this.exitOnGLError("setData");
	}

	@Override
	public void setData(int p_format, boolean p_signed, IntBuffer p_data) throws Exception
	{
		int type;
		if(p_signed)
			type = GL11.GL_INT;
		else
			type = GL11.GL_UNSIGNED_INT;

		if(p_format != m_internalFormatBase)
			throw new Exception("Invalid buffer type for texture");
		if(p_data.limit() != formatSize(p_format)*m_size.get(0)*m_size.get(1))
			throw new Exception("Invalid buffer size for texture");

		p_data.rewind();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, m_id);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, m_internalFormat, m_size.get(0), m_size.get(1), 0, p_format, type, p_data);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		this.exitOnGLError("setData");
	}

	@Override
	public void setData(int p_format, FloatBuffer p_data) throws Exception
	{
		int type = GL11.GL_FLOAT;

		if(p_format != m_internalFormatBase)
			throw new Exception("Invalid buffer type for texture");
		if(p_data.limit() != formatSize(p_format)*m_size.get(0)*m_size.get(1))
			throw new Exception("Invalid buffer size for texture");
		
		p_data.rewind();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, m_id);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, m_internalFormat, m_size.get(0), m_size.get(1), 0, p_format, type, p_data);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		this.exitOnGLError("setData");
	}
	
	public void readData() throws Exception
	{
		if(m_integer)
		{
			IntBuffer data = ByteBuffer.allocateDirect(4 * m_size.get(0) * m_size.get(1)).asIntBuffer();
			data.rewind();
			
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, m_id);
			GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL30.GL_RED_INTEGER, GL11.GL_INT, data);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

			this.exitOnGLError("setData3");
			
			data.rewind();
			while (data.hasRemaining())
				System.out.println(data.position() + " -> " + data.get());
		}
		else
		{
			//FloatBuffer data = ByteBuffer.allocateDirect(4 * m_size.get(0) * m_size.get(1)).asFloatBuffer();
			//IntBuffer data = ByteBuffer.allocateDirect(4 * m_size.get(0) * m_size.get(1)).asIntBuffer();
			ByteBuffer data = ByteBuffer.allocateDirect(4 * m_size.get(0) * m_size.get(1));
			data.rewind();
			
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, m_id);
			GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RED, GL11.GL_BYTE, data);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

			this.exitOnGLError("setData3");
			
			data.rewind();
			while (data.hasRemaining())
				System.out.println(data.position() + " -> " + data.get());
		}
	}
	
	public void setImmutable()
	{
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, m_id);
		GL42.glTexStorage2D(GL11.GL_TEXTURE_2D, 1, m_internalFormat, m_size.get(0), m_size.get(1));
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

		this.exitOnGLError("setSize");
	}
}

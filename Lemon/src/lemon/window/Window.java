package lemon.window;

import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.opengl.GL11;

import lemon.Lemon;
import lemon.pass.AbstractPass;

public class Window extends Lemon {
	private String m_windowTitle = "noTitle";
	private int m_width = 1024;

	private int m_height = 512;
	private int m_frequency = 60;
	
	private boolean m_compatibilityMode = true;
	private boolean m_coreProfile = true;
	
	private int m_major = 4;
	private int m_minor = 3;
	
	public String getWindowTitle() {
		return m_windowTitle;
	}

	public void setWindowTitle(String p_windowTitle) {
		this.m_windowTitle = p_windowTitle;
	}

	public int getWidth() {
		return m_width;
	}

	public void setWidth(int p_width) {
		this.m_width = p_width;
	}

	public int getHeight() {
		return m_height;
	}

	public void setHeight(int p_height) {
		this.m_height = p_height;
	}

	public int getFrequency() {
		return m_frequency;
	}

	public void setFrequency(int p_frequency) {
		this.m_frequency = p_frequency;
	}

	public boolean isCompatibilityMode() {
		return m_compatibilityMode;
	}

	public void setCompatibilityMode(boolean p_compatibilityMode) {
		this.m_compatibilityMode = p_compatibilityMode;
	}

	public boolean isCoreProfile() {
		return m_coreProfile;
	}

	public void setCoreProfile(boolean p_coreProfile) {
		this.m_coreProfile = p_coreProfile;
	}

	public int getMajor() {
		return m_major;
	}

	public void setMajor(int p_major) {
		this.m_major = p_major;
	}

	public int getMinor() {
		return m_minor;
	}

	public void setMinor(int p_minor) {
		this.m_minor = p_minor;
	}
	
	public Window()
	{
		
	}
	
	public void create()
	{
		try {
			PixelFormat pixelFormat = new PixelFormat();
			ContextAttribs contextAtrributes = new ContextAttribs(m_major, m_minor)
				.withForwardCompatible(m_compatibilityMode)
				.withProfileCore(m_coreProfile);
			
			Display.setDisplayMode(new DisplayMode(m_width, m_height));
			Display.setTitle(m_windowTitle);
			Display.create(pixelFormat, contextAtrributes);
			
			GL11.glViewport(0, 0, m_width, m_height);
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public void exec(AbstractPass p_pass)
	{
		while (!Display.isCloseRequested()) 
		{
			try 
			{
				p_pass.render();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				System.exit(-1);
			}
			
			// Force a maximum FPS of about 60
			Display.sync(m_frequency);
			// Let the CPU synchronize with the GPU if GPU is tagging behind
			Display.update();
		}
		
		Display.destroy();
	}
}

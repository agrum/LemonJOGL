package lemon.pass;

import org.lwjgl.opengl.GL42;

public class Barrier {
	private int m_bitfield;
	
	public Barrier(int p_bitfield)
	{
		m_bitfield = p_bitfield;
	}
	
	public void block()
	{
		GL42.glMemoryBarrier(m_bitfield);
	}
}

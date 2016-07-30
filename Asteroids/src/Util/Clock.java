/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */

package Util;

public class Clock
{
	private float millisPerCycle;
	private float excessCycles;
	private long lastUpdate;
	private int elapsedCycles;
	
	public Clock (float cyclesPerSecond)
	{
		this.setCyclesPerSecond(cyclesPerSecond);
		this.reset();
	}
	
	private static long getCurrentTime ()
	{
		return (System.nanoTime() / 1000000L);
	}
	
	public boolean hasElapsedCycle ()
	{
		if (this.elapsedCycles > 0)
		{
			this.elapsedCycles--;
			
			return true;
		}
		
		return false;
	}
	
	public boolean peekElapsedCycle ()
	{
		return (this.elapsedCycles > 0);
	}
	
	public void setCyclesPerSecond (float cyclesPerSecond)
	{
		this.millisPerCycle = (1.0f / cyclesPerSecond) * 1000;
	}
	
	public void reset ()
	{
		this.elapsedCycles = 0;
		this.excessCycles = 0.0f;
		this.lastUpdate = Clock.getCurrentTime();
	}
	
	public void update ()
	{
		long currUpdate = Clock.getCurrentTime();
		float delta = (float) (currUpdate - this.lastUpdate) + this.excessCycles;

		this.elapsedCycles = this.elapsedCycles + (int) Math.floor(delta / this.millisPerCycle);
		this.excessCycles = delta % millisPerCycle;

		this.lastUpdate = currUpdate;
	}
}
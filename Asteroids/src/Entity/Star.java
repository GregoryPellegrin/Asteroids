/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */

package Entity;

import Game.WorldPanel;
import java.awt.Color;
import java.awt.Graphics2D;

public class Star
{
	private Color color;
	private int x;
	private int y;
	private int w;
	private int h;
	
	public Star ()
	{
		int red = (int) (Math.random() * 256);
		int green = (int) (Math.random() * 256);
		int blue = (int) (Math.random() * 256);
		
		this.color = new Color (red, green, blue);
		
		this.x = (int) (Math.random() * WorldPanel.W_MAP_PIXEL);
		this.y = (int) (Math.random() * WorldPanel.H_MAP_PIXEL);
		
		this.w = 1 + (int) (Math.random() * 4);
		this.h = w;
	}
	
	public void reCreate ()
	{
		int red = (int) (Math.random() * 256);
		int green = (int) (Math.random() * 256);
		int blue = (int) (Math.random() * 256);
		
		this.color = new Color (red, green, blue);
		
		this.x = (int) (Math.random() * WorldPanel.W_MAP_PIXEL);
		this.y = (int) (Math.random() * WorldPanel.H_MAP_PIXEL);
		
		this.w = 1 + (int) (Math.random() * 4);
		this.h = w;
	}
	
	public void update (int starSpeed)
	{
		if ((int) (Math.random() * 100) == 0)
			this.reCreate();
		
		if (this.y >= WorldPanel.H_MAP_PIXEL)
			this.reCreate();
		
		this.y = this.y + starSpeed;
	}
	
	public void drawStar (Graphics2D g)
	{
		g.setColor(this.color);
		g.fillOval(this.x, this.y, this.w, this.h);

		g.setColor(WorldPanel.COLOR_DEFAULT);
	}
}
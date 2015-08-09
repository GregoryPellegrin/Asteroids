/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */

package IA;

import Entity.Ennemi;
import Game.Game;
import Game.WorldPanel;
import Util.Vector;
import java.awt.Color;
import java.awt.Graphics2D;

public class Basic extends Ennemi
{
	private static final double SPEED_MAGNITUDE = 0.0385;
	private static final double MISSILE_MAGNITUDE = 6.75;
	private static final int MAX_MISSILES = 4;
	private static final int FIRE_RATE = 4;
	private static final int RECHARGE_COOLDOWN = 30;
	
	public Basic ()
	{
		super (new Vector (200, 200), new Vector (1.1, 1.1), Color.RED, Color.RED, SPEED_MAGNITUDE, MISSILE_MAGNITUDE, 10.0, MAX_MISSILES, FIRE_RATE, RECHARGE_COOLDOWN, 100);
		
		this.flamesMotorColor.add(Color.YELLOW);
		this.flamesMotorColor.add(Color.RED);
	}
	
	@Override
	public void draw (Graphics2D g, Game game)
	{
		g.setColor(this.color);
		g.drawLine(-10, -8, 10, 0);
		g.drawLine(-10, 8, 10, 0);
		g.drawLine(-6, -6, -6, 6);

		g.setColor(WorldPanel.COLOR_DEFAULT);

		if (! game.isPaused() && this.isThrustPressed() && this.getAnimationFrame() % 6 < 3)
		{
			g.setColor(this.flamesMotorColor.get(0));
			g.drawLine(-6, -6, -14, 0);
			g.drawLine(-6, 6, -14, 0);

			g.setColor(this.flamesMotorColor.get(1));
			g.fillOval(-14, -2, 7, 4);

			g.setColor(this.flamesMotorColor.get(1));
			g.fillOval(-14, -1, 7, 4);

			g.setColor(WorldPanel.COLOR_DEFAULT);
		}
	}
}
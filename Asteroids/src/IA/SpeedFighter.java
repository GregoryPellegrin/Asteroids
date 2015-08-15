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

public class SpeedFighter extends Ennemi
{
	private static final double SPEED_SHIP = 0.0385;
	private static final double SPEED_MISSILE = 6.75;
	private static final int MISSILE_MAX = 4;
	private static final int FIRE_RATE = 4;
	private static final int RECHARGE_COOLDOWN = 30;
	
	public SpeedFighter (int x, int y, int startingPosition)
	{
		super (new Vector (x, y), new Vector (1, 0), Color.RED, Color.RED, SpeedFighter.SPEED_SHIP, SpeedFighter.SPEED_MISSILE, 10.0, SpeedFighter.MISSILE_MAX, SpeedFighter.FIRE_RATE, SpeedFighter.RECHARGE_COOLDOWN, startingPosition, 100);
		
		super.flamesMotorColor.add(Color.YELLOW);
		super.flamesMotorColor.add(Color.RED);
	}
	
	@Override
	public void draw (Graphics2D g, Game game)
	{
		g.setColor(super.color);
		g.drawLine(-10, -8, 10, 0);
		g.drawLine(-10, 8, 10, 0);
		g.drawLine(-10, -8, -4, 0);
		g.drawLine(-10, 8, -4, 0);

		g.setColor(WorldPanel.COLOR_DEFAULT);

		if (! game.isPaused() && super.isMovePressed() && ((super.getAnimationFrame() % 6) < 3))
		{
			g.setColor(super.flamesMotorColor.get(0));
			g.drawLine(-6, -6, -14, 0);
			g.drawLine(-6, 6, -14, 0);

			g.setColor(super.flamesMotorColor.get(1));
			g.fillOval(-14, -2, 7, 4);

			g.setColor(super.flamesMotorColor.get(1));
			g.fillOval(-14, -1, 7, 4);

			g.setColor(WorldPanel.COLOR_DEFAULT);
		}
	}
}
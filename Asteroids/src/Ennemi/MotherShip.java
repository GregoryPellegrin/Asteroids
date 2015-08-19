/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */

package Ennemi;

import Entity.Ennemi;
import Game.Game;
import Game.WorldPanel;
import Util.Vector;
import java.awt.Color;
import java.awt.Graphics2D;

public class MotherShip extends Ennemi
{
	private static final double SPEED_SHIP = 0.0285;
	private static final double SPEED_MISSILE = 6.75;
	private static final int MISSILE_MAX = 4;
	private static final int FIRE_RATE = 4;
	private static final int RECHARGE_COOLDOWN = 30;
	private static final int LIFE = 3;
	
	public MotherShip (int x, int y, int startingPosition)
	{
		super (new Vector (x, y), new Vector (1, 0), Color.RED, Color.RED, MotherShip.SPEED_SHIP, MotherShip.SPEED_MISSILE, 10.0, MotherShip.MISSILE_MAX, MotherShip.FIRE_RATE, MotherShip.RECHARGE_COOLDOWN, startingPosition, LIFE, 100);
		
		super.flamesMotorColor.add(Color.YELLOW);
		super.flamesMotorColor.add(Color.RED);
	}
	
	@Override
	public void draw (Graphics2D g, Game game)
	{
		g.setColor(super.color);
		g.drawLine(80, -12, 32, -32);
		g.drawLine(32, -32, -4, -32);
		g.drawLine(-4, -32, -16, -20);
		g.drawLine(-16, -20, -16, 20);
		g.drawLine(-16, 20, -4, 32);
		g.drawLine(-4, 32, 32, 32);
		g.drawLine(32, 32, 80, 12);
		g.drawLine(80, 12, 32, 24);
		g.drawLine(32, 24, -4, 24);
		g.drawLine(-4, 24, -8, 20);
		g.drawLine(-8, 20, -8, -20);
		g.drawLine(-8, -20, -4, -24);
		g.drawLine(-4, -24, 32, -24);
		g.drawLine(32, -24, 80, -12);
		g.drawLine(-16, -16, -20, -16);
		g.drawLine(-20, -16, -20, 16);
		g.drawLine(-20, 16, -16, 16);
		g.drawLine(-8, -4, -4, -4);
		g.drawLine(-4, -4, -4, 4);
		g.drawLine(-4, 4, -8, 4);

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
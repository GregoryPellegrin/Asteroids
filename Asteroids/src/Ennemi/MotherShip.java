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
	private static final double SPEED_SHIP = 0.0175;
	private static final double SPEED_MISSILE = 4.75;
	private static final double SPEED_ROTATION = 0.030;
	private static final int MISSILE_MAX = 7;
	private static final int FIRE_RATE = 4;
	private static final int RECHARGE_COOLDOWN = 20;
	private static final int LIFE = 5;
	
	public MotherShip (int x, int y, int startingPosition)
	{
		super (new Vector (x, y), new Vector (1, 0), Color.RED, Color.RED, MotherShip.SPEED_SHIP, MotherShip.SPEED_MISSILE, 10.0, MotherShip.SPEED_ROTATION, MotherShip.MISSILE_MAX, MotherShip.FIRE_RATE, MotherShip.RECHARGE_COOLDOWN, startingPosition, LIFE, 500);
		
		super.flamesMotorColor.add(Color.YELLOW);
		super.flamesMotorColor.add(Color.RED);
	}
	
	@Override
	public void draw (Graphics2D g, Game game)
	{
		g.setColor(super.color);
		
		g.drawLine(60, -9, 24, -24);
		g.drawLine(24, -24, -5, -24);
		g.drawLine(-5, -24, -14, -15);
		g.drawLine(-14, -15, -14, 15);
		g.drawLine(-14, 15, -5, 24);
		g.drawLine(-5, 24, 24, 24);
		g.drawLine(24, 24, 60, 9);
		g.drawLine(60, 9, 24, 16);
		g.drawLine(24, 16, -3, 16);
		g.drawLine(-3, 16, -6, 15);
		g.drawLine(-6, 15, -6, -15);
		g.drawLine(-6, -15, -3, -16);
		g.drawLine(-3, -16, 24, -16);
		g.drawLine(24, -16, 60, -9);
		g.drawLine(-14, -12, -17, -12);
		g.drawLine(-17, -12, -17, 12);
		g.drawLine(-17, 12, -14, 12);
		g.drawLine(-6, -4, -3, -4);
		g.drawLine(-3, -4, -3, 4);
		g.drawLine(-3, 4, -6, 4);

		g.setColor(WorldPanel.COLOR_DEFAULT);

		if (super.isMovePressed() && ((super.getAnimationFrame() % 6) < 3))
		{
			g.setColor(super.flamesMotorColor.get(0));
			g.drawLine(-17, -12, -20, 0);
			g.drawLine(-17, 12, -20, 0);

			g.setColor(super.flamesMotorColor.get(1));
			g.drawLine(-18, -12, -21, 0);
			g.drawLine(-18, 12, -21, 0);

			g.setColor(WorldPanel.COLOR_DEFAULT);
		}
	}
}
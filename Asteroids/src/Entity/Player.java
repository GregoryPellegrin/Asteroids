/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */

package Entity;

import Game.Game;
import Game.WorldPanel;
import Util.Vector;
import java.awt.Color;
import java.awt.Graphics2D;

public class Player extends Ship
{
	private static final double SPEED_SHIP = 0.0385;
	private static final double SUPER_SPEED_SHIP = 0.2385;
	private static final double SPEED_MISSILE = 6.75;
	private static final double SUPER_SPEED_MISSILE = 10.00;
	private static final int MISSILE_MAX = 4;
	private static final int FIRE_RATE = 4;
	private static final int RECHARGE_COOLDOWN = 30;
	
	private boolean superSpeed;

	public Player ()
	{
		super (new Vector (300, 200), new Vector (0, 0), Color.BLUE, Color.GREEN, Player.SPEED_SHIP, Player.SPEED_MISSILE, 10.0, Player.MISSILE_MAX, Player.FIRE_RATE, Player.RECHARGE_COOLDOWN, 0);
		
		super.flamesMotorColor.add(Color.YELLOW);
		super.flamesMotorColor.add(Color.RED);
		super.flamesMotorColor.add(Color.YELLOW);
		
		this.superSpeed = false;
	}
	
	public void setSuperSpeed (boolean state)
	{
		this.superSpeed = state;
		
		if (this.superSpeed)
		{
			this.setSpeedMagnitude(Player.SUPER_SPEED_SHIP);
			this.setMissileMagnitude(Player.SUPER_SPEED_MISSILE);
		}
		else
		{
			this.setSpeedMagnitude(Player.SPEED_SHIP);
			this.setMissileMagnitude(Player.SPEED_MISSILE);
		}
	}

	@Override
	public void checkCollision (Game game, Entity other)
	{
		if (other.getClass() == Ennemi.class)
			game.killPlayer();
	}

	@Override
	public void draw (Graphics2D g, Game game)
	{
		if (! game.isPlayerInvulnerable() || game.isPaused() || ((super.getAnimationFrame() % 20) < 10))
		{
			g.setColor(super.color);
			g.drawLine(-10, -8, 10, 0);
			g.drawLine(-10, 8, 10, 0);
			g.drawLine(-6, -6, -6, 6);
			g.setColor(WorldPanel.COLOR_DEFAULT);

			if (! game.isPaused() && super.isThrustPressed() && ((super.getAnimationFrame() % 6) < 3))
			{
				g.setColor(super.flamesMotorColor.get(0));
				g.drawLine(-6, -6, -14, 0);
				g.drawLine(-6, 6, -14, 0);
				
				g.setColor(super.flamesMotorColor.get(1));
				g.fillOval(-14, -2, 7, 4);
				
				g.setColor(super.flamesMotorColor.get(1));
				g.fillOval(-14, -1, 7, 4);
				
				if (this.superSpeed)
				{
					g.setColor(super.flamesMotorColor.get(1));
					g.fillOval(-24, -3, 17, 7);
					
					g.setColor(super.flamesMotorColor.get(2));
					g.fillOval(-20, -1, 9, 2);
				}
				
				g.setColor(WorldPanel.COLOR_DEFAULT);
			}
		}
	}
}
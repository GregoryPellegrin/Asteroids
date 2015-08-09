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
	private static final double SPEED_MAGNITUDE = 0.0385;
	private static final double SUPER_SPEED_MAGNITUDE = 0.2385;
	private static final double MISSILE_MAGNITUDE = 6.75;
	private static final double SUPER_SPEED_MISSILE_MAGNITUDE = 10.00;
	private static final int MAX_MISSILES = 4;
	private static final int FIRE_RATE = 4;
	private static final int RECHARGE_COOLDOWN = 30;
	
	private boolean superSpeed;

	public Player ()
	{
		super (new Vector (300, 200), new Vector (1.1, 1.1), Color.BLUE, Color.GREEN, SPEED_MAGNITUDE, MISSILE_MAGNITUDE, 10.0, MAX_MISSILES, FIRE_RATE, RECHARGE_COOLDOWN, 0);
		
		this.flamesMotorColor.add(Color.YELLOW);
		this.flamesMotorColor.add(Color.RED);
		this.flamesMotorColor.add(Color.YELLOW);
		
		this.superSpeed = false;
	}
	
	public void setSuperSpeed (boolean state)
	{
		this.superSpeed = state;
		
		if (this.superSpeed)
		{
			this.setSpeedMagnitude(Player.SUPER_SPEED_MAGNITUDE);
			this.setMissileMagnitude(Player.SUPER_SPEED_MISSILE_MAGNITUDE);
		}
		else
		{
			this.setSpeedMagnitude(Player.SPEED_MAGNITUDE);
			this.setMissileMagnitude(Player.MISSILE_MAGNITUDE);
		}
	}

	@Override
	public void handleCollision (Game game, Entity other)
	{
		if (other.getClass() == Ennemi.class)
			game.killPlayer();
	}

	@Override
	public void draw (Graphics2D g, Game game)
	{
		if (! game.isPlayerInvulnerable() || game.isPaused() || this.getAnimationFrame() % 20 < 10)
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
				
				if (this.superSpeed)
				{
					g.setColor(this.flamesMotorColor.get(1));
					g.fillOval(-24, -3, 17, 7);
					
					g.setColor(this.flamesMotorColor.get(2));
					g.fillOval(-20, -1, 9, 2);
				}
				
				g.setColor(WorldPanel.COLOR_DEFAULT);
			}
		}
	}
}
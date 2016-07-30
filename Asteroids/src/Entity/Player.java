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
	private static final double SPEED_ROTATION = 0.065;
	private static final int MISSILE_MAX = 4;
	private static final int FIRE_RATE = 4;
	private static final int RECHARGE_COOLDOWN = 30;
	
	private boolean superSpeed;

	public Player ()
	{
		super (new Vector (300, 200), new Vector (0, 0), Color.BLUE, Color.GREEN, Player.SPEED_SHIP, Player.SPEED_MISSILE, 10.0, Player.SPEED_ROTATION, Player.MISSILE_MAX, Player.FIRE_RATE, Player.RECHARGE_COOLDOWN, 0);
		
		super.flamesMotorColor.add(Color.YELLOW);
		super.flamesMotorColor.add(Color.RED);
		super.flamesMotorColor.add(Color.RED);
		super.flamesMotorColor.add(Color.YELLOW);
		
		this.superSpeed = false;
	}
	
	public void setSuperSpeed (boolean superSpeed)
	{
		if (superSpeed)
		{
			this.superSpeed = true;
			this.setSpeedShip(Player.SUPER_SPEED_SHIP);
			this.setSpeedMissile(Player.SUPER_SPEED_MISSILE);
		}
		else
		{
			this.superSpeed = false;
			this.setSpeedShip(Player.SPEED_SHIP);
			this.setSpeedMissile(Player.SPEED_MISSILE);
		}
	}

	@Override
	public void reset ()
	{
		super.reset();
		
		super.position.set(WorldPanel.W_MAP_PIXEL / 2.0, WorldPanel.H_MAP_PIXEL - 50);
		super.speed.set(0.0, 0.0);
	}

	@Override
	public void checkCollision (Game game, Entity other)
	{
		if (other.getClass().getSuperclass() == Ennemi.class)
			game.killPlayer();
	}
	
	@Override
	public void draw (Graphics2D g, Game game)
	{
		if (! game.isPlayerInvulnerable() || ((super.getAnimationFrame() % 20) < 10))
		{
			g.setColor(super.color);
			g.drawLine(-10, -8, 10, 0);
			g.drawLine(-10, 8, 10, 0);
			g.drawLine(-10, -8, -4, 0);
			g.drawLine(-10, 8, -4, 0);
			
			g.setColor(WorldPanel.COLOR_DEFAULT);

			if (super.isMovePressed() && ((super.getAnimationFrame() % 6) < 3))
			{
				g.setColor(super.flamesMotorColor.get(0));
				g.drawLine(-12, -8, -6, 0);
				g.drawLine(-12, 8, -6, 0);
				
				g.setColor(super.flamesMotorColor.get(1));
				g.drawLine(-13, -8, -7, 0);
				g.drawLine(-13, 8, -7, 0);
				
				if (this.superSpeed)
				{
					g.setColor(super.flamesMotorColor.get(2));
					g.fillOval(-24, -3, 17, 7);
					
					g.setColor(super.flamesMotorColor.get(3));
					g.fillOval(-20, -1, 9, 2);
				}
				
				g.setColor(WorldPanel.COLOR_DEFAULT);
			}
		}
	}
}
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Ship extends Entity
{
	protected List <Color> flamesMotorColor;
	
	private static final double SPEED_SHIP_MAX = 6.5;
	private static final double SPEED_STOP = 0.995;
	private static final double SPEED_ROTATION = 0.052;
	private static final double SPEED_ROTATION_DEFAULT = -Math.PI / 2.0;
	private static final int CONSECUTIVE_SHOTS_MAX = 8;
	
	private final Color MISSILE_COLOR;
	private final int MISSILE_MAX;
	private final int FIRE_RATE;
	private final int RECHARGE_COOLDOWN;
	
	private List <Missile> missile;
	private boolean thrustPressed;
	private boolean rotationRightPressed;
	private boolean rotationLeftPressed;
	private boolean firePressed;
	private boolean firingEnabled;
	private double speedShip;
	private double speedMissile;
	private int consecutiveShots;
	private int fireCooldown;
	private int overheatCooldown;
	private int animationFrame;

	public Ship (Vector position, Vector velocity, Color shipColor, Color missileColor, double speedShip, double speedMissile, double radius, int missileMax, int fireRate, int rechargeCooldown, int killScore)
	{
		super(position, velocity, shipColor, radius, killScore);
		
		this.flamesMotorColor = new ArrayList <> ();
		this.missile = new ArrayList <> ();
		this.rotation = Ship.SPEED_ROTATION_DEFAULT;
		this.thrustPressed = false;
		this.rotationLeftPressed = false;
		this.rotationRightPressed = false;
		this.firePressed = false;
		this.firingEnabled = true;
		this.speedShip = speedShip;
		this.speedMissile = speedMissile;
		this.MISSILE_COLOR = missileColor;
		this.MISSILE_MAX = missileMax;
		this.FIRE_RATE = fireRate;
		this.RECHARGE_COOLDOWN = rechargeCooldown;
		this.fireCooldown = 0;
		this.overheatCooldown = 0;
		this.animationFrame = 0;
	}
	
	public int getAnimationFrame ()
	{
		return this.animationFrame;
	}
	
	public boolean isThrustPressed ()
	{
		return this.thrustPressed;
	}

	public void setThrusting (boolean state)
	{
		this.thrustPressed = state;
	}

	public void setRotateLeft (boolean state)
	{
		this.rotationLeftPressed = state;
	}
	
	public void setRotateRight (boolean state)
	{
		this.rotationRightPressed = state;
	}

	public void setFiring (boolean state)
	{
		this.firePressed = state;
	}

	public void setFiringEnabled (boolean state)
	{
		this.firingEnabled = state;
	}

	public void setSpeedMagnitude (double magnitude)
	{
		this.speedShip = magnitude;
	}

	public void setMissileMagnitude (double magnitude)
	{
		this.speedMissile = magnitude;
	}

	public void reset ()
	{
		this.rotation = SPEED_ROTATION_DEFAULT;
		
		this.position.set(WorldPanel.W_MAP_PIXEL / 2.0, WorldPanel.H_MAP_PIXEL / 2.0);
		super.speed.set(0.0, 0.0);
		this.missile.clear();
	}

	@Override
	public void update (Game game)
	{
		super.update(game);

		this.animationFrame++;
		
		if (rotationLeftPressed != rotationRightPressed)
			rotate(rotationLeftPressed ? - SPEED_ROTATION : SPEED_ROTATION);
		
		if (thrustPressed)
		{
			super.speed.add(new Vector(rotation).scale(this.speedShip));
			
			if (super.speed.getLengthSquared() >= SPEED_SHIP_MAX * SPEED_SHIP_MAX)
				super.speed.normalize().scale(SPEED_SHIP_MAX);
		}
		
		if (super.speed.getLengthSquared() != 0.0)
			super.speed.scale(SPEED_STOP);
		
		Iterator <Missile> iter = missile.iterator();
		while (iter.hasNext())
		{
			Missile bullet = iter.next();
			
			if (bullet.needsRemoval())
				iter.remove();
		}
		
		this.fireCooldown--;
		this.overheatCooldown--;
		if (firingEnabled && firePressed && fireCooldown <= 0 && overheatCooldown <= 0)
		{
			if (this.missile.size() < this.MISSILE_MAX)
			{
				this.fireCooldown = this.FIRE_RATE;
				
				Missile bullet = new Missile(this, this.MISSILE_COLOR, rotation, this.speedMissile);
				
				this.missile.add(bullet);
				game.registerEntity(bullet);
			}
			
			this.consecutiveShots++;
			if (this.consecutiveShots == Ship.CONSECUTIVE_SHOTS_MAX)
			{
				this.consecutiveShots = 0;
				this.overheatCooldown = this.RECHARGE_COOLDOWN;
			}
		}
		else
		{
			if (this.consecutiveShots > 0)
				this.consecutiveShots--;
		}
	}

	@Override
	public void checkCollision (Game game, Entity other) {}

	@Override
	public void draw (Graphics2D g, Game game) {}
}
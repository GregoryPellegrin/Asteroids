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
	
	private static final double DEFAULT_ROTATION = -Math.PI / 2.0;
	private static final double MAX_VELOCITY_MAGNITUDE = 6.5;
	private static final double ROTATION_SPEED = 0.052;
	private static final double SLOW_RATE = 0.995;
	private static final int MAX_CONSECUTIVE_SHOTS = 8;
	
	private final Color MISSILE_COLOR;
	private final int MAX_MISSILES;
	private final int FIRE_RATE;
	private final int RECHARGE_COOLDOWN;
	
	private List <Missile> missiles;
	private boolean thrustPressed;
	private boolean rotateLeftPressed;
	private boolean rotateRightPressed;
	private boolean firePressed;
	private boolean firingEnabled;
	private double speed;
	private double missileMagnitude;
	private int consecutiveShots;
	private int fireCooldown;
	private int overheatCooldown;
	private int animationFrame;

	public Ship (Vector position, Vector velocity, Color shipColor, Color missileColor, double speedMagnitude, double missileMagnitude, double radius, int maxMissiles, int fireRate, int rechargeCooldown, int killScore)
	{
		super(position, velocity, shipColor, radius, killScore);
		
		this.flamesMotorColor = new ArrayList <> ();
		this.missiles = new ArrayList <> ();
		this.rotation = Ship.DEFAULT_ROTATION;
		this.thrustPressed = false;
		this.rotateLeftPressed = false;
		this.rotateRightPressed = false;
		this.firePressed = false;
		this.firingEnabled = true;
		this.speed = speedMagnitude;
		this.missileMagnitude = missileMagnitude;
		this.MISSILE_COLOR = missileColor;
		this.MAX_MISSILES = maxMissiles;
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
		this.rotateLeftPressed = state;
	}
	
	public void setRotateRight (boolean state)
	{
		this.rotateRightPressed = state;
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
		this.speed = magnitude;
	}

	public void setMissileMagnitude (double magnitude)
	{
		this.missileMagnitude = magnitude;
	}

	public void reset ()
	{
		this.rotation = DEFAULT_ROTATION;
		
		this.position.set(WorldPanel.W_MAP_PIXEL / 2.0, WorldPanel.H_MAP_PIXEL / 2.0);
		this.velocity.set(0.0, 0.0);
		this.missiles.clear();
	}

	@Override
	public void update (Game game)
	{
		super.update(game);

		this.animationFrame++;
		
		if (rotateLeftPressed != rotateRightPressed)
			rotate(rotateLeftPressed ? - ROTATION_SPEED : ROTATION_SPEED);
		
		if (thrustPressed)
		{
			velocity.add(new Vector(rotation).scale(this.speed));
			
			if (velocity.getLengthSquared() >= MAX_VELOCITY_MAGNITUDE * MAX_VELOCITY_MAGNITUDE)
				velocity.normalize().scale(MAX_VELOCITY_MAGNITUDE);
		}
		
		if (velocity.getLengthSquared() != 0.0)
			velocity.scale(SLOW_RATE);
		
		Iterator <Missile> iter = missiles.iterator();
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
			if (this.missiles.size() < this.MAX_MISSILES)
			{
				this.fireCooldown = this.FIRE_RATE;
				
				Missile bullet = new Missile(this, this.MISSILE_COLOR, rotation, this.missileMagnitude);
				
				this.missiles.add(bullet);
				game.registerEntity(bullet);
			}
			
			this.consecutiveShots++;
			if (this.consecutiveShots == Ship.MAX_CONSECUTIVE_SHOTS)
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
	public void handleCollision (Game game, Entity other) {}

	@Override
	public void draw (Graphics2D g, Game game) {}
}
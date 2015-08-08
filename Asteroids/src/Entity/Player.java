package Entity;

import Game.Game;
import Game.WorldPanel;
import Util.Vector;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Player extends Entity
{
	private static final double DEFAULT_ROTATION = -Math.PI / 2.0;

	private static final double SPEED_MAGNITUDE = 0.0385;
	private static final double SUPER_SPEED_MAGNITUDE = 0.2085;
	private static final double MAX_VELOCITY_MAGNITUDE = 6.5;
	private static final double ROTATION_SPEED = 0.052;

	/**
	 * The factor at which our ship slows down.
	 */
	private static final double SLOW_RATE = 0.995;
	private static final int MAX_BULLETS = 4;

	/**
	 * The number of cycles that must elapse between shots.
	 */
	private static final int FIRE_RATE = 4;
	private static final int MAX_CONSECUTIVE_SHOTS = 8;

	/**
	 * The number of cycles that must elapse before we stop overheating.
	 */
	private static final int MAX_OVERHEAT = 30;
	private List <Missile> missiles;
	private Color flames1Color;
	private Color flames2Color;
	private Color flames3Color;
	/**
	 * Whether the ship should apply thrust when it updates.
	 */
	private boolean thrustPressed;

	/**
	 * Whether the ship should rotate to the left when it updates.
	 */
	private boolean rotateLeftPressed;

	/**
	 * Whether the ship should rotate to the right when it updates.
	 */
	private boolean rotateRightPressed;
	private boolean superSpeed;

	/**
	 * Whether the ship should fire a bullet when it updates.
	 */
	private boolean firePressed;

	/**
	 * Whether the ship is allowed to fire a bullet.
	 */
	private boolean firingEnabled;
	private double speed;
	private int consecutiveShots;
	private int fireCooldown;
	/**
	 * The cooldown timer for overheating.
	 */
	private int overheatCooldown;
	private int animationFrame;

	public Player ()
	{
		super(new Vector(WorldPanel.W_MAP_PIXEL / 2.0, WorldPanel.H_MAP_PIXEL / 2.0), new Vector(0.0, 0.0), Color.BLUE, 10.0, 0);
		
		this.missiles = new ArrayList <> ();
		this.flames1Color = Color.YELLOW;
		this.flames2Color = Color.RED;
		this.flames3Color = Color.RED;
		this.rotation = DEFAULT_ROTATION;
		this.thrustPressed = false;
		this.rotateLeftPressed = false;
		this.rotateRightPressed = false;
		this.superSpeed = false;
		this.firePressed = false;
		this.firingEnabled = true;
		this.speed = SPEED_MAGNITUDE;
		this.fireCooldown = 0;
		this.overheatCooldown = 0;
		this.animationFrame = 0;
	}

	/**
	 * Sets whether this player should apply thrust when it updates.
	 *
	 * @param state Whether to apply thrust.
	 */
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
	
	public void setSuperSpeed (boolean state)
	{
		this.superSpeed = state;
		
		if (this.superSpeed)
			this.speed = SUPER_SPEED_MAGNITUDE;
		else
			this.speed = SPEED_MAGNITUDE;
	}

	public void setFiring (boolean state)
	{
		this.firePressed = state;
	}

	/**
	 * Sets whether this player can fire when it updates.
	 *
	 * @param state Whether this player can fire.
	 */
	public void setFiringEnabled (boolean state)
	{
		this.firingEnabled = state;
	}

	/**
	 * Resets the player to it's default spawn position, speed, and rotation,
	 * and clears the list of bullets.
	 */
	public void reset ()
	{
		this.rotation = DEFAULT_ROTATION;
		
		position.set(WorldPanel.W_MAP_PIXEL / 2.0, WorldPanel.H_MAP_PIXEL / 2.0);
		velocity.set(0.0, 0.0);
		missiles.clear();
	}

	@Override
	public void update (Game game)
	{
		super.update(game);

		//Increment the animation frame.
		this.animationFrame++;

		/*
		 * Rotate the ship if only one of the rotation flags are true, as doing
		 * one rotation will cancel the effect of doing the other.
		 * 
		 * The conditional statement can alternatively be written like this:
		 * 
		 * if(rotateLeftPressed) {
		 *     rotate(-ROTATION_SPEED);
		 * } else {
		 *     rotate(ROTATION_SPEED);
		 * }
		 */
		if (rotateLeftPressed != rotateRightPressed)
			rotate(rotateLeftPressed ? - ROTATION_SPEED : ROTATION_SPEED);

		/*
		 * Apply thrust to our ship's velocity, and ensure that the ship is not
		 * going faster than the maximum magnitude.
		 */
		if (thrustPressed)
		{
			/*
			 * Here we create a new vector based on our ship's rotation, and scale
			 * it by our thrust's magnitude. Then we add that vector to our velocity.
			 */
			velocity.add(new Vector(rotation).scale(this.speed));

			/*
			 * Here we determine whether our ship is going faster than is
			 * allowed. Like when checking for collisions, we check the squared
			 * magnitude because it is quicker to square a value than it is to
			 * take the square root.
			 * 
			 * If our velocity exceeds our maximum allowed velocity, we normalize
			 * it (giving it a magnitude of 1.0), and scale it to be he maximum.
			 */
			if (velocity.getLengthSquared() >= MAX_VELOCITY_MAGNITUDE * MAX_VELOCITY_MAGNITUDE)
				velocity.normalize().scale(MAX_VELOCITY_MAGNITUDE);
		}

		/*
		 * If our ship is moving, slow it down slightly, which causes the ship
		 * to some to a gradual stop.
		 */
		if (velocity.getLengthSquared() != 0.0)
			velocity.scale(SLOW_RATE);

		/*
		 * Loop through each bullet and remove it from the list if necessary.
		 */
		Iterator <Missile> iter = missiles.iterator();
		while (iter.hasNext())
		{
			Missile bullet = iter.next();
			
			if (bullet.needsRemoval())
				iter.remove();
		}

		/*
		 * Decrement the fire and overheat cooldowns, and determine if we can fire another
		 * bullet.
		 */
		this.fireCooldown--;
		this.overheatCooldown--;
		if (firingEnabled && firePressed && fireCooldown <= 0 && overheatCooldown <= 0)
		{
			/*
			 * We can only create a new bullet if we haven't yet exceeded the
			 * maximum number of bullets that we can have fired at once.
			 * 
			 * If a new bullet can be fired, we reset the fire cooldown, and
			 * register a new bullet to the game world.
			 */
			if (this.missiles.size() < MAX_BULLETS)
			{
				this.fireCooldown = FIRE_RATE;

				Missile bullet = new Missile(this, Color.RED, rotation);
				
				this.missiles.add(bullet);
				game.registerEntity(bullet);
			}

			/*
			 * Since we're attempting to fire a bullet, we increment the number
			 * of consecutive shots and determine if we should set the overheat
			 * flag.
			 * 
			 * This prevents us from being able to wipe out entire groups of
			 * asteroids in one burst if we're accurate enough, and will prevent
			 * us from firing a continuous stream of bullets until we start missing.
			 */
			this.consecutiveShots++;
			if (this.consecutiveShots == MAX_CONSECUTIVE_SHOTS)
			{
				this.consecutiveShots = 0;
				this.overheatCooldown = MAX_OVERHEAT;
			}
		}
		else
		{
			if (this.consecutiveShots > 0)
			{
				//Decrement the number of consecutive shots, since we're not trying to fire.
				this.consecutiveShots--;
			}
		}
	}

	@Override
	public void handleCollision (Game game, Entity other)
	{
		//Kill the player if it collides with an Asteroid.
		if (other.getClass() == Asteroid.class)
			game.killPlayer();
	}

	@Override
	public void draw (Graphics2D g, Game game)
	{
		/*
		 * When the player recently spawned, it will flash for a few seconds to indicate
		 * that it is invulnerable. The player will not flash if the game is paused.
		 */
		if (! game.isPlayerInvulnerable() || game.isPaused() || this.animationFrame % 20 < 10)
		{
			/*
			 * Draw the ship. The nose will face right (0.0 on the unit circle). All
			 * transformations will be handled by the WorldPanel before calling the draw
			 * function.
			 */
			g.setColor(this.color);
			g.drawLine(-10, -8, 10, 0);
			g.drawLine(-10, 8, 10, 0);
			g.drawLine(-6, -6, -6, 6);
			g.setColor(WorldPanel.COLOR_DEFAULT);

			if (! game.isPaused() && this.thrustPressed && this.animationFrame % 6 < 3)
			{
				g.setColor(this.flames1Color);
				g.drawLine(-6, -6, -14, 0);
				g.drawLine(-6, 6, -14, 0);
				
				g.setColor(this.flames2Color);
				g.fillOval(-14, -2, 7, 4);
				
				g.setColor(this.flames2Color);
				g.fillOval(-14, -1, 7, 4);
				
				if (this.superSpeed)
				{
					g.setColor(this.flames2Color);
					g.fillOval(-24, -3, 17, 7);
				}
				
				g.setColor(WorldPanel.COLOR_DEFAULT);
			}
		}
	}
}
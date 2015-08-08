package Entity;

import Game.Game;
import Game.WorldPanel;
import Util.Vector;
import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Entity
{
	protected Vector position;
	protected Vector velocity;
	protected Color color;
	protected double rotation;
	protected double collisionRadius;
	private boolean needsRemoval;
	private int killScore;

	public Entity (Vector position, Vector velocity, Color color, double radius, int killScore)
	{
		this.position = position;
		this.velocity = velocity;
		this.color = color;
		this.collisionRadius = radius;
		this.rotation = 0.0f;
		this.killScore = killScore;
		this.needsRemoval = false;
	}

	public Vector getPosition ()
	{
		return this.position;
	}

	public Vector getVelocity ()
	{
		return this.velocity;
	}

	public double getRotation ()
	{
		return this.rotation;
	}

	public double getCollisionRadius ()
	{
		return this.collisionRadius;
	}

	public boolean needsRemoval ()
	{
		return this.needsRemoval;
	}

	public int getKillScore ()
	{
		return this.killScore;
	}

	public void flagForRemoval ()
	{
		this.needsRemoval = true;
	}

	/**
	 * Fait rotationner l'entity par amount
	 *
	 * @param amount Le montant de rotation
	 */
	public void rotate (double amount)
	{
		this.rotation =  this.rotation + amount;
		this.rotation %= Math.PI * 2;
	}

	public void update (Game game)
	{
		this.position.add(this.velocity);
		
		if (this.position.x < 0.0f)
			this.position.x = this.position.x + WorldPanel.W_MAP_PIXEL;
		if (this.position.y < 0.0f)
			this.position.y = this.position.y + WorldPanel.H_MAP_PIXEL;
		
		this.position.x %= WorldPanel.W_MAP_PIXEL;
		this.position.y %= WorldPanel.H_MAP_PIXEL;
	}

	/**
	 * Determines whether two Entities have collided.
	 *
	 * @param entity The Entity to check against.
	 * @return Whether a collision occurred.
	 */
	public boolean checkCollision (Entity entity)
	{
		/*
		 * Here we use the Pythagorean Theorem to determine whether the two
		 * Entities are close enough to collide.
		 * 
		 * The reason we are squaring everything is because it's much, much
		 * quicker to square one variable than it is to take the square root
		 * of another. While this game is simple enough that such minor
		 * optimizations are unnecessary, it's still a good habit to get
		 * into.
		 */
		double radius = entity.getCollisionRadius() + this.getCollisionRadius();
		
		return (position.getDistanceToSquared(entity.position) < radius * radius);
	}

	public abstract void handleCollision (Game game, Entity other);

	public abstract void draw (Graphics2D g, Game game);
}
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

public abstract class Entity
{
	protected Vector position;
	protected Vector velocity;
	protected Color color;
	protected double rotation;
	protected double collisionRadius;
	
	private final int killScore;
	private boolean needsRemoval;

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

	public boolean needsRemoval ()
	{
		return this.needsRemoval;
	}

	public double getRotation ()
	{
		return this.rotation;
	}

	public double getCollisionRadius ()
	{
		return this.collisionRadius;
	}

	public int getKillScore ()
	{
		return this.killScore;
	}

	public void flagForRemoval ()
	{
		this.needsRemoval = true;
	}
	
	public void rotate (double amount)
	{
		this.rotation = this.rotation + amount;
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
	
	public boolean checkCollision (Entity entity)
	{
		double radius = entity.getCollisionRadius() + this.getCollisionRadius();
		
		return (position.getDistanceToSquared(entity.position) < radius * radius);
	}

	public abstract void handleCollision (Game game, Entity other);

	public abstract void draw (Graphics2D g, Game game);
}
/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */

package Entity;

import Game.Game;
import Util.Vector;
import java.awt.Color;
import java.awt.Graphics2D;

public class Ennemi extends Ship
{
	public final static int START_RIGHT = 300;
	public final static int START_LEFT = -300;
	public final static int START_UP = 600;
	public final static int START_DOWN = 0;
	
	public int life;
	
	public Ennemi (Vector position, Vector shipVelocity, Color shipColor, Color missileColor, double shipSpeed, double missileSpeed, double radius, double rotationSpeed, int missileMax, int fireRate, int rechargeCooldown, int startingPosition, int life, int killScore)
	{
		super (position, shipVelocity, shipColor, missileColor, shipSpeed, missileSpeed, radius, rotationSpeed, missileMax, fireRate, rechargeCooldown, killScore);
	
		super.rotate(startingPosition);
		
		this.life = life;
	}
	
	public void flagForRemoval (Game game)
	{
		if (this.life > 0)
			this.life--;
		
		if (this.life == 0)
		{
			super.flagForRemoval();
			
			game.addScore(super.getKillScore());
		}
	}

	@Override
	public void checkCollision (Game game, Entity other)
	{
		if (other.getClass() == Missile.class)
			this.flagForRemoval(game);
	}

	@Override
	public void draw (Graphics2D g, Game game) {}
}
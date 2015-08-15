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
	
	public Ennemi (Vector position, Vector shipVelocity, Color shipColor, Color missileColor, double shipSpeed, double missileSpeed, double radius, int missileMax, int fireRate, int rechargeCooldown, int startingPosition, int killScore)
	{
		super (position, shipVelocity, shipColor, missileColor, shipSpeed, missileSpeed, radius, missileMax, fireRate, rechargeCooldown, killScore);
	
		super.rotate(startingPosition);
	}

	@Override
	public void checkCollision (Game game, Entity other)
	{
		if (other.getClass() == Missile.class)
		{
			super.flagForRemoval();

			game.addScore(super.getKillScore());
		}
	}

	@Override
	public void draw (Graphics2D g, Game game) {}
}
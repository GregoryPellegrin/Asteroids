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
	public Ennemi (Vector position, Vector shipVelocity, Color shipColor, Color missileColor, double shipSpeed, double missileSpeed, double radius, int missileMax, int fireRate, int rechargeCooldown, int killScore)
	{
		super (position, shipVelocity, shipColor, missileColor, shipSpeed, missileSpeed, radius, missileMax, fireRate, rechargeCooldown, killScore);
	}

	@Override
	public void checkCollision (Game game, Entity other)
	{
		if ((other.getClass() == Missile.class) || (other.getClass() == Player.class))
		{
			super.flagForRemoval();

			game.addScore(super.getKillScore());
		}
	}

	@Override
	public void draw (Graphics2D g, Game game) {}
}
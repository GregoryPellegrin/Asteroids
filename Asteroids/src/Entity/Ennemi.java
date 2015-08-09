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
	public Ennemi (Vector position, Vector velocity, Color shipColor, Color missileColor, double speedMagnitude, double missileMagnitude, double radius, int maxMissiles, int fireRate, int rechargeCooldown, int killScore)
	{
		super (position, velocity, shipColor, missileColor, speedMagnitude, missileMagnitude, radius, maxMissiles, fireRate, rechargeCooldown, killScore);
	}

	@Override
	public void handleCollision (Game game, Entity other)
	{
		if ((other.getClass() == Missile.class) || (other.getClass() == Player.class))
		{
			this.flagForRemoval();

			game.addScore(this.getKillScore());
		}
	}

	@Override
	public void draw (Graphics2D g, Game game) {}
}
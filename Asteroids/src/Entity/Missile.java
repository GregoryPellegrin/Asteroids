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

public class Missile extends Entity
{
	private static final int LIFESPAN_MAX = 60;
	
	private int lifeSpan;

	public Missile (Entity owner, Color color, double direction, double speed)
	{
		super(new Vector(owner.getPosition()), new Vector(direction).scale(speed), color, 2.0, 0);
		
		this.lifeSpan = LIFESPAN_MAX;
	}

	@Override
	public void update (Game game)
	{
		super.update(game);

		this.lifeSpan--;
		
		if (lifeSpan <= 0)
			flagForRemoval();
	}

	@Override
	public void checkCollision (Game game, Entity other)
	{
		if (other.getClass() != Player.class)
			flagForRemoval();
	}

	@Override
	public void draw (Graphics2D g, Game game)
	{
		g.setColor(super.color);	
		g.drawOval(-1, -1, 2, 2);
		
		g.setColor(WorldPanel.COLOR_DEFAULT);
	}
}
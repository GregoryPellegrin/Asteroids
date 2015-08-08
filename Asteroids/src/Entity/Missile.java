package Entity;

import Game.Game;
import Game.WorldPanel;
import Util.Vector;
import java.awt.Color;
import java.awt.Graphics2D;

public class Missile extends Entity
{
	private static final double SPEED_MAGNITUDE = 6.75;
	private static final int LIFESPAN_MAX = 60;
	private int lifeSpan;

	public Missile (Entity owner, Color color, double direction)
	{
		super(new Vector(owner.position), new Vector(direction).scale(SPEED_MAGNITUDE), color, 2.0, 0);
		
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
	public void handleCollision (Game game, Entity other)
	{
		if (other.getClass() != Player.class)
			flagForRemoval();
	}

	@Override
	public void draw (Graphics2D g, Game game)
	{
		g.setColor(this.color);
		g.drawOval(-1, -1, 2, 2);
		
		g.setColor(WorldPanel.COLOR_DEFAULT);
	}
}
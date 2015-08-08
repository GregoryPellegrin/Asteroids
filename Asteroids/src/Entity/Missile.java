package Entity;

import Game.Game;
import Util.Vector;
import java.awt.Color;
import java.awt.Graphics2D;

public class Missile extends Entity
{
	/**
	 * La magnitude de la velocite d'un missile
	 */
	private static final double MAGNITUDE_VELOCITE = 6.75;
	private static final int LIFESPAN_MAX = 60;
	private int lifeSpan;

	/**
	 * @param owner L'objet qui a tire le missile
	 * @param direction La direction du missile
	 */
	public Missile (Entity owner, double direction)
	{
		super(new Vector(owner.position), new Vector(direction).scale(MAGNITUDE_VELOCITE), 2.0, 0);
		
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
		g.setColor(Color.RED);
		g.drawOval(-1, -1, 2, 2);
		g.setColor(Color.WHITE);
	}
}
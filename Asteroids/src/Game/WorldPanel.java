package Game;

import Entity.Entity;
import Util.Vector;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.Iterator;
import javax.swing.JPanel;

public class WorldPanel extends JPanel
{
	public static final Color COLOR_DEFAULT = Color.WHITE;
	public static final int W_MAP_PIXEL = 900;
	public static final int H_MAP_PIXEL = 500;
	
	private static final long serialVersionUID = -5107151667799471396L;
	private static final Font MENU_POLICE = new Font("Helvetica", Font.BOLD, 25);
	private static final Font INFORMATION_POLICE = new Font("Helvetica", Font.BOLD, 20);
	private Game game;

	public WorldPanel (Game game)
	{
		this.game = game;

		this.setPreferredSize(new Dimension(WorldPanel.W_MAP_PIXEL, WorldPanel.H_MAP_PIXEL));
		this.setBackground(Color.BLACK);
	}

	private void drawTextCentered (String text, Font font, Graphics2D g, int y)
	{
		g.setFont(font);
		g.drawString(text, WorldPanel.W_MAP_PIXEL / 2 - g.getFontMetrics().stringWidth(text) / 2, WorldPanel.H_MAP_PIXEL / 2 + y);
	}

	private void drawEntity (Graphics2D g2d, Entity entity, double x, double y)
	{
		g2d.translate(x, y);
		
		double rotation = entity.getRotation();
		
		if (rotation != 0.0f)
			g2d.rotate(entity.getRotation());
			
		entity.draw(g2d, this.game);
	}

	@Override
	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setColor(WorldPanel.COLOR_DEFAULT);

		//Grab a reference to the current "identity" transformation, so we can reset for each object.
		AffineTransform identity = g2d.getTransform();

		Iterator <Entity> iter = this.game.getEntities().iterator();
		
		while (iter.hasNext())
		{
			Entity entity = iter.next();
			
			/*
			 * We should only draw the player if it is not dead, so we need to
			 * ensure that the entity can be rendered.
			 */
			if (entity != this.game.getPlayer() || this.game.canDrawPlayer())
			{
				Vector pos = entity.getPosition(); //Get the position of the entity.

				//Draw the entity at it's actual position, and reset the transformation.
				this.drawEntity(g2d, entity, pos.x, pos.y);
				g2d.setTransform(identity);

				/*
				 * Here we need to determine whether or not the entity is close enough
				 * to the edge of the window to wrap around to the other side.
				 * 
				 * The conditional statements might look confusing, but they're
				 * equivalent to:
				 * 
				 * double x = pos.x;
				 * if(pos.x < radius) {
				 *     x = pos.x + WORLD_SIZE;
				 * } else if(pos.x > WORLD_SIZE - radius) {
				 *     x = pos.x - WORLD_SIZE;
				 * }
				 * 
				 */
				double radius = entity.getCollisionRadius();
				double x = (pos.x < radius) ? pos.x + WorldPanel.W_MAP_PIXEL
						: (pos.x > WorldPanel.W_MAP_PIXEL - radius) ? pos.x - WorldPanel.W_MAP_PIXEL : pos.x;
				double y = (pos.y < radius) ? pos.y + WorldPanel.H_MAP_PIXEL
						: (pos.y > WorldPanel.H_MAP_PIXEL - radius) ? pos.y - WorldPanel.H_MAP_PIXEL : pos.y;

				//Draw the entity at it's wrapped position, and reset the transformation.
				if (x != pos.x || y != pos.y)
				{
					this.drawEntity(g2d, entity, x, y);
					g2d.setTransform(identity);
				}
			}
		}

		if (! this.game.isGameOver())
		{
			g.setFont(WorldPanel.INFORMATION_POLICE);
			g.setColor(Color.RED);
			g.drawString("SCORE", 10, 25);
			
			g.setFont(WorldPanel.INFORMATION_POLICE);
			g.setColor(Color.CYAN);
			g.drawString(String.valueOf(this.game.getScore()), 10, 50);
			
			g.setFont(WorldPanel.INFORMATION_POLICE);
			g.setColor(Color.RED);
			g.drawString("HIGH SCORE", WorldPanel.W_MAP_PIXEL / 2 - g.getFontMetrics().stringWidth("HIGH SCORE") / 2, 25);
			
			g.setFont(WorldPanel.INFORMATION_POLICE);
			g.setColor(Color.CYAN);
			g.drawString(String.valueOf(this.game.getScore()), WorldPanel.W_MAP_PIXEL / 2 - g.getFontMetrics().stringWidth(String.valueOf(this.game.getScore())) / 2, 50);
			
			g2d.setColor(WorldPanel.COLOR_DEFAULT);
		}

		if (this.game.isGameOver())
		{
			g.setColor(Color.RED);
			this.drawTextCentered("GAME OVER", MENU_POLICE, g2d, -25);
			
			g.setColor(Color.CYAN);
			this.drawTextCentered("FINAL SCORE " + this.game.getScore(), MENU_POLICE, g2d, 10);
			
			g2d.setColor(WorldPanel.COLOR_DEFAULT);
		}
		else
		{
			if (this.game.isPaused())
				this.drawTextCentered("PAUSED", MENU_POLICE, g2d, -25);
			else
				if (this.game.isShowingLevel())
					this.drawTextCentered("LEVEL " + this.game.getLevel(), MENU_POLICE, g2d, -25);
		}

		for (int i = 0; i < this.game.getLives(); i++)
		{
			g2d.setColor(Color.BLUE);
			g2d.drawLine(W_MAP_PIXEL - 28, 30, W_MAP_PIXEL - 20, 10);
			g2d.drawLine(W_MAP_PIXEL - 12, 30, W_MAP_PIXEL - 20, 10);
			g2d.drawLine(W_MAP_PIXEL - 26, 26, W_MAP_PIXEL - 14, 26);
			
			g2d.translate(-20, 0);
		}
		
		g2d.setColor(WorldPanel.COLOR_DEFAULT);
	}
}
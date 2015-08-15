/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */

package Game;

import Entity.Star;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JPanel;

public class MenuPanel extends JPanel
{	
	private final ArrayList <Star> starBackground = new ArrayList <> ();
	
	private Font massiveFont;
	private Font largeFont;
	private Font mediumFont;
	private Game game;

	public MenuPanel (Game game)
	{
		this.game = game;

		this.setPreferredSize(new Dimension (WorldPanel.W_MAP_PIXEL, WorldPanel.H_MAP_PIXEL));
		this.setBackground(Color.BLACK);
		
		for (int i = 0; i < WorldPanel.STAR_BACKGROUND_MAX; i++)
			this.starBackground.add(new Star ());
		
		try
		{
			Font arcadeFont = Font.createFont(Font.TRUETYPE_FONT, new File ("ressources/arcadeClassic.ttf"));
			
			massiveFont = arcadeFont.deriveFont(Font.PLAIN, 80);
			largeFont = arcadeFont.deriveFont(Font.PLAIN, 30);
			mediumFont = arcadeFont.deriveFont(Font.PLAIN, 25);
		}
		catch (FontFormatException | IOException e)
		{
			System.out.println(e.getMessage());
		}
	}

	private void drawGameTitle (Graphics2D g, Font font, String text, int y)
	{
		g.setFont(font);
		g.drawString(text, (WorldPanel.W_MAP_PIXEL / 2) - (g.getFontMetrics().stringWidth(text) / 2), y);
	}

	private void drawGameMode (Graphics2D g, Font font, String text, int x, int y)
	{
		g.setFont(font);
		g.drawString(text, (WorldPanel.W_MAP_PIXEL / 2) - x, y);
	}

	private void drawGameCreator (Graphics2D g, Font font, String text)
	{
		g.setFont(font);
		g.drawString(text, (WorldPanel.W_MAP_PIXEL / 2) - (g.getFontMetrics().stringWidth(text) / 2), WorldPanel.H_MAP_PIXEL - 30);
	}
	
	@Override
	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(WorldPanel.COLOR_DEFAULT);
		
		for (int i = 0; i < WorldPanel.STAR_BACKGROUND_MAX; i++)
		{
			this.starBackground.get(i).update(this.game.getStarSpeed());
			this.starBackground.get(i).drawStar(g2d);
		}

		g.setColor(Color.RED);
		this.drawGameTitle(g2d, this.massiveFont, "ASTEROIDS", 80);
		
		g.setColor(Color.YELLOW);
		if (game.getGameMode() == 0)
			g.fillOval((WorldPanel.W_MAP_PIXEL / 2) - 80, WorldPanel.H_MAP_PIXEL / 2, 10, 10);
		this.drawGameMode(g2d, this.largeFont, "1 PLAYER", 60, WorldPanel.H_MAP_PIXEL / 2);
		
		g.setColor(Color.YELLOW);
		if (game.getGameMode() == 1)
			g.fillOval((WorldPanel.W_MAP_PIXEL / 2) - 80, (WorldPanel.H_MAP_PIXEL / 2) + 30, 10, 10);
		this.drawGameMode(g2d, this.largeFont, "2 PLAYERS", 60, (WorldPanel.H_MAP_PIXEL / 2) + 30);
		
		g.setColor(Color.YELLOW);
		if (game.getGameMode() == 2)
			g.fillOval((WorldPanel.W_MAP_PIXEL / 2) - 80, (WorldPanel.H_MAP_PIXEL / 2) + 60, 10, 10);
		this.drawGameMode(g2d, this.largeFont, "VERSUS MODE", 60, (WorldPanel.H_MAP_PIXEL / 2) + 60);
		
		g.setColor(Color.WHITE);
		this.drawGameCreator(g2d, this.mediumFont, "CREATED BY MASTER OF SEX");
		
		g2d.setColor(WorldPanel.COLOR_DEFAULT);
	}
}
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
			this.starBackground.add(new Star (Star.STAR_SPEED_NORMAL));
		
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

	private void drawCenter (Graphics2D g, Font font, String text)
	{
		g.setFont(font);
		g.drawString(text, (WorldPanel.W_MAP_PIXEL / 2) - (g.getFontMetrics().stringWidth(text) / 2), WorldPanel.H_MAP_PIXEL - 30);
	}

	private void drawCenter (Graphics2D g, Font font, String text, int y)
	{
		g.setFont(font);
		g.drawString(text, (WorldPanel.W_MAP_PIXEL / 2) - (g.getFontMetrics().stringWidth(text) / 2), y);
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
			this.starBackground.get(i).update();
			this.starBackground.get(i).drawStar(g2d);
		}

		g.setColor(Color.RED);
		this.drawGameTitle(g2d, this.massiveFont, "ASTEROIDS", 80);
		
		g.setColor(Color.YELLOW);
		//if (this.game.isGameModeChoose())
		//	g.setColor(Color.WHITE);
		this.drawCenter(g2d, this.massiveFont, "START", WorldPanel.H_MAP_PIXEL / 2);
		
		g.setColor(Color.WHITE);
		this.drawCenter(g2d, this.largeFont, "CREATED BY SUSHI");
		
		g2d.setColor(WorldPanel.COLOR_DEFAULT);
	}
}
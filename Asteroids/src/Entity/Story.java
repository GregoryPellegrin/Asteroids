/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */

package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Story
{
	public int startText;
	public int endText;
	
	private final ArrayList <ArrayList<Font>> font = new ArrayList <> ();
	private final ArrayList <ArrayList<String>> text = new ArrayList <> ();
	private final ArrayList <ArrayList<Integer>> yPosition = new ArrayList <> ();
	private final ArrayList <Color> color = new ArrayList <> ();
	
	private Font massiveFont;
	private Font largeFont;
	private Font mediumFont;
	
	public Story ()
	{
		try
		{
			Font arcadeFont = Font.createFont(Font.TRUETYPE_FONT, new File ("ressources/arcadeClassic.ttf"));
			
			this.massiveFont = arcadeFont.deriveFont(Font.BOLD, 40);
			this.largeFont = arcadeFont.deriveFont(Font.PLAIN, 30);
			this.mediumFont = arcadeFont.deriveFont(Font.PLAIN, 25);
		}
		catch (FontFormatException | IOException e)
		{
			System.out.println(e.getMessage());
		}
		
		this.font.add(new ArrayList <Font> ());
		this.text.add(new ArrayList <String> ());
		this.yPosition.add(new ArrayList <Integer> ());
		
		this.font.get(0).add(this.mediumFont);
		this.font.get(0).add(this.mediumFont);
		this.font.get(0).add(this.mediumFont);
		this.font.get(0).add(this.mediumFont);
		this.font.get(0).add(this.mediumFont);
		this.font.get(0).add(this.massiveFont);
		this.font.get(0).add(this.massiveFont);
		
		this.text.get(0).add("En 2100 nous avons decouvert une existence");
		this.text.get(0).add("extraterrestre hostile dans notre systeme solaire");
		this.text.get(0).add("Vous etes le seul vaisseau de reconnaissance");
		this.text.get(0).add("et vous avez pour mission la destruction");
		this.text.get(0).add("du plus de race alien que possible");
		this.text.get(0).add("OUI ! Vous etes le Rambo spatial");
		this.text.get(0).add("Et votre mission est impossible");
		
		this.yPosition.get(0).add(30);
		this.yPosition.get(0).add(50);
		this.yPosition.get(0).add(70);
		this.yPosition.get(0).add(90);
		this.yPosition.get(0).add(110);
		this.yPosition.get(0).add(160);
		this.yPosition.get(0).add(190);
		
		this.color.add(Color.RED);
	}
	
	public Font getFont (int i, int j)
	{
		return this.font.get(i).get(j);
	}
	
	public String getText (int i, int j)
	{
		return this.text.get(i).get(j);
	}
	
	public int getYPosition (int i, int j)
	{
		return this.yPosition.get(i).get(j);
	}
	
	public int getSize (int i)
	{
		return this.font.get(i).size();
	}
	
	public Color getColor (int i)
	{
		return this.color.get(i);
	}
}
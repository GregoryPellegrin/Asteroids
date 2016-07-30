/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */

/*
 * TODO
 * 
 * Refactoring
 * Multijoueurs
 * Collisions
 * Objectifs annexes
 * Map
 * IA
 * Site Web
 * Son
 */

package Game;

import Entity.Ennemi;
import Entity.Entity;
import Entity.Player;
import Ennemi.SuperSpeedShip;
import Entity.Story;
import Util.Clock;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFrame;

public class Game extends JFrame
{
	public Story story;
	
	private static final int FRAMES_PER_SECOND = 60;
	private static final long FRAME_TIME = (long) (1000000000.0 / FRAMES_PER_SECOND);
	private static final int DEATH_COOLDOWN_LIMIT = 200;
	private static final int RESPAWN_COOLDOWN_LIMIT = 100;
	private static final int INVULN_COOLDOWN_LIMIT = 0;
	private static final int RESET_COOLDOWN_LIMIT = 120;
	
	private final MenuPanel menu;
	private final WorldPanel world;
	private final KeyAdapter menuListener;
	private final KeyAdapter playerListener;
	
	private List <Entity> entities;
	private List <Entity> pendingEntities;
	private Clock logicTimer;
	private Player player;
	private boolean isGameStart;
	private boolean isGameStop;
	private int deathCooldown;
	private int restartCooldown;
	private int score;
	private int lives;

	private Game ()
	{
		super ("Asteroids");
		
		this.menu = new MenuPanel (this);
		this.world = new WorldPanel (this);
		
		this.menuListener = new KeyAdapter ()
		{
			@Override
			public void keyPressed (KeyEvent e)
			{
				switch (e.getKeyCode())
				{						
					case KeyEvent.VK_SPACE:
						startTheGame();
						break;

					case KeyEvent.VK_ENTER:
						startTheGame();
						break;
				}
			}
		};
		
		this.playerListener = new KeyAdapter ()
		{
			@Override
			public void keyPressed (KeyEvent e)
			{
				switch (e.getKeyCode())
				{
					case KeyEvent.VK_Z:
						player.setMove(true);
						break;

					case KeyEvent.VK_D:
						player.setRotateRight(true);
						break;

					case KeyEvent.VK_Q:
						player.setRotateLeft(true);
						break;

					case KeyEvent.VK_O:
						player.setSuperSpeed(true);
						break;

					case KeyEvent.VK_SPACE:
						player.setFiring(true);
						break;
					
					case KeyEvent.VK_ESCAPE:
						stopTheGame();
						break;
				}
			}

			@Override
			public void keyReleased (KeyEvent e)
			{
				switch (e.getKeyCode())
				{
					case KeyEvent.VK_Z:
						player.setMove(false);
						break;

					case KeyEvent.VK_D:
						player.setRotateRight(false);
						break;
						
					case KeyEvent.VK_Q:
						player.setRotateLeft(false);
						break;

					case KeyEvent.VK_O:
						player.setSuperSpeed(false);
						break;

					case KeyEvent.VK_SPACE:
						player.setFiring(false);
						break;
				}
			}
		};
		
		this.story = new Story ();
		
		this.setLayout(new BorderLayout ());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		
		this.add(this.menu, BorderLayout.CENTER);
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public List <Entity> getEntities ()
	{
		return this.entities;
	}

	public Player getPlayer ()
	{
		return this.player;
	}

	public boolean isPlayerInvulnerable ()
	{
		return (this.deathCooldown > Game.INVULN_COOLDOWN_LIMIT);
	}

	public boolean canDrawPlayer ()
	{
		return (this.deathCooldown <= Game.RESPAWN_COOLDOWN_LIMIT);
	}

	public int getScore ()
	{
		return this.score;
	}

	public int getLives ()
	{
		return this.lives;
	}

	public void registerEntity (Entity entity)
	{
		this.pendingEntities.add(entity);
	}

	public void addScore (int score)
	{
		this.score = this.score + score;
	}
	
	public void killPlayer ()
	{
		this.lives--;

		if (this.lives == 0)
		{
			this.restartCooldown = Game.RESET_COOLDOWN_LIMIT;
			this.deathCooldown = Integer.MAX_VALUE;
		}
		else
			this.deathCooldown = Game.DEATH_COOLDOWN_LIMIT;

		this.player.setFiringEnabled(false);
	}

	private boolean isGameStart ()
	{
		return isGameStart;
	}

	private boolean isGameStop ()
	{
		return isGameStop;
	}
	
	private void startTheGame ()
	{
		this.isGameStart = true;
	}
	
	private void stopTheGame ()
	{
		this.isGameStop = true;
	}
	
	private void resetEntityLists ()
	{
		this.pendingEntities.clear();
		this.entities.clear();
		this.entities.add(this.player);
	}
	
	private void removeKeyListener ()
	{
		this.removeKeyListener(this.menuListener);
		this.removeKeyListener(this.playerListener);
	}
	
	private void resetMenu ()
	{
		this.isGameStart = false;
		
		this.resetEntityLists();
		this.removeKeyListener();
		
		this.remove(this.world);
		this.add(this.menu, BorderLayout.CENTER);
		this.revalidate();
		
		this.addKeyListener(this.menuListener);
	}
	
	private void resetGame ()
	{
		this.score = 0;
		this.lives = 3;
		this.deathCooldown = 0;
		this.isGameStop = false;
		
		this.resetEntityLists();
		this.removeKeyListener();
		
		this.remove(this.menu);
		this.add(this.world, BorderLayout.CENTER);
		this.revalidate();
		
		this.addKeyListener(this.playerListener);
	}

	private void startMenu ()
	{
		this.entities = new LinkedList <> ();
		this.pendingEntities = new ArrayList <> ();

		this.resetMenu();

		this.logicTimer = new Clock (Game.FRAMES_PER_SECOND);
		
		while (! this.isGameStart())
		{
			long start = System.nanoTime();
			
			this.logicTimer.update();

			this.menu.repaint();

			long delta = Game.FRAME_TIME - (System.nanoTime() - start);			
			if (delta > 0)
				try
				{
					Thread.sleep(delta / 1000000L, (int) delta % 1000000);
				}
				catch (Exception e)
				{
					System.out.println(e.getMessage());
				}
		}
	}

	private void startGame ()
	{
		this.entities = new LinkedList <> ();
		this.pendingEntities = new ArrayList <> ();
		this.player = new Player ();

		this.resetGame();

		this.logicTimer = new Clock (Game.FRAMES_PER_SECOND);
		
		for (int i = 0; i < 4 * 2; i++)
			this.registerEntity(new SuperSpeedShip (50 + i * 50, 100, Ennemi.START_LEFT));
		
		while (! this.isGameStop())
		{
			long start = System.nanoTime();
			
			this.logicTimer.update();
			for (int i = 0; i < 5 && this.logicTimer.hasElapsedCycle(); i++)
				this.updateGame();

			this.world.repaint();

			long delta = Game.FRAME_TIME - (System.nanoTime() - start);			
			if (delta > 0)
				try
				{
					Thread.sleep(delta / 1000000L, (int) delta % 1000000);
				}
				catch (Exception e)
				{
					System.out.println(e.getMessage());
				}
		}
	}
	
	private void updateGame ()
	{
		this.entities.addAll(this.pendingEntities);
		this.pendingEntities.clear();

		if (this.restartCooldown > 0)
			this.restartCooldown--;
		
		if (this.deathCooldown > 0)
		{
			this.deathCooldown--;
			
			switch (this.deathCooldown)
			{
				case Game.RESPAWN_COOLDOWN_LIMIT:
					this.player.reset();
					this.player.setFiringEnabled(false);
					break;
				
				case Game.INVULN_COOLDOWN_LIMIT:
					this.player.setFiringEnabled(true);
					break;
			}
		}
		
		for (Entity entity : this.entities)
			entity.update(this);

		for (int i = 0; i < this.entities.size(); i++)
		{
			Entity a = this.entities.get(i);

			for (int j = i + 1; j < this.entities.size(); j++)
			{
				Entity b = this.entities.get(j);

				if (i != j && a.checkCollision(b) && ((a != this.player && b != this.player) || this.deathCooldown <= Game.INVULN_COOLDOWN_LIMIT))
				{
					a.checkCollision(this, b);
					b.checkCollision(this, a);
				}
			}
		}

		Iterator <Entity> iter = this.entities.iterator();
		while (iter.hasNext())
			if (iter.next().needsRemoval())
				iter.remove();
	}

	public static void main (String [] args)
	{
		Game game = new Game();
		
		while (true)
		{
			game.startMenu();
			game.startGame();
		}
	}
}
/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 */

package Game;

import Entity.Entity;
import Entity.Player;
import IA.BasicFitghter;
import Util.Clock;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;

public class Game extends JFrame
{
	private static final int FRAMES_PER_SECOND = 60;
	private static final long FRAME_TIME = (long) (1000000000.0 / FRAMES_PER_SECOND);
	private static final int DISPLAY_LEVEL_LIMIT = 60;
	private static final int DEATH_COOLDOWN_LIMIT = 200;
	private static final int RESPAWN_COOLDOWN_LIMIT = 100;
	private static final int INVULN_COOLDOWN_LIMIT = 0;
	private static final int RESET_COOLDOWN_LIMIT = 120;
	
	private List <Entity> entities;
	private List <Entity> pendingEntities;
	private WorldPanel world;
	private Clock logicTimer;
	private Random random;
	private Player player;
	private boolean isGameOver;
	private boolean restartGame;
	private int deathCooldown;
	private int showLevelCooldown;
	private int restartCooldown;
	private int score;
	private int lives;
	private int level;

	private Game ()
	{
		super ("Asteroids");
		
		this.setLayout(new BorderLayout ());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.add(this.world = new WorldPanel (this), BorderLayout.CENTER);
		
		this.addKeyListener(new KeyAdapter ()
		{
			@Override
			public void keyPressed (KeyEvent e)
			{
				switch (e.getKeyCode())
				{
					case KeyEvent.VK_Z:
					case KeyEvent.VK_UP:
						if (! checkForRestart())
							player.setThrusting(true);
						break;

					case KeyEvent.VK_Q:
					case KeyEvent.VK_LEFT:
						if (! checkForRestart())
							player.setRotateLeft(true);
						break;

					case KeyEvent.VK_D:
					case KeyEvent.VK_RIGHT:
						if (! checkForRestart())
							player.setRotateRight(true);
						break;

					case KeyEvent.VK_O:
						if (! checkForRestart())
							player.setSuperSpeed(true);
						break;

					case KeyEvent.VK_SPACE:
						if (! checkForRestart())
							player.setFiring(true);
						break;

					case KeyEvent.VK_P:
						if (! checkForRestart())
							logicTimer.setPaused(! logicTimer.isPaused());
						break;

					default:
						checkForRestart();
						break;
				}
			}

			@Override
			public void keyReleased (KeyEvent e)
			{
				switch (e.getKeyCode())
				{
					case KeyEvent.VK_Z:
					case KeyEvent.VK_UP:
						player.setThrusting(false);
						break;

					case KeyEvent.VK_Q:
					case KeyEvent.VK_LEFT:
						player.setRotateLeft(false);
						break;

					case KeyEvent.VK_D:
					case KeyEvent.VK_RIGHT:
						player.setRotateRight(false);
						break;

					case KeyEvent.VK_O:
						player.setSuperSpeed(false);
						break;

					case KeyEvent.VK_SPACE:
						player.setFiring(false);
						break;
				}
			}
		});

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
	
	public Random getRandom ()
	{
		return this.random;
	}

	public boolean isGameOver ()
	{
		return this.isGameOver;
	}

	public int getScore ()
	{
		return this.score;
	}

	public int getLives ()
	{
		return this.lives;
	}

	public int getLevel ()
	{
		return this.level;
	}

	public boolean isPaused ()
	{
		return this.logicTimer.isPaused();
	}

	public boolean isPlayerInvulnerable ()
	{
		return (this.deathCooldown > Game.INVULN_COOLDOWN_LIMIT);
	}

	public boolean canDrawPlayer ()
	{
		return (this.deathCooldown <= Game.RESPAWN_COOLDOWN_LIMIT);
	}
	
	public boolean isShowingLevel ()
	{
		return (this.showLevelCooldown > 0);
	}
	
	private boolean areEnemiesDead ()
	{
		for (Entity e : entities)
			if (e.getClass() == BasicFitghter.class)
				return false;
		
		return true;
	}

	private boolean checkForRestart ()
	{
		boolean restart = (this.isGameOver && this.restartCooldown <= 0);
		
		if (restart)
			this.restartGame = true;
		
		return restart;
	}

	public void addScore (int score)
	{
		this.score = this.score + score;
	}

	public void registerEntity (Entity entity)
	{
		this.pendingEntities.add(entity);
	}
	
	private void resetEntityLists ()
	{
		this.pendingEntities.clear();
		this.entities.clear();
		this.entities.add(this.player);
	}
	
	public void killPlayer ()
	{
		this.lives--;

		if (this.lives == 0)
		{
			this.isGameOver = true;
			this.restartCooldown = Game.RESET_COOLDOWN_LIMIT;
			this.deathCooldown = Integer.MAX_VALUE;
		}
		else
			this.deathCooldown = Game.DEATH_COOLDOWN_LIMIT;

		this.player.setFiringEnabled(false);
	}
	
	private void resetGame ()
	{
		this.score = 0;
		this.level = 0;
		this.lives = 3;
		this.deathCooldown = 0;
		this.isGameOver = false;
		this.restartGame = false;
		
		resetEntityLists();
	}

	private void startGame ()
	{
		this.random = new Random ();
		this.entities = new LinkedList <> ();
		this.pendingEntities = new ArrayList <> ();
		this.player = new Player ();

		this.resetGame();

		this.logicTimer = new Clock (Game.FRAMES_PER_SECOND);
		
		while (true)
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
					e.printStackTrace();
				}
		}
	}
	
	private void updateGame ()
	{
		this.entities.addAll(this.pendingEntities);
		this.pendingEntities.clear();

		if (this.restartCooldown > 0)
			this.restartCooldown--;

		if (this.showLevelCooldown > 0)
			this.showLevelCooldown--;

		if (this.isGameOver && this.restartGame)
			this.resetGame();

		if (! this.isGameOver && this.areEnemiesDead())
		{
			this.level++;
			this.showLevelCooldown = Game.DISPLAY_LEVEL_LIMIT;

			this.resetEntityLists();

			this.player.reset();
			this.player.setFiringEnabled(true);

			/*
			 * ENNEMIS
			 * 
			 * if (level < 3
			 * new Chasseur (position)
			 * 
			 * if (level > 3
			 * new Chasseur (position)
			 * new Elite (position)
			 * 
			 * if (level = 5
			 * new Boss (position)
			 */
			this.registerEntity(new BasicFitghter ());
		}
		
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
		
		if (this.showLevelCooldown == 0)
		{
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
	}

	public static void main (String [] args)
	{
		Game game = new Game();
		
		game.startGame();
	}
}
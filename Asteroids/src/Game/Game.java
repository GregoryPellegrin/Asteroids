package Game;

import Entity.Asteroid;
import Entity.Ennemi;
import Entity.Entity;
import Entity.Player;
import Util.Clock;
import Util.Vector;
import java.awt.BorderLayout;
import java.awt.Color;
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
	private static final long serialVersionUID = -3535839203174039672L;
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

	/**
	 * <p>
	 * The death cooldown timer is responsible for spreading a Player's death
	 * out over time, so the player does not instantly spawn.</p>
	 *
	 * <p>
	 * Upon death, this value is set to {@code DEATH_COOLDOWN_LIMIT}, and is
	 * decremented each frame.</p>
	 *
	 * <p>
	 * Once the value reaches {@code RESPAWN_COOLDOWN_LIMIT}, the player's ship
	 * will be reset and the player will regain the ability to move.</p>
	 *
	 * <p>
	 * Once the value reaches {@code INVULN_COOLDOWN_LIMIT}, the Player's ship
	 * will be vulnerable to collisions and the Player will regain the ability to
	 * shoot.</p>
	 */
	private int deathCooldown;

	/**
	 * <p>
	 * The show level cooldown timer is responsible for displaying the current
	 * level briefly after the previous level has been completed.</p>
	 */
	private int showLevelCooldown;

	/**
	 * <p>
	 * The reset cooldown prevents the game from instantly restarting if the
	 * player is pressing any keys upon death, as key events are continuously
	 * fired until the player lets go of the key.</p>
	 *
	 * <p>
	 * This timer adds a short delay that must expire before the game can
	 * be reset, giving the player time to react.</p>
	 */
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

		/*
		 * Here we add a key listener to the window so that we can process incoming
		 * user input.
		 * 
		 * Because the player is updated every cycle, rather than when it receives
		 * input (like I did for Tetris), we're only going to set a flag to indicate
		 * the current input state. The actual change in the player's entity's state
		 * will be handled in the game loop.
		 * 
		 * The reason we do this is simple. Events are only fired when input is received
		 * from the user. While the keyPressed event is continuously fired, it isn't
		 * necessarily going to be in sync with our main thread, which would cause
		 * all sorts of unpredictable behavior from our ship.
		 * 
		 * Note that any "pressed" event will restart the game rather than change the
		 * ship's state if the conditions are met.
		 */
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
			if (e.getClass() == Asteroid.class)
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

		resetGame();

		this.logicTimer = new Clock (FRAMES_PER_SECOND);
		
		while (true)
		{
			//Get the time that the frame started.
			long start = System.nanoTime();

			/*
			 * Update the game once for every cycle that has elapsed. If the game
			 * starts to fall behind, the game will update multiple times for each
			 * frame that is rendered in order to catch up.
			 */
			logicTimer.update();
			for (int i = 0; i < 5 && logicTimer.hasElapsedCycle(); i++)
				updateGame();

			//Repaint the window.
			world.repaint();

			/*
			 * Determine how many nanoseconds we have left during this cycle,
			 * and sleep until it is time for the next frame to start.
			 */
			long delta = FRAME_TIME - (System.nanoTime() - start);
			if (delta > 0)
			{
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
	}

	/**
	 * Update the game entities and states.
	 */
	private void updateGame ()
	{
		/*
		 * Here we add any pending entities to the world.
		 * 
		 * Two lists are required because we will frequently add entities to the
		 * world while we are iterating over them, which causes all sorts of
		 * errors.
		 */
		entities.addAll(pendingEntities);
		pendingEntities.clear();

		/*
		 * Decrement the restart cooldown.
		 */
		if (restartCooldown > 0)
			this.restartCooldown--;

		/*
		 * Decrement the show level cooldown.
		 */
		if (showLevelCooldown > 0)
			this.showLevelCooldown--;

		/*
		 * Restart the game if needed.
		 */
		if (isGameOver && restartGame)
			resetGame();

		/*
		 * If the game is currently in progress, and there are no enemies left alive,
		 * we prepare the next level.
		 */
		if (! isGameOver && areEnemiesDead())
		{
			//Increment the current level, and set the show level cooldown.
			this.level++;
			this.showLevelCooldown = DISPLAY_LEVEL_LIMIT;

			//Reset the entity lists (to remove bullets).
			resetEntityLists();

			//Reset the player's entity to it's default state, and re-enable firing.
			this.player.reset();
			this.player.setFiringEnabled(true);

			//Add the asteroids to the world.
			for (int i = 0; i < this.level + 2; i++)
				registerEntity(new Asteroid (random));
			registerEntity(new Ennemi (random, new Vector (1.1, 1.1), Color.RED, 10.0, 100));
		}

		/*
		 * If the player has recently died, decrement the cooldown and handle any
		 * special cases when they occur.
		 */
		if (deathCooldown > 0)
		{
			this.deathCooldown--;
			
			switch (deathCooldown)
			{

				//Reset the entity to it's default spawn state, and disable firing.
				case RESPAWN_COOLDOWN_LIMIT:
					player.reset();
					player.setFiringEnabled(false);
					break;

				//Re-enable the ability to fire, as we're no longer invulnerable.
				case INVULN_COOLDOWN_LIMIT:
					player.setFiringEnabled(true);
					break;

			}
		}

		/*
		 * Only run any of the update code if we're not currently displaying the
		 * level to the player.
		 */
		if (showLevelCooldown == 0)
		{

			//Iterate through the Entities and update their states.
			for (Entity entity : entities)
				entity.update(this);

			/*
			 * Handle any collisions that take place.
			 * 
			 * The outer loop iterates through all registered entities, while the
			 * inner loop only iterates through the Entities later in the list
			 * than the outer Entity.
			 * 
			 * This ensures that the same collision isn't handled multiple times,
			 * which allows us to make changes to an entity without it interfering
			 * with other collision results.
			 */
			for (int i = 0; i < entities.size(); i++)
			{
				Entity a = entities.get(i);
				for (int j = i + 1; j < entities.size(); j++)
				{
					Entity b = entities.get(j);
					if (i != j && a.checkCollision(b) && ((a != player && b != player) || deathCooldown <= INVULN_COOLDOWN_LIMIT))
					{
						a.handleCollision(this, b);
						b.handleCollision(this, a);
					}
				}
			}

			//Loop through and remove "dead" entities.
			Iterator <Entity> iter = this.entities.iterator();
			while (iter.hasNext())
			{
				if (iter.next().needsRemoval())
					iter.remove();
			}
		}
	}

	public static void main (String [] args)
	{
		Game game = new Game();
		
		game.startGame();
	}
}
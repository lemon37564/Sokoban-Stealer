package java2020.finalProject;

import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;
import java.awt.Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Time;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.util.Date;
import java.util.Random;
import java.util.ArrayList;
import java.awt.Dimension;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class Stage extends JPanel {

	private Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	private final double baseWidth = 1536.0;
	private final double scale = dimension.getWidth() / baseWidth; // suitable for all screen size

	// constant
	private final int MARGIN = (int) (40 * scale);
	private final int SPACE = (int) (40 * scale); // actor side length
	private final int LEFT = 1;
	private final int RIGHT = 2;
	private final int UP = 3;
	private final int DOWN = 4;
	private final int playerSkinOne = 1;
	private final int playerSkinTwo = 2;
	private final int playerSkinThree = 3;
	private final int LevelCount = 9;

	private long timeStart;
	private long timeMin;
	private long timeSec;
	// int variable
	private int currentlyFacing = DOWN;
	public int executetime = 0; // repaint time
	public static int forbutton = 0;
	private int width = 0; // Stage width
	private int height = 0; // Stage height
	private int policePeriod;
	private int toward = 1;
	private int Achived = 0;
	private int playerSkin;
	private int selection; // map selection
	private int bufferedFrames = 0; // for arrow image
	private int pauseSelect = 1; // pause button(manual)
	private int mapX, mapY;
	private int lossBuffer = 0; // loss buffer(don't close immediately)
	private int wonBuffer = 0; // don't switch immediately
	private Boolean checkLost = false;
	private long collisionIgnoreTime;
	private Long restartTime;
	private Long lossTime;
	private Long wonTime;

	private ArrayList<Police> cops;
	private ArrayList<Wall> walls;
	private ArrayList<Treasure> treasures;
	private ArrayList<Goal> goals;
	private ArrayList<HardWall> hardWalls;
	private Bomb bomb;
	// private Police cop;
	private Player stealer;
	private Portal portal;

	private BackgroundMP3Player sounds;

	private enum sound {
		bulletSound, bagSound, bombSound
	};

	private Boolean trigger = false;
	private boolean isCompletedBool = false;
	private boolean lost = false;
	private boolean restarted = false; // restart frame
	private boolean restartBuffer = false; // restart buffering(for 0.3sec)
	private boolean gamePause = false;
	private boolean nextStage = false;
	private boolean closeSignal = false;
	private boolean ending = false;
	private int explodeTime = 0;
	private Graphics graphic; // for the global using
	private Image arrowImage = new ImageIcon().getImage();

	private ImageManager imageManager = new ImageManager();
	private CheatManager cheater = new CheatManager();

	private EndingAnimation animate = new EndingAnimation();
	private CollisionDetector collisionDetect = new CollisionDetector();
	private Panel panel;

	public Stage(int playerSkinChoosen, int level) {
		timeStart = System.currentTimeMillis();

		selection = level;

		if (playerSkinChoosen == playerSkinTwo)
			playerSkin = playerSkinTwo;
		else if (playerSkinChoosen == playerSkinThree)
			playerSkin = playerSkinThree;
		else
			playerSkin = playerSkinOne; // default

		this.width = (int) dimension.getWidth();
		this.height = (int) dimension.getHeight();

		panel = new Panel(scale, width, height);

		initStage();
	}

	private void initStage() {
		addKeyListener(new TAdapter());
		setFocusable(true);
		initWorld();
	}

	public int getStageWidth() {
		return this.width;
	}

	public int getStageHeight() {
		return this.height;
	}

	private void initWorld() {

		arrowImage = imageManager.getArrowImage();

		walls = new ArrayList<>();
		treasures = new ArrayList<>();
		goals = new ArrayList<>();
		hardWalls = new ArrayList<>();
		cops = new ArrayList<>();

		int x = 0;
		int y = MARGIN + 50;

		Map maptest = new Map();
		String level = maptest.getMap(selection);

		policePeriod = 10;

		portal = new Portal(0, 0);

		Achived = 0;

		collisionDetect.setCollisionIgnore(false); // penatrate init
		nextStage = false;
		ending = false;
		bufferedFrames = 0;
		pauseSelect = 1;
		lossBuffer = 0;
		wonBuffer = 0;

		if (level == "") // if map is none, return
			return;

		mapX = level.indexOf("\n", 0); // len of map width
		mapY = level.length() / mapX; // len of map height

		mapX = mapX * SPACE; // pixel of map width
		mapY = mapY * SPACE; // pixel of map height

		int modifyX = (this.width - mapX) / 2;
		int modifyY = 20;

		for (int i = 0; i < level.length(); i++) { // set width, height, actors specified by the string
			char item = level.charAt(i);
			switch (item) {

				case 'H':
					HardWall newHardWall = new HardWall(x + modifyX, y + modifyY);
					newHardWall.setImage(imageManager.getHardWallImage());
					hardWalls.add(newHardWall); // hard wall cannot be penetrated
					x += SPACE;
					break;

				case '#':
					Wall newWall = new Wall(x + modifyX, y + modifyY);
					newWall.setImage(imageManager.getWallImage());
					walls.add(newWall); // create wall at (x,y)
					x += SPACE;

					break;
				case '^':
					bomb = new Bomb(x + modifyX, y + modifyY);
					Image[] temp = imageManager.getBombImage();
					// bomb.setImage(temp[1]);
					bomb.setImageArray(temp);
					x += SPACE;

					break;
				case ' ':
					x += SPACE;
					break;

				case '\n':
					y += SPACE;
					x = 0;
					break;

				case '$':
					Treasure treasure = new Treasure(x + modifyX, y + modifyY);
					treasure.setImage(imageManager.getTreasureImage());
					treasures.add(treasure);
					x += SPACE;
					break;

				case '.':
					Goal newGoal = new Goal(x + modifyX, y + modifyY);
					newGoal.setImage(imageManager.getGoalImage());
					goals.add(newGoal); // target goal
					x += SPACE;
					break;

				case '!':
					Police newCop = new Police(x + modifyX, y + modifyY);
					Image[] fourDir = imageManager.getPoliceImages();

					newCop.getFourDirectionImage(fourDir);
					newCop.setImage(fourDir[3]);
					cops.add(newCop);
					x += SPACE;
					break;

				case '%':
					Goal newGoal2 = new Goal(x + modifyX, y + modifyY);
					newGoal2.setImage(imageManager.getGoalImage());
					goals.add(newGoal2); // target goal

					Treasure treasure2 = new Treasure(x + modifyX, y + modifyY);
					treasure2.setImage(imageManager.getTreasureImage());
					treasures.add(treasure2);

					x += SPACE;
					break;

				case '@':
					stealer = new Player(x + modifyX, y + modifyY, playerSkin); // player
					x += SPACE;
					break;

				default:
					break;
			}
		}
	}

	private void buildWorld(Graphics g) {

		int playerX = 0, playerY = 0;

		if (selection == LevelCount + 1) { // all completed, play ending
			nextStage = true;
			ending = true;
			animate.ending(g);
			if (animate.over())
				closeSignal = true;
			repaint();
			return;
		}

		if (lost) {
			checkLost = true;
			stealer.playerExplode();
		}

		if (restarted) {
			trigger = false;
			checkLost = false;
			explodeTime = 0;
			timeStart = System.currentTimeMillis();
			if (panel.drawRestart(g, restartTime))
				return;
			else
				restarted = false;
		}

		if (lossBuffer > 15 && lost) {
			Long time = new Date().getTime();
			if (!panel.drawLoss(g, time - lossTime)) {
				lost = false;
            	restartLevel();
			}
			return;
		}

		if (wonBuffer > 15 && isCompletedBool) {
			Long time = new Date().getTime();
			if (panel.drawWon(g, time - wonTime)) {
				return;
			} else {
				isCompletedBool = false;
				nextStage = true;
				selection++;
				initWorld();
			}
		}

		if (gamePause) {
			panel.drawPause(g, pauseSelect, selection);
			return;
		}

		String info = String.format("傳送門：%d", portal.getAvailability());
		info += String.format("        子彈：%2d", stealer.getAmmo());

		if (collisionDetect.getCollisionIgnore()) {
			Long checkCollisonTime = new Date().getTime() - collisionIgnoreTime;
			checkCollisonTime = 3000 - checkCollisonTime;
			if (checkCollisonTime <= 0) {
				collisionDetect.setCollisionIgnore(false);
			}
			double temp = checkCollisonTime / 1000.0;
			if (temp >= 0)
				info += String.format("        技能時間：%.2f", temp);
		} else {
			if (stealer.getPenetrateSkill()) {
				info += "        穿牆技能：可用";
			} else {
				info += "        穿牆技能：不可用";
			}
		}

		if (cheater.checkCondition()) {
			info = "傳送門：∞        子彈：∞";
			stealer.setPenetrateSkill(true);;
			stealer.setAmmo(99997);
			portal.setAvailability(99999);

			if (collisionDetect.getCollisionIgnore()) {
				Long checkCollisonTime = new Date().getTime() - collisionIgnoreTime;
				checkCollisonTime = 3000 - checkCollisonTime;
				if (checkCollisonTime <= 0) {
					collisionDetect.setCollisionIgnore(false);
				}
				double temp = checkCollisonTime / 1000.0;
				if (temp >= 0)
					info += String.format("        時間倒數：%.2f", temp);
			} else {
				info += "         穿牆技能：∞";
			}
		}

		String info2 = String.format("進度：%d / %d", Achived, goals.size());

		g.setColor(new Color(0, 0, 0));
		g.setFont(new Font("Microsoft JhengHei", Font.BOLD, (int) (25 * scale)));
		g.drawString(info, this.width * 5 / 16, 60);
		g.setColor(new Color(0, 204, 0));
		g.drawString(info2, this.width * 5 / 16, 90);

		ArrayList<Actor> world = new ArrayList<>();

		world.addAll(goals);

		if (stealer.getBullet() != null)
			world.add(stealer.getBullet());

		world.addAll(walls);
		world.addAll(hardWalls);
		world.addAll(treasures);

		if (cops.isEmpty() != true) {

			world.addAll(cops);
		}

		if (bomb != null) {
			world.add(bomb);
		}

		world.add(stealer);
		world.add(portal);

		int tempBulletX = -500, tempBulletY = -500;
		/*
		 * record new bullet x,y. If it collides with a wall, delete bullet, initialized
		 * to negative numbers to avoid error
		 */

		for (int i = 0; i < world.size(); i++) {

			Actor item = world.get(i);
			if (item != null && item instanceof Police && forbutton == 0 && executetime % policePeriod == 1
					&& !cheater.checkUserCommand()) {

				Police cop = (Police) item;
				int policeCanGo = 0; // means next direction police can move
				int accumulate = 0; // avoid police surrounded by box
				while (policeCanGo == 0) {
					if ((accumulate += 1) == 100) {

						world.remove(cop);
						cops.remove(cop);
						cop = null;
						break;
					}
					policeCanGo = 1;

					if (isCompletedBool)
						policeCanGo = 0;

					toward = cop.nextStep();

					if (collisionDetect.checkHardWallCollision(cop, toward, hardWalls)) {
						policeCanGo = 0;
					} else if (collisionDetect.checkWallCollision(cop, toward, walls)) {
						policeCanGo = 0;
					} else if (collisionDetect.checkBagCollisionforPolice(cop, toward, treasures)) {
						policeCanGo = 0;
					} else if (collisionDetect.checkPersonAndPersonCollision(cop, stealer, toward)) {
						playerLoss();
						return;
					}
					for (int c = 0; i < cops.size(); c++) { // 做每個警衛比較
						Police pol = cops.get(c);
						if (cop.equals(pol))
							continue;
						if (collisionDetect.checkPersonAndPersonCollision(cop, pol, toward)) {
							policeCanGo = 0;

						}

					}
					if (cop.x() == tempBulletX && cop.y() == tempBulletY) {
						stealer.setBullet(null);
						world.remove(cop);
						cop = null;
						break;
					}
					if (stealer.getBullet() != null && cop.x() == stealer.getBullet().x()
							&& cop.y() == stealer.getBullet().y()) {
						stealer.setBullet(null);
						world.remove(cop);
						cop = null;
						break;
					}

				}
				if (cop == null) {
					continue;
				}
				cop.setsituation_change(toward);

				g.drawImage(item.getImage(), item.x() + 2, item.y() + 2, this);
			} else if (item instanceof Bomb) {
				if (lost)
					g.drawImage(item.getImage(), item.x(), item.y(), this);
				else if ((executetime / 10) % 2 == 1)
					g.drawImage(item.getImageArray(1), item.x(), item.y(), this);
				else
					g.drawImage(item.getImageArray(0), item.x(), item.y(), this);

			} else if (item instanceof Treasure) {

				g.drawImage(item.getImage(), item.x(), item.y(), this);
				if (item.x() == tempBulletX && item.y() == tempBulletY) // bullet collides with treasure
					stealer.setBullet(null);

			} else if (item instanceof Portal) {

				Portal portalRef = (Portal) item;
				if (portalRef.getIsActive() == 1) {
					portalRef.animation();
					g.drawImage(item.getImage(), item.x() + 2, item.y() + 2, this);
				}

			} else if (item instanceof Bullet) {

				Bullet bulletRef = (Bullet) item;
				if (bulletRef != null && bulletRef.getMaxRange() > 0) {
					if (forbutton == 0)
						bulletRef.updateXY();
					tempBulletX = bulletRef.x();
					tempBulletY = bulletRef.y();
					int bulletExist = 1;
					if (!cops.isEmpty()) {
						for (int k = 0; k < cops.size(); k++) {
							Police cop = cops.get(k);
							if (cop.x() == tempBulletX && cop.y() == tempBulletY) {
								stealer.setBullet(null);
								cops.remove(k);
								world.remove(cop);
								cop = null;
								bulletExist = 0;
								continue;
							}
						}

					}
					if (bulletExist == 1)
						g.drawImage(item.getImage(), item.x() + 2, item.y() + 2, this);
				} else
					stealer.setBullet(null);

			} else if (item instanceof Wall || item instanceof HardWall) { // wall

				g.drawImage(item.getImage(), item.x(), item.y(), this);
				if (item.x() == tempBulletX && item.y() == tempBulletY) // bullet collides with wall
					stealer.setBullet(null);

			} else if (item instanceof Player) {
				playerX = item.x() + 2;
				playerY = item.y() + 2;

				int tempX = playerX, tempY = playerY;
				if (checkLost)
					g.drawImage(item.getImage(), tempX - 20, tempY - 20, this);
				else
					g.drawImage(item.getImage(), tempX, tempY, this);

			} else { // goal
				g.drawImage(item.getImage(), item.x(), item.y(), this);
			}

			if (bufferedFrames < 21000) { // arrow image(for opening)
				if ((bufferedFrames / 3000) % 2 == 0) {
					g.drawImage(arrowImage, playerX - 5, playerY - 60, this);
				}
				bufferedFrames++;
			}

			if (bomb != null) {
				g.setFont(new Font("Microsoft JhengHei", Font.BOLD, 50));
				g.setColor(new Color(255, 0, 0));
				int spendingTime = (int) (System.currentTimeMillis() - timeStart) / 1000;

				int remainingTime = ((selection / 3) + 1) * 200 - spendingTime;
				if (remainingTime <= 0) {
					remainingTime = 0;
					playerLoss();
				}
				String temp = String.format("%d:%02d", remainingTime / 60, remainingTime % 60);
				g.drawString(temp, 100, 100);
			}

			g.setFont(new Font("Microsoft JhengHei", Font.BOLD, 20));
			g.setColor(new Color(0, 0, 0));
			String information = "[ESC or P]-選單    [X]-穿牆技能    [Z]-傳送門    [SPACE]-武器";
			g.drawString(information, (int) (scale * this.width / 2 - 320), this.height - 40);

		}
		if (forbutton == 1)
			forbutton = 0; // prevent repeated execution when bottom is clicked

		if (lost) { // buffered frames

			lossBuffer++;
			if (bomb != null) {
				ImageManager temp;
				temp = new ImageManager();
				Image[] bombPics;
				bombPics = temp.getExploImage();
				g.drawImage(bombPics[explodeTime], bomb.x() - 60, bomb.y() - 60, 120, 120, this);
				if (explodeTime++ > 9)
					explodeTime = 10;
				playExploSound();
				Image bombImg;
				bombImg = bombPics[0];
				bomb.setImage(bombImg);
			}

		}
		if (isCompletedBool)
			wonBuffer++;

		isCompleted();
	}

	@Override
	public void paintComponent(Graphics g) {
		graphic = g;
		super.paintComponent(g);
		buildWorld(g);
	}

	private class TAdapter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {

			if (lost || isCompletedBool || restarted || ending) {
				return;
			}

			int key = e.getKeyCode();

			switch (key) {
				case KeyEvent.VK_LEFT:

					currentlyFacing = LEFT;
					cheater.pushCommand(LEFT);
					stealer.setPlayerImage(LEFT);

					if (collisionDetect.checkCollisions(stealer, LEFT, hardWalls, walls, treasures, cops)) {
						return;
					}

					stealer.move(-SPACE, 0);
					if (!cops.isEmpty()) {
						for (int i = 0; i < cops.size(); i++) {
							Police cop = cops.get(i);
							if (collisionDetect.checkPersonAndPersonCollision(stealer, cop, LEFT)) {
								playerLoss();
								return;
							}
						}

					}

					break;

				case KeyEvent.VK_RIGHT:

					currentlyFacing = RIGHT;
					cheater.pushCommand(RIGHT);
					stealer.setPlayerImage(RIGHT);

					if (collisionDetect.checkCollisions(stealer, RIGHT, hardWalls, walls, treasures, cops)){
						return;
					}

					stealer.move(SPACE, 0);
					if (!cops.isEmpty()) {
						for (int i = 0; i < cops.size(); i++) {
							Police cop = cops.get(i);
							if (collisionDetect.checkPersonAndPersonCollision(stealer, cop, RIGHT)) {
								playerLoss();
								return;
							}
						}

					}

					break;

				case KeyEvent.VK_UP:

					if (gamePause) {
						if (pauseSelect != 1)
							pauseSelect--;

						try {
							sounds = new BackgroundMP3Player();
							sounds.setSound(sound.bagSound.ordinal());
							sounds.play();
						} catch (FileNotFoundException | JavaLayerException e1) {
							System.out.printf("music err");
						}
						return;
					}
					currentlyFacing = UP;
					cheater.pushCommand(UP);
					stealer.setPlayerImage(UP);

					if (collisionDetect.checkCollisions(stealer, UP, hardWalls, walls, treasures, cops)) {
						return;
					}

					stealer.move(0, -SPACE);
					if (!cops.isEmpty()) {
						for (int i = 0; i < cops.size(); i++) {
							Police cop = cops.get(i);
							if (collisionDetect.checkPersonAndPersonCollision(stealer, cop, UP)) {
								playerLoss();
								return;
							}
						}

					}

					break;

				case KeyEvent.VK_DOWN:

					if (gamePause) {
						if (pauseSelect != 3)
							pauseSelect++;

						try {
							sounds = new BackgroundMP3Player();
							sounds.setSound(sound.bagSound.ordinal());
							sounds.play();
						} catch (FileNotFoundException | JavaLayerException e1) {
							System.out.printf("music err");
						}
						return;
					}
					currentlyFacing = DOWN;
					cheater.pushCommand(DOWN);
					stealer.setPlayerImage(DOWN);

					if (collisionDetect.checkCollisions(stealer, DOWN, hardWalls, walls, treasures, cops)) {
						return;
					}

					stealer.move(0, SPACE);
					if (!cops.isEmpty()) {
						for (int i = 0; i < cops.size(); i++) {
							Police cop = cops.get(i);
							if (collisionDetect.checkPersonAndPersonCollision(stealer, cop, DOWN)) {
								playerLoss();
								return;
							}
						}

					}

					break;

				case KeyEvent.VK_Z: // portal

					if (portal.getIsActive() == 1) {
						for (int i = 0; i < treasures.size(); i++) {
							Treasure ref = treasures.get(i);
							if (ref.x() == portal.x() && ref.y() == portal.y()) /* check if portal is blocked by box */
								return;

						}
						stealer.setX(portal.x());
						stealer.setY(portal.y());
						portal.setIsActive(0);
					} else {
						if (portal.getAvailability() == 0)
							return;
						portal.setAvailability(portal.getAvailability() - 1);
						portal.setX(stealer.x());
						portal.setY(stealer.y());
						portal.setIsActive(1);
					}
					break;

				case KeyEvent.VK_SPACE: // bullet

					if (stealer.getBullet() != null)
						return;
					else if (stealer.getRifleAvailable() == 1 && stealer.getAmmo() > 0) {
						try {
							sounds = new BackgroundMP3Player();
							sounds.setSound(sound.bulletSound.ordinal());
							sounds.play();
						} catch (FileNotFoundException | JavaLayerException e1) {
							System.out.printf("music err");
						}

						Bullet newBullet = new Bullet(stealer.x(), stealer.y(), currentlyFacing);
						newBullet.setImage(imageManager.getBulletImage());
						stealer.setBullet(newBullet);
						stealer.setAmmo(stealer.getAmmo() - 1);
					}

					break;

				case KeyEvent.VK_X: // penetrate

					if (stealer.getPenetrateSkill()){
						collisionDetect.setCollisionIgnore(true);
						collisionIgnoreTime = new Date().getTime();
						stealer.setPenetrateSkill(false);
					}
					break;

				case KeyEvent.VK_ESCAPE:// pause
					gamePause = true;
					break;

				case KeyEvent.VK_P: // pause
					gamePause = true;
					break;

				case KeyEvent.VK_ENTER:
					if (gamePause) {

						try {
							sounds = new BackgroundMP3Player();
							sounds.setSound(sound.bagSound.ordinal());
							sounds.play();
						} catch (FileNotFoundException | JavaLayerException e1) {
							System.out.printf("music err");
						}

						switch (pauseSelect) {
							case 1:
								gamePause = false;
								break;
							case 2:
								gamePause = false;
								restartLevel();
								break;
							case 3:
								gamePause = false;
								closeSignal = true;
							default:
								break;
						}

						pauseSelect = 1;
					}
					break;

				case KeyEvent.VK_S:
					cheater.pushChar('s');
					break;

				case KeyEvent.VK_O:
					cheater.pushChar('o');
					break;

				case KeyEvent.VK_K:
					cheater.pushChar('k');
					break;

				case KeyEvent.VK_B:
					cheater.pushChar('b');
					break;

				case KeyEvent.VK_A:
					cheater.pushChar('a');
					break;

				case KeyEvent.VK_N:
					cheater.pushChar('n');
					break;

				default:
					break;
			}
			forbutton = 1;
			repaint();
		}
	}

	private void playExploSound() {
		if (trigger == false) {
			trigger = true;

			try {
				sounds = new BackgroundMP3Player();
				sounds.setSound(sound.bombSound.ordinal());
				sounds.play();
			} catch (FileNotFoundException | JavaLayerException e1) {
				System.out.printf("music err");
			}
		}
	}

	public void playerLoss() {
		lost = true;
		lossTime = new Date().getTime();
		cheater.deactivate(); // deactivate both cheating
	}

	public void isCompleted() {
		int finishedBags = 0;
		int canGetAmmocount = 0;

		for (int i = 0; i < treasures.size(); i++) {
			Treasure box = treasures.get(i);

			for (int j = 0; j < goals.size(); j++) {
				Goal goal = goals.get(j);
				if (box.x() == goal.x() && box.y() == goal.y()) {
					finishedBags += 1;
					if (box.canGetAmmo()) {
						box.getAmmo();
						canGetAmmocount++;
					}
				}
			}
		}
		if (finishedBags > Achived) {
			Achived += 1;
			stealer.setAmmo(stealer.getAmmo() + 2 * canGetAmmocount);
		} else if (finishedBags < Achived) {
			Achived--;
		}
		if (finishedBags == goals.size()) {
			isCompletedBool = true;
			wonTime = new Date().getTime();
			cheater.deactivate();
		}
	}

	private void restartLevel() {

		cheater.deactivate();

		goals.clear();
		treasures.clear();
		walls.clear();
		cops.clear();
		hardWalls.clear();

		isCompletedBool = false;
		Achived = 1;
		lossBuffer = 0;

		restarted = true;
		restartTime = new Date().getTime();

		initWorld();
	}

	public boolean isLost() {
		return lost;
	}

	public boolean getisCompleted() {
		return isCompletedBool;
	}

	public boolean goNextStage() {
		return nextStage;
	}

	public boolean closeAct() {
		return closeSignal;
	}

	public void setNextStage(Boolean b) {
		nextStage = b;
	}
}

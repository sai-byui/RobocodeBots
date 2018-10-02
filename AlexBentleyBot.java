package RobocodeBots;
import robocode.*;
import java.awt.*;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * AlexBentleyBot - a robot by (your name here)
 */
public class AlexBentleyBot extends robocode.AdvancedRobot
{
	int tX = 0;
	int tY = 0;
	static double totalBulletsTracked = 0;
	static double bulletsHit = 0;
	double distanceMod = 774;
	double firePowerMod = 580;
	

	/**
	 * run: AlexBentleyBot's default behavior
	 */
	public void run() {
		
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForRobotTurn(true);
		setAdjustRadarForGunTurn(true);


		setColors(Color.red,Color.blue,Color.green); // body,gun,radar

		while(true) {
			turnRadarRight(90);
		}
	}
	
	private double[] adjustTargetPositionForDistanceAndVelocity(double bulletSpeed, double myX, double myY, double targetX, double targetY, double targetSpeed, double targetHeading) {
		// Find out how long it will take the bullet to get to the target's position
		double distanceToTarget = Math.sqrt(Math.pow((myX - targetX), 2) + Math.pow((myY - targetY), 2));
		double timeToTarget = distanceToTarget / bulletSpeed;
		// Find out where the target will be after that amount of time
		double distanceTargetWillMove = targetSpeed * timeToTarget;
		double newTargetX = targetX + distanceTargetWillMove * Math.sin(targetHeading);
		double newTargetY = targetY + distanceTargetWillMove * Math.cos(targetHeading);
		// Return the new target position
		double retVal[] = {newTargetX, newTargetY};
		return retVal;
	}

	public void onBulletHit( BulletHitEvent e) {
		totalBulletsTracked += 1;
		bulletsHit += 1;
	}

	public void onBulletMissed(BulletMissedEvent e) {
		totalBulletsTracked += 1;
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		turnRadarLeft(45);
		// Replace the next line with any behavior you would like
		double myX = getX();
		double myY = getY();
		double botX = myX + e.getDistance() * Math.sin(e.getBearingRadians() + Math.toRadians(getHeading()));
		double botY = myY + e.getDistance() * Math.cos(e.getBearingRadians() + Math.toRadians(getHeading()));
		double[] botPos = {botX, botY};
		double firePower = Math.min(firePowerMod / e.getDistance(), 3.0);
		for (int i = 0; i < 10; i++)
		{
			double vel = 0;
			if (i == 0)
			{
				vel = e.getVelocity();
			}
			botPos = adjustTargetPositionForDistanceAndVelocity(20 - 3 * firePower, myX, myY, botPos[0], botPos[1], vel, e.getHeadingRadians());
		}
		tX = (int) botPos[0];
		tY = (int) botPos[1];
		
		double adjustedX = botPos[0] - myX;
		double adjustedY = botPos[1] - myY;
		//out.println("AdjustedX: " + adjustedX);
		//out.println("AdjustedY: " + adjustedY);

		//If I know theta and r, then:
		//x = r*sin(theta)
		//y = r*cos(theta)
		//Therefore,
		//theta = asin(x/r)
		//theta = acos(y/r)
		//Problem found: I'm using the wrong distance measurement. Need from my bot to other bot's projected position, not to other bot.
		//NOTE:: Only replace distance measurement in conversion to polar, not in previous conversion to cartesian.

		double distance = Math.sqrt(Math.pow(myX - botPos[0], 2) + Math.pow(myY - botPos[1], 2));

		double t1 = Math.toDegrees(Math.asin(adjustedX / distance));
		double t2 = Math.toDegrees(Math.acos(adjustedY / distance));
		double t3 = Math.toDegrees(Math.atan(adjustedX / adjustedY));
		double t4 = Math.toDegrees(Math.atan(adjustedY / adjustedX));

		//out.println("T1: " + t1); //Keeps going to NaN ???
		//out.println("T2: " + t2); //Keeps going to NaN ???
		//out.println("T3: " + t3);
		//out.println("T4: " + t4);

		if (t1 < 0) {
			t2 = 180 + (180 - t2);
		}

		//out.println("Adjusted T2: " + t2);

		double angle_to_move_turret = t2 - getGunHeading();
		while (angle_to_move_turret > 180) {
			angle_to_move_turret -= 360;
		}
		while (angle_to_move_turret < -180) {
			angle_to_move_turret += 360;
		}

		//out.println("Angle_to_move_turret: " + angle_to_move_turret);

		if (angle_to_move_turret > 0) {
			turnGunRight(angle_to_move_turret);
		}
		else if (angle_to_move_turret < 0) {
			turnGunLeft(-1 * angle_to_move_turret);
		}
		if (angle_to_move_turret < distanceMod/distance && botPos[0] > 0 && botPos[1] > 0 && botPos[0] < getBattleFieldWidth() && botPos[1] < getBattleFieldHeight()) {
			fire(firePower);
		}
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		back(10);
	}

	public void onBattleEnded(BattleEndedEvent e) {
		out.println("Total bullets: " + totalBulletsTracked);
		out.println("Bullets hit: " + bulletsHit);
		out.println("Accuracy: " + (bulletsHit * 100 / totalBulletsTracked) + "%");
	}

	public void onPaint(Graphics2D g) {
		g.setColor(java.awt.Color.GREEN);
		g.fillOval(tX, tY, 10, 10);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		back(20);
	}	
}
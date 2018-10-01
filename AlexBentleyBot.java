package RobocodeBots;
import robocode.*;
import java.awt.*;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * AlexBentleyBot - a robot by (your name here)
 */
public class AlexBentleyBot extends robocode.Robot
{
	int tX = 0;
	int tY = 0;
	

	/**
	 * run: AlexBentleyBot's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here
		
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		setColors(Color.red,Color.blue,Color.green); // body,gun,radar

		// Robot main loop
		while(true) {
			// Replace the next 4 lines with any behavior you would like
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

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		turnRadarLeft(45);
		// Replace the next line with any behavior you would like
		double botX = getX() + e.getDistance() * Math.sin(e.getBearingRadians() + Math.toRadians(getHeading()));
		double botY = getY() + e.getDistance() * Math.cos(e.getBearingRadians() + Math.toRadians(getHeading()));
		double[] botPos = {botX, botY};
		for (int i = 0; i < 10; i++)
		{
			double vel = 0;
			if (i == 0)
			{
				vel = e.getVelocity();
			}
			botPos = adjustTargetPositionForDistanceAndVelocity(17, getX(), getY(), botPos[0], botPos[1], vel, e.getHeadingRadians());
		}
		tX = (int) botPos[0];
		tY = (int) botPos[1];
		
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		back(10);
	}

	public void onPaint(Graphics2D g) {
		// Set the paint color to red
		g.setColor(java.awt.Color.RED);
		// Paint a filled rectangle at (50,50) at size 100x150 pixels
		g.fillOval(tX, tY, 100, 100);
		g.setColor(java.awt.Color.YELLOW);
		g.fillOval(tX, tY, 30, 30);
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
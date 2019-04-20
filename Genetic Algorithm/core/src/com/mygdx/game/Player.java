package com.mygdx.game;

import java.util.Random;

public class Player {
	protected boolean dead, playing, previous;
	protected int movenum, fitness;
	protected float[] position, velocity;
	protected float[][] movements;

	Player(float[][] directions) {
		Random rand = new Random();

		this.previous = false;
		this.dead = false;
		this.playing = true;
		this.fitness = 0;
		this.movenum = 0;
		this.position = new float[] { 320, 20 };
		this.velocity = new float[] { 0, 0 };

		this.movements = new float[400][2];

		float rand1, rand2;

		if (directions.length == 0) {
			for (int i = 0; i < movements.length; i++) {
				for (int x = 0; x < 2; x++) {
					rand1 = rand.nextFloat();
					rand2 = rand.nextFloat();
					this.movements[i][x] = (rand1 - rand2);
				}
			}
		}

		else {
			float[][] movementsTemp = new float[directions.length][2];
			
			for (int i = 0; i < directions.length; i++) {
				movementsTemp[i] = directions[i].clone();
			}
			
			double mutationrate = 0.01;
			double randVal;

			for (int i = 0; i < movements.length; i++) {
				randVal = rand.nextDouble();

				if (randVal <= mutationrate) {
					for (int x = 0; x < 2; x++) {
						rand1 = rand.nextFloat();
						rand2 = rand.nextFloat();
						movementsTemp[i][x] = (rand1 - rand2);
					}
				}

				this.movements[i] = movementsTemp[i].clone();

			}
		}
	}

	public void update() {
		if (this.getPlaying() == true) {
			float[] veltemp = new float[2];
			float[] postemp = new float[2];
			for (int i = 0; i < 2; i++) {
				veltemp[i] = this.getVelocity()[i] + this.getMovements()[this.getMovenum()][i];
				postemp[i] = this.getPosition()[i] + veltemp[i];
			}
			this.setVelocity(veltemp);
			this.setPosition(postemp);

			if (this.getPosition()[0] >= 640 || this.getPosition()[0] <= 0 || this.getPosition()[1] <= 0 || this.getPosition()[1] >= 480) {
				this.setDead(true);
				this.setPlaying(false);
			}

			int fitnesstemp;
			float distance = (float)((320 - this.getPosition()[0]) * (320 - this.getPosition()[0])) + ((440 - this.getPosition()[1]) * (440 - this.getPosition()[1]));
			if (distance > 100) {
				fitnesstemp = (int) (100000 / distance);
			} else {
				fitnesstemp = 1000 + (int)(2000000000 / (this.getMovenum() * this.getMovenum() * this.getMovenum()));
				this.setPlaying(false);
			}

			if (this.getFitness() < fitnesstemp) {
				this.setFitness(fitnesstemp);
			}

			this.setMovenum(this.getMovenum() + 1);

			if (this.getMovenum() >= this.getMovements().length) {
				this.setDead(true);
				this.setPlaying(false);
			}
		}
	}

	public void setPrevious(boolean value) {
		previous = value;
	}

	public boolean getPrevious() {
		return previous;
	}

	public void setFitness(int value) {
		fitness = value;
	}

	public int getFitness() {
		return fitness;
	}

	public void setDead(boolean value) {
		dead = value;
	}

	public boolean getDead() {
		return dead;
	}

	public void setPlaying(boolean value) {
		playing = value;
	}

	public boolean getPlaying() {
		return playing;
	}

	public void setPosition(float[] value) {
		position = value;
	}

	public float[] getPosition() {
		return position;
	}

	public void setVelocity(float[] value) {
		velocity = value;
	}

	public float[] getVelocity() {
		return velocity;
	}

	public void setMovenum(int value) {
		movenum = value;
	}

	public int getMovenum() {
		return movenum;
	}

	public void setMovements(float[][] value) {
		movements = value;
	}

	public float[][] getMovements() {
		return movements;
	}
}

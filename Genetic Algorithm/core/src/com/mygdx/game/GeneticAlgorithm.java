package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GeneticAlgorithm extends ApplicationAdapter {
	ShapeRenderer sr;
	SpriteBatch batch;
	BitmapFont font;
	Player[] players;
	float[][] directions = new float[0][0], bestdirections = new float[0][0];// = new int[400][2]; //x, y From the chosen of last gen
	int generation = 0, highestfitness = 0, minmoves = 0;
	boolean newgen = true, firstgen = true, anyleft, haswon = false;
	
	Random rand = new Random();
	
	@Override
	public void create () {
		sr = new ShapeRenderer();
		font = new BitmapFont();
		font.setColor(Color.BLACK);
		batch = new SpriteBatch();
	}
	
	public float[][] genEnd() { //Decides which directions gets passed over onto the next generation
		float[][] nextgen = new float[0][0];
		
		int total = 0;
		for (int i = 0; i < players.length; i++) {
			total += players[i].getFitness();
			
			if (players[i].getFitness() > highestfitness) {
				highestfitness = players[i].getFitness();
				
				bestdirections = new float[players[i].getMovements().length][2];
				for (int x = 0; x < players[i].getMovements().length; x++) {
					bestdirections[x] = players[i].getMovements()[x].clone();
				}
			}
		}
		int choice = rand.nextInt(total);
		
		for (int i = 0; i < players.length; i++) {
			if (choice > players[i].getFitness()) {
				choice -= players[i].getFitness();
			}
			else {
				nextgen = new float[players[i].getMovements().length][2];
				for (int x = 0; x < players[i].getMovements().length; x++) {
					nextgen[x] = players[i].getMovements()[x].clone();
				}
				break;
			}
		}
		
		return nextgen;
	}
	
	

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (newgen == true) {
			newgen = false;
			generation += 1;
			players = new Player[400];
			for (int i = 0; i < 400; i++) {
				players[i] = new Player(directions);
			}
			
			if (directions.length > 0) {
				players[0].setMovements(bestdirections);
				players[0].setPrevious(true);
			}
		}
		
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.BLACK);
		
		anyleft = false;
		for (int i = 0; i < players.length; i++) {
			players[i].update();
			if(players[i].getPrevious() == true) {
				sr.setColor(Color.GREEN);
			}
			else {
				sr.setColor(Color.BLACK);
			}
			
			sr.circle(players[i].getPosition()[0], players[i].getPosition()[1], 2);
			
			if (players[i].getPlaying() == false && players[i].getDead() == false) {
				haswon = true;
				if (players[i].getMovenum() + 1 < minmoves || minmoves == 0) {
					minmoves = players[i].getMovenum() + 1;
				}
			}
			
			if (players[i].getPlaying() == true) {
				anyleft = true;
			}
		}
		
		if (anyleft == false) {
			newgen = true;
			directions = genEnd();
		}
		
		sr.setColor(Color.RED);
		sr.circle(320, 440, 10);
		sr.end();
		
		batch.begin();
		font.draw(batch, "Generation: " + generation, 500, 460);
		
		if (haswon == false) {
			font.draw(batch, "Fitness: " + highestfitness, 20, 460);
		}
		else {
			font.draw(batch, "Wins in " + minmoves + " moves!", 20, 460);
		}
		batch.end();
	}
	
	
	@Override
	public void dispose () {
		batch.dispose();
		sr.dispose();
		font.dispose();
	}
}

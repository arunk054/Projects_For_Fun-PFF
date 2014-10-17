package arun.ui.cli;

/**
 * @author Arun K (MT2009054)
 * 
 */

import arun.aco.Ant;
import arun.aco.AntColony;
import arun.aco.Controller;
import arun.aco.GlobalSettings;
import arun.world.Food;
import arun.world.World;

public class Launcher implements Controller{

	AntColony ac;
	
	public static void main(String[] args) {
		Launcher launch = new Launcher();
		
		//Create world
		World world = new World(GlobalSettings.WORLD_WIDTH, GlobalSettings.WORLD_HEIGHT);
		//create food
		Food food =  new Food(GlobalSettings.FOOD_X, GlobalSettings.FOOD_Y,GlobalSettings.FOOD_WIDTH,GlobalSettings.FOOD_HEIGHT);
		//initialize world, start node has to be 0,0 no other option
		world.initializeWorld(food, 0, 0);
		//Create ant colony
		launch.ac = new AntColony(world, GlobalSettings.TOTAL_ANTS, GlobalSettings.TOTAL_ITERATIONS, launch);
		//Initialize ant
		Ant.initialize(launch.ac, GlobalSettings.STEPS_PER_ITERATION);
		
		launch.ac.start();
				
	}

	public void setAntAt(int x, int y) {
		System.out.println("ant at x = "+x+" y = "+y);
		
	}

	public void setEndOfIteration() {
		//System.out.println("=================End of an iter=========================");
		
	}

	@Override
	public void setPheromoneAt(int x, int y, int level, int lowest_level) {
		System.out.println("Pheromone trail at.. "+x+" "+y);
		
	}

	@Override
	public void setStartOfIteration() {
		//System.out.println("============Start of iter==============================");
	}
}

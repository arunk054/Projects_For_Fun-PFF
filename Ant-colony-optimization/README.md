
Finding the Shortest Path Using Ant Colony Optimization

This project was done at IIIT-B during Fall 2009 under the guidance of Prof. G.N.S Prasanna.
The project aims to find the shortest path between two objects in the presence of obstacles when the coordinates one of the objects is unknown. We solve this by simulating the behavior of Ants in finding food. When an ant returns to its colony after finding food, it lays down a trail of chemical called pheromone along its path which attracts other ants to follow the trail. The pheromone evaporates over time and eventually, the trails along all paths except the shortest path (may not be the global minimum) evaporates. This causes almost all ants to follow the current shortest path until another ant found an even shorter path (by wandering randomly). The simulation reports the length of the shortest path found after a fixed number of iterations. Our results show the error to be less than one percent of the optimal solution with a reasonable number of iterations.
Downloads: Source Code
Our implementation provides several tunable parameters that give the simulation a finer control. These parameters can be modified in the source file arun/aco/GlobalSettings.java. Following table lists some of the parameters and their description.


Parameter Name	Description
EVAPORATION_STEPS_PER_ITERAION	The rate of evaporation of pheromone

PROB_PHEROMONE_NODE	The probability with which an ant decides to follow the pheromone trail. A value close to 1 may result in ants not able to find new paths, and a value close to zero results in highly random motion, hence a value around 0.7-0.8 is found to be satisfactory.

LOWEST_PHEROMONE_INDEX	This determines the minimum strength of the pheromone trail after complete evaporation, which is found using the relation -  0.9^( LOWEST_PHEROMONE_INDEX+1). 

Note: This also impacts the pheromone evaporation rate.
STEPS_PER_ITERATION	This is the number of steps that ants take before reporting to the observer (gui). As this increases, the movement of ants appears faster on the display. 


In our simulation, the GUI displays ants as black dots, food as blue rectangular block, pheromone trails change their color as it evaporates - red implies highest intensity which fades away as it evaporates. Obstacles are shown as green rectangular block. The number of ants, location of food and the obstacles are configurable in the source code.


Demo: https://www.youtube.com/watch?v=uDbwPeTu91E

Demo 2 : https://www.youtube.com/watch?v=1PHiGC8oZsg 



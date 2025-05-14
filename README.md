# Überdüngung




## Description
This game is a game that if not teach, notify children and young teens about how washing and throwing away clothing ends up putting micro plastics into the water

### The Game
The Game itself starts of with a start screen prompting people to scan a barcode of 3 preselected clothing articles.
Once Scanned the game show how when clothing lands in water it gives of microplastics.
Then the game starts for 30 seconds and where 2 players can control the 2 fish to try to avoid these microplastics.
Once the 30 seconds are over the fishes move out of screen. 
Then we show their score compared to the 10 best scores reached.
after the high scores are shown the game goes back to phase 1.

## Visuals
Well add some pictures of the done game

## Installation
For the Project you need java 21 and apache-maven 3.9.9

````shell
mvn clean package
````

To run on Pi you need to first chose the profile **Run on Pi** and change the ip address on line 34 of the [pom.xml](./pom.xml).
Careful you have to be on the same internet.

To make it auto start when the pie boots you have to write a script that start the game on startup or use ours: [startGame.sh](./) and put it in a place that your linux version uses for startup scripts.
Careful you might need to change the path.

## Future development
I explain some of our classes and design patterns here. For more details and reasoning consult the documentation on the SAD.

This well update once most of the code is final well split it up in how the code works so mvc.

### Model
Our model is split into a few objects that inherit from movable. Specifically **player** and all children of **obstacle**, including itself.
All of these are saved in a **world** class that keeps track of them and other data like the **score, map edges, time passed...**. 

#### Spawner
The **spawner** spawn the individual **obstacles**.
It follows the factory pattern. All **obstacles** are saved in a map with their constructors. 

### View
The view has a main function called render that calls a next function based of the current game phase.

We use a graphicscontext of a javaFX canvas to draw our visuals onto the screen.

First we always draw the background and then any other objects or animations.

### Controller
The controller has a main function called gameStep that calls a next function based of the current game phase.



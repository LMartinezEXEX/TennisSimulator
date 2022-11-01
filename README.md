# Tennis Simulator
---

### How to compile and run?
You can just import this project to your favourite IDE and run it from there. Otherwise, you would need to compile and run it by yourself. Here is a little guide to achieve this.
1. Make your current directory the TennisSimulator folder. 
2. Compile the .java files to their corresponding .class files. I encourage you to put them in a different folder from the .java files to avoid clutter the folder.
```bash
mkdir -p bin/com/simulator/app
javac src/com/simulator/app/*.java -d bin/
```
3. Now you are almost ready. We just need to run the program with
```bash
java -cp bin/ com.simulator.app.Simulator
```
4. Once started you will be asked data for the simulation. Have fun! :D

### About this project
This is a 1 vs. 1 tennis game simulator following the **International Tennis Federation** (**ITF**) rules.

The project is composed by a total of 6 classes:

* Simulator (*main*): This class is in charge of asking the users input for the generation of the match and starting the simulation.
* Tournament: Is in charge of simulating successive games updating the scoreboard, until it says to stop. This class is also responsible for the output of diferent stages of the simulation.
* Scoreboard: This is the most complex class because it is in charge of keeping the scores of the game following the rules. It posses two inner classes:
    - Game: responsible for keeping track of points in a single game
    - Set: responsible of keeping track of games won by the players of the current set.
* Player: This class represents an actual player in the field with a probability (```winnable```) of making good swings to the ball or high speed to reach where it it going to fall.
* Ball: A simple class who keeps track if the player hit it with a lot of force making a slam, and the direction of the court it it going to fall.
* Court: Represent half court keeping information of the 4 parts differentiated in this project (the ad area, deuce area, left backcourt and right backcourt). It also has a method to calculate the time needed by a player to reach a ball in a different section that it is in.


#### Some key points to note.
- The ```winnable``` percentage of each player determines his speed and probability of hitting the ball and making slams.
- The speed needed by the player to reach the ball, calculated by the Court class, has an aditional small random value to simulate some unexpected time needed, like response time, friction with ground or awareness.
- This is a discrte-event simulation where all the important entities (Player, Ball, Court) were modeled as classes to (in a future) be able to graphic in a GUI easily. If you need to, you can create an abtsract class (let's say ```Entity```) to be extended by this classes and requiere a method like ```draw()``` to display each entity in a grapich manner to the user.

### Output
This potject works in the command line, it (*at the moment*) doesn't have a GUI so let me explain what you will see when a simulation starts.
- In every set, will print the games it has simulated for it.
- In every game output, it will print the number of the game (*within the set*), the server (*who is the same in all the set*) and how the game evolved. It will finish the output of the game with the winner.
- In some cases, the game will enter in **deuce**, so it will print when that moment starts.
- After every set, it will print the winner of the set and the status of the full game so far.
- In some cases, the set will enter in **tie break**, so it will print when that moment starts.
- In the last set, the winner of it will be printed with the satus and points of the full game adding the tournament name and final winner of mentioned tournament.  

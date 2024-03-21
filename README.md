# a SMELLY (Simple Material Evaluation, Low Level plaYer) chess AI
this code is for a class project. to install this on your local machine, 
first pull it from the repository in a directory of your choosing.
```shell
git clone https://github.com/jadeddelta/Chess.git
```
development on this program uses IntelliJ, so developing this in the IntelliJ 
IDE is recommended, as the /.idea/ directory has the build configuration .xml file. 

if you want to straight up run the program without accessing the IntelliJ IDE, 
a file exists already in the /release/ folder, as the jar file is small enough to be run.
(note that the compiler specifically targets java 21, so your JRE must be >=java 21)
```shell
cd Chess 
java -jar release/Chess.jar
```

if you would rather not, the repo comes with an IntelliJ run configuration that will automatically 
run the src/Main.java file. just make sure you set your SDK's version is >= 21. after that, you'll be 
put into the main game loop where you can play against SMELLY, using a minimax search function w/
alpha-beta pruning @ depth level 4.

-------
## code note references 

- 1: pawn
- 2: knight
- 3: bishop
- 4: rook
- 5: queen
- 6: king

in case we need this mapping (but in intellij, this 
does not work as chess pieces aren't monospaced):
```
switch (piece) {
    case 1 -> sb.append("♙");
    case 2 -> sb.append("♘");
    case 3 -> sb.append("♗");
    case 4 -> sb.append("♖");
    case 5 -> sb.append("♕");
    case 6 -> sb.append("♔");
    case -1 -> sb.append("♟");
    case -2 -> sb.append("♞");
    case -3 -> sb.append("♝");
    case -4 -> sb.append("♜");
    case -5 -> sb.append("♛");
    case -6 -> sb.append("♚");
}
```
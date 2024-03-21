# a SMELLY (Simple Material Evaluation, Low Level plaYer) chess AI
this code is for a class project. to install this on your local machine, 
first pull it from the repository in a directory of your choosing.
```shell
git clone https://github.com/jadeddelta/Chess.git
```
development on this program uses IntelliJ, so developing this in the IntelliJ 
IDE is recommended, as the /.idea/ directory has the build configuration .xml file. 

if you want to straight up run the program, a file exists already in the /release/ folder,
as the jar file is small enough to be run. 

-------
code note references

- 1: pawn
- 2: knight
- 3: bishop
- 4: rook
- 5: queen
- 6: king

any classes prefixed with "M" was originally meant for mailbox implementation.

all psvm methods are just for debugging 

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
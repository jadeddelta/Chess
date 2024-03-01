# SMELLY ENGINE 
hi prof, in case you're reading this, thank you. this is a separate 
version of the repository from my local machine that contains the 
full, updated code. there are bugs! there are little things that 
aren't fully implemented! but as of 2024-02-29 ~8pm EST, this is the best
version of SMELLY that i've been able to make within the past 6 weeks.

hopefully i can transfer this code over to whichever group i am moved over
towards. i feel like with the code base that i have, and the fact that 
i've grown very much attached to SMELLY, i'd probably still work on it 
just so that we have a point of comparison between SMELLY and other chess 
engines that we happen to make. :)

either way, thank you so much for being understanding of the situation. it's 
been a pretty harsh start of the semester, so hopefully everything going forward
will be very smooth.

everything below this line is the original readme

-------

for reference:
- 1: pawn
- 2: knight
- 3: bishop
- 4: rook
- 5: queen
- 6: king

any classes prefixed with "M" was originally meant for mailbox implementation.
i think it would be a great idea if we could see what specific methods we would 
need in order to abstract a base class per major part (evaluation, engine, search function, etc...)
so we can take advantage of polymorphism and quickly swap between components

all psvm methods are just for debugging :3

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
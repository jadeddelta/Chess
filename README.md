# SMELLY ENGINE 


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
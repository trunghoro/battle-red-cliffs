# Battle of Red Cliffs

Java program that finds one minimum-length attack sequence to destroy Cao
Cao's fleet.

## Rules

- The board is rectangular, with at most 6 rows and 6 columns.
- `0` is an empty cell; `1..4` is a warship's HP.
- Each direct attack reduces one living warship's HP by 1.
- At 0 HP, the warship explodes and damages the first living warship by 1 in
  each direction: up, down, left, and right.
- A warship reduced to 0 HP by an explosion also explodes, continuing the chain
  reaction.
- Destroyed warships do not block fire.

## Implementation

- `BattleBoard`: validates the board and processes attacks and explosions.
- `Solver`: uses BFS to find a shortest attack sequence.
- `Node`: stores state and parent information.
- `Attack`: stores an attacked position using one-based output coordinates.
- `Main`: defines the sample board and prints the solution.

Change the `input` matrix in `Main.java` to test another board.

## Run

Requires Java 17 or later. From the project root:

```powershell
javac -d bin src\*.java
java -cp bin Main
```

> BFS guarantees a shortest solution but may require too much time or memory
> for a dense 6-by-6 board because the state space grows exponentially.

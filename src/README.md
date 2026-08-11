# Battle of Red Cliffs

This project finds attack sequences that destroy Cao Cao's entire fleet.
The current implementation uses breadth-first search (BFS), so it returns one
attack sequence with the minimum number of attacks.

## Game rules

- The fleet is placed on a rectangular grid.
- The grid has between `1` and `6` rows and columns.
- `0` represents an empty cell.
- A value from `1` to `4` represents a warship and its initial health points.
- Each turn, one living warship can be attacked.
- An attack reduces that ship's health by `1`.
- A ship with `0` health explodes.
- An explosion spreads up, down, left, and right.
- The first living ship found in each direction loses `1` health.
- A ship destroyed by the explosion creates another explosion.
- Empty cells and already destroyed ships are skipped while searching in a direction.

## Algorithm

The program represents a state by the current health of every warship.

For every state, the solver tries attacking each living warship and creates a
new state. A queue processes states level by level:

```text
Initial state
    -> try every possible attack
    -> process explosion chains
    -> create the next states
    -> stop after reaching a fully destroyed fleet
```

Because every direct attack has the same cost (`1`), BFS explores states in
increasing attack count. The first successful level is therefore optimal.
The `solve` method stops when BFS reaches the first goal. Because BFS explores
states by increasing attack count, that first solution is optimal. Other
solutions with the same length are not enumerated.

## Source files

### `Main.java`

Creates the sample board, runs the solver, and prints one optimal solution.
The board is currently declared directly in the source:

```java
int[][] input = {
        {1, 0},
        {2, 2}
};
```

Change this matrix to test another board.

### `BattleBoard.java`

Responsible for:

- Validating the board dimensions and health values.
- Mapping each warship to an internal index.
- Applying direct attacks.
- Processing explosion chain reactions.
- Checking whether all warships are destroyed.

### `Solver.java`

Performs BFS over health states and returns one shortest attack sequence.

### `Attack.java`

Stores a row and column for one attack. Its `toString()` method displays
coordinates using one-based numbering for users.

## Build and run

From the project root:

```bash
javac -d bin src/*.java
java -cp bin Main
```

On Windows PowerShell, the equivalent commands are:

```powershell
javac -d bin src\*.java
java -cp bin Main
```

## Example output

For the current board, the program starts with:

```text
Minimum attacks: 3
Attack 1: (1, 1)
Attack 2: (2, 1)
Attack 3: (2, 2)
```

The program prints one optimal sequence. Other sequences with the same length
may also exist, but they are not enumerated.

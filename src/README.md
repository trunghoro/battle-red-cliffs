# Battle of Red Cliffs

Find one shortest attack sequence that destroys Cao Cao's fleet.

## Rules

- Grid size: `1..6` rows and columns.
- `0` is an empty cell; `1..4` is a ship's HP.
- Each attack reduces one living ship's HP by `1`.
- A ship reaching `0` HP explodes.
- Fire spreads up, down, left, and right.
- The first living ship in each direction loses `1` HP.
- Destroyed ships can trigger a chain reaction.

## Algorithm

`Solver` uses BFS to find one optimal sequence:

- `byte[]` stores the compact HP state.
- `Queue` stores states waiting to be searched.
- `visited` removes duplicate states.
- `Node` stores parent information for reconstruction.
- BFS stops at the first goal, which guarantees the fewest attacks.

`BattleBoard` handles attacks and explosion chains.

## Run

The sample board is defined in `Main.java`.

```bash
javac -d bin src/*.java
java -cp bin Main
```

Example output:

```text
Minimum attacks: 2
Attack 1: (1, 2)
Attack 2: (3, 2)
```

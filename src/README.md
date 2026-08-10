# Battle of Red Cliffs

The program uses breadth-first search to find an optimal sequence: the fewest
direct attacks required to destroy every warship.

## Input

The first line contains `n m` (`1 <= n, m <= 6`). The next `n` lines contain
`m` integers. `0` is an empty cell; `1..4` is a warship's health.

Example:

```text
4 5
0 3 0 0 2
0 2 0 0 1
1 1 2 0 2
0 1 0 0 0
```

## Run

```text
javac -d bin src/*.java
java -cp bin Main
```

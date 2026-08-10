public class Main {

    public static void main(String[] args) {

        int[][] input = {
            {1, 3, 0, 0, 2},
            {0, 2, 0, 0, 1},
            {4, 4, 2, 0, 2},
            {0, 1, 0, 0, 0}
        };

        BattleBoard board =
                new BattleBoard(input);

        byte[] initialHp =
                board.createInitialHp(input);

        Solver solver =
                new Solver(board);

        var solution =
                solver.solve(initialHp);

        System.out.println(
                "Minimum attacks: "
                + solution.size()
        );

        for (int i = 0;
             i < solution.size();
             i++) {

            System.out.println(
                    "Attack "
                    + (i + 1)
                    + ": "
                    + solution.get(i)
            );
        }
    }
}
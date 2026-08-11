import java.util.List;

public class Main {

        /**
         * Creates the board, finds one shortest attack sequence, and prints it.
         *
         * @param args command-line arguments; not used by the program
         */
        public static void main(String[] args) {

                // A positive value represents a ship and its initial health.
                int[][] input = {
                                { 0, 3, 0, 0, 2 },
                                { 0, 2, 0, 0, 1 },
                                { 1, 1, 2, 0, 2 },
                                { 0, 1, 0, 0, 0 }
                };

                BattleBoard board = new BattleBoard(input);
                byte[] initialHp = board.createInitialHp();

                // BFS returns one optimal solution and stops at the first goal.
                Solver solver = new Solver(board);
                List<Attack> solution = solver.solve(initialHp);

                System.out.println(
                                "Minimum attacks: ".concat(
                                                String.valueOf(solution.size())));

                for (int i = 0; i < solution.size(); i++) {
                        System.out.println(
                                        "Attack ".concat(String.valueOf(i + 1))
                                                        .concat(": ")
                                                        .concat(solution.get(i).toString()));
                }
        }
}

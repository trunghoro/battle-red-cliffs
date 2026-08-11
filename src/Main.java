import java.util.List;

public class Main {

        /**
         * Creates the board, finds the shortest attack sequence, and prints it.
         *
         * @param args command-line arguments; not used by the program
         */
        public static void main(String[] args) {

                // A positive value represents a ship and its initial health.
                int[][] input = {
                                { 1, 0 },
                                { 2, 2 }
                };

                // Convert the input matrix into the board representation.
                BattleBoard board = new BattleBoard(input);

                // Read the initial health values in internal ship-index order.
                byte[] initialHp = board.createInitialHp();

                // Solver uses BFS to find the minimum number of attacks.
                Solver solver = new Solver(board);
                List<List<Attack>> solutions = solver.solveAll(initialHp);
                int minimumAttacks = solutions.isEmpty()
                                ? 0
                                : solutions.get(0).size();

                System.out.println(
                                "Minimum attacks: ".concat(
                                                String.valueOf(minimumAttacks)));

                System.out.println(
                                "Optimal solutions: ".concat(
                                                String.valueOf(solutions.size())));

                for (int solutionIndex = 0; solutionIndex < solutions.size(); solutionIndex++) {
                        System.out.println("Solution ".concat(
                                        String.valueOf(solutionIndex + 1)).concat(":"));

                        List<Attack> solution = solutions.get(solutionIndex);
                        for (int attackIndex = 0; attackIndex < solution.size(); attackIndex++) {
                                System.out.println(
                                                "Attack ".concat(String.valueOf(attackIndex + 1))
                                                                .concat(": ")
                                                                .concat(solution.get(attackIndex).toString()));
                        }
                }
        }
}

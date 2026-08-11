public record Attack(int row, int col) {

    /**
     * Formats the attack position using one-based row and column numbers.
     *
     * @return the formatted attack position
     */
    @Override
    public String toString() {
        return "(".concat(String.valueOf(row + 1))
                .concat(", ")
                .concat(String.valueOf(col + 1))
                .concat(")");
    }

}

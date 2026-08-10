
public record Attack(int row, int col) {

    @Override
    public String toString() {
        return "(".concat(String.valueOf(row + 1))
                .concat(", ")
                .concat(String.valueOf(col + 1))
                .concat(")");
    }

}

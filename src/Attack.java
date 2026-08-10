
public record Attack(int row, int col) {

    @Override
    public String toString() {
        return "(" + (row + 1) + ", " + (col + 1) + ")";
    }

}
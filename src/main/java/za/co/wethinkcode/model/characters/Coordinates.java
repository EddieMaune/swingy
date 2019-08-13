package za.co.wethinkcode.model.characters;

import lombok.Data;

@Data
public class Coordinates {
    int row;
    int column;
    int limit;

    public Coordinates (int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Coordinates  setRow(int row) {
        this.row = row;
        return this;
    }

    public Coordinates setColumn(int column) {
        this.column = column;
        return this;
    }
}

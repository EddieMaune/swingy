package za.co.wethinkcode.model;

import lombok.Data;
import za.co.wethinkcode.model.characters.Character;

import java.util.List;

@Data
public class Game {
    private char [][] map;
    private int size;

    public Game(int level) {
        size = (level - 1) * 5 + 10 - (level % 2);
        map = new char[size][size];
    }
}

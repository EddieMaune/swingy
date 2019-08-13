package za.co.wethinkcode.model.Artefacts;

import lombok.Data;

@Data
public class Armor implements Artefact {
    private String name;
    private int power;
    private String type;

    public Armor(int luck, int level) {
        type = "Armor";
        if (luck % 8 == 0) {
            name = "Vibranium Suit";
            power = 10 * level;
        } else if (luck % 3 == 0) {
            name = "Shield";
            power = 4 * level;
        }
        else {
            name = "Leather Suit";
            power = 2 * level;
        }
    }

    public Armor(String name) {
        this.name = name;
        type = "Armor";
        power = 10;
    }
}

package za.co.wethinkcode.model.Artefacts;

import lombok.Data;

@Data
public class Weapon implements Artefact {

    private String name;
    private int power;
    private String type;

    public Weapon(int luck, int level) {
        type = "Weapon";
        if (luck % 8 == 0) {
            name = "Dragon Slayer";
            power = 10 * level;
        } else if (luck % 3 == 0) {
            name = "Mjolnir";
            power = 4 * level;
        }
        else {
            name = "Katana";
            power = 2 * level;
        }
    }

    public Weapon(String name) {
        this.name = name;
        type = "Weapon";
        power = 10;
    }
}

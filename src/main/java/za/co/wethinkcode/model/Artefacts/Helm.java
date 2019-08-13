package za.co.wethinkcode.model.Artefacts;

import lombok.Data;

@Data
public class Helm implements Artefact{
    private String name;
    private int power;
    private String type;

    public Helm(int luck, int level) {
        type = "Helm";
        if (luck % 8 == 0) {
            name = "Senzu bean";
            power = 100 * level;
        } else if (luck % 3 == 0) {
            name = "Health Potion";
            power = 40 * level;
        }
        else {
            name = "Panado";
            power = 20 * level;
        }
    }

    public  Helm(String name) {
        this.name = name;
    }
}

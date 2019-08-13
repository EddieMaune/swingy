package za.co.wethinkcode.controller;

import org.apache.commons.lang3.StringUtils;
import za.co.wethinkcode.model.Artefacts.Artefact;
import za.co.wethinkcode.model.Artefacts.ArtefactFactory;
import za.co.wethinkcode.model.Game;
import za.co.wethinkcode.model.characters.*;
import za.co.wethinkcode.view.cli.Console;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Thread.sleep;

public class GameController {

    private Console console;
    public static boolean inputGiven = false;
    private String consoleInput;
    private HeroBuilder heroBuilder;
    private Hero hero;
    private Coordinates heroPreviousPosition;
    private Villain[][] villains;
    private Game game;
    private Connection connection;
    private boolean heroExist;
    private int nextLevelXp;
    private Artefact artefact;
    private boolean heroWon;

    public GameController(Console c, Connection connection) {
        heroExist = false;
        console = c;
        this.connection = connection;
        heroBuilder = new HeroBuilder();
        nextLevelXp = 0;
        mainMenu();
    }

    public void play() {
        String selectedOption;
        String currentView;
        currentView = "MAIN MENU";
        try {
            while (true) {
                sleep(0);
                if (inputGiven) {
                    consoleInput = console.getInput();
                    selectedOption = null;
                    if (viewIsMenu(console.getTextPaneContent())) {
                        selectedOption = getSelectedOption();
                    }
                    if (selectedOption != null) {
                        if (currentView.equals("SELECT HERO")) {
                            Statement statement = connection.createStatement();
                            ResultSet rs = statement.executeQuery("select * from heroes where name = '" + selectedOption + "'");
                            while (rs.next()) {
                                hero = heroBuilder.build(rs);

                                heroExist = true;
                                if (beanValidates(hero))
                                    startGame();
                                break;
                            }
                        }
                        selectedOption = selectedOption.toUpperCase();
                        switch (selectedOption) {
                            case "MAIN MENU":
                                mainMenu();
                                break;
                            case "SELECT HERO":
                                currentView = selectedOption;
                                selectHero(currentView);
                                break;
                            case "CREATE HERO":
                                currentView = selectedOption;
                                createHero(currentView);
                                break;
                            case "BACK":
                                navigateBack(currentView);
                                break;
                            case "KNIGHT":
                                buildHero("Knight");
                                break;
                            case "ELF":
                                buildHero("Elf");
                                break;
                            case "DWARF":
                                buildHero("Dwarf");
                                break;
                            case "START GAME":
                                currentView = "GAME";
                                startGame();
                                break;
                            case "NORTH":
                                moveHero("N");
                                break;
                            case "EAST":
                                moveHero("E");
                                break;
                            case "SOUTH":
                                moveHero("S");
                                break;
                            case "WEST":
                                moveHero("W");
                                break;
                            case "VIEW STATS":
                                currentView = "HERO STATS";
                                viewStats();
                                break;
                            case "FIGHT":
                                fight();
                                break;
                            case "RUN":
                                escape();
                                break;
                            case "VILLAIN STATS":
                                currentView = "VILLAIN STATS";
                                viewVillainStats();
                                break;
                            case "SEARCH CORPSE":
                                searchCorpse();
                                break;
                            case "PICK UP":
                                pickUpArtefact();
                                break;
                            case "RESUME QUEST":
                                if (heroWon) {
                                    heroWon = false;
                                    startGame();
                                } else
                                    resumeGame();
                                break;
                            case "QUIT":
                                quit();
                                break;
                        }
                        inputGiven = false;
                        continue;
                    }
                    if (console.getMenuTitle().getText()
                            .equals(console.getView("CREATE HERO").get("menuTitle"))) {
                        currentView = "SELECT CLASS";
                        heroBuilder.setName(consoleInput);
                        selectHeroClass("SELECT CLASS");
                    }
                    inputGiven = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pickUpArtefact() {
        if (hero.getArtefact() == null) {
            hero.setArtefact(artefact);
            switch (hero.getArtefact().getType()) {
                case "Weapon":
                    hero.setAttack(hero.getAttack() + artefact.getPower());
                    break;
                case "Armor":
                    hero.setDefense(hero.getDefense() + artefact.getPower());
                    break;
                case "Helm":
                    hero.setHp(hero.getHp() + artefact.getPower());
                    hero.setArtefact(null);
                    break;
            }
        } else {
            switch (hero.getArtefact().getType()) {
                case "Weapon":
                    hero.setAttack(hero.getAttack() - hero.getArtefact().getPower());
                    hero.setArtefact(null);
                    break;
                case "Armor":
                    hero.setDefense(hero.getDefense() - hero.getArtefact().getPower());
                    hero.setArtefact(null);
                    break;
            }
            switch (artefact.getType()) {
                case "Weapon":
                    hero.setAttack(hero.getAttack() + artefact.getPower());
                    hero.setArtefact(artefact);
                    break;
                case "Armor":
                    hero.setDefense(hero.getDefense() + artefact.getPower());
                    hero.setArtefact(artefact);
                    break;
                case "Helm":
                    hero.setHp(hero.getHp() + artefact.getPower());
                    break;
            }
        }
        resumeGame();
    }

    private void searchCorpse() {

        if ((int) (Math.random() * 10) % 2 == 0) {
            artefact = ArtefactFactory.getArtefact((int) (Math.random() * 10), hero.getLevel() + 1);
            console.getMenuTitle().setText("YOU FOUND A " + artefact.getType().toUpperCase() + "!");
            console.getMenu().setText(String.format("The %s you found is a %s. You can pick it up or resume quest.\n(1)\tPick Up\n(2)\tResume Quest",
                    artefact.getType(), artefact.getName()));
        } else {
            console.getMenuTitle().setText("YOU FOUND NOTHING");
            console.getMenu().setText("(1)\tResume Quest\n(2)\tQuit");
        }
    }

    public void mainMenu() {
        console.normalMode();
        console.getMenuTitle().setText(console.getView("MAIN MENU").get("menuTitle"));
        console.getMenu().setText(console.getView("MAIN MENU").get("body"));
    }

    private void selectHero(String view) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from heroes");
        int i = 1;

        while (rs.next()) {
            // read the result set
            stringBuilder.append(String.format("(%d)\t%s\n", rs.getInt("id"), rs.getString("name")));
            i = rs.getInt("id") + 1;
        }
        console.getMenuTitle().setText(console.getView(view).get("menuTitle"));
        console.getMenu().setText(stringBuilder.toString() + "(" + i + ")\tBack");
    }

    private boolean viewIsMenu(String[] options) {
        for (String option : options) {
            if (option.matches("\\(\\d\\)\t[\\w\\s]+"))
                return true;
        }
        return false;
    }

    private void navigateBack(String view) {
        console.getMenuTitle().setText(console.getView(view).get("previousMenuTitle"));
        console.getMenu().setText(console.getView(view).get("previous"));
    }

    private void createHero(String view) {
        console.getMenuTitle().setText(console.getView(view).get("menuTitle"));
        console.getMenu().setText(console.getView(view).get("body"));
    }

    private void selectHeroClass(String view) {
        console.getMenuTitle().setText(console.getView(view).get("menuTitle"));
        console.getMenu().setText(console.getView(view).get("body"));
    }

    private String getSelectedOption() {
        String[] tokens;
        for (String option : console.getTextPaneContent()) {
            tokens = option.split("\t");
            if (tokens.length > 1) {
                if (tokens[0].replaceAll("[()]+", "").equals(consoleInput)) {
                    return tokens[1];
                } else if (tokens[1].toUpperCase().equals(consoleInput.toUpperCase())) {
                    return consoleInput;
                }
            }
        }
        return null;
    }

    private void buildHero(String heroClass) {
        heroBuilder.setHeroClass(heroClass);
        hero = heroBuilder.build();

       // beanValidation(hero);
        console.getMenuTitle().setText("HERO STATS");
        console.getMenu().setText(console.getHeroStats(hero) + "(1)\tStart Game\n(2)\tBack");
    }

    private void createMap(int level) {
        game = new Game(level);

    }

    private void populateMap() {
        villains = new Villain[game.getSize()][game.getSize()];
        hero.getPosition().setRow(game.getSize() / 2).setColumn(game.getSize() / 2);
        game.getMap()[hero.getPosition().getRow()][hero.getPosition().getColumn()] = 'H';
        heroPreviousPosition = new Coordinates(hero.getPosition().getRow(), hero.getPosition().getColumn());
        for (int y = 0; y < game.getSize(); y++) {
            for (int x = 0; x < game.getSize(); x++) {
                if ((int) (Math.random() * 10) % (game.getSize()) / 2 == 0) {
                    game.getMap()[y][x] = 'V';
                    villains[y][x] = VillainFactory.createVillain((int) (Math.random() * 10), hero.getLevel() + 1);
                }
            }
        }
    }

    private void startGame() {
        createMap(hero.getLevel());
        populateMap();
        console.getMenuTitle().setText(console.getView("GAME").get("menuTitle"));
        console.getMenu().setText(console.getView("GAME").get("body"));
    }

    private void resumeGame() {
        if (hero.getXp() >= nextLevelXp && nextLevelXp > 0) {
            startGame();
        } else {
            console.getMenuTitle().setText(console.getView("GAME").get("menuTitle"));
            console.getMenu().setText(console.getView("GAME").get("body"));
        }
    }

    private void win() {
        heroWon = true;
        console.getMenuTitle().setText(console.getView("YOU WIN").get("menuTitle"));
        console.getMenu().setText(console.getView("YOU WIN").get("body"));
    }

    private void moveHero(String direction) {
        heroPreviousPosition.setRow(hero.getPosition().getRow());
        heroPreviousPosition.setColumn(hero.getPosition().getColumn());
        switch (direction) {
            case "N":
                game.getMap()[hero.getPosition().getRow()][hero.getPosition().getColumn()] = '\0';
                hero.getPosition().setRow(hero.getPosition().getRow() - 1);
                if (hero.getPosition().getRow() < 0) {
                    win();
                } else if (game.getMap()[hero.getPosition().getRow()][hero.getPosition().getColumn()] == 'V')
                    fightOrFlight();
                else
                    game.getMap()[hero.getPosition().getRow()][hero.getPosition().getColumn()] = 'H';
                break;
            case "E":
                game.getMap()[hero.getPosition().getRow()][hero.getPosition().getColumn()] = '\0';
                hero.getPosition().setColumn(hero.getPosition().getColumn() + 1);
                if (hero.getPosition().getColumn() > game.getSize() - 1) {
                    win();
                } else if (game.getMap()[hero.getPosition().getRow()][hero.getPosition().getColumn()] == 'V')
                    fightOrFlight();
                else
                    game.getMap()[hero.getPosition().getRow()][hero.getPosition().getColumn()] = 'H';
                break;
            case "S":
                game.getMap()[hero.getPosition().getRow()][hero.getPosition().getColumn()] = '\0';
                hero.getPosition().setRow(hero.getPosition().getRow() + 1);
                if (hero.getPosition().getRow() > game.getSize() - 1) {
                    win();
                } else if (game.getMap()[hero.getPosition().getRow()][hero.getPosition().getColumn()] == 'V')
                    fightOrFlight();
                else
                    game.getMap()[hero.getPosition().getRow()][hero.getPosition().getColumn()] = 'H';
                break;
            case "W":
                game.getMap()[hero.getPosition().getRow()][hero.getPosition().getColumn()] = '\0';
                hero.getPosition().setColumn(hero.getPosition().getColumn() - 1);
                if (hero.getPosition().getColumn() < 0) {
                    win();
                } else if (game.getMap()[hero.getPosition().getRow()][hero.getPosition().getColumn()] == 'V')
                    fightOrFlight();
                else
                    game.getMap()[hero.getPosition().getRow()][hero.getPosition().getColumn()] = 'H';
                break;
        }
        printBoard();
    }

    private void fightOrFlight() {
        console.getMenu().setText(console.getView("FIGHT OR FLIGHT").get("body"));
        console.getMenuTitle().setText(console.getView("FIGHT OR FLIGHT").get("menuTitle"));
    }

    private void fight() {
        int turns = 0;
        int goodLuck;

        Villain opponent = villains[hero.getPosition().getRow()][hero.getPosition().getColumn()];
        while (hero.getHp() > 0 && opponent.getHp() > 0) {
            goodLuck = (int) (Math.random() * 7);
           // System.out.format("Hero Life = %d ---- Villain Life = %d\nHero Attack = %d --- Villain Attack = %d\nHero Defense = %d --- Villain Defense = %d\nDamage = %d\n",
             //       hero.getHp(), opponent.getHp(), hero.getAttack(), opponent.getAttack(), hero.getDefense(), opponent.getDefense(), goodLuck + hero.getAttack() - opponent.getDefense());
            opponent.setHp(opponent.getHp() - hero.getAttack() + (opponent.getDefense() > hero.getAttack() ? hero.getAttack() : opponent.getDefense()) - goodLuck);
            hero.setHp(hero.getHp() - opponent.getAttack() + (hero.getDefense() > opponent.getAttack() ? opponent.getAttack() : hero.getDefense()));
            turns++;
        }
        //System.out.println("hero HP = " + hero.getHp());
        if (hero.getHp() <= 0) {
            console.getMenuTitle().setText("GAME OVER, YOU DIED!");
            console.getMenu().setText("(1)\tMAIN MENU\n(2)\tQuit");

        } else {
            hero.setHp(100 - (turns / 2));
            game.getMap()[hero.getPosition().getRow()][hero.getPosition().getColumn()] = 'H';
            console.getMenuTitle().setText("YOU WON THE BATTLE!");
            console.getMenu().setText("(1)\tSearch Corpse\n(2)\tResume Quest");
            hero.setXp(hero.getXp() + turns * 10);
            levelUp();
        }
        printBoard();
    }

    private void levelUp() {

        nextLevelXp = (hero.getLevel() + 1) * 1000 + (int) Math.pow(hero.getLevel(), 2) * 450;
        if (hero.getXp() >= nextLevelXp) {
            console.getMenuTitle().setText("YOU WON THE BATTLE AND LEVELED UP!");
            hero.setLevel(hero.getLevel() + 1);
            hero.setHp(100 * (1 + hero.getLevel()));
            hero.setAttack(hero.getAttack() + 10);
            hero.setDefense(hero.getDefense() + 10);
        }
    }

    private void escape() {
        hero.getPosition().setRow(heroPreviousPosition.getRow());
        hero.getPosition().setColumn(heroPreviousPosition.getColumn());
        game.getMap()[hero.getPosition().getRow()][hero.getPosition().getColumn()] = 'H';
        resumeGame();
        printBoard();
    }

    private void printBoard() {
        //System.out.println("\n");
        for (int y = 0; y < game.getSize(); y++) {
            for (int x = 0; x < game.getSize(); x++) {
          //      System.out.print(game.getMap()[y][x]);
            }
            //System.out.print("\n");
        }
    }

    private void viewStats() {
        console.getMenuTitle().setText("HERO STATS");
        console.getMenu().setText(console.getHeroStats(hero) + "(1)\tBack");
    }

    private void viewVillainStats() {
        console.getMenuTitle().setText("VILLAIN STATS");
        console.getMenu().setText(console.getVillainStats(villains[hero.getPosition().getRow()][hero.getPosition().getColumn()]) + "(1)\tBack");
    }

    private void quit() throws SQLException {
        Statement statement;
        if (hero != null && !heroExist) {
            statement = connection.createStatement();
            statement.executeUpdate(String.format("insert into heroes (name, class, hp, attack, defense, xp, level, artefact) values('%s', '%s', %d, %d, %d, %d, %d, '%s')",
                    hero.getName(), hero.getHeroClass(), hero.getHp(), hero.getAttack(), hero.getDefense(), hero.getXp(), hero.getLevel(), (hero.getArtefact() == null ? "none" : hero.getArtefact().getName())));

        } else if (hero != null && hero.getHp() > 0) {
            PreparedStatement pstmt = connection.prepareStatement("update heroes set hp = ?, attack = ?, defense = ?, xp = ?, level = ?, artefact = ? where id = ?");

            pstmt.setInt(1, hero.getHp());
            pstmt.setInt(2, hero.getAttack());
            pstmt.setInt(3, hero.getDefense());
            pstmt.setInt(4, hero.getXp());
            pstmt.setInt(5, hero.getLevel());
            pstmt.setString(6, (hero.getArtefact() == null ? "none" : hero.getArtefact().getName()));
            pstmt.setInt(7, hero.getId());
            pstmt.executeUpdate();
        }
        console.getFrame().dispose();
        System.exit(0);
    }

    private boolean beanValidates(Hero hero) {
        ValidatorFactory validatorFactory = Validation
                .buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        Set<ConstraintViolation<Hero>> constraintViolations = validator.validate(hero);

        if (constraintViolations.size() > 0) {
            console.getMenuTitle().setText("YOUR HERO RECORD IS CORRUPTED");
            Set<String> violationMessages = new HashSet<String>();

            for (ConstraintViolation<Hero> constraintViolation : constraintViolations) {
                violationMessages.add(constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage());
            }
            console.errorMode();
            console.getMenu().setText(StringUtils.join(violationMessages, "\n") + "\n\n(1)\tMain Menu\n(2)\tQuit");
            return false;
        }
        return true;
    }
}

package za.co.wethinkcode.view.cli;

import sun.java2d.pipe.SpanShapeRenderer;
import za.co.wethinkcode.controller.GameController;
import za.co.wethinkcode.model.characters.Hero;
import za.co.wethinkcode.model.characters.Villain;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

public class Console extends JFrame {
    private JPanel header;
    private JTextPane tp;
    private JPanel body;
    private JTextPane bodyTextPane;
    private JTextField textField;
    private JTextField menuTitle;
    private JTextField prompt;
    private Map<String, Map<String, String>> views;
    private static String input;

    public Console(String title) {
        super(title);
        createViews();
        setBounds(60, 60, 500, 500);
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.setBackground(Color.BLACK);
        container.add(generateTitle(), BorderLayout.NORTH);
        container.add(generateBody(), BorderLayout.CENTER);


//        System.setOut(new PrintStream(new OutputStream() {
//            @Override
//            public void write(int b) throws IOException {
//                ///     textArea.append(String.valueOf((char) b));
//            }
//        }));
        getContentPane().setBackground(Color.BLACK);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().add(container);

        setVisible(true);
    }

    public JPanel generateTitle() {

        if (header == null) {
            header = new JPanel();
            header.setBackground(Color.BLACK);
            header.setLayout(new BorderLayout());
            tp = new JTextPane();
            tp.setText("\n" +
                    "              _                   \n" +
                    "             (_)                  \n" +
                    " _____      ___ _ __   __ _ _   _ \n" +
                    "/ __\\ \\ /\\ / / | '_ \\ / _` | | | |\n" +
                    "\\__ \\\\ V  V /| | | | | (_| | |_| |\n" +
                    "|___/ \\_/\\_/ |_|_| |_|\\__, |\\__, |\n" +
                    "                       __/ | __/ |\n" +
                    "                      |___/ |___/ \n");
            StyledDocument doc = tp.getStyledDocument();

            tp.setEditable(false);
            tp.setBackground(Color.BLACK);
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            StyleConstants.setBackground(center, Color.BLACK);
            StyleConstants.setForeground(center, Color.GREEN);
            tp.setOpaque(false);
            doc.setParagraphAttributes(0, doc.getLength(), center, false);
            tp.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            tp.setPreferredSize(new Dimension(20, 200));

            header.add(tp, BorderLayout.NORTH);
        }
        return header;
    }

    JPanel generateBody() {


        if (body == null ) {
            body = new JPanel();
            body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
            body.setBackground(Color.BLACK);
            menuTitle = new JTextField();
            if (bodyTextPane == null) {


                menuTitle.setForeground(Color.GREEN);
                menuTitle.setBackground(Color.BLACK);
                menuTitle.setBorder(null);
                menuTitle.setHorizontalAlignment(JTextField.CENTER);
                menuTitle.setText("OPTIONS");
                menuTitle.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
                menuTitle.setEditable(false);
                bodyTextPane = new JTextPane();
              //  bodyTextPane.setText("(1)\tSelect Hero" + "\n" + "(2)\tCreate Hero");
                StyledDocument doc = bodyTextPane.getStyledDocument();
                bodyTextPane.setBackground(Color.BLACK);
                bodyTextPane.setEditable(false);
                SimpleAttributeSet styles = new SimpleAttributeSet();
                StyleConstants.setForeground(styles, Color.GREEN);
                StyleConstants.setBackground(styles, Color.BLACK);
               // bodyTextPane.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                bodyTextPane.setOpaque(false);
                doc.setParagraphAttributes(0, doc.getLength(), styles, false);
                bodyTextPane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
                bodyTextPane.setPreferredSize(new Dimension(20, 200));
                bodyTextPane.setMaximumSize(new Dimension(200, 200));
                body.add(menuTitle);
                body.add(bodyTextPane);
                if (textField == null) {
                    prompt = new JTextField();
                    prompt.setBackground(Color.BLACK);
                    prompt.setForeground(Color.GREEN);
                    prompt.setText("Enter number or type out option.");
                    prompt.setHorizontalAlignment(JLabel.CENTER);
                    prompt.setBorder(null);
                    prompt.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
                    prompt.setEditable(false);
                    textField = new JTextField(40);
                    addWindowListener( new WindowAdapter() {
                        public void windowOpened( WindowEvent e ){
                            textField.requestFocus();
                        }
                    });
                    textField.setForeground(Color.GREEN);
                    textField.setBackground(Color.BLACK);
                    textField.setBorder(null);
                    textField.setHorizontalAlignment(JTextField.CENTER);
                    textField.setCaretColor(Color.GREEN);
                    textField.setCaret(new CustomCaret());
                    textField.getCaret().setBlinkRate(300);
                    textField.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
                    textField.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            input = textField.getText();
                            //bodyTextPane.setText(input + "INPUT");
                            GameController.inputGiven = true;
                            textField.setText("");
                        }
                    });
                }
                body.add(prompt);
                body.add(textField);
            }
        }
        return body;

    }

    private void createViews() {
        views = new HashMap<String, Map<String, String>>();

        views.put("MAIN MENU", new HashMap<String, String>());
        views.get("MAIN MENU").put("body", "(1)\tSelect Hero\n(2)\tCreate Hero\n(3)\tQuit");
        views.get("MAIN MENU").put("menuTitle", "OPTIONS");

        views.put("SELECT HERO", new HashMap<String, String>());
        views.get("SELECT HERO").put("body", "(1)\tJX" + "\n" + "(2)\tCap\n(3)\tBack");
        views.get("SELECT HERO").put("previous", views.get("MAIN MENU").get("body"));
        views.get("SELECT HERO").put("previousMenuTitle", views.get("MAIN MENU").get("menuTitle"));
        views.get("SELECT HERO").put("menuTitle", "THE FOLLOWING HEROES WERE SAVED:");

        views.put("CREATE HERO", new HashMap<String, String>());
        views.get("CREATE HERO").put("body", "(1)\tBack");
        views.get("CREATE HERO").put("menuTitle", "ENTER A NAME FOR YOUR HERO");
        views.get("CREATE HERO").put("previous", views.get("MAIN MENU").get("body"));
        views.get("CREATE HERO").put("previousMenuTitle", views.get("MAIN MENU").get("menuTitle"));

        views.put("SELECT CLASS", new HashMap<String, String>());
        views.get("SELECT CLASS").put("body", "(1)\tKnight\n(2)\tElf\n(3)\tDwarf\n(4)\tBack");
        views.get("SELECT CLASS").put("menuTitle", "SELECT A CLASS FOR YOUR HERO");
        views.get("SELECT CLASS").put("previous", views.get("CREATE HERO").get("body"));
        views.get("SELECT CLASS").put("previousMenuTitle", views.get("CREATE HERO").get("menuTitle"));

        views.put("HERO STATS", new HashMap<String, String>());
        views.get("HERO STATS").put("previous", views.get("MAIN MENU").get("body"));
        views.get("HERO STATS").put("previousMenuTitle", views.get("MAIN MENU").get("menuTitle"));

        views.put("GAME", new HashMap<String, String>());
        views.get("GAME").put("body", "(1)\tNorth\n(2)\tEast\n(3)\tSouth\n(4)\tWest\n(5)\tView Stats\n(6)\tQuit");
        views.get("GAME").put("menuTitle", "WHERE WOULD YOU LIKE TO GO?");

        views.put("HERO STATS", new HashMap<String, String>());
        views.get("HERO STATS").put("previousMenuTitle", views.get("GAME").get("menuTitle"));
        views.get("HERO STATS").put("previous", views.get("GAME").get("body"));

        views.put("FIGHT OR FLIGHT", new HashMap<String, String>());
        views.get("FIGHT OR FLIGHT").put("previousMenuTitle", views.get("GAME").get("menuTitle"));
        views.get("FIGHT OR FLIGHT").put("previous", views.get("GAME").get("body"));
        views.get("FIGHT OR FLIGHT").put("body", "(1)\tFight\n(2)\tRun\n(3)\tVillain Stats");
        views.get("FIGHT OR FLIGHT").put("menuTitle", "YOU HAVE ENCOUNTERED A VILLAIN");

        views.put("VILLAIN STATS", new HashMap<String, String>());
        views.get("VILLAIN STATS").put("previousMenuTitle", views.get("FIGHT OR FLIGHT").get("menuTitle"));
        views.get("VILLAIN STATS").put("previous", views.get("FIGHT OR FLIGHT").get("body"));

        views.put("YOU WIN", new HashMap<String, String>());
        views.get("YOU WIN").put("body", "(1)\tResume Quest\n(2)\tQuit");
        views.get("YOU WIN").put("menuTitle", "YOU WIN! YOU REACHED THE EDGE OF THE MAP.");
    }

    public Map<String, String> getView(String view) {
        return views.get(view);
    }

    public String getHeroStats(Hero hero) {
        return "Name\t-\t" + hero.getName() + "\n" +
                "Class\t-\t" + hero.getHeroClass() + "\n" +
                "Level\t-\t" + hero.getLevel() + "\n" +
                "XP\t-\t" + hero.getXp() + "\n" +
                "HP\t-\t" + hero.getHp() + "\n" +
                "Attack\t-\t" + hero.getAttack() + "\n" +
                "Defense\t-\t" + hero.getDefense() + "\n" +
                "Artefact\t-\t" + (hero.getArtefact() != null ? hero.getArtefact().getName() : "none") + "\n";
    }

    public String getVillainStats(Villain villain) {
        return "Class\t-\t" + villain.getVillainClass() + "\n" +
                "HP\t-\t" + villain.getHp() + "\n" +
                "Attack\t-\t" + villain.getAttack() + "\n" +
                "Defense\t-\t" + villain.getDefense() + "\n";
    }

    public String getInput() {
        return input;
    }

    public JTextField getMenuTitle() {
        return menuTitle;
    }

    public JTextField getPrompt() {
        return prompt;
    }

    public JTextPane getMenu() {
        return bodyTextPane;
    }

    public String[] getTextPaneContent() {
        return bodyTextPane.getText().split("\n");
    }

    public JFrame getFrame() {
        return this;
    }

    public void errorMode() {
        StyledDocument docMenu = bodyTextPane.getStyledDocument();
        StyledDocument docTitle = tp.getStyledDocument();
        SimpleAttributeSet styles = new SimpleAttributeSet();
        SimpleAttributeSet headerStyle = new SimpleAttributeSet();

        StyleConstants.setForeground(styles, Color.RED);
        StyleConstants.setForeground(headerStyle, Color.RED);
        docMenu.setParagraphAttributes(0, docMenu.getLength(), styles, false);
        docTitle.setParagraphAttributes(0, docTitle.getLength(), headerStyle, false);
        prompt.setForeground(Color.RED);
        textField.setForeground(Color.RED);
        textField.setCaretColor(Color.RED);
        getMenuTitle().setForeground(Color.RED);
    }

    public void normalMode() {
        StyledDocument docMenu = bodyTextPane.getStyledDocument();
        StyledDocument docTitle = tp.getStyledDocument();
        SimpleAttributeSet styles = new SimpleAttributeSet();
        SimpleAttributeSet headerStyle = new SimpleAttributeSet();

        StyleConstants.setForeground(styles, Color.GREEN);
        StyleConstants.setForeground(headerStyle, Color.GREEN);
        docMenu.setParagraphAttributes(0, docMenu.getLength(), styles, false);
        docTitle.setParagraphAttributes(0, docTitle.getLength(), headerStyle, false);
        prompt.setForeground(Color.GREEN);
        textField.setForeground(Color.GREEN);
        textField.setCaretColor(Color.GREEN);
        getMenuTitle().setForeground(Color.GREEN);
    }
}
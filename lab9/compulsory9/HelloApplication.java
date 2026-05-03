package lab9.compulsory9;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class HelloApplication extends Application
{
    private Maze labirint;
    private final Pane mazeContainer        = new Pane();
    private final Pane controlMazeContainer = new Pane();

    private GameState    gameState;
    private SharedMemory sharedMemory;
    private BunnyThread  bunnyThread;
    private List<RobotThread> robotThreads = new ArrayList<>();
    private Timeline     simulationTimeline;
    private final Pane   simulationPane = new Pane();

    private static final double CELL = 30.0;


    private static final Color COL_WALL   = Color.BLACK;
    private static final Color COL_FREE   = Color.WHITE;
    private static final Color COL_BUNNY  = Color.web("#00cc44");   // verde
    private static final Color COL_ROBOT  = Color.web("#cc2222");   // rosu
    private static final Color COL_EXIT   = Color.web("#ffdd00");   // galben
    private static final Color COL_STROKE = Color.web("#444444");

    private void deseneazaLabirintEditabil(Pane container)
    {
        container.getChildren().clear();
        double dim = 30.0;
        for (int i = 0; i < labirint.getN(); i++)
        {
            for (int j = 0; j < labirint.getM(); j++)
            {
                final int row = i, col = j;
                Celula celula = labirint.getCelula(i, j);

                Rectangle rect = new Rectangle(j * dim, i * dim, dim, dim);
                rect.setFill(celula.esteZid() ? Color.BLACK : Color.WHITE);
                rect.setStroke(Color.BLACK);
                rect.setStrokeWidth(1.0);
                rect.setSmooth(false);

                rect.setOnMouseClicked(event -> {
                    Celula c = labirint.getCelula(row, col);
                    c.setZid(!c.esteZid());
                    rect.setFill(c.esteZid() ? Color.BLACK : Color.WHITE);
                });
                container.getChildren().add(rect);
            }
        }
    }

    private boolean isTraversable(int startR, int startC, int endR, int endC)
    {
        int n = labirint.getN(), m = labirint.getM();
        if (labirint.getCelula(startR, startC).esteZid() ||
                labirint.getCelula(endR, endC).esteZid()) return false;

        boolean[][] visited = new boolean[n][m];
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startR, startC});
        visited[startR][startC] = true;
        int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            if (curr[0] == endR && curr[1] == endC) return true;
            for (int[] d : dirs) {
                int nr = curr[0]+d[0], nc = curr[1]+d[1];
                if (nr>=0 && nr<n && nc>=0 && nc<m && !visited[nr][nc]
                        && !labirint.getCelula(nr, nc).esteZid()) {
                    visited[nr][nc] = true;
                    queue.add(new int[]{nr, nc});
                }
            }
        }
        return false;
    }

    private void exportToPNG(Label resultLabel)
    {
        if (labirint == null || mazeContainer.getChildren().isEmpty()) {
            resultLabel.setText("Deseneaza labirintul intai");
            resultLabel.setStyle("-fx-text-fill: #ff6666;"); return;
        }
        try {
            SnapshotParameters sp = new SnapshotParameters();
            sp.setFill(Color.WHITE);
            WritableImage img  = mazeContainer.snapshot(sp, null);
            BufferedImage bImg = SwingFXUtils.fromFXImage(img, null);
            ImageIO.write(bImg, "png", new File("maze_export.png"));
            resultLabel.setStyle("-fx-text-fill: #aaffaa;");
        } catch (Exception ex) {
            resultLabel.setText("Eroare: " + ex.getMessage());
            resultLabel.setStyle("-fx-text-fill: #ff6666;");
        }
    }

    private void salveazaLabirint(Label resultLabel)
    {
        if (labirint == null) {
            resultLabel.setText("Nu avem labirint de salvat");
            resultLabel.setStyle("-fx-text-fill: #ff6666;"); return;
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("maze_save.ser"))) {
            oos.writeObject(labirint);
            resultLabel.setText("Salvat maze_save.ser");
            resultLabel.setStyle("-fx-text-fill: #aaffaa;");
        } catch (IOException ex) {
            resultLabel.setText("Eroare" + ex.getMessage());
            resultLabel.setStyle("-fx-text-fill: #ff6666;");
        }
    }

    private void incarcaLabirint(Label resultLabel)
    {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("maze_save.ser"))) {
            labirint = (Maze) ois.readObject();
            deseneazaLabirintEditabil(mazeContainer);
            resultLabel.setText("Incarcat" + labirint.getN() + "x" + labirint.getM());
            resultLabel.setStyle("-fx-text-fill: #aaffaa;");
        } catch (FileNotFoundException ex) {
            resultLabel.setText("Fisierul nu a fost gasit");
            resultLabel.setStyle("-fx-text-fill: #ff6666;");
        } catch (Exception ex) {
            resultLabel.setText("Eroare " + ex.getMessage());
            resultLabel.setStyle("-fx-text-fill: #ff6666;");
        }
    }

    // -----------------------------------------------------------------------
    //  Logica simulare (NOU)
    // -----------------------------------------------------------------------

    /** Deseneaza starea curenta a simularii in simulationPane. Rulat pe FX thread. */
    private void redrawSimulation()
    {
        if (gameState == null || labirint == null) return;
        simulationPane.getChildren().clear();

        int n = labirint.getN(), m = labirint.getM();
        int bunnyR = gameState.getBunnyRow(), bunnyC = gameState.getBunnyCol();
        int exitR  = gameState.getExitRow(),  exitC  = gameState.getExitCol();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                Rectangle rect = new Rectangle(j*CELL, i*CELL, CELL, CELL);
                rect.setStroke(COL_STROKE);
                rect.setStrokeWidth(0.8);
                rect.setSmooth(false);

                Color fill;
                if (labirint.getCelula(i, j).esteZid()) {
                    fill = COL_WALL;
                } else if (i == bunnyR && j == bunnyC) {
                    fill = COL_BUNNY;   // iepuras = verde
                } else if (i == exitR && j == exitC) {
                    fill = COL_EXIT;    // iesire = galben
                } else {
                    fill = COL_FREE;
                    // verificam daca e robot
                    for (int r = 0; r < gameState.getNumRobots(); r++) {
                        if (gameState.getRobotRow(r) == i && gameState.getRobotCol(r) == j) {
                            fill = COL_ROBOT; break;
                        }
                    }
                }
                rect.setFill(fill);
                simulationPane.getChildren().add(rect);
            }
        }
    }

    /** Opreste o simulare in curs (daca exista). */
    private void stopSimulation()
    {
        if (simulationTimeline != null) simulationTimeline.stop();
        if (bunnyThread  != null) bunnyThread.interrupt();
        for (RobotThread rt : robotThreads) rt.interrupt();
        robotThreads.clear();
        gameState = null;
    }

    /**
     * Porneste simularea pe labirintul curent.
     * @param numRobots  cati roboti
     * @param statusLbl  label pentru mesaje de stare
     */
    private void startSimulation(int numRobots, Label statusLbl)
    {
        if (labirint == null) {
            statusLbl.setText("Creaza sau incarca un labirint intai (Panel 1/2)!");
            statusLbl.setStyle("-fx-text-fill: #ff6666;"); return;
        }
        stopSimulation(); // opreste orice simulare anterioara

        // Creem state-ul si memoria partajata
        gameState    = new GameState(labirint, numRobots);
        sharedMemory = new SharedMemory();

        // Desenam starea initiala
        redrawSimulation();
        statusLbl.setText("Simulare pornita — " + numRobots + " roboti");
        statusLbl.setStyle("-fx-text-fill: #aaffaa;");

        // Pornim thread-urile
        bunnyThread = new BunnyThread(gameState, 350);
        bunnyThread.setDaemon(true);
        bunnyThread.start();

        for (int i = 0; i < numRobots; i++) {
            RobotThread rt = new RobotThread(i, gameState, sharedMemory, 450);
            rt.setDaemon(true);
            rt.start();
            robotThreads.add(rt);
        }

        // Timeline JavaFX: redeseneaza UI la fiecare 150ms (pe FX thread)
        simulationTimeline = new Timeline(
                new KeyFrame(Duration.millis(150), e -> {
                    redrawSimulation();
                    if (!gameState.isGameRunning()) {
                        simulationTimeline.stop();
                        GameState.GameResult res = gameState.getResult();
                        if (res == GameState.GameResult.BUNNY_ESCAPED) {
                            statusLbl.setText("🐰 Iepurasul a SCAPAT! (celula galbena)");
                            statusLbl.setStyle("-fx-text-fill: #ffdd00;");
                        } else {
                            statusLbl.setText("🤖 Robotul a PRINS iepurasul!");
                            statusLbl.setStyle("-fx-text-fill: #ff4444;");
                        }
                    }
                })
        );
        simulationTimeline.setCycleCount(Timeline.INDEFINITE);
        simulationTimeline.play();
    }

    // -----------------------------------------------------------------------
    //  start() — UI principal
    // -----------------------------------------------------------------------
    @Override
    public void start(Stage stage)
    {
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: #3f3087;");

        // ===== PANEL 1 — Configuratie =====
        VBox panel1 = new VBox(15);
        panel1.setStyle("-fx-background-color: #3f3087; -fx-padding: 20; -fx-border-color: #7041a2; -fx-border-width: 2;");
        panel1.setPrefSize(820, 620);
        panel1.setVisible(false);

        Label label1 = new Label("Configuratie");
        label1.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        HBox rowLines = new HBox(10); rowLines.setAlignment(Pos.CENTER_LEFT);
        Label lblLines = new Label("Nr de linii:"); lblLines.setStyle("-fx-text-fill: white;"); lblLines.setPrefWidth(130);
        TextField inputLines = new TextField("10"); inputLines.setPrefWidth(60);
        rowLines.getChildren().addAll(lblLines, inputLines);

        HBox rowCols = new HBox(10); rowCols.setAlignment(Pos.CENTER_LEFT);
        Label lblCols = new Label("Nr de coloane:"); lblCols.setStyle("-fx-text-fill: white;"); lblCols.setPrefWidth(130);
        TextField inputCols = new TextField("10"); inputCols.setPrefWidth(60);
        rowCols.getChildren().addAll(lblCols, inputCols);

        Button buildBtn = new Button("Construieste-l");
        buildBtn.setPrefSize(160, 35);
        buildBtn.setStyle("-fx-background-color: #8342a6; -fx-text-fill: white; -fx-font-weight: bold;");

        Label statusLabel1 = new Label("Baga dimensiunile si apasa 'Construieste-l'.");
        statusLabel1.setStyle("-fx-text-fill: #cccccc;");

        buildBtn.setOnAction(event -> {
            try {
                int linii = Integer.parseInt(inputLines.getText().trim());
                int coloane = Integer.parseInt(inputCols.getText().trim());
                if (linii <= 0 || coloane <= 0) throw new NumberFormatException();
                labirint = new Maze(linii, coloane);
                statusLabel1.setText("Labirintul creat: " + linii + " x " + coloane);
                statusLabel1.setStyle("-fx-text-fill: #aaffaa;");
            } catch (NumberFormatException e) {
                statusLabel1.setText("Numere pozitive!");
                statusLabel1.setStyle("-fx-text-fill: #ff6666;");
            }
        });
        panel1.getChildren().addAll(label1, rowLines, rowCols, buildBtn, statusLabel1);

        // ===== PANEL 2 — Desenare =====
        VBox panel2 = new VBox(10);
        panel2.setStyle("-fx-background-color: #3f3087; -fx-padding: 15; -fx-border-color: #7041a2; -fx-border-width: 2;");
        panel2.setPrefSize(820, 620);
        panel2.setVisible(false);

        Label label2 = new Label("Click pe o celula pentru a seta/sterge un zid");
        label2.setStyle("-fx-text-fill: white; -fx-font-size: 13px;");
        Label legendLabel = new Label("Negru = Zid    Alb = Liber");
        legendLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 11px;");

        Button reloadBtn = new Button("Incarca / Redeseneaza");
        reloadBtn.setStyle("-fx-background-color: #8342a6; -fx-text-fill: white;");
        reloadBtn.setOnAction(e -> {
            if (labirint != null) deseneazaLabirintEditabil(mazeContainer);
            else label2.setText("Mergi la 'Configuratie' si construieste labirintul");
        });

        ScrollPane sp2 = new ScrollPane(mazeContainer);
        sp2.setPrefSize(790, 500);
        sp2.setStyle("-fx-background: #3f3087;");
        panel2.getChildren().addAll(label2, legendLabel, reloadBtn, sp2);

        // ===== PANEL 3 — Control Panel =====
        VBox panel3 = new VBox(12);
        panel3.setStyle("-fx-background-color: #3f3087; -fx-padding: 15; -fx-border-color: #7041a2; -fx-border-width: 2;");
        panel3.setPrefSize(820, 620);
        panel3.setVisible(false);

        Label label3 = new Label("Control Panel");
        label3.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        Label lblValidSect = new Label("Validare traseu");
        lblValidSect.setStyle("-fx-text-fill: #aaaaff; -fx-font-size: 13px;");

        HBox startRow = new HBox(8); startRow.setAlignment(Pos.CENTER_LEFT);
        Label lblStart = new Label("Start (rand, col):"); lblStart.setStyle("-fx-text-fill: white;"); lblStart.setPrefWidth(150);
        TextField tfStartR = new TextField("0"); tfStartR.setPrefWidth(50);
        TextField tfStartC = new TextField("0"); tfStartC.setPrefWidth(50);
        startRow.getChildren().addAll(lblStart, tfStartR, new Label(",") {{ setStyle("-fx-text-fill:white;"); }}, tfStartC);

        HBox endRow = new HBox(8); endRow.setAlignment(Pos.CENTER_LEFT);
        Label lblEnd = new Label("End (rand, col):"); lblEnd.setStyle("-fx-text-fill: white;"); lblEnd.setPrefWidth(150);
        TextField tfEndR = new TextField(); tfEndR.setPrefWidth(50);
        TextField tfEndC = new TextField(); tfEndC.setPrefWidth(50);
        endRow.getChildren().addAll(lblEnd, tfEndR, new Label(",") {{ setStyle("-fx-text-fill:white;"); }}, tfEndC);

        Button validateBtn = new Button("Valideaza Traseu");
        validateBtn.setPrefSize(180, 32);
        validateBtn.setStyle("-fx-background-color: #2a7a2a; -fx-text-fill: white; -fx-font-weight: bold;");

        Label validateResult = new Label("");
        validateResult.setStyle("-fx-text-fill: white; -fx-font-size: 13px;");

        validateBtn.setOnAction(event -> {
            if (labirint == null) {
                validateResult.setText("✗ Labirintul nu a fost creat");
                validateResult.setStyle("-fx-text-fill: #ff6666; -fx-font-size: 13px;"); return;
            }
            try {
                int sr = Integer.parseInt(tfStartR.getText().trim()), sc = Integer.parseInt(tfStartC.getText().trim());
                int er = Integer.parseInt(tfEndR.getText().trim()),   ec = Integer.parseInt(tfEndC.getText().trim());
                int n = labirint.getN(), m = labirint.getM();
                if (sr<0||sr>=n||sc<0||sc>=m||er<0||er>=n||ec<0||ec>=m) {
                    validateResult.setText("✗ Coordonate in afara limitelor");
                    validateResult.setStyle("-fx-text-fill: #ff6666; -fx-font-size: 13px;"); return;
                }
                boolean ok = isTraversable(sr, sc, er, ec);
                validateResult.setText(ok
                        ? "✓ Traversabil de la ("+sr+","+sc+") la ("+er+","+ec+")!"
                        : "✗ Nu exista traseu de la ("+sr+","+sc+") la ("+er+","+ec+")!");
                validateResult.setStyle((ok?"-fx-text-fill: #aaffaa;":"-fx-text-fill: #ff6666;")+" -fx-font-size: 13px;");
            } catch (NumberFormatException ex) {
                validateResult.setText("Eroare: introduceti coordonate valide");
                validateResult.setStyle("-fx-text-fill: #ff6666; -fx-font-size: 13px;");
            }
        });

        tfEndR.focusedProperty().addListener((obs, old, nw) -> { if (tfEndR.getText().isEmpty() && labirint!=null) tfEndR.setText(String.valueOf(labirint.getN()-1)); });
        tfEndC.focusedProperty().addListener((obs, old, nw) -> { if (tfEndC.getText().isEmpty() && labirint!=null) tfEndC.setText(String.valueOf(labirint.getM()-1)); });

        Separator sep1 = new Separator();
        Label lblExportSect = new Label("Exporta PNG");
        lblExportSect.setStyle("-fx-text-fill: #aaaaff; -fx-font-size: 13px;");

        Button exportBtn = new Button("Exporta ca PNG");
        exportBtn.setPrefSize(180, 32);
        exportBtn.setStyle("-fx-background-color: #1a5a8a; -fx-text-fill: white; -fx-font-weight: bold;");
        Label exportResult = new Label(""); exportResult.setStyle("-fx-text-fill: white;");
        exportBtn.setOnAction(e -> exportToPNG(exportResult));

        Separator sep2 = new Separator();
        Label lblSerialSect = new Label("Salvare / Incarcare");
        lblSerialSect.setStyle("-fx-text-fill: #aaaaff; -fx-font-size: 13px;");

        HBox serialBtns = new HBox(12);
        Button saveBtn = new Button("💾 Salveaza (.ser)"); saveBtn.setPrefSize(175, 32);
        saveBtn.setStyle("-fx-background-color: #7a5a1a; -fx-text-fill: white; -fx-font-weight: bold;");
        Button loadBtn = new Button("📂 Incarca (.ser)"); loadBtn.setPrefSize(175, 32);
        loadBtn.setStyle("-fx-background-color: #7a5a1a; -fx-text-fill: white; -fx-font-weight: bold;");
        serialBtns.getChildren().addAll(saveBtn, loadBtn);
        Label serialResult = new Label(""); serialResult.setStyle("-fx-text-fill: white;");
        saveBtn.setOnAction(e -> salveazaLabirint(serialResult));
        loadBtn.setOnAction(e -> incarcaLabirint(serialResult));

        panel3.getChildren().addAll(label3, lblValidSect, startRow, endRow, validateBtn, validateResult,
                sep1, lblExportSect, exportBtn, exportResult, sep2, lblSerialSect, serialBtns, serialResult);

        // ===== PANEL 4 — Simulare Concurenta (NOU) =====
        VBox panel4 = new VBox(12);
        panel4.setStyle("-fx-background-color: #3f3087; -fx-padding: 15; -fx-border-color: #7041a2; -fx-border-width: 2;");
        panel4.setPrefSize(820, 620);
        panel4.setVisible(false);

        Label label4 = new Label("Simulare Concurenta — Iepuras vs Roboti");
        label4.setStyle("-fx-text-fill: white; -fx-font-size: 17px; -fx-font-weight: bold;");

        // Legenda vizuala
        HBox legendaBox = new HBox(18);
        legendaBox.setAlignment(Pos.CENTER_LEFT);
        legendaBox.getChildren().addAll(
                legendItem("#00cc44", "Iepuras (B)"),
                legendItem("#cc2222", "Robot (R)"),
                legendItem("#ffdd00", "Iesire (E)"),
                legendItem("#000000", "Zid"),
                legendItem("#ffffff", "Liber")
        );

        // Configurare roboti
        HBox configRow = new HBox(10); configRow.setAlignment(Pos.CENTER_LEFT);
        Label lblRoboti = new Label("Nr. roboti:");
        lblRoboti.setStyle("-fx-text-fill: white;");
        Spinner<Integer> spinnerRoboti = new Spinner<>(1, 5, 3);
        spinnerRoboti.setPrefWidth(70);
        spinnerRoboti.setStyle("-fx-background-color: #555; -fx-text-fill: white;");
        configRow.getChildren().addAll(lblRoboti, spinnerRoboti);

        // Butoane Start / Stop
        HBox btnRow = new HBox(12);
        Button btnStart = new Button("▶ Porneste Simularea");
        btnStart.setPrefSize(200, 35);
        btnStart.setStyle("-fx-background-color: #2a7a2a; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");

        Button btnStop = new Button("■ Opreste");
        btnStop.setPrefSize(120, 35);
        btnStop.setStyle("-fx-background-color: #882222; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
        btnRow.getChildren().addAll(btnStart, btnStop);

        Label simStatus = new Label("Apasa 'Porneste' pentru a incepe. Labirintul curent este folosit.");
        simStatus.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 12px;");
        simStatus.setWrapText(true);

        ScrollPane simScroll = new ScrollPane(simulationPane);
        simScroll.setPrefSize(790, 430);
        simScroll.setStyle("-fx-background: #2a2060;");
        simulationPane.setStyle("-fx-background-color: #2a2060;");

        btnStart.setOnAction(e -> {
            int numR = spinnerRoboti.getValue();
            startSimulation(numR, simStatus);
        });
        btnStop.setOnAction(e -> {
            stopSimulation();
            simStatus.setText("Simulare oprita.");
            simStatus.setStyle("-fx-text-fill: #cccccc;");
        });

        panel4.getChildren().addAll(label4, legendaBox, configRow, btnRow, simStatus, simScroll);

        // ===== POZITIONARE PANELURI =====
        for (VBox p : new VBox[]{panel1, panel2, panel3, panel4}) {
            AnchorPane.setTopAnchor(p, 10.0);
            AnchorPane.setLeftAnchor(p, 10.0);
        }

        // ===== BUTOANE NAVIGARE =====
        Button btn1 = navButton("Configuratie");
        Button btn2 = navButton("Deseneaza");
        Button btn3 = navButton("Control Panel");
        Button btn4 = navButton("🐰 Simulare");
        btn4.setStyle("-fx-background-color: #2a5a2a; -fx-text-fill: white; -fx-font-size: 13px;");

        AnchorPane.setTopAnchor(btn1, 10.0);  AnchorPane.setRightAnchor(btn1, 10.0);
        AnchorPane.setTopAnchor(btn2, 90.0);  AnchorPane.setRightAnchor(btn2, 10.0);
        AnchorPane.setTopAnchor(btn3, 170.0); AnchorPane.setRightAnchor(btn3, 10.0);
        AnchorPane.setTopAnchor(btn4, 250.0); AnchorPane.setRightAnchor(btn4, 10.0);

        // Logica toggle paneluri
        btn1.setOnAction(event -> togglePanel(panel1, new VBox[]{panel2,panel3,panel4}, btn1, new Button[]{btn2,btn3,btn4}, "Configuratie", "Deseneaza", "Control Panel", "🐰 Simulare", null, null));
        btn2.setOnAction(event -> {
            boolean show = !panel2.isVisible();
            panel1.setVisible(false); panel3.setVisible(false); panel4.setVisible(false);
            panel2.setVisible(show);
            if (show && labirint != null) deseneazaLabirintEditabil(mazeContainer);
            btn1.setText("Configuratie"); btn2.setText(show ? "Inchide" : "Deseneaza"); btn3.setText("Control Panel"); btn4.setText("🐰 Simulare");
        });
        btn3.setOnAction(event -> {
            boolean show = !panel3.isVisible();
            panel1.setVisible(false); panel2.setVisible(false); panel4.setVisible(false);
            panel3.setVisible(show);
            if (show && labirint != null) {
                if (tfEndR.getText().isEmpty()) tfEndR.setText(String.valueOf(labirint.getN()-1));
                if (tfEndC.getText().isEmpty()) tfEndC.setText(String.valueOf(labirint.getM()-1));
            }
            btn1.setText("Configuratie"); btn2.setText("Deseneaza"); btn3.setText(show ? "Inchide" : "Control Panel"); btn4.setText("🐰 Simulare");
        });
        btn4.setOnAction(event -> {
            boolean show = !panel4.isVisible();
            panel1.setVisible(false); panel2.setVisible(false); panel3.setVisible(false);
            panel4.setVisible(show);
            btn1.setText("Configuratie"); btn2.setText("Deseneaza"); btn3.setText("Control Panel"); btn4.setText(show ? "Inchide" : "🐰 Simulare");
            if (!show) stopSimulation();
        });

        root.getChildren().addAll(panel1, panel2, panel3, panel4, btn1, btn2, btn3, btn4);

        Scene scene = new Scene(root, 1100, 700);
        stage.setTitle("Lab8 — Maze Editor + Simulare");
        stage.setScene(scene);
        stage.show();

        // Oprim thread-urile la inchiderea ferestrei
        stage.setOnCloseRequest(e -> stopSimulation());
    }

    // -----------------------------------------------------------------------
    //  Utilitare UI
    // -----------------------------------------------------------------------

    /** Creeaza un item de legenda colorat. */
    private HBox legendItem(String hexColor, String text) {
        Rectangle r = new Rectangle(18, 18);
        r.setFill(Color.web(hexColor));
        r.setStroke(Color.web("#888888"));
        Label lbl = new Label(text);
        lbl.setStyle("-fx-text-fill: #dddddd; -fx-font-size: 11px;");
        HBox box = new HBox(5, r, lbl);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    /** Creeaza un buton de navigare cu stilul standard. */
    private Button navButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: #555555; -fx-text-fill: white; -fx-font-size: 13px;");
        btn.setPrefSize(210, 70);
        return btn;
    }

    /** Toggle generic pentru panel1 (btn1). */
    private void togglePanel(VBox target, VBox[] others, Button selfBtn,
                             Button[] otherBtns, String selfLabel,
                             String l2, String l3, String l4,
                             Object unused1, Object unused2) {
        boolean show = !target.isVisible();
        for (VBox p : others) p.setVisible(false);
        target.setVisible(show);
        selfBtn.setText(show ? "Inchide" : selfLabel);
        String[] labels = {l2, l3, l4};
        for (int i = 0; i < otherBtns.length; i++) otherBtns[i].setText(labels[i]);
    }

    public static void main(String[] args) { launch(); }
}
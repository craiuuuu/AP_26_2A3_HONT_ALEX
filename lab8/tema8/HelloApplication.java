package lab8.tema8;

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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class HelloApplication extends Application
{
    private Maze labirint;
    private final Pane mazeContainer = new Pane();       // Panel 2 - editabil
    private final Pane controlMazeContainer = new Pane(); // Panel 3 - vizualizare


    private void deseneazaLabirintEditabil(Pane container)
    {
        container.getChildren().clear();
        double dim = 30.0;

        for (int i = 0; i < labirint.getN(); i++)
        {
            for (int j = 0; j < labirint.getM(); j++)
            {
                final int row = i;
                final int col = j;
                Celula celula = labirint.getCelula(i, j);

                Rectangle rect = new Rectangle(j * dim, i * dim, dim, dim);
                rect.setFill(celula.esteZid() ? Color.BLACK : Color.WHITE);
                rect.setStroke(Color.BLACK);
                rect.setStrokeWidth(1.0);
                rect.setSmooth(false);

                rect.setOnMouseClicked(event ->
                {
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
        int n = labirint.getN();
        int m = labirint.getM();

        if (labirint.getCelula(startR, startC).esteZid() ||
            labirint.getCelula(endR, endC).esteZid())
            return false;

        boolean[][] visited = new boolean[n][m];
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startR, startC});
        visited[startR][startC] = true;

        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!queue.isEmpty())
        {
            int[] curr = queue.poll();
            if (curr[0] == endR && curr[1] == endC)
                return true;

            for (int[] d : dirs)
            {
                int nr = curr[0] + d[0];
                int nc = curr[1] + d[1];
                if (nr >= 0 && nr < n && nc >= 0 && nc < m
                        && !visited[nr][nc]
                        && !labirint.getCelula(nr, nc).esteZid())
                {
                    visited[nr][nc] = true;
                    queue.add(new int[]{nr, nc});
                }
            }
        }
        return false;
    }


    private void exportToPNG(Label resultLabel)
    {
        if (labirint == null || mazeContainer.getChildren().isEmpty())
        {
            resultLabel.setText("Deseneaza labirintu intai");
            resultLabel.setStyle("-fx-text-fill: #ff6666;");
            return;
        }
        try
        {
            SnapshotParameters sp = new SnapshotParameters();
            sp.setFill(Color.WHITE);
            WritableImage img = mazeContainer.snapshot(sp, null);
            BufferedImage bImg = SwingFXUtils.fromFXImage(img, null);
            File file = new File("maze_export.png");
            ImageIO.write(bImg, "png", file);
            resultLabel.setStyle("-fx-text-fill: #aaffaa;");
        }
        catch (Exception ex)
        {
            resultLabel.setText("eroarea:" + ex.getMessage());

            resultLabel.setStyle("-fx-text-fill: #ff6666;");
        }
    }

    private void salveazaLabirint(Label resultLabel)
    {
        if (labirint == null)
        {
            resultLabel.setText("nu avem labirint de salvat");
            resultLabel.setStyle("-fx-text-fill: #ff6666;");
            return;
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("maze_save.ser")))
        {
            oos.writeObject(labirint);
            resultLabel.setText("Am salvat: maze_save.ser");
            resultLabel.setStyle("-fx-text-fill: #aaffaa;");
        }
        catch (IOException ex)
        {
            resultLabel.setText("Eroare " + ex.getMessage());
            resultLabel.setStyle("-fx-text-fill: #ff6666;");
        }
    }

    private void incarcaLabirint(Label resultLabel)
    {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("maze_save.ser")))
        {
            labirint = (Maze) ois.readObject();
            deseneazaLabirintEditabil(mazeContainer); // redeseneaza dupa incarcare
            resultLabel.setText("incarcat:" + labirint.getN() + "x" + labirint.getM() );
            resultLabel.setStyle("-fx-text-fill: #aaffaa;");
        }
        catch (FileNotFoundException ex)
        {
            resultLabel.setText("nu am gasit fisieru");
            resultLabel.setStyle("-fx-text-fill: #ff6666;");
        }
        catch (Exception ex)
        {
            resultLabel.setText("eroare " + ex.getMessage());
            resultLabel.setStyle("-fx-text-fill: #ff6666;");
        }
    }

    @Override
    public void start(Stage stage)
    {
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: #3f3087;");

        VBox panel1 = new VBox(15);
        panel1.setStyle("-fx-background-color: #3f3087; -fx-padding: 20; -fx-border-color: #7041a2; -fx-border-width: 2;");
        panel1.setPrefSize(820, 620);
        panel1.setVisible(false);

        Label label1 = new Label("Configuratie");
        label1.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        HBox rowLines = new HBox(10);
        rowLines.setAlignment(Pos.CENTER_LEFT);
        Label lblLines = new Label("Nr de linii:");
        lblLines.setStyle("-fx-text-fill: white;");
        lblLines.setPrefWidth(130);
        TextField inputLines = new TextField("10");
        inputLines.setPrefWidth(60);
        rowLines.getChildren().addAll(lblLines, inputLines);

        HBox rowCols = new HBox(10);
        rowCols.setAlignment(Pos.CENTER_LEFT);
        Label lblCols = new Label("Nr de coloane:");
        lblCols.setStyle("-fx-text-fill: white;");
        lblCols.setPrefWidth(130);
        TextField inputCols = new TextField("10");
        inputCols.setPrefWidth(60);
        rowCols.getChildren().addAll(lblCols, inputCols);

        Button buildBtn = new Button("Construieste-l");
        buildBtn.setPrefSize(160, 35);
        buildBtn.setStyle("-fx-background-color: #8342a6; -fx-text-fill: white; -fx-font-weight: bold;");

        Label statusLabel1 = new Label("baga  dimensiunile si apasa 'Construieste-l'.");
        statusLabel1.setStyle("-fx-text-fill: #cccccc;");

        buildBtn.setOnAction(event ->
        {
            try
            {
                int linii  = Integer.parseInt(inputLines.getText().trim());
                int coloane = Integer.parseInt(inputCols.getText().trim());
                if (linii <= 0 || coloane <= 0) throw new NumberFormatException();

                labirint = new Maze(linii, coloane);
                statusLabel1.setText("Labirintu este creat  " + linii + " linii x " + coloane);
                statusLabel1.setStyle("-fx-text-fill: #aaffaa;");
            }
            catch (NumberFormatException e)
            {
                statusLabel1.setText("nr pozitive!");
                statusLabel1.setStyle("-fx-text-fill: #ff6666;");
            }
        });

        panel1.getChildren().addAll(label1, rowLines, rowCols, buildBtn, statusLabel1);

        VBox panel2 = new VBox(10);
        panel2.setStyle("-fx-background-color: #3f3087; -fx-padding: 15; -fx-border-color: #7041a2; -fx-border-width: 2;");
        panel2.setPrefSize(820, 620);
        panel2.setVisible(false);

        Label label2 = new Label("Deseneaza si editeaza.click pe o celula pentru a seta sau a sterge un zid");
        label2.setStyle("-fx-text-fill: white; -fx-font-size: 13px;");

        Label legendLabel = new Label("nergru = Zid (blocat)   alb = Liber (traversabil)");
        legendLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 11px;");

        Button reloadBtn = new Button("Incarca sau redeseneaza");
        reloadBtn.setStyle("-fx-background-color: #8342a6; -fx-text-fill: white;");
        reloadBtn.setOnAction(e ->
        {
            if (labirint != null)
                deseneazaLabirintEditabil(mazeContainer);
            else
                label2.setText("Mergi la 'Configuratie' si construieste labirintul");
        });

        ScrollPane sp2 = new ScrollPane(mazeContainer);
        sp2.setPrefSize(790, 500);
        sp2.setStyle("-fx-background: #3f3087;");

        panel2.getChildren().addAll(label2, legendLabel, reloadBtn, sp2);

        VBox panel3 = new VBox(12);
        panel3.setStyle("-fx-background-color: #3f3087; -fx-padding: 15; -fx-border-color: #7041a2; -fx-border-width: 2;");
        panel3.setPrefSize(820, 620);
        panel3.setVisible(false);

        Label label3 = new Label("Control Panel");
        label3.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        Label lblValidSect = new Label("Validare traseu");
        lblValidSect.setStyle("-fx-text-fill: #aaaaff; -fx-font-size: 13px;");


        HBox startRow = new HBox(8);
        startRow.setAlignment(Pos.CENTER_LEFT);
        Label lblStart = new Label("Start (rand, col):");
        lblStart.setStyle("-fx-text-fill: white;");
        lblStart.setPrefWidth(150);
        TextField tfStartR = new TextField("0");
        tfStartR.setPrefWidth(50);
        TextField tfStartC = new TextField("0");
        tfStartC.setPrefWidth(50);
        startRow.getChildren().addAll(lblStart, tfStartR, new Label(",") {{setStyle("-fx-text-fill:white;");}}, tfStartC);

        HBox endRow = new HBox(8);
        endRow.setAlignment(Pos.CENTER_LEFT);
        Label lblEnd = new Label("End (rand, col):");
        lblEnd.setStyle("-fx-text-fill: white;");
        lblEnd.setPrefWidth(150);
        TextField tfEndR = new TextField();
        tfEndR.setPrefWidth(50);
        TextField tfEndC = new TextField();
        tfEndC.setPrefWidth(50);
        endRow.getChildren().addAll(lblEnd, tfEndR, new Label(",") {{setStyle("-fx-text-fill:white;");}}, tfEndC);

        Button validateBtn = new Button("Valideaza Traseu");
        validateBtn.setPrefSize(180, 32);
        validateBtn.setStyle("-fx-background-color: #2a7a2a; -fx-text-fill: white; -fx-font-weight: bold;");

        Label validateResult = new Label("");
        validateResult.setStyle("-fx-text-fill: white; -fx-font-size: 13px;");

        validateBtn.setOnAction(event ->
        {
            if (labirint == null)
            {
                validateResult.setText("✗ Labirintul nu a fost creat");
                validateResult.setStyle("-fx-text-fill: #ff6666; -fx-font-size: 13px;");
                return;
            }
            try
            {
                int sr = Integer.parseInt(tfStartR.getText().trim());
                int sc = Integer.parseInt(tfStartC.getText().trim());
                int er = Integer.parseInt(tfEndR.getText().trim());
                int ec = Integer.parseInt(tfEndC.getText().trim());

                int n = labirint.getN();
                int m = labirint.getM();
                if (sr < 0 || sr >= n || sc < 0 || sc >= m ||
                    er < 0 || er >= n || ec < 0 || ec >= m)
                {
                    validateResult.setText("✗ Coordonate in afara limitelor labirintului");
                    validateResult.setStyle("-fx-text-fill: #ff6666; -fx-font-size: 13px;");
                    return;
                }

                boolean ok = isTraversable(sr, sc, er, ec);
                validateResult.setText(ok
                        ? " Labirintul este traversabil de la (" + sr + "," + sc + ") la (" + er + "," + ec + ")!"
                        : "nu exista traseu de la (" + sr + "," + sc + ") la (" + er + "," + ec + ")!");
                validateResult.setStyle(
                        (ok ? "-fx-text-fill: #aaffaa;" : "-fx-text-fill: #ff6666;")
                        + " -fx-font-size: 13px;");
            }
            catch (NumberFormatException ex)
            {
                validateResult.setText("Eroare: introduceti coordonate intregi valide");
                validateResult.setStyle("-fx-text-fill: #ff6666; -fx-font-size: 13px;");
            }
        });

        tfEndR.focusedProperty().addListener((obs, old, nw) ->
        {
            if (tfEndR.getText().isEmpty() && labirint != null)
                tfEndR.setText(String.valueOf(labirint.getN() - 1));
        });
        tfEndC.focusedProperty().addListener((obs, old, nw) ->
        {
            if (tfEndC.getText().isEmpty() && labirint != null)
                tfEndC.setText(String.valueOf(labirint.getM() - 1));
        });

        Separator sep1 = new Separator();

        Label lblExportSect = new Label("Exportam png");
        lblExportSect.setStyle("-fx-text-fill: #aaaaff; -fx-font-size: 13px;");

        Button exportBtn = new Button("Exporta ca PNG");
        exportBtn.setPrefSize(180, 32);
        exportBtn.setStyle("-fx-background-color: #1a5a8a; -fx-text-fill: white; -fx-font-weight: bold;");

        Label exportResult = new Label("");
        exportResult.setStyle("-fx-text-fill: white;");
        exportBtn.setOnAction(e -> exportToPNG(exportResult));

        Separator sep2 = new Separator();

        Label lblSerialSect = new Label("Salvare sau Incarcare ");
        lblSerialSect.setStyle("-fx-text-fill: #aaaaff; -fx-font-size: 13px;");

        HBox serialBtns = new HBox(12);
        Button saveBtn = new Button("💾 Salveaza (.ser)");
        saveBtn.setPrefSize(175, 32);
        saveBtn.setStyle("-fx-background-color: #7a5a1a; -fx-text-fill: white; -fx-font-weight: bold;");

        Button loadBtn = new Button("📂 Incarca (.ser)");
        loadBtn.setPrefSize(175, 32);
        loadBtn.setStyle("-fx-background-color: #7a5a1a; -fx-text-fill: white; -fx-font-weight: bold;");
        serialBtns.getChildren().addAll(saveBtn, loadBtn);

        Label serialResult = new Label("");
        serialResult.setStyle("-fx-text-fill: white;");

        saveBtn.setOnAction(e -> salveazaLabirint(serialResult));
        loadBtn.setOnAction(e -> incarcaLabirint(serialResult));

        panel3.getChildren().addAll(
                label3,
                lblValidSect, startRow, endRow, validateBtn, validateResult,
                sep1,
                lblExportSect, exportBtn, exportResult,
                sep2,
                lblSerialSect, serialBtns, serialResult
        );

        AnchorPane.setTopAnchor(panel1, 10.0);
        AnchorPane.setLeftAnchor(panel1, 10.0);
        AnchorPane.setTopAnchor(panel2, 10.0);
        AnchorPane.setLeftAnchor(panel2, 10.0);
        AnchorPane.setTopAnchor(panel3, 10.0);
        AnchorPane.setLeftAnchor(panel3, 10.0);

        Button btn1 = new Button("Configuratie");
        btn1.setStyle("-fx-background-color: #555555; -fx-text-fill: white; -fx-font-size: 13px;");
        btn1.setPrefSize(210, 70);
        AnchorPane.setTopAnchor(btn1, 10.0);
        AnchorPane.setRightAnchor(btn1, 10.0);

        Button btn2 = new Button(" Deseneaza");
        btn2.setStyle("-fx-background-color: #555555; -fx-text-fill: white; -fx-font-size: 13px;");
        btn2.setPrefSize(210, 70);
        AnchorPane.setTopAnchor(btn2, 90.0);
        AnchorPane.setRightAnchor(btn2, 10.0);

        Button btn3 = new Button("Control Panel");
        btn3.setStyle("-fx-background-color: #555555; -fx-text-fill: white; -fx-font-size: 13px;");
        btn3.setPrefSize(210, 70);
        AnchorPane.setTopAnchor(btn3, 170.0);
        AnchorPane.setRightAnchor(btn3, 10.0);

        btn1.setOnAction(event ->
        {
            boolean show = !panel1.isVisible();
            panel1.setVisible(show);
            panel2.setVisible(false);
            panel3.setVisible(false);
            btn1.setText(show ? "Inchide" : " Configuratie");
            btn2.setText("Deseneaza");
            btn3.setText("Control Panel");
        });

        btn2.setOnAction(event ->
        {
            boolean show = !panel2.isVisible();
            panel2.setVisible(show);
            panel1.setVisible(false);
            panel3.setVisible(false);
            if (show && labirint != null)
                deseneazaLabirintEditabil(mazeContainer);
            btn2.setText(show ? " Inchide" : " Deseneaza");
            btn1.setText("Configuratie");
            btn3.setText("Control Panel");
        });

        btn3.setOnAction(event ->
        {
            boolean show = !panel3.isVisible();
            panel3.setVisible(show);
            panel1.setVisible(false);
            panel2.setVisible(false);
            if (show && labirint != null)
            {
                if (tfEndR.getText().isEmpty())
                    tfEndR.setText(String.valueOf(labirint.getN() - 1));
                if (tfEndC.getText().isEmpty())
                    tfEndC.setText(String.valueOf(labirint.getM() - 1));
            }
            btn3.setText(show ? " Inchide" : " Control Panel");
            btn1.setText("Configuratie");
            btn2.setText("Deseneaza");
        });

        root.getChildren().addAll(panel1, panel2, panel3, btn1, btn2, btn3);

        Scene scene = new Scene(root, 1100, 700);
        stage.setTitle("Lab8 — Maze Editor");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}

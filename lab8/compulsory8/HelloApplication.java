package lab8.compulsory8;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class HelloApplication extends Application
{

    private Maze labirint;

    private Rectangle creeazaPatrat(double x, double y, double dimensiune, String culoareHex, int r, int c)
    {
        Rectangle patrat = new Rectangle(x, y, dimensiune, dimensiune);
        patrat.setStyle("-fx-fill: " + culoareHex + "; -fx-stroke: black; -fx-stroke-width: 1;");

        patrat.setOnMouseClicked(event ->
        {
            String rosu = "#ed0000";
            patrat.setStyle("-fx-fill: " + rosu + "; -fx-stroke: black;");

            if (labirint != null)
            {
                labirint.getCelula(r, c).setCuloareHex(rosu);
            }
        });

        return patrat;
    }

    @Override
    public void start(Stage stage)
    {


        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: #3f3087;");

        VBox panel1 = new VBox(20);
        panel1.setStyle("-fx-background-color: #3f3087; -fx-padding: 20; -fx-border-color: #7041a2;");
        panel1.setPrefSize(1000, 650);
        panel1.setVisible(false);
        panel1.setAlignment(Pos.TOP_LEFT);

        Label label1 = new Label("Configuratie");
        label1.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        HBox rowLines = new HBox(10);
        rowLines.setAlignment(Pos.CENTER_LEFT);
        Label lblLines = new Label("Nr de linii:");
        lblLines.setStyle("-fx-text-fill: white;");
        lblLines.setPrefWidth(120);
        TextField inputLines = new TextField();
        inputLines.setPrefWidth(60);
        rowLines.getChildren().addAll(lblLines, inputLines);

        HBox rowCols = new HBox(10);
        rowCols.setAlignment(Pos.CENTER_LEFT);
        Label lblCols = new Label("Nr de coloane:");
        lblCols.setStyle("-fx-text-fill: white;");
        lblCols.setPrefWidth(120);
        TextField inputCols = new TextField();
        inputCols.setPrefWidth(60);
        rowCols.getChildren().addAll(lblCols, inputCols);

        Button configBtn3 = new Button("Construieste-l");
        configBtn3.setPrefSize(150, 30);
        configBtn3.setStyle("-fx-background-color: #8342a6; -fx-text-fill: white;");

        Pane mazeContainer = new Pane();
        mazeContainer.setPrefSize(800, 500);



        VBox panel2 = new VBox(10);
        panel2.setStyle("-fx-background-color: #3f3087; -fx-padding: 10; -fx-border-color: #7041a2;");
        panel2.setPrefSize(1000, 650);
        panel2.setVisible(false);
        Label label2 = new Label("Deseneaza");
        label2.setStyle("-fx-text-fill: white;");
        panel2.getChildren().addAll(label2, mazeContainer);



        VBox panel3 = new VBox(10);
        panel3.setStyle("-fx-background-color: #444444; -fx-padding: 10; -fx-border-color: #7041a2;");
        panel3.setPrefSize(1000, 650);
        panel3.setVisible(false);
        Label label3 = new Label("Control Panel");
        label3.setStyle("-fx-text-fill: white;");

        Pane controlMazeContainer = new Pane();
        controlMazeContainer.setPrefSize(800, 400);



        configBtn3.setOnAction(event ->
        {
            try
            {
                int linii = Integer.parseInt(inputLines.getText());
                int coloane = Integer.parseInt(inputCols.getText());

                this.labirint = new Maze(linii, coloane);

                mazeContainer.getChildren().clear();

                double dim = 30.0;
                for (int i = 0; i < linii; i++)
                {
                    for (int j = 0; j < coloane; j++)
                    {
                        Rectangle celula = creeazaPatrat(j*dim, i*dim, dim, "#FFFFFF", i, j);
                        mazeContainer.getChildren().add(celula);
                    }
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("Eroare,numerele nu sunt intregi sau valide");
            }
        });



        panel1.getChildren().addAll(label1, rowLines, rowCols, configBtn3);



        Button createBtn = new Button("Creeaza");
        createBtn.setPrefSize(150, 30);
        createBtn.setStyle("-fx-background-color: #666666; -fx-text-fill: white;");

        createBtn.setOnAction(event ->
        {
            if (labirint != null)
            {
                MazeDrawer drawer = new MazeDrawer();
                drawer.drawMazeRemovedBorders(labirint, controlMazeContainer);
                System.out.println("labirintu a fost creat");
            }
            else
            {
                System.out.println("mergi la configuratie si apasa construieste/creeaza mai intai!");
            }
        });

        Button resetBtn = new Button("Reset");
        resetBtn.setPrefSize(150, 30);
        resetBtn.setStyle("-fx-background-color: #666666; -fx-text-fill: white;");

        resetBtn.setOnAction(event ->
        {
            if (labirint != null)
            {
                MazeDrawer drawer = new MazeDrawer();
                drawer.drawMaze(labirint, controlMazeContainer);
                System.out.println("labirintu a fost creat");
            }
            else
            {
                System.out.println("mergi la configuratie si apasa construieste/creeaza mai intai!");
            }
        });

        panel3.getChildren().addAll(label3, createBtn, resetBtn, controlMazeContainer);

        AnchorPane.setTopAnchor(panel1, 100.0);
        AnchorPane.setRightAnchor(panel1, 500.0);
        AnchorPane.setTopAnchor(panel2, 100.0);
        AnchorPane.setRightAnchor(panel2, 500.0);
        AnchorPane.setTopAnchor(panel3, 100.0);
        AnchorPane.setRightAnchor(panel3, 500.0);


        Button btn1 = new Button("Configuratie");
        btn1.setStyle("-fx-background-color: #888888; -fx-text-fill: white;");
        btn1.setPrefSize(200, 100);
        AnchorPane.setTopAnchor(btn1, 100.0);
        AnchorPane.setRightAnchor(btn1, 100.0);


        Button btn2 = new Button("Deseneaza");
        btn2.setStyle("-fx-background-color: #888888; -fx-text-fill: white;");
        btn2.setPrefSize(200, 100);
        AnchorPane.setTopAnchor(btn2, 400.0);
        AnchorPane.setRightAnchor(btn2, 100.0);


        Button btn3 = new Button("Control Panel");
        btn3.setStyle("-fx-background-color: #888888; -fx-text-fill: white;");
        btn3.setPrefSize(200, 100);
        AnchorPane.setTopAnchor(btn3, 700.0);
        AnchorPane.setRightAnchor(btn3, 100.0);



        btn1.setOnAction(event ->
        {
            panel1.setVisible(!panel1.isVisible());
            panel2.setVisible(false);
            panel3.setVisible(false);

            if (panel1.isVisible())
            {
                btn1.setText("Inchide meniul");
            }
            else
            {
                btn1.setText("Configuratie");
            }
            btn2.setText("Deseneaza");
            btn3.setText("Control Panel");
        });

        btn2.setOnAction(event ->
        {
            panel2.setVisible(!panel2.isVisible());
            panel1.setVisible(false);
            panel3.setVisible(false);

            if (panel2.isVisible())
            {
                btn2.setText("Close Menu");
            }
            else
            {
                btn2.setText("Deseneaza");
            }
            btn1.setText("Configuratie");
            btn3.setText("Deseneaza labirintu");


        });

        btn3.setOnAction(event ->
        {
            panel3.setVisible(!panel3.isVisible());
            panel1.setVisible(false);
            panel2.setVisible(false);

            if (panel3.isVisible())
            {
                btn3.setText("Inchide meniul");
            }
            else
            {
                btn3.setText("Control Panel");
            }
            btn1.setText("Configuratie");
            btn2.setText("Deseneaza labirintu");
        });


        root.getChildren().addAll(btn1, btn2, btn3, panel1, panel2, panel3);

        Scene scene = new Scene(root, 1000, 700);
        stage.setTitle("Lab8");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}
package circles;

import java.util.stream.Stream;
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Spinner;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;

/**
 * A lab exercise to introduce Java 8 streams and JavaFX
 * @author Alia Andersen
 */
public class Circles extends Application {

    public static final int ROWS = 4;
    public static final int COLS = 5;
    public static final int CELL_SIZE = 100;
    
    @Override
    public void start(Stage primaryStage) {
        root = new VBox();
        child = new HBox();
        labels = new HBox();
        canvas = new Pane();
        rows = new Spinner(0, 5, 0);
        columns = new Spinner(0, 5, 0);
        cellSize = new Slider(50, 150, 50);
        xScale = new Spinner(-3, 3, 0);
        yScale = new Spinner(-3, 3, 0);
        

        root.setAlignment(Pos.CENTER);
        canvas.setPrefSize(COLS*CELL_SIZE, ROWS*CELL_SIZE);
        
        addButtonHandler();
        
        rows.setPrefWidth(75);
        columns.setPrefWidth(75);
        xScale.setPrefWidth(75);
        yScale.setPrefWidth(75);
        
        rowsLabel = new Label("  Rows           ");
        columnsLabel = new Label("Columns             ");
        sliderLabel = new Label("   Cell Size             ");
        sLabel = new Label("    Cell Size           ");
        xLabel = new Label("        X Scale");
        yLabel = new Label("     Y Scale");
        
        sliderLabel.textProperty().bind((ObservableValue)Bindings.createStringBinding(() -> String.format("%3d", (int)this.cellSize.getValue()), (Observable[])new Observable[]{this.cellSize.valueProperty()}));
       
     
        root.getChildren().addAll(canvas, labels, child);
        labels.getChildren().addAll(rowsLabel, columnsLabel, sLabel, xLabel, yLabel);
        child.getChildren().addAll(rows, columns, cellSize, sliderLabel, xScale, yScale);
        primaryStage.setTitle("Java 8 Lab Exercise");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    private void addButtonHandler() {
            rows.valueProperty().addListener( e -> { 
                canvas.getChildren().clear(); 
                addAllRowsToCanvas(makeAllRows()); });
            columns.valueProperty().addListener( e -> { 
                canvas.getChildren().clear(); 
                addAllRowsToCanvas(makeAllRows()); });
            cellSize.valueProperty().addListener( e -> { 
                canvas.getChildren().clear(); 
                addAllRowsToCanvas(makeAllRows());
                cellSize.getValue();
            });
            xScale.valueProperty().addListener( e -> { 
                canvas.getChildren().clear(); 
                 addAllRowsToCanvas(makeAllRows()); });
            yScale.valueProperty().addListener( e -> { 
                canvas.getChildren().clear(); 
                addAllRowsToCanvas(makeAllRows()); });

    }

    private Stream<Stream<Circle>> makeAllRows(){
        return Stream.generate(() -> makeRow()).limit((int)rows.getValue());
    }
    
    private void addAllRowsToCanvas(Stream<Stream<Circle>> s){
        row = 0;
        s.forEach(r -> {addRowToCanvas(r); row++;});
    }
    
    private Stream<Circle> makeRow() {
        return Stream.generate(() -> new Circle(25)).limit((int)columns.getValue());
    }

    private void addRowToCanvas(Stream <Circle> s) {
        col = 0;
        s.forEach(c -> {addToCanvas(c); col++;});
    }

    private void addToCanvas(Circle c) {
        c.setFill((Paint) new Color(Math.random(), Math.random(), Math.random(), 1));
        
        int toX = (col*(int)cellSize.getValue()) - ((int)cellSize.getValue()/2);
        int toY = (row*(int)cellSize.getValue()) - ((int)cellSize.getValue()/2);
        
        int fromX = (int)col * (int)cellSize.getValue() + ((int)cellSize.getValue()/2);
        int fromY = (int)row * (int)cellSize.getValue() + ((int)cellSize.getValue()/2);
        
        c.setCenterX(fromX);
        c.setCenterY(fromY);
        
        canvas.getChildren().add(c);

        TranslateTransition tt = new TranslateTransition(Duration.millis((double)500));
        tt.setNode((Node)c);
        tt.setByX((int)xScale.getValue());
        tt.setByY((int)xScale.getValue());
        tt.play();
        
        ScaleTransition st = new ScaleTransition(Duration.millis((double)(500 * Math.random() + 100)));
        st.setNode((Node)c);
        st.setByX((int)xScale.getValue());
        st.setByY((int)yScale.getValue());
        st.setCycleCount(Animation.INDEFINITE);
        st.setAutoReverse(true);
        st.play();
        
    }

    private VBox root;
    private HBox child, labels;
    private Pane canvas;
    private Spinner rows, columns, xScale, yScale;
    private Slider cellSize;
    private int row = ROWS - 1;
    private int col = COLS - 1;
    private Label rowsLabel, columnsLabel, sliderLabel, xLabel, yLabel, sLabel;
    /**
     * @param args the command line arguments
     */
    public static void main(String... args) {
        launch(args);
    }
}

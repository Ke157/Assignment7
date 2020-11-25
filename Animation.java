
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Animation extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Scene scene = new Scene(new Board());
		stage.setScene(scene);
		stage.setTitle("Animation");
		stage.show();
	}

	// the board is a border pane
	class Board extends BorderPane {
		HBox hBox1, hBox2, hBox3, hBox4;
		Node selectedNode;

		public Board() {
			setPadding(new Insets(10, 10, 10, 10));

			// create the symbols array
			String[] symbols = new String[] { "Rotate", "Scale", "Sequential", "Fade" };
			HBox menuBox = new HBox(10);
			for (int i = 0; i < symbols.length; i++) {
				Button button = new Button(symbols[i]);
				button.setOnAction(new ButtonHandler());
				button.setPrefSize(100, 50);
				menuBox.getChildren().add(button);
			}
			menuBox.setAlignment(Pos.CENTER);

			// create a grid pane for operators
			GridPane gridPane = new GridPane();

			PhongMaterial greyMaterial = new PhongMaterial();
			greyMaterial.setDiffuseColor(Color.DARKGREY);
			greyMaterial.setSpecularColor(Color.GREY);
			Sphere shape1 = new Sphere(50);
			shape1.setMaterial(greyMaterial);

			PhongMaterial blueMaterial = new PhongMaterial();
			blueMaterial.setDiffuseColor(Color.BLUE);
			blueMaterial.setSpecularColor(Color.LIGHTBLUE);
			Cylinder shape2 = new Cylinder(50, 70);
			shape2.setMaterial(blueMaterial);

			PhongMaterial redMaterial = new PhongMaterial();
			redMaterial.setSpecularColor(Color.ORANGE);
			redMaterial.setDiffuseColor(Color.RED);
			Box shape3 = new Box(100, 50, 30);
			shape3.setMaterial(redMaterial);

			Polygon shape4 = new Polygon(0, 0, 70, 40, 150, 50, 64, 20, 50, 100);
			shape4.setStroke(Color.CORAL);
			shape4.setFill(Color.TRANSPARENT);
			shape4.setStrokeWidth(3);

			shape1.setOnMouseClicked(new ShapeClickHandler());
			shape2.setOnMouseClicked(new ShapeClickHandler());
			shape3.setOnMouseClicked(new ShapeClickHandler());
			shape4.setOnMouseClicked(new ShapeClickHandler());
			hBox1 = new HBox(shape1);
			hBox1.setAlignment(Pos.CENTER);
			hBox1.setStyle("-fx-border-color:black");
			hBox2 = new HBox(shape2);
			hBox2.setAlignment(Pos.CENTER);
			hBox2.setStyle("-fx-border-color:black");
			hBox3 = new HBox(shape3);
			hBox3.setAlignment(Pos.CENTER);
			hBox3.setStyle("-fx-border-color:black");
			hBox4 = new HBox(shape4);
			hBox4.setAlignment(Pos.CENTER);
			hBox4.setStyle("-fx-border-color:black");
			hBox1.setPrefSize(400, 400);
			hBox2.setPrefSize(400, 400);
			hBox3.setPrefSize(400, 400);
			hBox4.setPrefSize(400, 400);
			gridPane.add(hBox1, 0, 0);
			gridPane.add(hBox2, 0, 1);
			gridPane.add(hBox3, 1, 0);
			gridPane.add(hBox4, 1, 1);
			gridPane.setPadding(new Insets(50));
			setCenter(gridPane);
			setTop(menuBox);

			selectedNode = shape1;
		}

		class ButtonHandler implements EventHandler<ActionEvent> {
			Duration duration = Duration.millis(3000);
			Duration duration2 = Duration.millis(1000);
			
			@Override
			public void handle(ActionEvent event) {
				Button button = (Button) event.getSource();
				String buttonSymbol = button.getText();

				if (buttonSymbol.equals("Rotate")) {
					Rotate();
				}
				if (buttonSymbol.equals("Scale")) {
					Scale();
				}
				if (buttonSymbol.equals("Sequential")) {
					Sequential();
				}
				if (buttonSymbol.equals("Fade")) {
					Fade();
				}
			}

			private void Fade() {
				FadeTransition ft = new FadeTransition(duration2, selectedNode);
				ft.setFromValue(1.0f);
				ft.setToValue(0);
				ft.setCycleCount(4);
				ft.setAutoReverse(true);
				ft.play();
			}

			private void Sequential() {
				ScaleTransition st = new ScaleTransition(duration);
				st.setByX(1.5f);
				st.setByY(1.5f);
				st.setCycleCount(1);
				st.setAutoReverse(true);

				ScaleTransition st2 = new ScaleTransition(duration);
				st2.setByX(-0.66);
				st2.setByY(-0.66);
				st2.setCycleCount(1);
				st2.setAutoReverse(true);

				RotateTransition rt = new RotateTransition(duration);
				rt.setByAngle(180f);
				rt.setCycleCount(1);
				rt.setAutoReverse(true);

				SequentialTransition seqT = new SequentialTransition(selectedNode, st, rt, st2);
				seqT.play();
			}

			private void Scale() {
				ScaleTransition st = new ScaleTransition(duration, selectedNode);
				st.setByX(1.5f);
				st.setByY(1.5f);
				st.setCycleCount(4);
				st.setAutoReverse(true);
				st.play();
			}

			private void Rotate() {
				RotateTransition rt = new RotateTransition(duration, selectedNode);
				rt.setByAngle(180f);
				rt.setCycleCount(4);
				rt.setAutoReverse(true);
				rt.play();
			}
		}

		class ShapeClickHandler implements EventHandler<MouseEvent> {
			@Override
			public void handle(MouseEvent event) {
				hBox1.setStyle("-fx-border-color:black");
				hBox2.setStyle("-fx-border-color:black");
				hBox3.setStyle("-fx-border-color:black");
				hBox4.setStyle("-fx-border-color:black");

				Node node = (Node) event.getSource();
				HBox hBox = (HBox) node.getParent();
				hBox.setStyle("-fx-border-color:red;-fx-border-width:5");

				selectedNode = node;
			}
		}
	}
}

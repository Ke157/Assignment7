
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Calculator extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Scene scene = new Scene(new Board());
		stage.setScene(scene);
		stage.setTitle("Calculator");
		stage.show();
	}

	// the board is a border pane
	class Board extends BorderPane implements EventHandler<ActionEvent> {
		TextField textField;
		String num1 = ""; // the left number
		String num2 = "";// the right number
		String operator = "+"; // the operator
		int currentNum = 0; // the current number input
		double lastResult = 0;

		public Board() {
			setPadding(new Insets(10, 60, 60, 50));

			textField = new TextField();

			// create the symbols array
			String[][] symbols1 = new String[][] { { "0", "1", "2", }, { "3", "4", "5", }, { "6", "7", "8" },
					{ "9", ".", "AC" } };
			String[] symbols2 = new String[] { "+", "-", "*", "/", "=" };

			// create a grid pane for numbers
			GridPane gridPane1 = new GridPane();
			for (int i = 0; i < symbols1.length; i++) {
				for (int j = 0; j < symbols1[i].length; j++) {
					Button button = new Button(symbols1[i][j]);
					button.setOnAction(this);
					button.setPrefSize(50, 50);
					gridPane1.add(button, j, i);
				}
			}

			// create a grid pane for operators
			GridPane gridPane2 = new GridPane();
			for (int i = 0; i < symbols2.length; i++) {
				Button button = new Button(symbols2[i]);
				button.setOnAction(this);
				button.setPrefSize(120, 40);
				gridPane2.add(button, 0, i);
			}

			// arrange buttons
			VBox leftBox = new VBox(textField);
			leftBox.setPadding(new Insets(50));
			leftBox.setAlignment(Pos.CENTER);
			setLeft(leftBox);
			setRight(new HBox(gridPane1, gridPane2));

			Menu menu = new Menu("Operations");
			MenuItem menuItemAdd = new MenuItem("Add");
			MenuItem menuItemSubtact = new MenuItem("Subtact");
			MenuItem menuItemMultiply = new MenuItem("Multiply");
			MenuItem menuItemDivide = new MenuItem("Divide");
			MenuItem menuItemClear = new MenuItem("Clear");

			menuItemAdd.setOnAction(this);
			menuItemSubtact.setOnAction(this);
			menuItemMultiply.setOnAction(this);
			menuItemDivide.setOnAction(this);
			menuItemClear.setOnAction(this);
			menu.getItems().addAll(menuItemAdd, menuItemSubtact, menuItemMultiply, menuItemDivide, menuItemClear);
			MenuBar menuBar = new MenuBar(menu);
			setTop(menuBar);
		}

		// handle button event
		@Override
		public void handle(ActionEvent event) {
			String buttonSymbol = "";
			if (event.getSource() instanceof Button) {
				Button button = (Button) event.getSource();
				buttonSymbol = button.getText();
			} else if (event.getSource() instanceof MenuItem) {
				MenuItem menuItem = (MenuItem) event.getSource();
				buttonSymbol = menuItem.getText();

				if (buttonSymbol.equals("Add")) {
					buttonSymbol = "+";
				} else if (buttonSymbol.equals("Subtract")) {
					buttonSymbol = "-";
				} else if (buttonSymbol.equals("Multiply")) {
					buttonSymbol = "*";
				} else if (buttonSymbol.equals("Divide")) {
					buttonSymbol = "/";
				} else if (buttonSymbol.equals("Clear")) {
					buttonSymbol = "AC";
				}
			}

			if (buttonSymbol.equals("=")) {
				showResult();
				clear();
			} else if (isOperator(buttonSymbol)) {
				if (currentNum == 1) {
					// should input number now
					showResult();
					num1 = lastResult + "";
					num2 = "";
					currentNum = 1;
				} else {
					// if current number is first one, and first one is not empty, start to input
					// second number

					if (num1.isEmpty()) {
						// number 1 is not entered, copy last result to number 1
						num1 = lastResult + "";
					}

					// start to input number 2
					currentNum = 1;
					operator = buttonSymbol;

					textField.setText("0");
				}
			} else if (buttonSymbol.equals(".")) {
				// .
				if (currentNum == 0 && !num1.contains(".")) {
					num1 += ".";
				} else if (currentNum == 1 && !num2.contains(".")) {
					num2 += ".";
				}

				textField.setText(currentNum == 0 ? num1 : num2);
			} else if (buttonSymbol.equals("AC")) {
				// AC
				clear();
				textField.setText("0");
			} else {
				// numbers
				if (currentNum == 0) {
					num1 += buttonSymbol;
				} else if (currentNum == 1) {
					num2 += buttonSymbol;
				}

				if (currentNum == 0) {
					textField.setText(num1);
				} else {
					textField.setText(num2);
				}
			}
		}

		private boolean isOperator(String symbol) {
			return symbol.equals("+") || symbol.equals("-") || symbol.equals("*") || symbol.equals("/");
		}

		// calculate the result: result = num1 operator num2
		private void showResult() {
			if (num1.isEmpty()) {
				num1 = "0";
			}
			if (num2.isEmpty()) {
				num1 = "0";
			}

			double left = Double.parseDouble(num1);
			double right = Double.parseDouble(num2);
			double result = 0;
			
			if(operator.equals("+")) {
				result = left + right;
			}else if(operator.equals("-")) {
				result = left - right;
			}else if(operator.equals("*")) {
				result = left * right;
			}else if(operator.equals("/")) {
				if (right == 0) {
					result = 0;
				} else {
					result = left / right;
				}
			}
			
			textField.setText(result + "");
			num1 = result + "";
			lastResult = result;
			currentNum = 0;
		}

		void clear() {
			num1 = num2 = ""; // the left and right number
			operator = "+"; // the operator
			currentNum = 0;
			lastResult = 0;
		}
	}
}

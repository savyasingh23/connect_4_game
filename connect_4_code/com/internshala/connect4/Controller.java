package com.internshala.connect4;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {
	private static final int columns = 7;
	private static final int rows = 6;
	private static final int diameter = 80;

	private static final String discColor1 = "#24303E";
	private static final String discColor2 = "#4CAA88";

	private static String playerOne = "Player One";
	private static String playerTwo = "Player Two";

	private boolean is_player1_turn = true;

	private Disc[][] insertdiscArray = new Disc[rows][columns];
	@FXML
	public GridPane rootGridPane;
	@FXML
	public Pane insertedDiscsPane;
	@FXML
	public Label playerNameLabel;
	@FXML
	public TextField playerOneTextField,playerTwoTextField;
	@FXML
	public Button setNamesButton;

	private boolean isallowedtoinsert = true;

	public void createPlayground() {
		setNamesButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				String s = playerOneTextField.getText();
				playerOne = s;
				String s1 = playerTwoTextField.getText();
				playerTwo = s1;
				playerNameLabel.setText(s);
			}
		});
		Shape rectangleWithHoles = createGameStructuralGrid();
		rootGridPane.add(rectangleWithHoles,0,1);
		List<Rectangle> rectangleList = createClickableColumns();
		for (Rectangle rectangle:rectangleList) {
			rootGridPane.add(rectangle, 0, 1);
		}
	}

	private Shape createGameStructuralGrid() {

		Shape rectangleWithHoles = new Rectangle((columns+1) * diameter ,(rows+1) * diameter);
		for (int row =0;row<rows;row++) {
			for (int column = 0; column < columns; column++) {

				Circle circle = new Circle();
				circle.setRadius(diameter / 2);
				circle.setCenterX(diameter / 2);
				circle.setCenterY(diameter / 2);
				circle.setSmooth(true);
				circle.setTranslateX(column * (diameter+5) + (diameter/4));
				circle.setTranslateY(row * (diameter+5) + (diameter/4));
				rectangleWithHoles = Shape.subtract(rectangleWithHoles, circle);
			}
		}
		rectangleWithHoles.setFill(Color.WHITE);
		return rectangleWithHoles;
	}

	private List<Rectangle> createClickableColumns() {

		List<Rectangle> rectangleList = new ArrayList<>() ;

		for (int col =0;col<columns;col++) {
			Rectangle rectangle = new Rectangle(diameter, (rows + 1) * diameter);
			rectangle.setFill(Color.TRANSPARENT);
			rectangle.setTranslateX(col * (diameter+5) + (diameter/4));

			rectangle.setOnMouseEntered(mouseEvent -> rectangle.setFill(Color.valueOf("#EEEEEE26")));
			rectangle.setOnMouseExited(mouseEvent -> rectangle.setFill(Color.TRANSPARENT));
			final int column = col;
			rectangle.setOnMouseClicked(mouseEvent ->{
				if(isallowedtoinsert) {
					isallowedtoinsert=false;
					insertDisc(new Disc(is_player1_turn), column);
				}
		});
					rectangleList.add(rectangle);
		}
		return rectangleList;
	}

	private void insertDisc(Disc disc,int column ) {

		int row = rows-1;
		while(row>=0){
			if(getDiscIfPresent(row,column)==null)
				break;

			row--;
		}
		if(row<0) {
			return;
		}
		insertdiscArray [row][column]=disc;
		insertedDiscsPane.getChildren().add(disc);

		disc.setTranslateX(column * (diameter+5) + (diameter/4));
		int currentRow = row;
		TranslateTransition tt = new TranslateTransition(Duration.seconds(0.5),disc);
		tt.setToY(row * (diameter+5) + (diameter/4));
		tt.setOnFinished(actionEvent -> {

			isallowedtoinsert=true;
			if(gameEnded(currentRow,column)){
			gameOver();
			}

			is_player1_turn=!is_player1_turn;
			playerNameLabel.setText(is_player1_turn? playerOne:playerTwo);
		});
		tt.play();
	}

	private boolean gameEnded(int row, int col) {

		List<Point2D> verticalPoints = IntStream.rangeClosed(row-3,row+3)
										.mapToObj(r->new Point2D(r,col))
										.collect(Collectors.toList());
		List<Point2D> horizontalPoints = IntStream.rangeClosed(col-3,col+3)
				.mapToObj(c->new Point2D(row,c))
				.collect(Collectors.toList());

		Point2D startpoint1 = new Point2D(row-3,col+3);
		List<Point2D> diagonal1Points = IntStream.rangeClosed(0,6)
				.mapToObj(i->startpoint1.add(i,-i))
				.collect(Collectors.toList());

		Point2D startpoint2 = new Point2D(row-3,col-3);
		List<Point2D> diagonal2Points = IntStream.rangeClosed(0,6)
				.mapToObj(i->startpoint2.add(i,i))
				.collect(Collectors.toList());


		boolean isEnded = checkCombinations(verticalPoints)  || checkCombinations(horizontalPoints)
				|| checkCombinations(diagonal1Points) ||checkCombinations(diagonal2Points);
		return isEnded;
	}

	private boolean checkCombinations(List<Point2D> points) {

		int chain = 0;

			for (Point2D point:points) {
			int rowIndexForArray = (int) point.getX();
			int colIndexForArray = (int) point.getY();

			Disc disc = getDiscIfPresent(rowIndexForArray,colIndexForArray);

			if(disc!=null && disc.isplayeronemove==is_player1_turn) {

				chain++;
				if (chain == 4) {
					return true;
				}
			} else {
					chain=0;
				}
			}
			return false;
	}

	private Disc getDiscIfPresent(int row,int col) {
		if(row>=rows ||row<0 || col>=columns || col<0) {
			return null;
		}
		return insertdiscArray[row][col];
	}

	private void gameOver() {
			String winner = is_player1_turn? playerOne:playerTwo;
			System.out.println("The winner is "+winner);

			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Connect4");
			alert.setHeaderText("The Winner is "+winner);
			alert.setContentText("Want to play again?");

			ButtonType ybtn = new ButtonType("Yes");
			ButtonType nbtn = new ButtonType("No, Exit");
			alert.getButtonTypes().setAll(ybtn,nbtn);

			Platform.runLater( ()-> {
				Optional<ButtonType> optional = alert.showAndWait();
				if(optional.isPresent() && optional.get()==ybtn) {
					resetGame();
				}else {
					Platform.exit();
					System.exit(0);
				}

			});
	}

	public void resetGame() {

		insertedDiscsPane.getChildren().clear();
		for (int row=0;row<insertdiscArray.length;row++) {
			for(int col=0;col<insertdiscArray[row].length;col++) {
				insertdiscArray[row][col] = null;
			}
		}
		is_player1_turn=true;
		playerNameLabel.setText(playerOne);
		createPlayground();
	}

	private static class Disc extends Circle{
		private final boolean isplayeronemove;
		public Disc(boolean isplayeronemove){
			this.isplayeronemove = isplayeronemove;
			setRadius(diameter/2);
			setFill(isplayeronemove? Color.valueOf(discColor1):Color.valueOf(discColor2));
			setCenterX(diameter/2);
			setCenterY(diameter/2);
		}
	}
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

	}
}

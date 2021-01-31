package atj;

import java.util.Optional;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;

public class Main extends Application {

	public static final String appName = "Sockets-JavaFX-MVC";

	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root, 400, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getUserName() {
		TextInputDialog textInputDialog = new TextInputDialog("Anonymous");
		textInputDialog.setTitle("Title");
		textInputDialog.setHeaderText("Header");
		textInputDialog.setContentText("Content");
		Optional<String> result = textInputDialog.showAndWait();
		return result.orElse("Anonymous");
	}

	public static void main(String[] args) {

		ViewLoader<AnchorPane, ChatController> viewLoader = new ViewLoader<>("Chat.fxml");
		viewLoader.getController().setUserName(getUserName());
		viewLoader.getController().setHost("localhost");
		viewLoader.getController().setPort(9001);
		viewLoader.getController().run();
		Scene scene = new Scene(viewLoader.getLayout());
		primaryStage.setScene(scene);
		primaryStage.setTitle(appName);
		primaryStage.setOnHiding(e -> primaryStage_Hiding(e, viewLoader.getController()));
		primaryStage.show();

		launch(args);
	}

	private void primaryStage_Hiding(WindowEvent e, ChatController controller) {
		controller.close();
	}
}

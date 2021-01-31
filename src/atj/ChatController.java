package atj;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.w3c.dom.Document;

import com.sun.javafx.scene.paint.GradientUtils.Parser;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebView;

public class ChatController implements AutoCloseable {
	@FXML
	TextField messageTextField;
	@FXML
	Label welcomeLabel;
	@FXML
	WebView webViewMessages;
	@FXML
	Circle circleImage;
	@FXML
	ImageView sendImageView;

	private String senderName;
	private String userName = "";

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
		welcomeLabel.setText("Hello " + this.userName + "!");
		Image myImage = new Image(new File("res/harveyspecter.png").toURI().toString());
		ImagePattern pattern = new ImagePattern(myImage);
		circleImage.setFill(pattern);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	private String host;
	private String port;
	private Socket socket;
	private BufferedReader inputBufferedReader;
	private PrintWriter outputPrintWriter;
	private final int PROTOCOL_PREFIX_LENGTH = 3;
	private Document messagesLayout;
	Task<Void> task;

	@FXML
	private void initialize() {
		String welcome = "Nice to see you there!This is a welcome message. " + "Say hello to other users.";
		messagesLayout = Jsoup.parse("<html><head><meta charset='UTF-8'>"
				+ "</head><body><ul><li class=\"welcome\"><div class=\"message\"><div class=\"content\">" + welcome
				+ "</div></div></li></ul></body></html>", "UTF-16", Parser.xmlParser());
		webViewMessages.getEngine().loadContent(messagesLayout.html());
		webViewMessages.getEngine().setUserStyleSheetLocation(getClass().getResource("application.css").toString());
	}

	@FXML
	void close(ActionEvent event) {
		// close gniazdo
		// cancel task created in call method
	}

	@FXML
	void run() {
		socket = new Socket(host, PORT);
		inputBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		outputPrintWriter = new PrintWriter(socket.getOutputStream(), true);
		sendMessage(userName);
		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() throws IOException {
			}
		};
	}

	@FXML
	void call() {
		task = new Task<Void>() {
			@Override
			protected Void call() {
				try {
					while (true) {
						if (isCancelled()) {
							return null;
						}
						String msg = receiveMessage();
						showMessage(toHTML(decodeUID(msg), "response"));
						System.out.println(msg);
						Thread.sleep(100);
					}
				} catch (IOException | InterruptedException ex) {
					if (isCancelled()) {
						return null;
					}
				}
				return null;
			}
		};
		new Thread(task).start();

	}

	@FXML
	private void sendImageView_MouseReleased() {
		if (messageTextField.getLength() == 0)
			return;
		sendMessage(messageTextField.getText());
		showMessage(toHTML(messageTextField.getText(), "request"));
		messageTextField.clear();
	}

	@FXML
	private void messageTextField_KeyPressed(KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER) {
			sendImageView_MouseReleased();
		}
	}

}
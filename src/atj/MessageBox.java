package atj;

import java.io.IOException;

public class MessageBox {
	private String receiveMessage() throws IOException {
		return inputBufferedReader.readLine();
	}

	private void sendMessage(String message) {
		outputPrintWriter.println(message);
	}

	private void showMessage(Element message) {
		Element wrapper = messagesLayout.getElementsByTag("ul").first();
		wrapper.appendChild(message);
		Platform.runLater(new Runnable() {
			public void run() {
				webViewMessages.getEngine().loadContent(messagesLayout.html());
			}
		});
	}

	private Element toHTML(String message, String msgClass) {
		System.out.println("toHTML:" + message);
		Element wrapper = new Element("li").attr("class", msgClass);
		Element image = new Element("img").attr("class", "avatar").attr("src",
				new File("res/mikeross.png").toURI().toString());
		if (msgClass.equals("request")) {
			image.attr("src", new File("res/harveyspecter.png").toURI().toString());
			new Element("span").attr("class", "author").append(senderName).appendTo(wrapper);
		}
		image.appendTo(wrapper);
		Element message_div = new Element("div").attr("class", "message").appendTo(wrapper);
		new Element("div").attr("class", "content").append(message).appendTo(message_div);
		return wrapper;
	}

	private String decodeUID(String msg) {
		msg = msg.substring(PROTOCOL_PREFIX_LENGTH);
		char sep = (char) 31;
		String[] param = msg.split(String.valueOf(sep));
		senderName = param[0];
		return msg.substring(param[0].length() + 1);
	}

}

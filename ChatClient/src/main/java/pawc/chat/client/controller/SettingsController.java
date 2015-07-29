package pawc.chat.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SettingsController {

	@FXML private TextField nickfield;
	@FXML private TextField hostfield;
	@FXML private TextField portfield;
	@FXML private Button button;
	
	public void initialize(){
		
		nickfield.setText(Controller.nick);
		hostfield.setText(Controller.host);
		portfield.setText(String.valueOf(Controller.port));
		
		button.setOnAction(event->{
			Controller.nick=nickfield.getText();
			Controller.host=hostfield.getText();
			Controller.port=Integer.parseInt(portfield.getText());
			
		});
	}
	
}

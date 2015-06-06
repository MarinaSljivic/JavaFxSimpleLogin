package bildit.simple.login;

import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class LoginApp extends Application {

	Stage window;
	Scene scene1, scene2;
	public static CryptedUserMenagmentContext c = new CryptedUserMenagmentContext();
	@SuppressWarnings("rawtypes")
	ComboBox comboBox = new ComboBox();

	public static void main(String[] args) {
		// manual add of a user...
		// c.addUser("a", "a");
		// c.addUser("b", "b");
		// c.addUser("c", "c");
		// c.addUser("d", "d");
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) {

		// svi gridovi za scene
		GridPane LoginGrid = new GridPane();
		GridPane addUserGrid = new GridPane();
		GridPane removeUserGrid = new GridPane();
		GridPane adminMenuGrid = new GridPane();

		// podesavanje gridova -------------------------
		LoginGrid.setAlignment(Pos.CENTER);
		LoginGrid.setHgap(10);
		LoginGrid.setVgap(10);
		LoginGrid.setPadding(new Insets(25, 25, 25, 25));

		adminMenuGrid.setAlignment(Pos.CENTER);
		adminMenuGrid.setHgap(5);
		adminMenuGrid.setVgap(10);
		adminMenuGrid.setPadding(new Insets(25, 25, 25, 25));

		removeUserGrid.setAlignment(Pos.CENTER);
		removeUserGrid.setHgap(5);
		removeUserGrid.setVgap(10);
		removeUserGrid.setPadding(new Insets(25, 25, 25, 25));

		// sve scene -------------------------
		Scene sceneLogin = new Scene(LoginGrid, 300, 275);
		Scene sceneAddUser = new Scene(addUserGrid, 380, 300);
		Scene sceneRemoveUser = new Scene(removeUserGrid, 380, 300);
		Scene sceneAdminMenu = new Scene(adminMenuGrid, 380, 300);

		// login scena ---------------------------------------------------------

		primaryStage.setTitle("Login please");
		Text scenetitle = new Text("Welcome");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		LoginGrid.add(scenetitle, 0, 0, 2, 1);

		Label userName = new Label("User Name:");
		LoginGrid.add(userName, 0, 1);

		TextField userTextField = new TextField("");
		LoginGrid.add(userTextField, 1, 1);

		Label pw = new Label("Password:");
		LoginGrid.add(pw, 0, 2);

		PasswordField pwBox = new PasswordField();
		LoginGrid.add(pwBox, 1, 2);

		Button loginButton = new Button("Sign in");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(loginButton);
		LoginGrid.add(hbBtn, 1, 4);
		final Text actiontarget = new Text();
		LoginGrid.add(actiontarget, 1, 6);

		loginButton.setOnAction(e -> {

			actiontarget.setFill(Color.FIREBRICK);

			if (c.logIn(userTextField.getText(), pwBox.getText())) {
				actiontarget.setText("Login successful!");
				primaryStage.setScene(sceneAdminMenu);
				primaryStage.setTitle("-Admin Menu-");

			} else {
				actiontarget.setText("Login failed!");
			}
		});

		// scena za adminMenu ----------------------------------------
		Text scenetitleAdminMenu = new Text("Menu:");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

		Label menuLable = new Label("Users menagment:");
		adminMenuGrid.add(menuLable, 0, 1);

		Button addUserAdminMenu = new Button("Add");
		adminMenuGrid.add(addUserAdminMenu, 0, 2);

		Button removeUserAdminMenu = new Button("Remove");
		adminMenuGrid.add(removeUserAdminMenu, 0, 3);

		Button logoutButtonAdminMenu = new Button("LogOut");
		adminMenuGrid.add(logoutButtonAdminMenu, 20, 20);

		addUserAdminMenu.setOnAction(e -> {
			userTextField.setText("");
			pwBox.setText("");
			primaryStage.setScene(sceneAddUser);
			actiontarget.setText("");
			List<User> users = c.readCsv();

		});
		removeUserAdminMenu.setOnAction(e -> {

			updateUserComboBox();
			userTextField.setText("");
			pwBox.setText("");
			primaryStage.setScene(sceneRemoveUser);
			actiontarget.setText("");

		});

		logoutButtonAdminMenu.setOnAction(e -> {
			userTextField.setText("");
			pwBox.setText("");
			primaryStage.setScene(sceneLogin);
			actiontarget.setText("");
			primaryStage.setTitle("Login please");
			c.logOut();

		});

		// scena za addUsera ----------------------------------------
		Button addUserButton = new Button("addUser");
		addUserGrid.add(addUserButton, 1, 5);

		Button backButton = new Button("back");
		addUserGrid.add(backButton, 1, 13);

		addUserGrid.setHgap(10);
		addUserGrid.setVgap(10);
		addUserGrid.setPadding(new Insets(25, 25, 25, 25));

		Label addUserName = new Label("New User:");
		addUserGrid.add(addUserName, 0, 1);
		TextField addUserNameText = new TextField("");
		addUserGrid.add(addUserNameText, 1, 1);

		Label addUserPassword0 = new Label("New Password:");
		addUserGrid.add(addUserPassword0, 0, 2);
		PasswordField addUserPassword0Text = new PasswordField();
		addUserGrid.add(addUserPassword0Text, 1, 2);

		Label addUserPassword1 = new Label("Repeat Password:");
		addUserGrid.add(addUserPassword1, 0, 3);
		PasswordField addUserPassword1Text = new PasswordField();
		addUserGrid.add(addUserPassword1Text, 1, 3);

		final Text actiontargetLogIn = new Text();
		addUserGrid.add(actiontargetLogIn, 1, 6);

		backButton.setOnAction(e -> {
			userTextField.setText("");
			pwBox.setText("");
			addUserNameText.setText("");
			addUserPassword0Text.setText("");
			addUserPassword1Text.setText("");
			primaryStage.setScene(sceneAdminMenu);
			actiontarget.setText("");
			actiontargetLogIn.setText("");

		});

		addUserButton.setOnAction(e -> {

			boolean canAddUser = false;

			if (addUserNameText.getText().isEmpty()) {
				actiontargetLogIn.setFill(Color.FIREBRICK);
				actiontargetLogIn.setText("User name is null!");
				canAddUser = false;
			} else {

				if (addUserPassword0Text.getText().isEmpty()) {
					actiontargetLogIn.setFill(Color.FIREBRICK);
					actiontargetLogIn.setText("Passwords is Null!");
				} else {
					if (addUserPassword0Text.getText().length() < 8) {
						actiontargetLogIn.setFill(Color.FIREBRICK);
						actiontargetLogIn.setText("Passwords is too short!");
					} else {

						if (!(addUserPassword0Text.getText()
								.equals(addUserPassword1Text.getText()))) {
							actiontargetLogIn.setFill(Color.FIREBRICK);
							actiontargetLogIn
									.setText("Passwords do not match!");
							canAddUser = false;
						} else {
							canAddUser = true;
						}
					}
				}

			}

			if (canAddUser) {

				if (c.doesUserExist(addUserNameText.getText())) {
					actiontargetLogIn.setFill(Color.FIREBRICK);
					actiontargetLogIn.setText("User exist!");
				} else {
					c.addUser(addUserNameText.getText(),
							addUserPassword0Text.getText());
					actiontargetLogIn.setFill(Color.GREEN);
					actiontargetLogIn.setText("User is Added");
				}
			}

		});

		// scena za removeUsera ----------------------------------------
		Button backButtonRemove = new Button("back");
		removeUserGrid.add(backButtonRemove, 2, 19);

		Button Remove = new Button("remove");
		removeUserGrid.add(Remove, 4, 4);

		Label selectUserLable = new Label("Select user:");
		removeUserGrid.add(selectUserLable, 2, 4);

		final Text actiontargetremove = new Text();
		removeUserGrid.add(actiontargetremove, 3, 5);
		removeUserGrid.add(comboBox, 3, 4);

		backButtonRemove.setOnAction(e -> {

			userTextField.setText("");
			pwBox.setText("");
			addUserNameText.setText("");
			addUserPassword0Text.setText("");
			addUserPassword1Text.setText("");
			primaryStage.setScene(sceneAdminMenu);
			actiontarget.setText("");
			actiontargetLogIn.setText("");
			comboBox.setValue("Select user");

		});
		Remove.setOnAction(e -> {

			if (comboBox.getValue().equals("Select user")) {

				actiontargetremove.setFill(Color.FIREBRICK);
				actiontargetremove.setText("User not selected!");
			} else {
				c.removeUser((String) comboBox.getValue());

				actiontargetremove.setFill(Color.GREEN);
				actiontargetremove.setText((String) comboBox.getValue()
						+ "is removed!");

			}
			updateUserComboBox();
		});

		primaryStage.setScene(sceneLogin);
		primaryStage.show();
	}

	// metoda koja updetuje comboBox usera na osvnovu csv fajla
	public void updateUserComboBox() {
		List<User> users = c.readCsv();

		// clear the list
		while (!(comboBox.getItems().isEmpty())) {
			comboBox.getItems().remove(0);
		}
		for (User u : users) {
			// POPULATE THE FUCKING COMBO BOX HERE
			// and do not give user chance to delete himself
			if (!(c.getCurrentUser().equals(u.getUserName())))
				comboBox.getItems().add(u.getUserName());
		}
		comboBox.setValue("Select user");
	}
}

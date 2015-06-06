package bildit.simple.login;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CryptedUserMenagmentContext {

	final static String csvPath = "E:\\_all_workspaces\\bildit_workspace\\bilditJavaFxLoginCrypto\\src\\main\\java\\users.csv";
	final static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	static String currentUser;

	public static String getCurrentUser() {
		return currentUser;
	}

	// returns true if login is OK
	// sets curentUser sesion to that user
	public static boolean logIn(String userName, String userPassword) {
		boolean resultOfLogin = false;

		if (returnUser(userName) != null) {
			User foundUser = returnUser(userName);

			if (BCrypt.checkpw(userPassword, foundUser.getUserPass())) {
				resultOfLogin = true;
				currentUser = userName;
			}

		} else {
			// user does not exist
			resultOfLogin = false;
		}

		return resultOfLogin;

	}

	// adds user if that user is not taken
	public static void addUser(String userName, String userPassword) {

		List<User> Users = new ArrayList<User>();
		Users = readCsv();

		// if no users... write who cares... its empty..
		if (Users.isEmpty()) {
			FileWriter writer;
			try {

				writer = new FileWriter(csvPath);
				writer.append(userName + ","
						+ passwordEncoder.encode(userPassword) + "\n");
				writer.flush();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// not empty lest see whats happens
		} else {

			boolean noUserAddUser = false;
			// see if that user exists

			for (User u : Users) {

				if (userName.equals(u.getUserName())) {

					noUserAddUser = false;
					break;
				} else {
					noUserAddUser = true;
				}

			}

			// if there is no that user write the new user down
			if (noUserAddUser == true) {
				try {

					FileWriter writer = new FileWriter(csvPath);

					for (User u1 : Users) {
						writer.append(u1.getUserName() + "," + u1.getUserPass()
								+ "\n");
					}
					writer.append(userName + ","
							+ passwordEncoder.encode(userPassword) + "\n");
					writer.flush();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

	// returns list of all users and thair hashed passwords
	// directly from csv
	public static List<User> readCsv() {
		List<User> Users = new ArrayList<User>();

		BufferedReader bufferReader = null;
		String line = "";
		String cvsSplitBy = ",";
		try {

			bufferReader = new BufferedReader(new FileReader(csvPath));

			while ((line = bufferReader.readLine()) != null) {

				// use comma as separator
				String[] splitedLine = line.split(cvsSplitBy);

				User user = new User();
				user.setUserName(splitedLine[0]);
				user.setUserPass(splitedLine[1]);
				Users.add(user);

			}

		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			if (bufferReader != null) {
				try {
					bufferReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// System.out.println("---------------");
		// System.out.println("Reading csv file done");
		// System.out.println("---------------");
		return Users;
	}

	// returns user if exists returns null if not...
	public static User returnUser(String userName) {

		User resultUser = new User();
		List<User> Users = new ArrayList<User>();
		Users = readCsv();

		// see if that user exists
		for (User u : Users) {

			if (userName.equals(u.getUserName())) {
				resultUser = u;
				break;
			} else {
				resultUser = null;
			}

		}

		return resultUser;

	}

	// returns true if user Exists
	public static boolean doesUserExist(String userName) {
		boolean result = false;
		List<User> Users = readCsv();

		// see if that user exists
		for (User u : Users) {

			if (userName.equals(u.getUserName())) {
				result = true;
				break;
			} else {
				result = false;
			}

		}

		return result;

	}

	// remove user
	public static void removeUser(String userName) {

		List<User> Users = readCsv();
		for (User u : Users) {

			if (u.getUserName().equals(userName)) {
				Users.remove(u);
				break;
			}

		}

		try {

			FileWriter writer = new FileWriter(csvPath);

			for (User u : Users) {
				writer.append(u.getUserName() + "," + u.getUserPass() + "\n");
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// loging out the current user
	public static void logOut() {
		currentUser = null;
	}
}

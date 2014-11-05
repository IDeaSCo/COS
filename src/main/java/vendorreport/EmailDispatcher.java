package vendorreport;

import java.util.TimerTask;

import com.ideas.controller.CreateExcelFile;

public class EmailDispatcher extends TimerTask {

	public void run() {

		String from = "khoorafati@gmail.com";
		String pass = "ideas@1234";
		String[] to = { "mittal.prachi22@gmail.com" }; // list of recipient
														// email addresses
		String subject = "Java send mail example";
		String body = "Welcome to JavaMail!";

		try {
			CreateExcelFile createExcelFile = new CreateExcelFile();
			createExcelFile.createExcel();
			Email.sendFromGMail(from, pass, to, subject, body);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Email Sent Succesfully...");

	}
}

package vendorReport;

import java.util.*;
import java.io.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

public class Email {

    private static String USER_NAME = "khoorafati";  // GMail user name (just the part before "@gmail.com")
    private static String PASSWORD = "ideas@1234"; // GMail password
    private static String[] RECIPIENT = {"mittal.prachi22@gmail.com"};

    public static void main(String[] args) {
Date date = new Date();
		//        String from = USER_NAME+"@gmail.com";
//        String pass = PASSWORD;
//        String[] to = RECIPIENT; // list of recipient email addresses
//        String subject = "Java send mail example";
//        String body = "Welcome to JavaMail!";
//
//        sendFromGMail(from, pass, to, subject, body);
        System.out.println(date);
    }

    static void sendFromGMail(String from, String pass, String[] to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        String filePath = "c:/data.xlsx";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Here's the file");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filePath);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(new File(filePath).getName());
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}
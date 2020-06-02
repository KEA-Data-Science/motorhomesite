package kea.motorhome.motorhomesite.mail;
// by kcn

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Class enables the sending of emails from a Gmail account.
 * NOI: Access to project specifically-configured SimpleMessageSender
 * open through static method.
 */
@Component
public class SimpleMessageSender
{
    private String user;
    private String password;
    private JavaMailSender javaMailSender;

    public SimpleMessageSender(String user, String password)
    {
        this.user = user;
        this.password = password;
        this.javaMailSender = getJavaMailSender();
    }

    /**
     * Return a SimpleMessageSender, user and password uninitialized.
     */
    public SimpleMessageSender(){}

    private JavaMailSender getJavaMailSender()
    {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(user);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");


        return mailSender;
    }

    /**
     * Set user and password fields, and configure JavaMailSender */
    public void configureMailDetails(String emailString, String password){
        user = emailString;
        this.password = password;
        javaMailSender = getJavaMailSender();
    }

    /**
     * Return a SimpleMessageSender, user and password initialized to mail Nordic Motorhome user/pass.
     */
    public static SimpleMessageSender motorhomeStandardConnection()
    {
        return new SimpleMessageSender("nordicmotorhomerental@gmail.com", "NordicMotorhome!2020");
    }

    public void sendEmail(String recipient, String subject, String text)
    {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(recipient);
        message.setSubject(subject);
        message.setText(text);

        javaMailSender.send(message);
    }

}
/* Helpful tutorials made this possible:
 * https://www.baeldung.com/spring-email
 * https://mkyong.com/spring-boot/spring-boot-how-to-send-email-via-smtp/
 *  */

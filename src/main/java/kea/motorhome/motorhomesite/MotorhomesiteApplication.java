package kea.motorhome.motorhomesite;

import kea.motorhome.motorhomesite.mail.SimpleMessageSender;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@SpringBootApplication
public class MotorhomesiteApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(MotorhomesiteApplication.class, args);

        /* Message sent is intended for debugging and general logging */
        SimpleMessageSender.motorhomeStandardConnection().sendEmail("nordicmotorhomerental@gmail.com",
                                            "Server Restart at " + LocalDateTime.now(),
                                            "Automated Restart Message.");
    }

}

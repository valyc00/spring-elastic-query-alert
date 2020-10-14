package it.eng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by rajeevkumarsingh on 02/08/17.
 */
@Component
public class ScheduledTasks {
	
	@Autowired
    private JavaMailSender javaMailSender;
	
	@Autowired
	ElasticQueryService elasticQueryService;
	int fixedDelay = 2000;

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Scheduled(fixedRateString  = "${fixedRateString}")
    public void scheduleTaskWithFixedRate() {
        logger.info("Fixed Rate Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()) );
        
        elasticQueryService.query();
        
    }

//    @Scheduled(fixedDelay = 2000)
//    public void scheduleTaskWithFixedDelay() {
//        logger.info("Fixed Delay Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
//        try {
//            TimeUnit.SECONDS.sleep(5);
//        } catch (InterruptedException ex) {
//            logger.error("Ran into an error {}", ex);
//            throw new IllegalStateException(ex);
//        }
//    }
//
//    @Scheduled(fixedRate = 2000, initialDelay = 5000)
//    public void scheduleTaskWithInitialDelay() {
//        logger.info("Fixed Rate Task with Initial Delay :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
//    }
//
//    @Scheduled(cron="${cronExpression}")
//    public void scheduleTaskWithCronExpression() {
//        logger.info("Cron Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
//    }
    
    
    
    public void sendEmail() {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("valyc0@gmail.com");

        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello World \n Spring Boot Email");

        javaMailSender.send(msg);

    }
    
    public void sendEmailWithAttachment() throws MessagingException, IOException {

        MimeMessage msg = javaMailSender.createMimeMessage();

        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo("1@gmail.com");

        helper.setSubject("Testing from Spring Boot");

        // default = text/plain
        //helper.setText("Check attachment for image!");

        // true = text/html
        helper.setText("<h1>Check attachment for image!</h1>", true);

        //FileSystemResource file = new FileSystemResource(new File("classpath:android.png"));

        //Resource resource = new ClassPathResource("android.png");
        //InputStream input = resource.getInputStream();

        //ResourceUtils.getFile("classpath:android.png");

        helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));

        javaMailSender.send(msg);

    }


}
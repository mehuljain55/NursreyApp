package com.ayush.nursery.serviceImpl;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class BackupService {

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Autowired
    private MailingService mailingService;

    private static final String EMAIL_ID = "cafeconnet02@gmail.com\n";
    private static final String DB_NAME = "test";
    private static final String DB_HOST = "69.62.85.76";
    private static final String BACKUP_DIRECTORY = "resources" + File.separator +"Test"+File.separator +"Backup";

    @Scheduled(cron = "0 0 2 * * ?") // Daily at 5 AM
    public void scheduledBackup() {
        backupDatabaseAndSendMail();
    }

    public void createBackup() {
        backupDatabaseAndSendMail();
    }

    @Async("backupTaskExecutor")
    public void backupDatabaseAndSendMail() {
        System.out.println("Backup process started...");

        try {
            String timestamp = new SimpleDateFormat("dd_MM_yyyy").format(new Date());
            String projectDir = System.getProperty("user.dir");

            String backupDirPath = projectDir + File.separator + BACKUP_DIRECTORY;
            String backupFilePath = backupDirPath + File.separator + "Backup_" + timestamp + ".sql";

            // Ensure directory exists
            new File(backupDirPath).mkdirs();

            String mysqldumpPath = findMySQLDump();
            if (mysqldumpPath == null) {
                System.err.println("mysqldump not found");
                return;
            }

            boolean success = createDatabaseBackup(mysqldumpPath, backupFilePath);

            if (success) {
                System.out.println("Backup created: " + backupFilePath);
                sendBackupFileMail(backupFilePath);
            } else {
                System.err.println("Backup failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean createDatabaseBackup(String mysqldumpPath, String filePath) {
        try {
            List<String> command;
            String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);

            if (os.contains("win")) {
                command = List.of(
                        mysqldumpPath,
                        "-u" + dbUser,
                        "-p" + dbPassword,
                        "-h", DB_HOST,
                        DB_NAME,
                        "--result-file=" + filePath
                );
            } else {
                command = List.of(
                        "bash", "-c",
                        String.format(
                                "%s -u%s -p%s -h %s %s > %s",
                                mysqldumpPath, dbUser, dbPassword, DB_HOST, DB_NAME, filePath
                        )
                );
            }

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            try (BufferedReader reader =
                         new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            int exitCode = process.waitFor();
            return exitCode == 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void sendBackupFileMail(String filePath) throws MessagingException {
        String date = getFormattedDate(new Date());
        String time = getFormattedTime(new Date());

        String subject = "Database Backup - " + date;
        String body = mailingService.createMailTemplate(date, time);

        Path path = Paths.get(filePath).toAbsolutePath();
        File attachment = path.toFile();

        if (attachment.exists()) {
            mailingService.sendMail(EMAIL_ID, subject, body, attachment);
            System.out.println("Backup mail sent successfully");
        } else {
            System.err.println("Backup file not found for email");
        }
    }

    private String findMySQLDump() {
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (os.contains("win")) {
            return "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump.exe";
        } else {
            return "/usr/bin/mysqldump"; // Linux VPS
        }
    }

    private String getFormattedDate(Date date) {
        return new SimpleDateFormat("dd-MM-yyyy").format(date);
    }

    private String getFormattedTime(Date date) {
        return new SimpleDateFormat("hh:mm:ss a").format(date);
    }

}

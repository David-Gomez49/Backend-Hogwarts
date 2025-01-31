package com.api.backend.services;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String student, String subject) throws MessagingException {
        String studentFormatted = capitalizeWords(student);
        String subjectFormatted = capitalizeWords(subject);

        String body = String.format(
                "<p>Cordial saludo,</p> "
                        + "<p>El estudiante <strong>%s</strong> presenta dificultades en la materia de <strong>%s</strong>. </p> "
                        + "<p>Por favor, revise la página en el siguiente enlace para más información:</p> "
                        + "<p><a href='https://frontend-hogwarts.vercel.app/' target='_blank'>Ir a Hogwarts Academy</a></p> "
                        + "<p>Cordialmente,<br>Hogwarts Academy</p>",
                studentFormatted, subjectFormatted);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("Hogwarts Academy - Alerta Académica - " + studentFormatted);
        helper.setText(body, true); // Importante: `true` indica que es HTML
        helper.setFrom("hogwarts.academy.udea@gmail.com");

        mailSender.send(message);
    }

    public static String capitalizeWords(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return Arrays.stream(input.split("\\s+")) // Divide en palabras
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase()) // Capitaliza
                .collect(Collectors.joining(" ")); // Une las palabras nuevamente
    }
}

package com.tcc.seboonline.servicos;

import com.tcc.seboonline.controladores.RecuperarSenhaController;
import com.tcc.seboonline.interfaces.MailService;
import com.tcc.seboonline.modelos.LivroUsuario;
import com.tcc.seboonline.modelos.PerfilUsuario;
import com.tcc.seboonline.modelos.Usuario;
import com.tcc.seboonline.repositorios.PerfilUsuarioRepository;
import com.tcc.seboonline.repositorios.UsuarioRepository;
import com.tcc.seboonline.utils.EnumEmail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class EmailServiceImpl implements MailService {

    private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);


    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private PerfilUsuarioRepository usuarioRepository;


    @Override
    public void sendEmail(String email) {
        Optional<Usuario> usuario = usuarioRepo.findByEmail(email);

        if (usuario.isPresent()) {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            try {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

                String emailTcc = EnumEmail.CLIENTE_1;
                String assunto = EnumEmail.ASSUNTO;
                helper.setFrom(emailTcc);
                helper.setTo(usuario.get().getEmail());
                helper.setSubject(assunto);

                String body = "Olá, conforme solicitado no Sistema SeboOnline Sr(a) " + usuario.get().getFirstName() + " " + usuario.get().getLastName() + " a recuperação da sua senha, é : " + usuario.get().getPassword();

                helper.setText(body);
                mailSender.send(mimeMessage);

                LOGGER.info("Email enviado com sucesso para: " + usuario.get().getEmail());
            } catch (MessagingException e) {
                LOGGER.error("Erro ao criar mensagem de email: " + e.getMessage(), e);
                throw new RuntimeException(e);
            }
        } else {
            LOGGER.error("Usuário não encontrado para o email: " + email);
        }
    }

    @Override
    public void sendEmailUsuario(LivroUsuario livroUsuario, Usuario usuario) {

        if (livroUsuario != null) {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            try {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                String emailTcc = EnumEmail.CLIENTE_1;
                String assunto = EnumEmail.ASSUNTO_TROCA;

                helper.setFrom(emailTcc);
                helper.setTo(livroUsuario.getEmail());
                helper.setSubject(assunto);

                Optional<PerfilUsuario>  perfilUsuario = usuarioRepository.findByOwner(usuario);

                String body = "Olá, eu visualizei seu post no site SeboOnline e gostei muito da descrição do livro "
                        + livroUsuario.getNome_livro()
                        + " Me chamo: " + usuario.getFirstName() + " " + usuario.getLastName() + "\n"
                        + "Meu Whatsapp é: " + perfilUsuario.get().getPhoneNumber() + "\n"
                        + "E o meu e-mail é: " + usuario.getEmail() + "\n"
                        + " Aguardo o seu contato para podermos conversar "  + "\n"
                        + " Atenciosamente"+ "\n"
                        + usuario.getFirstName();

                helper.setText(body);
                mailSender.send(mimeMessage);

                LOGGER.info("Email enviado com sucesso para: " + livroUsuario.getEmail());
            } catch (MessagingException e) {
                LOGGER.error("Erro ao criar mensagem de email: " + e.getMessage(), e);
                throw new RuntimeException(e);
            }
        } else {
            LOGGER.error("Usuário não encontrado para o email: " + livroUsuario.getEmail());
        }
    }

}

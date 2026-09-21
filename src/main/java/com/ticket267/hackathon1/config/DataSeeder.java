package com.ticket267.hackathon1.config;

import com.ticket267.hackathon1.model.Player;
import com.ticket267.hackathon1.model.Role;
import com.ticket267.hackathon1.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final PlayerRepository repo;
    private final PasswordEncoder encoder;

    @Value("${app.seed.admin.username:admin}")
    private String adminUser;

    @Value("${app.seed.admin.password:admin123}")
    private String adminPass;

    @Value("${app.seed.admin.email:admin@tuckersoft.com}")
    private String adminEmail;

    @Value("${app.seed.user.username:user}")
    private String userUser;

    @Value("${app.seed.user.password:user123}")
    private String userPass;

    @Value("${app.seed.user.email:user@tuckersoft.com}")
    private String userEmail;

    @Override
    public void run(String... args) {
        seed(adminUser, adminEmail, adminPass, Role.ADMIN);
        seed(userUser,  userEmail,  userPass,  Role.USER);
    }

    private void seed(String username, String email, String rawPass, Role role) {
        if (repo.existsByUsername(username)) {
            log.info("Seeder: {} ya existe, no se crea.", username);
            return;
        }
        Player p = Player.builder()
                .username(username)
                .email(email)
                .passwordHash(encoder.encode(rawPass))
                .role(role)
                .build();
        repo.save(p);
        log.info("Seeder: {} ({}) creado.", username, role);
    }
}

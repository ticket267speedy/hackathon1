package com.tuckersoft.branchengine.config;
import com.tuckersoft.branchengine.model.*;
import com.tuckersoft.branchengine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
@Component @RequiredArgsConstructor @Slf4j
public class DataSeeder implements CommandLineRunner {
    private final UserRepository repo; private final PasswordEncoder encoder;
    @Value("${app.seed.admin.username:admin}") private String aU;
    @Value("${app.seed.admin.password:admin123}") private String aP;
    @Value("${app.seed.admin.email:admin@tuckersoft.com}") private String aE;
    @Value("${app.seed.user.username:user}") private String uU;
    @Value("${app.seed.user.password:user123}") private String uP;
    @Value("${app.seed.user.email:user@tuckersoft.com}") private String uE;
    @Override public void run(String... args){seed(aU,aE,aP,Role.ADMIN); seed(uU,uE,uP,Role.USER);}
    private void seed(String un, String em, String pw, Role role){
        if (repo.existsByUsername(un)){log.info("Seeder: {} ya existe",un);return;}
        repo.save(User.builder().username(un).email(em).passwordHash(encoder.encode(pw)).role(role).build());
        log.info("Seeder: {} ({}) creado", un, role);
    }
}

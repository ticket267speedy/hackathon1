package com.tuckersoft.branchengine.config;
import com.tuckersoft.branchengine.dto.ErrorResponse;
import com.tuckersoft.branchengine.security.CustomUserDetailsService;
import com.tuckersoft.branchengine.security.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;
@Configuration @EnableMethodSecurity @RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtFilter;
    private final CustomUserDetailsService uds;
    private final ObjectMapper mapper;
    @Bean public PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder(10);}
    @Bean public DaoAuthenticationProvider daoAuthenticationProvider(){
        var p=new DaoAuthenticationProvider(); p.setUserDetailsService(uds); p.setPasswordEncoder(passwordEncoder()); return p;
    }
    @Bean public AuthenticationManager authenticationManager(AuthenticationConfiguration c) throws Exception {return c.getAuthenticationManager();}
    @Bean public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).cors(c->{})
            .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(a->a
                .requestMatchers("/api/v1/auth/register","/api/v1/auth/login","/api/v1/auth/refresh").permitAll()
                .requestMatchers("/error").permitAll()
                .anyRequest().authenticated())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(e->e
                .authenticationEntryPoint((rq,rs,ex)->writeJson(rs,rq.getRequestURI(),HttpStatus.UNAUTHORIZED,"UNAUTHORIZED","No autenticado o token invalido"))
                .accessDeniedHandler((rq,rs,ex)->writeJson(rs,rq.getRequestURI(),HttpStatus.FORBIDDEN,"FORBIDDEN","No tienes permiso")));
        return http.build();
    }
    private void writeJson(jakarta.servlet.http.HttpServletResponse res,String path,HttpStatus s,String c,String m) throws IOException {
        res.setStatus(s.value()); res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.getWriter().write(mapper.writeValueAsString(ErrorResponse.of(c,m,path)));
    }
}

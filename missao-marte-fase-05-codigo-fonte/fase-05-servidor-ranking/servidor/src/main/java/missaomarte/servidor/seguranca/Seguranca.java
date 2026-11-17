package missaomarte.servidor.seguranca;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Duration;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class Seguranca {
    @Bean PasswordEncoder senhas() { return new BCryptPasswordEncoder(10); }

    @Bean SecretKey chave(@Value("${missaomarte.jwt.segredo}") String configurado) {
        byte[] bytes = configurado.getBytes(StandardCharsets.UTF_8);
        if (configurado.isEmpty()) {
            bytes = new byte[32];
            new SecureRandom().nextBytes(bytes);
            LoggerFactory.getLogger(Seguranca.class).info("Segredo JWT temporário. Tokens expiram ao reiniciar.");
        } else if (bytes.length < 32) {
            throw new IllegalArgumentException("MISSAOMARTE_JWT_SEGREDO deve ter pelo menos 32 bytes.");
        }
        return new SecretKeySpec(bytes, "HmacSHA256");
    }

    @Bean JwtEncoder emissor(SecretKey chave) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(chave));
    }

    @Bean JwtDecoder verificador(SecretKey chave) {
        var decoder = NimbusJwtDecoder.withSecretKey(chave).build();
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                new JwtTimestampValidator(Duration.ZERO),
                new JwtClaimValidator<java.time.Instant>("exp", exp -> exp != null),
                new JwtClaimValidator<String>("sub", sub -> sub != null && sub.matches("[1-9][0-9]{0,17}"))));
        return decoder;
    }

    @Bean SecurityFilterChain proteger(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> a
                        .requestMatchers(HttpMethod.POST, "/api/usuarios", "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(e -> e.authenticationEntryPoint((req, res, ex) -> naoAutorizado(res)))
                .oauth2ResourceServer(o -> o.authenticationEntryPoint((req, res, ex) -> naoAutorizado(res))
                        .jwt(Customizer.withDefaults()))
                .build();
    }

    private void naoAutorizado(HttpServletResponse resposta) throws java.io.IOException {
        resposta.setStatus(401);
        resposta.setCharacterEncoding("UTF-8");
        resposta.setContentType("application/problem+json");
        resposta.setHeader("WWW-Authenticate", "Bearer");
        resposta.getWriter().write("""
                {"type":"about:blank","title":"Unauthorized","status":401,"detail":"Autenticação necessária ou sessão expirada."}
                """);
    }
}

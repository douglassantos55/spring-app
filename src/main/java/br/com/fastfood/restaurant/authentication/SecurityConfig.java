package br.com.fastfood.restaurant.authentication;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.NullSecurityContextRepository;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${jwt.public_key}")
    private RSAPublicKey key;

    @Value("${jwt.private_key}")
    private RSAPrivateKey priv;
    private final br.com.fastfood.restaurant.repository.CustomerRepository customerRepository;

    public SecurityConfig(br.com.fastfood.restaurant.repository.CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK key = new RSAKey.Builder(this.key).privateKey(this.priv).build();
        JWKSource<SecurityContext> source = new ImmutableJWKSet(new JWKSet(key));
        return new NimbusJwtEncoder(source);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.key).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomerRepository repository(DataSource dataSource) {
        return new CustomerRepository(dataSource);
    }

    public AuthenticationProvider customersProvider(CustomerRepository repository) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(repository);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    public AuthenticationProvider adminProvider(DataSource dataSource) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(new JdbcUserDetailsManager(dataSource));
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomerRepository repository, DataSource dataSource) throws Exception {
        return http
                .authorizeHttpRequests()
                .requestMatchers("/customers")
                .hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .csrf()
                .disable()
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .securityContext(context -> context.securityContextRepository(new NullSecurityContextRepository()))
                .authenticationProvider(customersProvider(repository))
                .authenticationProvider(adminProvider(dataSource))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                )
                .formLogin(login -> login
                        .loginProcessingUrl("/auth")
                        .successHandler(new JwtAuthenticationSuccessHandler(jwtEncoder()))
                        .failureHandler(new JwtAuthenticationFailureHandler())
                )
                .build();
    }
}

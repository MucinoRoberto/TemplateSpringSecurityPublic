package dev.mucino.backend.security;

import java.util.Arrays;

// import org.apache.catalina.filters.CorsFilter;
// import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
// import org.springframework.core.Ordered;
// import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import dev.mucino.backend.security.filter.JwtAuthenticationFilter;
import dev.mucino.backend.security.filter.JwtValidateFilter;

@Configuration 
@EnableMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {

    private AuthenticationConfiguration authenticationConfiguration;
    private final CustomPermissionEvaluator customPermissionEvaluator;
    public SpringSecurityConfig(AuthenticationConfiguration authenticationConfiguration, CustomPermissionEvaluator customPermissionEvaluator) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.customPermissionEvaluator = customPermissionEvaluator;
    }

    @Bean 
    AuthenticationManager authenticationManager (){
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean 
    PasswordEncoder passwordEncoder (){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MethodSecurityExpressionHandler expressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(customPermissionEvaluator);
        return expressionHandler;
    }

    @Bean 
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        return http.authorizeHttpRequests(auth -> auth
            // .requestMatchers(HttpMethod.GET,"/api/users").permitAll()
            
            .requestMatchers(HttpMethod.POST, "/login").permitAll()
            .requestMatchers(HttpMethod.POST,"/api/users/register").permitAll()
            .anyRequest().authenticated())
            .csrf(config -> config.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .addFilter(new JwtAuthenticationFilter(authenticationManager()))
            .addFilter(new JwtValidateFilter(authenticationManager()))
            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();
    }

   @Bean 
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("http://localhost:5174", "https://www.mucinoapp.com"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

   

}

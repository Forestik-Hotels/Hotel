package com.hotels.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.hotels.filters.AccessTokenAuthenticationFilter;
import com.hotels.jwt.JwtTool;
import com.hotels.providers.JwtAuthenticationProvider;
import com.hotels.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static com.hotels.constant.AppConstant.ADMIN;
import static com.hotels.constant.AppConstant.USER;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

/**
 * Config for security.
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTool jwtTool;
    private final UserService userService;

    /**
     * Constructor.
     */

    @Autowired
    public SecurityConfig(JwtTool jwtTool, UserService userService) {
        this.jwtTool = jwtTool;
        this.userService = userService;
    }

    /**
     * Bean {@link PasswordEncoder} that uses in coding password.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Method for configure security.
     *
     * @param http {@link HttpSecurity}
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
            .and()
            .csrf()
            .disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(
                new AccessTokenAuthenticationFilter(jwtTool, authenticationManager(), userService),
                UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint((req, resp, exc) -> resp.sendError(SC_UNAUTHORIZED, "Authorize first."))
            .accessDeniedHandler((req, resp, exc) -> resp.sendError(SC_FORBIDDEN, "You don't have authorities."))
            .and()
            .authorizeRequests()
            .antMatchers("/css/**",
                "/js/**",
                "/img/**",
                "/home/**",
                "/registration",
                "/login",
                "/signUp",
                "/signIn")
            .permitAll()
            .antMatchers(HttpMethod.GET,
                "/booking/**",
                "/hotel",
                "/googleSecurity/**",
                "/facebookSecurity/**",
                "/ownSecurity/verifyEmail",
                "/ownSecurity/updateAccessToken",
                "/ownSecurity/restorePassword",
                "/googleSecurity",
                "/user/info/{id}",
                "/user/emailNotifications",
                "/user/activatedUsersAmount",
                "/user/{userId}/habit/assign")
            .permitAll()
            .antMatchers(HttpMethod.POST,
                "/booking/cancel/*",
                "/ownSecurity/signUp",
                "/ownSecurity/signIn",
                "/ownSecurity/changePassword")
            .permitAll()
            .antMatchers(HttpMethod.PATCH,
                "/user/info")
            .permitAll()
            .antMatchers(HttpMethod.GET,
                "/hotel/**",
                "/room/**",
                "/user",
                "/user/{userId}/profile/",
                "/user/isOnline/{userId}/",
                "/user/{userId}/profileStatistics/",
                "/user/userAndSixFriendsWithOnlineStatus",
                "/user/userAndAllFriendsWithOnlineStatus",
                "/user/{userId}/recommendedFriends/",
                "/user/{userId}/friends/")
            .hasAnyRole(USER, ADMIN)
            .antMatchers(HttpMethod.PUT,
                "/ownSecurity",
                "/user/profile")
            .hasAnyRole(USER, ADMIN).antMatchers(HttpMethod.PUT,
                "/ownSecurity",
                "/user/profile")
            .hasAnyRole(USER, ADMIN).antMatchers(HttpMethod.POST,
                "/booking",
                "/user/profile")
            .hasAnyRole(USER, ADMIN)
            .antMatchers(HttpMethod.PATCH,
                "/user/profilePicture",
                "/user/deleteProfilePicture")
            .hasAnyRole(USER, ADMIN)
            .antMatchers(HttpMethod.GET,
                "/user/all",
                "/user/roles")
            .hasAnyRole(ADMIN)
            .antMatchers(HttpMethod.POST,
                "/hotel/**",
                "/booking/**",
                "/place/filter/predicate")
            .hasAnyRole(ADMIN)
            .antMatchers(HttpMethod.PUT,
                "/hotel/**",
                "/place/update/")
            .hasAnyRole(ADMIN)
            .antMatchers(HttpMethod.PATCH,
                "/user",
                "/user/status",
                "/user/role",
                "/user/update/role")
            .hasRole(ADMIN)
            .anyRequest().hasAnyRole(ADMIN);
    }

    /**
     * Method for configure matchers that will be ignored in security.
     *
     * @param web {@link WebSecurity}
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs/**");
        web.ignoring().antMatchers("/swagger.json");
        web.ignoring().antMatchers("/swagger-ui.html");
        web.ignoring().antMatchers("/swagger-resources/**");
        web.ignoring().antMatchers("/webjars/**");
    }

    /**
     * Method for configure type of authentication provider.
     *
     * @param auth {@link AuthenticationManagerBuilder}
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(new JwtAuthenticationProvider(jwtTool));
    }

    /**
     * Provides AuthenticationManager.
     *
     * @return {@link AuthenticationManager}
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * Bean {@link CorsConfigurationSource} that uses for CORS setup.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Collections.singletonList("http://localhost:8080"));
        configuration.setAllowedMethods(
            Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
        configuration.setAllowedHeaders(
            Arrays.asList(
                "X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization",
                "Access-Control-Allow-Origin"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Bean {@link GoogleIdTokenVerifier} that uses in verify googleIdToken.
     *
     * @param clientId {@link String} - google client id.
     */
    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier(@Value("${google.clientId}") String clientId) {
        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
            .setAudience(Collections.singletonList(clientId))
            .build();
    }
}

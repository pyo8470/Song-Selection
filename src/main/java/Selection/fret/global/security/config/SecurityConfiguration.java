package Selection.fret.global.security.config;

import Selection.fret.domain.member.mapper.MemberMapper;
import Selection.fret.domain.member.repository.MemberRepository;
import Selection.fret.domain.member.service.MemberService;
import Selection.fret.global.jwt.JwtTokenizer;
import Selection.fret.global.security.auth.filter.JwtAuthenticationFilter;
import Selection.fret.global.security.auth.filter.JwtVerificationFilter;
import Selection.fret.global.security.auth.handler.MemberAccessDeniedHandler;
import Selection.fret.global.security.auth.handler.MemberAuthenticationEntryPoint;
import Selection.fret.global.security.auth.handler.MemberAuthenticationFailureHandler;
import Selection.fret.global.security.auth.handler.MemberAuthenticationSuccessHandler;
import Selection.fret.global.security.auth.utils.CustomAuthorityUtils;
import Selection.fret.global.security.token.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
@Configuration
@Slf4j
@RequiredArgsConstructor
public class SecurityConfiguration  {

    @Value("${spring.security.oauth2.client.registration.google.clientId}")  // (1)
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.clientSecret}") // (2)
    private String clientSecret;
    private final JwtTokenizer jwtTokenizer;
    private final MemberService memberService;
    private final CustomAuthorityUtils authorityUtils;
    private final TokenService tokenService;
    private final MemberMapper memberMapper;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .headers().frameOptions().sameOrigin()
                .and()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new MemberAuthenticationEntryPoint())
                .accessDeniedHandler(new MemberAccessDeniedHandler())
                .and()
                .apply(new CustomFilterConfigurer())
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/users/signup")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/users/login")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/users/logout")).hasAnyRole("USER", "ADMIN")
                        .anyRequest().permitAll()
                )
                .logout().permitAll();
        return httpSecurity.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        // 허용할 클라이언트 도메인
        configuration.setAllowedOrigins(List.of("http://localhost:3000","https://localhost:3000","https://music-select-helper.web.app"));
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {


        @Override
        public void configure(HttpSecurity builder)  {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

            JwtAuthenticationFilter jwtAuthenticationFilter =
                    new JwtAuthenticationFilter(authenticationManager, jwtTokenizer, memberService,tokenService, memberMapper);

            jwtAuthenticationFilter.setFilterProcessesUrl("/api/users/login");
            // Exception 추가
            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new MemberAuthenticationSuccessHandler());
            jwtAuthenticationFilter.setAuthenticationFailureHandler(new MemberAuthenticationFailureHandler());


            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, authorityUtils);

            builder
                    .addFilter(jwtAuthenticationFilter)
                    .addFilterAfter(jwtVerificationFilter, OAuth2LoginAuthenticationFilter.class);
        }
        @Bean
        public ClientRegistrationRepository clientRegistrationRepository() {
            var clientRegistration = clientRegistration();    // (3-1)
            return new InMemoryClientRegistrationRepository(clientRegistration);   // (3-2)
        }

        // (4)
        private ClientRegistration clientRegistration() {
            // (4-1)
            return CommonOAuth2Provider
                    .GOOGLE
                    .getBuilder("google")
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .scope("openid", "profile", "email")
                    .build();
        }
    }
}
package bg.documentor.config.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Configuration @EnableWebSecurity @EnableGlobalMethodSecurity(prePostEnabled = true) public class SecurityConfig
		extends WebSecurityConfigurerAdapter {

	private static final Set<String> ALLOWED_HEADERS = Set
			.of("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization");
	private static final String[] AUTH_WHITELIST = {
			// -- swagger ui
			"/v2/api-docs", "/config/ui", "/swagger-resources/**", "/config/**", "/swagger-ui.html", "/webjars/**" };
	private final AuthProperties authProperties;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserDetailsService userDetailsService;

	public SecurityConfig(AuthProperties authProperties,
			@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
			JwtTokenProvider jwtTokenProvider) {
		this.authProperties = authProperties;
		this.userDetailsService = userDetailsService;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean @Override public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override public void configure(WebSecurity web) {
		web.ignoring().antMatchers(AUTH_WHITELIST);
	}

	@Override protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests(
				authorizeRequests -> authorizeRequests.antMatchers("/authenticate", "/users/register").permitAll()
						.anyRequest().authenticated()).sessionManagement(
				sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(exceptionHandling -> exceptionHandling
						.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
				.addFilterBefore(new JwtAuthorizationFilter(jwtTokenProvider),
						UsernamePasswordAuthenticationFilter.class).cors();
	}

	@Bean public CorsConfigurationSource corsConfigurationSource() {
		List<String> allowedMethods = stream(HttpMethod.values()).map(Enum::name).collect(toList());

		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of(authProperties.getCorsOrigins()));
		configuration.setAllowedMethods(allowedMethods);
		configuration.setAllowedHeaders(new ArrayList<>(ALLOWED_HEADERS));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}

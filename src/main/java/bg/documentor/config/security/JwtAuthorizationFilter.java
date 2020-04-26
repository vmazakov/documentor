package bg.documentor.config.security;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.*;

@Slf4j public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private static final String BEARER = "Bearer ";

	private final JwtTokenProvider jwtTokenProvider;

	public JwtAuthorizationFilter(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {

		try {
			resolveToken(request).flatMap(jwtTokenProvider::getAuthentication)
					.ifPresent(auth -> SecurityContextHolder.getContext().setAuthentication(auth));

		} catch (JwtException jwtException) {
			log.warn("Authentication request failed: {}", jwtException.getMessage());
			SecurityContextHolder.clearContext();
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}

		filterChain.doFilter(request, response);
	}

	private Optional<String> resolveToken(HttpServletRequest request) {
		//we can add from cookie case
		// and make sure that both are not present at the same time
		return ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION)).filter(this::isValidBearerToken)
				.map(token -> substringAfter(token, BEARER));
	}

	private Boolean isValidBearerToken(String token) {
		return startsWithIgnoreCase(token, BEARER) && isNotBlank(substringAfter(token, BEARER));
	}
}

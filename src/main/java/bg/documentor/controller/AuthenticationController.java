package bg.documentor.controller;

import bg.documentor.config.security.JwtTokenProvider;
import bg.documentor.config.security.models.JwtRequest;
import bg.documentor.config.security.models.JwtResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j @RestController public class AuthenticationController {

	private final JwtTokenProvider jwtTokenProvider;
	private final AuthenticationManager authenticationManager;

	public AuthenticationController(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.authenticationManager = authenticationManager;
	}

	@ApiOperation(value = "Authenticate user", notes = "If successful returns access token") @PostMapping("/authenticate") public ResponseEntity<JwtResponse> createAuthenticationToken(
			@RequestBody JwtRequest loginDto, HttpServletResponse response) {
		log.debug("Authenticating user: {}", loginDto.getUsername());
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtTokenProvider.createToken(authentication, false);

		response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
		return ResponseEntity.ok(new JwtResponse(jwt));
	}

}

package org.haycco.tanlan.server.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.util.Base64;


@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Mono<SecurityContext> load(ServerWebExchange swe) {
		ServerHttpRequest request = swe.getRequest();
		String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String authToken = authHeader.substring(7);
			Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
			return this.authenticationManager.authenticate(auth).map(SecurityContextImpl::new);
		} else if (authHeader != null && authHeader.startsWith("Basic ")){
			String[] strings = new String[0];
			try {
				strings = extractAndDecodeHeader(authHeader);
			} catch (UnsupportedEncodingException e) {
				return Mono.empty();
			}
			if ("admin".equals(strings[0]) && "admin".equals(strings[1])){
							UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
									"admin",
									null,
									null);

							return Mono.just(new SecurityContextImpl(auth));
			}
		}

		return Mono.empty();
	}

	private String[] extractAndDecodeHeader(String header) throws UnsupportedEncodingException {

		byte[] base64Token = new byte[0];
			base64Token = header.substring(6).getBytes("UTF-8");
		byte[] decoded;
		try {
			decoded = Base64.getDecoder().decode(base64Token);
		}
		catch (IllegalArgumentException e) {
			throw new BadCredentialsException(
					"Failed to decode basic authentication token");
		}

		String token = new String(decoded, "UTF-8");

		int delim = token.indexOf(":");

		if (delim == -1) {
			throw new BadCredentialsException("Invalid basic authentication token");
		}
		return new String[] { token.substring(0, delim), token.substring(delim + 1) };
	}
	
}

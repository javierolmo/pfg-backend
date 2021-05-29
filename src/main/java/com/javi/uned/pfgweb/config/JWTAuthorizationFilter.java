package com.javi.uned.pfgweb.config;

import com.javi.uned.pfgweb.exceptions.AuthException;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    public static final String HEADER = "Authorization";
    public static final String PREFIX = "jUgnlLgD";
    public static final String SECRET = "gjN12sSF";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {

            Claims claims = validateToken(request);

            if (claims.get("authorities") != null) {
                setUpSpringAuthentication(claims);
            } else {
                SecurityContextHolder.clearContext();
            }

            chain.doFilter(request, response);

        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        } catch (AuthException authException) {
            SecurityContextHolder.clearContext();
            chain.doFilter(request, response);
        }
    }

    /**
     * Validates JWT token obtaining it's claims
     * @param request http request with token in header
     * @return claims
     * @throws AuthException
     */
    private Claims validateToken(HttpServletRequest request) throws AuthException {
        try {

            // Check header
            String authenticationHeader = request.getHeader(HEADER);
            if(authenticationHeader == null || !authenticationHeader.startsWith(PREFIX)) {
                throw new IOException("Error in checkJWTToken(request)");
            }

            // Parse token
            String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
            Claims claims = Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();

            return claims;

        } catch (Exception e) {
            throw new AuthException("Cannot parse JWT Token", e);
        }

    }

    /**
     * Authentication method in Spring flow
     *
     * @param claims
     */
    private void setUpSpringAuthentication(Claims claims) {
        @SuppressWarnings("unchecked")
        List<String> authorities = (List) claims.get("authorities");

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(auth);

    }

    public boolean ensureApplicantId(HttpServletRequest request, Integer id) throws AuthException {
        Claims claims = validateToken(request);
        return claims.get("id").equals(id);
    }
}

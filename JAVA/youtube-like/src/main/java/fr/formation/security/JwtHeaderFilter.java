package fr.formation.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import fr.formation.model.User;
import fr.formation.repo.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtHeaderFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = this.getToken(request);
        Jwt jwt = JwtUtil.parse(token);

        if (jwt.isValid()) {
            Optional<User> optUser = this.userRepository.findById(jwt.getUserId());
            
            if (optUser.isPresent()) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                
                if (optUser.get().isAdmin()) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                }

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                    jwt.getUserId(),
                    null,
                    authorities
                );
    
                // On ajoute l'authentification au contexte de Sécurité de Spring Security
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Passer à la suite de la chaine de filtres
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || authHeader.length() <= 7) {
            return null;
        }

        // Bearer letoken
        return authHeader.substring(7);
    }
}
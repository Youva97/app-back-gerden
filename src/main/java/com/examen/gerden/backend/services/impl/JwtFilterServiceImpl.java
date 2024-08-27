package com.examen.gerden.backend.services.impl;

import com.examen.gerden.backend.models.Jwt;
import com.examen.gerden.backend.repositories.JwtRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Service
public class JwtFilterServiceImpl extends OncePerRequestFilter {
    private HandlerExceptionResolver handlerExceptionResolver;
    private UtilisateurServiceImpl utilisateurService;
    private JwtServiceImpl jwtServiceImpl;
    private JwtRepository jwtRepository;
    public JwtFilterServiceImpl(UtilisateurServiceImpl utilisateurService, JwtServiceImpl jwtServiceImpl, JwtRepository jwtRepository, HandlerExceptionResolver handlerExceptionResolver ) {
        this.utilisateurService = utilisateurService;
        this.jwtServiceImpl = jwtServiceImpl;
        this.jwtRepository=jwtRepository;
        this.handlerExceptionResolver=handlerExceptionResolver;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String username=null;
        String token=null;
        boolean isTokenExpired=true;
        Jwt tokenDansBDD = null;

        try {
            final String authorization = request.getHeader("Authorization");
            if (authorization!=null && authorization.startsWith("Bearer ")){
                token = authorization.substring(7);
                tokenDansBDD=this.jwtServiceImpl.tokenByValue(token);
                isTokenExpired = jwtServiceImpl.isTokenExpired(token);
                username = jwtServiceImpl.extractUsername(token);
            }

            if(!isTokenExpired && username != null && tokenDansBDD.getUtilisateur().getEmail().equals(username) && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails=utilisateurService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            filterChain.doFilter(request,response);

        } catch (Exception exception){
            handlerExceptionResolver.resolveException(request,response,null, exception);

        }
    }
}

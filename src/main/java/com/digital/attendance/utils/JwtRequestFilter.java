/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digital.attendance.utils;

import com.digital.attendance.service.myUserDetailsService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author oreoluwa
 */
@Component
public class JwtRequestFilter  extends OncePerRequestFilter {
    
    @Autowired
    private myUserDetailsService myUserDetailsSrvice;
            
     @Autowired
     private JwtUtil jwtutil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        
        final String AuthorizationHeader = request.getHeader("Authorization");
        
        
        String username = null;
        String jwt = null;
        if (AuthorizationHeader != null && AuthorizationHeader.startsWith("Bearer ")) {
            jwt = AuthorizationHeader.substring(7);
            username = jwtutil.getUsernameFromToken(jwt);
    }
        
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.myUserDetailsSrvice.loadUserByUsername(username);
            
            if (jwtutil.validateToken(jwt, userDetails)) {
            
                
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            
            }

  
    
}
        chain.doFilter(request, response);
}
    


}
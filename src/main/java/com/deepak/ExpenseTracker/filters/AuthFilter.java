package com.deepak.ExpenseTracker.filters;

import java.io.IOException;
import java.net.http.HttpResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import com.deepak.ExpenseTracker.Constants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class AuthFilter extends GenericFilterBean{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String authHeader = httpRequest.getHeader("Authorization");
		
		if(authHeader!=null) {
			String[] authHeaderArr = authHeader.split("Bearer");
			if(authHeaderArr.length>1 && authHeaderArr[1]!=null) {
				String token = authHeaderArr[1];
				//System.out.println("token"+token);
				try {
					Claims claims = Jwts.parser().setSigningKey(Constants.API_SECRET_KEY)
							.parseClaimsJws(token).getBody();
					httpRequest.setAttribute("userId", claims.get("userId"));
				} catch (Exception e) {
					System.out.println("E:"+e);
					httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Invalid/Expired Token");
					return;
				}
			}else {
				httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Authorization token must be Bearer [token]");
				return;
			}
		}else {
			httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Auth toekn must be present");
			return;
		}
		
		chain.doFilter(httpRequest, httpResponse);
	}

}

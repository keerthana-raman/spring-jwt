package user.kraman.springjwt.controller;


import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import io.jsonwebtoken.*;
import java.util.Date;    
 

@RestController
@RequestMapping("/jwts")
public class JwtController {
	
	  static final long EXPIRATIONTIME = 864_000_000; // 10 days
	  static final String SECRET = "ThisIsASecret";
	  static final String TOKEN_PREFIX = "access_token";
	  static final String HEADER_STRING = "Set-Cookie";
	
	
	@RequestMapping(method = RequestMethod.GET)
	public String getJwt(HttpServletResponse  response) {
		createJWT(response, "kraman");
		return "All Ok";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String sendJwt(@CookieValue("access_token") String jwtToken) {
		System.out.println("Cookie "+jwtToken);
		verifyJwt(jwtToken);
		return "All Ok";
	}	
	
	//Sample method to construct a JWT
	private void createJWT(HttpServletResponse res, String username) {
	    String JWT = Jwts.builder()
	            .setSubject("userProfile")
	            .setIssuer("kraman")
	            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
	            .signWith(SignatureAlgorithm.HS512, SECRET)
	            .compact();
	        res.addHeader(HEADER_STRING, TOKEN_PREFIX + "=" + JWT);
  }
	
	private void verifyJwt(String token) {
	    //This line will throw an exception if it is not a signed JWS (as expected)
	    Claims claims = Jwts.parser()         
	       .setSigningKey(SECRET)
	       .parseClaimsJws(token).getBody();
	    System.out.println("ID: " + claims.getId());
	    System.out.println("Subject: " + claims.getSubject());
	    System.out.println("Issuer: " + claims.getIssuer());
	    System.out.println("Expiration: " + claims.getExpiration());		
	}

}

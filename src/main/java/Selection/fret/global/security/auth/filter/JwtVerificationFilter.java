package Selection.fret.global.security.auth.filter;

import Selection.fret.global.security.jwt.JwtTokenizer;
import Selection.fret.global.security.auth.utils.CustomAuthorityUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

// Front에서 request를 전송할 때 마다 header에 Token을 보냄 -> 검증 수행
public class JwtVerificationFilter extends OncePerRequestFilter {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;

    public JwtVerificationFilter(JwtTokenizer jwtTokenizer, CustomAuthorityUtils authorityUtils) {
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try{
            Map<String,Object > claims =verifyJws(request);
            setAuthenticationToContext(claims);
        } catch (Exception e){
            request.setAttribute("exception",e);
        }
        filterChain.doFilter(request,response);
    }
    // 만약 request에 전달받은 authorization이 없으면 해당 필터는 실행안함
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        Cookie[] cookie = request.getCookies();
        return authorization == null || !authorization.startsWith("Bearer");
    }

    private Map<String,Object> verifyJws(HttpServletRequest request){
        String jws = request.getHeader("Authorization").replace("Bearer ","");
        try {
            return jwtTokenizer.verifySignature(jws).getBody();
        }catch (Exception e){
            request.setAttribute("exception",e);
            throw e;
        }
    }
    private void setAuthenticationToContext(Map<String,Object> claims){
        String email = (String) claims.get("sub");
        List<GrantedAuthority> authorities = authorityUtils.createAuthorities((List<String>) claims.get("roles"));
        Authentication authentication =new UsernamePasswordAuthenticationToken(email,null ,authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}

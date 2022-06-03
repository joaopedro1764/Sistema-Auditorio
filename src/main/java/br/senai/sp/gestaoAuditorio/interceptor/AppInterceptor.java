package br.senai.sp.gestaoAuditorio.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import br.senai.sp.gestaoAuditorio.annotation.Privado;
import br.senai.sp.gestaoAuditorio.annotation.Publico;
import br.senai.sp.gestaoAuditorio.rest.AdministradorRest;
import br.senai.sp.gestaoAuditorio.rest.UsuarioRest;

@Component
public class AppInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// variável para descobrir pra onde estão tentando ir
		String uri = request.getRequestURI();

		// verifica se o handler é um HandlerMethod, o que indica que foi encontrado um
		// método em algum controller para a requisição
		if (handler instanceof HandlerMethod) {
			if (uri.startsWith("/api")) {

				String token = null;
				HandlerMethod metodoChamado = (HandlerMethod) handler;
				if (metodoChamado.getMethodAnnotation(Privado.class) != null) {
					try {
						token = request.getHeader("Authorization");
						Algorithm algorithm = Algorithm.HMAC256(UsuarioRest.SECRET);
						JWTVerifier verifier = JWT.require(algorithm).withIssuer(UsuarioRest.EMISSOR).build();
						DecodedJWT jwt = verifier.verify(token);
						Map<String, Claim> payload = jwt.getClaims();
						System.out.println(payload.get("nome_usuario"));
						
						token = request.getHeader("Authorization");
						Algorithm algorithm2 = Algorithm.HMAC256(AdministradorRest.SECRET);
						JWTVerifier verifier2 = JWT.require(algorithm2).withIssuer(AdministradorRest.EMISSOR).build();
						DecodedJWT jwt2 = verifier2.verify(token);
						Map<String, Claim> payload2 = jwt2.getClaims();
						
						System.out.println(payload2.get("nome_admin"));
						
						return true;

					} catch (Exception e) {
						if (token == null) {
							response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
						} else {
							response.sendError(HttpStatus.FORBIDDEN.value(), e.getMessage());
						}
						return false;
					}

				}
				return true;
			}
			// liberar o acesso á pagina inicial
			if (uri.equals("/")) {
				return true;
			}
			if (uri.endsWith("/error")) {
				return true;
			}
			// fazer o casting para HandlerMethod
			HandlerMethod metodoChamado = (HandlerMethod) handler;
			// se o método for publico, libera
			if (metodoChamado.getMethodAnnotation(Publico.class) != null) {
				return true;
			}

			// verifica se existe um usuario logado
			if (request.getSession().getAttribute("adminLogado") != null) {
				return true;
			}
			if (request.getSession().getAttribute("usuarioLogado") != null) {
				return true;
			} else {
				// redireciona para apagina inicial
				response.sendRedirect("/");
				return false;
			}

		}
		return true;
	}
}

package br.senai.sp.gestaoAuditorio.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import br.senai.sp.gestaoAuditorio.annotation.Privado;
import br.senai.sp.gestaoAuditorio.annotation.Publico;

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
				HandlerMethod metodoChamado = (HandlerMethod) handler;
				if (metodoChamado.getMethodAnnotation(Privado.class) != null) {
					return true;
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

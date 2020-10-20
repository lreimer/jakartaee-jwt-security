package cloud.nativ.jakartaee.auth0;

import fish.payara.security.openid.api.OpenIdContext;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebServlet(urlPatterns = {"/callback"})
public class CallbackServlet extends HttpServlet {

    @Inject
    private OpenIdContext context;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*
         * Since we perform the token exchange in Auth0AuthenticationMechanism (which happens prior to this Servlet's
         * execution in the filter chain), we simply redirect the home page or to the referring URI if one was set
         * in the session.
         */
        String referer = (String) request.getSession().getAttribute("Referer");
        String redirectTo = referer != null ? referer : "/";

        LOGGER.info("Access token: {}. Redirecting to: {}", context.getAccessToken(), referer);
        response.sendRedirect(redirectTo);
    }
}

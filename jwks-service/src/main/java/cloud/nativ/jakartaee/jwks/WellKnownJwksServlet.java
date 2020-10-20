package cloud.nativ.jakartaee.jwks;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.servlet.AsyncContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * A servlet to return the list of well-known JWK public keys.
 */
@Slf4j
@WebServlet(name = "wellKnownJwksServlet", urlPatterns = {"/.well-known/jwks.json"}, asyncSupported = true)
public class WellKnownJwksServlet extends HttpServlet {

    @Inject
    private JwkRepository jwkRepository;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final AsyncContext context = req.startAsync();
        context.start(() -> {
            HttpServletResponse response = (HttpServletResponse) context.getResponse();
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");

            try {
                PrintWriter responseWriter = response.getWriter();
                try (JsonWriter writer = Json.createWriter(responseWriter)) {
                    JsonObject jsonObject = jwksJsonObject();
                    writer.writeObject(jsonObject);
                }
            } catch (IOException e) {
                LOGGER.warn("Unable to write well-known JSON Web Key Set.", e);
            } finally {
                context.complete();
            }
        });
    }

    private JsonObject jwksJsonObject() {
        return Json.createObjectBuilder()
                .add("keys", Json.createArrayBuilder(jwkRepository.publicJWKs()))
                .build();
    }
}

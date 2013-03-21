package httpserver.responders;

import httpserver.HtmlGenerator;
import httpserver.Utilities;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class ParameterResponder implements Responder {
    private HtmlGenerator generator;

    public ParameterResponder() {
        generator = new HtmlGenerator();
    }

    @Override
    public Map<String, Object> respond(Map<String, Object> request) throws IOException {
        Map<String, String> headers = new HashMap<>();
        Map<String, String> parameters = (Map<String, String>) request.get("Parameters");

        byte[] body = generator.getEchoPage(parameters, "%s = %s<br/>").getBytes(Charset.forName("utf-8"));

        Utilities.writeCommonHeaders(headers, "text/html", body.length);

        return Utilities.generateResponse(Utilities.statusLine(200), headers, body);
    }
}

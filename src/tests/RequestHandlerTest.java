package tests;

import httpserver.RequestHandler;
import httpserver.responders.Responder;
import httpserver.Router;
import httpserver.sockets.HttpSocket;
import org.junit.Before;
import org.junit.Test;
import tests.mocks.Mocket;
import tests.mocks.MocketWrapper;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RequestHandlerTest {
    private Router router;
    private RequestHandler requestHandler;
    private HttpSocket socket;

    @Before
    public void setUp() {
        router = new Router();
        String request = "HTTP/1.1 / GET\r\n\r\n";
        Mocket mocket = new Mocket(new ByteArrayInputStream(request.getBytes(Charset.forName("utf-8"))), new ByteArrayOutputStream());
        socket = new MocketWrapper(mocket);
        requestHandler = new RequestHandler(socket, router);

        router.addRoute("/", new Responder() {
            @Override
            public Map<String, Object> respond(Map<String, Object> request) throws IOException {
                Map<String, Object> response = new HashMap<>();
                response.put("message-header", new HashMap<String, String>());
                response.put("status-line", "HTTP/1.1 200 OK");
                response.put("message-body", "We do what we must because we can.".getBytes(Charset.forName("utf-8")));
                return response;
            }
        });
    }

    @Test
    public void testRun() throws IOException {
        requestHandler.run();
        assertEquals("HTTP/1.1 200 OK\r\n\r\nWe do what we must because we can.", socket.getOutputStream().toString());
    }

    @Test
    public void testTheSocketIsClosedOnRun() {
        requestHandler.run();
        assertTrue(socket.isClosed());
    }
}

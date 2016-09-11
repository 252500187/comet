package com.springapp.mvc;

import org.apache.catalina.CometEvent;
import org.apache.catalina.CometProcessor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created with IntelliJ IDEA.
 * User: Yam_hard
 * Date: 16-5-4
 * Time: 下午11:34
 * To change this template use File | Settings | File Templates.
 */
public class cometServlet extends HttpServlet implements CometProcessor {
    private static final long serialVersionUID = 1L;
    private static final Integer TIMEOUT = 10 * 1000;

    private MessageSender sender = null;

    public void event(CometEvent event) throws IOException, ServletException {

        HttpServletRequest request = event.getHttpServletRequest();
        HttpServletResponse response = event.getHttpServletResponse();

        if (event.getEventType() == CometEvent.EventType.BEGIN) {
            log("Begin for session: " + request.getSession(true).getId());
            request.setAttribute("org.apache.tomcat.comet.timeout", TIMEOUT);

            sender = new MessageSender(response);
            sender.start();

        } else if (event.getEventType() == CometEvent.EventType.ERROR) {
            log("Error for session: " + request.getSession(true).getId());
            event.close();
        } else if (event.getEventType() == CometEvent.EventType.END) {
            log("End for session: " + request.getSession(true).getId());
            event.close();
        } else if (event.getEventType() == CometEvent.EventType.READ) {
            throw new UnsupportedOperationException("This servlet does not accept data");
        }
    }

    @Override
    public void destroy() {
        sender.interrupt();
        sender = null;
    }
}

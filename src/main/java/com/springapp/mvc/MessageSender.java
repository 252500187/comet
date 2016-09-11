package com.springapp.mvc;

import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Yam_hard
 * Date: 16-5-5
 * Time: 上午12:11
 * To change this template use File | Settings | File Templates.
 */
public class MessageSender extends Thread {

    private ServletResponse connection;

    public MessageSender(ServletResponse connection) {
        this.connection = connection;
    }

    public void run() {
        try {
            while (true) {
                if (connection == null) {
                    synchronized (this) {
                        wait();
                    }
                }
                OutputStream out = connection.getOutputStream();
                out.write(getString().getBytes());
                out.flush();
                connection.flushBuffer();
                System.out.print(getString());
                Thread.sleep(2000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    private String getString() {
        return Thread.currentThread()+" CurrentTime "+new Date().toLocaleString() + "\n";
    }

}
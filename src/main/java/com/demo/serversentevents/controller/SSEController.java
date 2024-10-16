package com.demo.serversentevents.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@RestController
public class SSEController {

    @GetMapping("/sse")
    public SseEmitter emitSSE() throws FileNotFoundException {
        SseEmitter emitter = new SseEmitter();

        Runnable task = () -> {
            try {
                BufferedReader reader = new BufferedReader(new FileReader("/Users/mokverma/Moksh/Personal/server-sent-events/src/main/resources/error.log"));
                String line = reader.readLine();
                while (line != null && !line.isEmpty()) {
                    emitter.send(SseEmitter.event().name("log").data(line));
                    line = reader.readLine();
                    Thread.sleep(500);
                }
                reader.close();
                emitter.send(SseEmitter.event().name("log").data("Work here is done!"));
                emitter.complete();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        Thread thread = new Thread(task);
        thread.start();
        return emitter;
    }

}

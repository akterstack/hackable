package io.hackable.test;

import io.hackable.EventListener;
import io.hackable.Hackable;

import java.util.Collections;
import java.util.Map;

public class MyHackable implements Hackable {

    {
        EventListener.on("created", getClass(), event -> {
            Map map = event.dataAt(0);
            System.out.println(map);
        });
        EventListener.on("created", event -> System.out.println(event.getData(0)));
    }

    public void create() {
        trigger("created", Collections.singletonMap("key", "val"));
    }

}

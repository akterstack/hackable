package io.hackable.test;

import io.hackable.Hackable;

import java.util.Collections;
import java.util.Map;

public class MyHackable implements Hackable {

    {
        Events.on("created", getClass(), event -> {
            Map map = event.dataAt(0);
            System.out.println(map);
        });
        Events.on("created", event -> System.out.println(event.getData(0)));
    }

    public void create() {
        trigger("created", Collections.singletonMap("key", "val"));
        Events.trigger("created", Collections.singletonMap("key", "val"));
    }

}

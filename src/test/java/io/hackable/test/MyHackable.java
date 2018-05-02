package io.hackable.test;

import java.util.Collections;

import static hackable.Hackable.*;

public class MyHackable {

    {
        /*on("created", getClass(), eventData -> {
            System.out.println(eventData);
        });
        on("created", event -> System.out.println(event));*/
    }

    public void create() {
        trigger("created", Collections.singletonMap("key", "val"));
        trigger("created", Collections.singletonMap("key", "val"));
    }

}

package io.hackable.test;

import io.hackable.EventListener;
import io.hackable.Hackable;

import java.util.Collections;

public class MyHackable implements Hackable {

    {
        EventListener.on("created", MyHackable.class, event -> System.out.println(event.data()));
    }

    public void create() {
        trigger("created", Collections.singletonMap("key", "val"));
    }

}

package io.hackable.test;

import io.hackable.Hackable;

import java.util.Collections;

public abstract class AbstractSample implements Hackable {

    public void sample() {
        trigger("abstractSample", Collections.singletonMap("Key", "Value"));
    }

}

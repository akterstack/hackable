package io.hackable.test;

import java.util.Collections;

import static hackable.Hackable.*;

public abstract class AbstractSample {

    public void sample() {
        trigger("abstractSample", Collections.singletonMap("Key", "Value"));
    }

}

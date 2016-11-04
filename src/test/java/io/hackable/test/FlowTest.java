package io.hackable.test;

import io.hackable.Flow;
import io.hackable.Flux;
import org.junit.Test;

public class FlowTest {

    @Test
    public void test() {
        Flow.start(i -> {
            System.out.println(i);
            return new Object[]{10};
        }).then(i -> {
            System.out.println(i[0]);
            return null;
        });
    }

    @Test
    public void abc() {

    }

}

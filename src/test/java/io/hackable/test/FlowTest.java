package io.hackable.test;

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

package io.hackable.test;

import org.junit.Test;

import static io.hackable.Hackable.*;

public class FilterTest {

    @Test
    public void testApplyFilter() {
        onFilter("init", (FilterMock mock) -> new FilterMock("Akter " + mock.getName()));
        FilterMock filterMock = applyFilter("init", new FilterMock("Sohag"));
        System.out.println(filterMock);
    }

    static {
        onFilter("init", (FilterMock mock) -> new FilterMock("Hossain " + mock.getName()));
    }

}

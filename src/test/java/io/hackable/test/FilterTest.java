package io.hackable.test;

import io.hackable.Filters;
import io.hackable.Hackable;
import org.junit.Test;

public class FilterTest implements Hackable {

    @Test
    public void testApplyFilter() {
        Filters.onFilter("init", this.getClass(), (FilterMock mock) -> new FilterMock("Akter " + mock.getName()));
        FilterMock filterMock = applyFilter("init", new FilterMock("Sohag"));
        System.out.println(filterMock);
    }

    static {
        Filters.onFilter("init", FilterTest.class, (FilterMock mock) -> new FilterMock("Hossain " + mock.getName()));
    }

}

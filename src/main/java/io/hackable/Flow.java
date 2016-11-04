package io.hackable;

public class Flow {

    private Object[] flx;

    public static Flow start(Flux flux) {
        Flow flow = new Flow();
        flow.flx = flux.handle(flow.flx);
        return flow;
    }

    public Flow then(Flux flux) {
        flux.handle(flx);
        return this;
    }

}

package it.killernik.scarletairdrop.WorkLoad;

import java.util.ArrayDeque;

public class WorkloadThread implements Runnable {
    private static final int MAX_MS_PER_TICK = 10;
    private static final int MAX_NANOS_PER_TICK = MAX_MS_PER_TICK * 1000000;
    private final ArrayDeque<Workload> workloads = new ArrayDeque<>();

    public void addWorkload(Workload workload) {
        this.workloads.add(workload);
    }

    @Override
    public void run() {
        long stopTime = System.nanoTime() + MAX_NANOS_PER_TICK;
        Workload workload;

        while (System.nanoTime() < stopTime && !workloads.isEmpty() && (workload = workloads.poll()) != null) {
            workload.compute();
        }
    }
}
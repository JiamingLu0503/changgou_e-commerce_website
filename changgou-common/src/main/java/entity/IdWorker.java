package entity;

import java.net.NetworkInterface;
import java.net.InetAddress;
import java.lang.management.ManagementFactory;

public class IdWorker {
    private static final long twepoch = 1288834974657L;
    private static final long workerIdBits = 5L;
    private static final long datacenterIdBits = 5L;
    private static final long maxWorkerId = 31L;
    private static final long maxDatacenterId = 31L;
    private static final long sequenceBits = 12L;
    private static final long workerIdShift = 12L;
    private static final long datacenterIdShift = 17L;
    private static final long timestampLeftShift = 22L;
    private static final long sequenceMask = 4095L;
    private static long lastTimestamp;
    private long sequence;
    private final long workerId;
    private final long datacenterId;

    public IdWorker() {
        this.sequence = 0L;
        this.datacenterId = getDatacenterId(31L);
        this.workerId = getMaxWorkerId(this.datacenterId, 31L);
    }

    public IdWorker(final long workerId, final long datacenterId) {
        this.sequence = 0L;
        if (workerId > 31L || workerId < 0L) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", 31L));
        }
        if (datacenterId > 31L || datacenterId < 0L) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0",
                    31L));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    public synchronized long nextId() {
        long timestamp = this.timeGen();
        if (timestamp < IdWorker.lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d " +
                    "milliseconds", IdWorker.lastTimestamp - timestamp));
        }
        if (IdWorker.lastTimestamp == timestamp) {
            this.sequence = (this.sequence + 1L & 0xFFFL);
            if (this.sequence == 0L) {
                timestamp = this.tilNextMillis(IdWorker.lastTimestamp);
            }
        } else {
            this.sequence = 0L;
        }
        IdWorker.lastTimestamp = timestamp;
        final long nextId =
                timestamp - 1288834974657L << 22 | this.datacenterId << 17 | this.workerId << 12 | this.sequence;
        return nextId;
    }

    private long tilNextMillis(final long lastTimestamp) {
        long timestamp;
        for (timestamp = this.timeGen(); timestamp <= lastTimestamp; timestamp = this.timeGen()) {
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    protected static long getMaxWorkerId(final long datacenterId, final long maxWorkerId) {
        final StringBuffer mpid = new StringBuffer();
        mpid.append(datacenterId);
        final String name = ManagementFactory.getRuntimeMXBean().getName();
        if (!name.isEmpty()) {
            mpid.append(name.split("@")[0]);
        }
        return (mpid.toString().hashCode() & 0xFFFF) % (maxWorkerId + 1L);
    }

    protected static long getDatacenterId(final long maxDatacenterId) {
        long id = 0L;
        try {
            final InetAddress ip = InetAddress.getLocalHost();
            final NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                final byte[] mac = network.getHardwareAddress();
                id = ((0xFFL & (long) mac[mac.length - 1]) | (0xFF00L & (long) mac[mac.length - 2] << 8)) >> 6;
                id %= maxDatacenterId + 1L;
            }
        } catch (Exception e) {
            System.out.println(" getDatacenterId: " + e.getMessage());
        }
        return id;
    }

    public static void main(final String[] args) {
        final IdWorker idWorker = new IdWorker(0L, 0L);
        for (int i = 0; i < 1000000; ++i) {
            System.out.println(idWorker.nextId());
        }
    }

    static {
        IdWorker.lastTimestamp = -1L;
    }
}

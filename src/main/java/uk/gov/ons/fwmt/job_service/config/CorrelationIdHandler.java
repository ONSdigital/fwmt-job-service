package uk.gov.ons.fwmt.job_service.config;

public class CorrelationIdHandler {
    private static final ThreadLocal<String> id = new InheritableThreadLocal<String>();

    public static String getId() { return id.get(); }

    public static void setId(String correlationId) { id.set(correlationId); }
}

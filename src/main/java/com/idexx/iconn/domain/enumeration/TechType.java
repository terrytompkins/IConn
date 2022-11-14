package com.idexx.iconn.domain.enumeration;

/**
 * The TechType enumeration.
 */
public enum TechType {
    PROGRAMMING_LANGUAGE("Programming Language"),
    SERVER("Server"),
    LOAD_BALANCER("Load Balancer"),
    BACKEND_FRAMEWORK("Backend Framework"),
    FRONTEND_FRAMEWORK("Frontend Framework"),
    MONITORING_TOOL("Monitoring Tool"),
    PERFORMANCE_TOOL("Performance Tool"),
    DATA_STORAGE("Data Storage"),
    DATA_QUERY_TOOL("Data Query Tool"),
    TEST_TOOL("Test Tool"),
    CODE_MANAGEMENT("Code Management"),
    SECURITY("Security"),
    AUTHENTICATION("Authentication"),
    IDE("Integrated Development Environment");

    private final String value;

    TechType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

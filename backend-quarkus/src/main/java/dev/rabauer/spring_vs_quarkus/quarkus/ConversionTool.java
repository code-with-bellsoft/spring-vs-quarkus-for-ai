package dev.rabauer.spring_vs_quarkus.quarkus;

import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;

@ApplicationScoped
public class ConversionTool {

    private static final double CONVERSION_RATE_EUROS_TO_DOLLARS = 2.0;

    @Tool("Converts Euros (€) to US Dollars.")
    public double convertEurToUsd(double euros)
    {
        return BigDecimal.valueOf(euros).multiply(BigDecimal.valueOf(CONVERSION_RATE_EUROS_TO_DOLLARS)).doubleValue();
    }
}

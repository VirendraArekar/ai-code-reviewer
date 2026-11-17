package com.aicode.reviewer.service.ai;

import com.aicode.reviewer.entity.enums.AIProvider;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class AIProviderFactory {

    private final Map<AIProvider, AIProviderService> providers = new EnumMap<>(AIProvider.class);

    public AIProviderFactory(List<AIProviderService> serviceList) {
        for (AIProviderService service : serviceList) {
            providers.put(service.getProviderType(), service);
        }
    }

    public AIProviderService getService(AIProvider provider) {
        AIProviderService service = providers.get(provider);
        if (service == null) {
            // Default to GEMINI if requested provider is missing
            return providers.get(AIProvider.GEMINI);
        }
        return service;
    }
}

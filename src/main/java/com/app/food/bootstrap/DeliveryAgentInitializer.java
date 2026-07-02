package com.app.food.bootstrap;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.app.food.model.DeliveryAgent;
import com.app.food.repository.DeliveryAgentRepository;

@Component
public class DeliveryAgentInitializer implements CommandLineRunner {

    private final DeliveryAgentRepository agentRepo;

    public DeliveryAgentInitializer(DeliveryAgentRepository agentRepo) {
        this.agentRepo = agentRepo;
    }

    @Override
    public void run(String... args) {
        if (agentRepo.count() > 0) return;

        DeliveryAgent a1 = new DeliveryAgent();
        a1.setName("Ravi");
        a1.setPhone("9000000001");
        a1.setVehicleNo("MH12AB1234");
        a1.setActive(true);

        DeliveryAgent a2 = new DeliveryAgent();
        a2.setName("Suresh");
        a2.setPhone("9000000002");
        a2.setVehicleNo("MH12CD5678");
        a2.setActive(true);

        DeliveryAgent a3 = new DeliveryAgent();
        a3.setName("Amit");
        a3.setPhone("9000000003");
        a3.setVehicleNo("MH12EF9999");
        a3.setActive(true);

        agentRepo.save(a1);
        agentRepo.save(a2);
        agentRepo.save(a3);
    }
}
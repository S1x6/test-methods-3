package org.nsu.fit.tm_backend.operations;

import org.glassfish.jersey.internal.guava.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nsu.fit.tm_backend.database.data.CustomerPojo;
import org.nsu.fit.tm_backend.database.data.SubscriptionPojo;
import org.nsu.fit.tm_backend.manager.CustomerManager;
import org.nsu.fit.tm_backend.manager.SubscriptionManager;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StatisticOperationTest {
    // Лабораторная 2: покрыть юнит тестами класс StatisticOperation на 100%.

    private CustomerManager customerManager;
    private SubscriptionManager subscriptionManager;
    private List<UUID> customerIds;

    private StatisticOperation statisticOperation;

    @BeforeEach
    void init() {
        // Создаем mock объекты.
        customerManager = mock(CustomerManager.class);
        subscriptionManager = mock(SubscriptionManager.class);
        customerIds = new ArrayList<>();
        customerIds.add(UUID.randomUUID());
        customerIds.add(UUID.randomUUID());
        statisticOperation = new StatisticOperation(customerManager, subscriptionManager, customerIds);
    }

    @Test
    void testConstructorNullCustomerManager() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                statisticOperation = new StatisticOperation(null, subscriptionManager, customerIds));
        assertEquals("customerManager", ex.getMessage());
    }

    @Test
    void testConstructorNullSubscriptionManager() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                statisticOperation = new StatisticOperation(customerManager, null, customerIds));
        assertEquals("subscriptionManager", ex.getMessage());
    }

    @Test
    void testConstructorNullIds() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                statisticOperation = new StatisticOperation(customerManager, subscriptionManager, null));
        assertEquals("customerIds", ex.getMessage());
    }

    @Test
    void testExecute() {
        CustomerPojo cmFirst = new CustomerPojo();
        cmFirst.id = customerIds.get(0);
        cmFirst.firstName = "John";
        cmFirst.lastName = "Wick";
        cmFirst.login = "john_wick@gmail.com";
        cmFirst.pass = "Baba_Jaga";
        cmFirst.balance = 100;

        CustomerPojo cmSecond = new CustomerPojo();
        cmSecond.id = customerIds.get(1);
        cmSecond.firstName = "John";
        cmSecond.lastName = "Wick";
        cmSecond.login = "john_wick1@gmail.com";
        cmSecond.pass = "Baba_Jaga";
        cmSecond.balance = 200;

        when(customerManager.getCustomer(customerIds.get(0))).thenReturn(cmFirst);
        when(customerManager.getCustomer(customerIds.get(1))).thenReturn(cmSecond);

        SubscriptionPojo spFirst = new SubscriptionPojo();
        spFirst.planFee = 10;
        SubscriptionPojo spSecond = new SubscriptionPojo();
        spSecond.planFee = 20;

        when(subscriptionManager.getSubscriptions(customerIds.get(0)))
                .thenReturn(Collections.singletonList(spFirst));
        when(subscriptionManager.getSubscriptions(customerIds.get(1)))
                .thenReturn(Collections.singletonList(spSecond));

        StatisticOperation.StatisticOperationResult res = statisticOperation.Execute();
        assertEquals(cmFirst.balance + cmSecond.balance, res.overallBalance);
        assertEquals(spFirst.planFee + spSecond.planFee, res.overallFee);
    }
}

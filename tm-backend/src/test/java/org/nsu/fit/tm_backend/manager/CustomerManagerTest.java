package org.nsu.fit.tm_backend.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.internal.util.collections.Sets;
import org.nsu.fit.tm_backend.database.data.ContactPojo;
import org.nsu.fit.tm_backend.database.data.TopUpBalancePojo;
import org.nsu.fit.tm_backend.manager.auth.data.AuthenticatedUserDetails;
import org.nsu.fit.tm_backend.shared.Authority;
import org.nsu.fit.tm_backend.shared.Globals;
import org.slf4j.Logger;
import org.nsu.fit.tm_backend.database.IDBService;
import org.nsu.fit.tm_backend.database.data.CustomerPojo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Лабораторная 2: покрыть юнит тестами класс CustomerManager на 100%.
class CustomerManagerTest {
    private Logger logger;
    private IDBService dbService;
    private CustomerManager customerManager;

    private CustomerPojo createCustomerInput;

    @BeforeEach
    void init() {
        // Создаем mock объекты.
        dbService = mock(IDBService.class);
        logger = mock(Logger.class);

        // Создаем класс, методы которого будем тестировать,
        // и передаем ему наши mock объекты.
        customerManager = new CustomerManager(dbService, logger);
    }

    private void createCustomerInput() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = "Baba_Jaga";
        createCustomerInput.balance = 0;
    }

    @Test
    void testMeAdmin() {
        createCustomerInput();
        createCustomerInput.id = UUID.randomUUID();
        AuthenticatedUserDetails details = new AuthenticatedUserDetails(createCustomerInput.id.toString(),
                createCustomerInput.login, Sets.newSet(Authority.ADMIN_ROLE));
        ContactPojo contactPojo = customerManager.me(details);
        assertEquals(Globals.ADMIN_LOGIN, contactPojo.login);
    }

    @Test
    void testMeCustomer() {
        createCustomerInput();
        createCustomerInput.id = UUID.randomUUID();
        AuthenticatedUserDetails details = new AuthenticatedUserDetails(createCustomerInput.id.toString(),
                createCustomerInput.login, Sets.newSet(Authority.CUSTOMER_ROLE));

        when(dbService.getCustomerByLogin(createCustomerInput.login)).thenReturn(createCustomerInput);

        ContactPojo contactPojo = customerManager.me(details);

        assertEquals(createCustomerInput.login, contactPojo.login);
        assertNull(contactPojo.pass);
    }

    @Test
    void testCreateCustomer() {
        // настраиваем mock.

        createCustomerInput();

        CustomerPojo createCustomerOutput = new CustomerPojo();
        createCustomerOutput.id = UUID.randomUUID();
        createCustomerOutput.firstName = "John";
        createCustomerOutput.lastName = "Wick";
        createCustomerOutput.login = "john_wick@gmail.com";
        createCustomerOutput.pass = "Baba_Jaga";
        createCustomerOutput.balance = 0;

        when(dbService.createCustomer(createCustomerInput)).thenReturn(createCustomerOutput);

        // Вызываем метод, который хотим протестировать
        CustomerPojo customer = customerManager.createCustomer(createCustomerInput);

        // Проверяем результат выполнения метода
        assertEquals(customer.id, createCustomerOutput.id);

        // Проверяем, что метод по созданию Customer был вызван ровно 1 раз с определенными аргументами
        verify(dbService, times(1)).createCustomer(createCustomerInput);

        // Проверяем, что другие методы не вызывались...
        verify(dbService, times(0)).getCustomers();
    }

    @Test
    void testCreateCustomerWithExistingLogin() {
        createCustomerInput();
        when(dbService.createCustomer(createCustomerInput)).thenReturn(createCustomerInput);
        when(dbService.getCustomerByLogin(createCustomerInput.login)).thenReturn(createCustomerInput);
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                customerManager.createCustomer(createCustomerInput));
        assertEquals("Customer with such login already exists", exception.getMessage());
    }

    @Test
    void testGetCustomers() {
        createCustomerInput();
        List<CustomerPojo> list = new ArrayList<>();
        list.add(createCustomerInput);
        when(dbService.getCustomers()).thenReturn(list);

        // Вызываем метод, который хотим протестировать
        List<CustomerPojo> customers = customerManager.getCustomers();

        // Проверяем результат выполнения метода
        assertEquals(1, customers.size());
        assertEquals(createCustomerInput.id, customers.get(0).id);

        // Проверяем, что метод по созданию Customer был вызван ровно 1 раз с определенными аргументами
        verify(dbService, times(1)).getCustomers();

        // Проверяем, что другие методы не вызывались...
        verify(dbService, times(0)).createCustomer(any());
    }

    @Test
    void testGetCustomer() {
        createCustomerInput();
        when(dbService.getCustomer(createCustomerInput.id)).thenReturn(createCustomerInput);

        // Вызываем метод, который хотим протестировать
        CustomerPojo customer = customerManager.getCustomer(createCustomerInput.id);

        // Проверяем результат выполнения метода
        assertEquals(createCustomerInput.id, customer.id);

        // Проверяем, что метод по созданию Customer был вызван ровно 1 раз с определенными аргументами
        verify(dbService, times(1)).getCustomer(createCustomerInput.id);

        // Проверяем, что другие методы не вызывались...
        verify(dbService, times(0)).getCustomers();
    }

    @Test
    void testLookup() {
        createCustomerInput();
        CustomerPojo anotherCustomer = new CustomerPojo();
        anotherCustomer.firstName = "John";
        anotherCustomer.lastName = "Wick";
        anotherCustomer.login = "john_wick1@gmail.com";
        anotherCustomer.pass = "Baba_Jaga";
        anotherCustomer.balance = 0;
        List<CustomerPojo> list = new ArrayList<>(2);
        list.add(anotherCustomer);
        list.add(createCustomerInput);
        when(dbService.getCustomers()).thenReturn(list);

        // Вызываем метод, который хотим протестировать
        CustomerPojo customer = customerManager.lookupCustomer(createCustomerInput.login);

        // Проверяем результат выполнения метода
        assertEquals(createCustomerInput.id, customer.id);

        CustomerPojo nullCustomer = customerManager.lookupCustomer("not existing login");
        assertNull(nullCustomer);

        // Проверяем, что другие методы не вызывались...
        verify(dbService, times(0)).getCustomer(any());
    }
    @Test
    void testTopUpBalance() {
        createCustomerInput();
        when(dbService.getCustomer(createCustomerInput.id)).thenReturn(createCustomerInput);
        TopUpBalancePojo tubp = new TopUpBalancePojo();
        tubp.customerId = createCustomerInput.id;
        tubp.money = 100;
        createCustomerInput();
        // Вызываем метод, который хотим протестировать
        CustomerPojo customer = customerManager.topUpBalance(tubp);

        // Проверяем результат выполнения метода
        assertEquals(createCustomerInput.balance + tubp.money, customer.balance);

        verify(dbService, times(1)).getCustomer(createCustomerInput.id);
        verify(dbService, times(1)).editCustomer(customer);

        // Проверяем, что другие методы не вызывались...
        verify(dbService, times(0)).getCustomers();
    }

    @Test
    void testCreateCustomerWithNullLogin() {
        createCustomerInput();
        createCustomerInput.pass = null;
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                customerManager.createCustomer(createCustomerInput));
        assertEquals("Field 'customer.pass' is null.", exception.getMessage());
    }

    @Test
    void testDeleteCustomer() {
        createCustomerInput();
        customerManager.deleteCustomer(createCustomerInput.id);
        verify(dbService, times(1)).deleteCustomer(createCustomerInput.id);
    }

    @Test
    void testCreateCustomerWithShortPassword() {
        createCustomerInput();
        createCustomerInput.pass = "123";
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                customerManager.createCustomer(createCustomerInput));
        assertEquals("Password's length should be more or equal 6 symbols and less or equal 12 symbols.", exception.getMessage());
    }

    // Как не надо писать тест...
    @Test
    void testCreateCustomerWithNullArgument_Wrong() {
        try {
            customerManager.createCustomer(null);
        } catch (IllegalArgumentException ex) {
            assertEquals("Argument 'customer' is null.", ex.getMessage());
        }
    }

    @Test
    void testCreateCustomerWithNullArgument_Right() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                customerManager.createCustomer(null));
        assertEquals("Argument 'customer' is null.", exception.getMessage());
    }

    @Test
    void testCreateCustomerWithEasyPassword() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = "123qwe";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Password is very easy.", exception.getMessage());
    }
}

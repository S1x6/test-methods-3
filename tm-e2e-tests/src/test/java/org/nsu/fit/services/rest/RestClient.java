package org.nsu.fit.services.rest;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.glassfish.jersey.client.ClientConfig;
import org.nsu.fit.services.log.Logger;
import org.nsu.fit.services.rest.data.AccountTokenPojo;
import org.nsu.fit.services.rest.data.ContactPojo;
import org.nsu.fit.services.rest.data.CredentialsPojo;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.nsu.fit.shared.JsonMapper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.Locale;

public class RestClient {
    private static final String REST_URI = "http://localhost:8080/tm-backend/rest";

    private static Client client = ClientBuilder.newClient(new ClientConfig().register(RestClientLogFilter.class));

    public AccountTokenPojo authenticate(String login, String pass) {
        CredentialsPojo credentialsPojo = new CredentialsPojo();

        credentialsPojo.login = login;
        credentialsPojo.pass = pass;

        return post("authenticate", JsonMapper.toJson(credentialsPojo, true), AccountTokenPojo.class, null);
    }

    public CustomerPojo createAutoGeneratedCustomer(AccountTokenPojo accountToken) {
        ContactPojo contactPojo = new ContactPojo();

        // Done
        // Лабораторная 3: Добавить обработку генерацию фейковых имен, фамилий и логинов.
        // * Исследовать этот вопрос более детально, возможно прикрутить специальную библиотеку для генерации фейковых данных.

        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());
        contactPojo.firstName = fakeValuesService.letterify("?????");
        contactPojo.lastName = fakeValuesService.letterify("?????????");
        contactPojo.login = fakeValuesService.bothify("?????_##@??????.com");
        contactPojo.pass = fakeValuesService.bothify("??##??#??#");

        return post("customers", JsonMapper.toJson(contactPojo, true), CustomerPojo.class, accountToken);
    }

    private static <R> R post(String path, String body, Class<R> responseType, AccountTokenPojo accountToken) {
        // Лабораторная 3: Добавить обработку Responses и Errors. Выводите их в лог.
        // Подумайте почему в filter нет Response чтобы можно было удобно его сохранить.
        Invocation.Builder request = client
                .target(REST_URI)
                .path(path)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        if (accountToken != null) {
            request.header("Authorization", "Bearer " + accountToken.token);
        }

        String response = request.post(Entity.entity(body, MediaType.APPLICATION_JSON), String.class);

        return JsonMapper.fromJson(response, responseType);
    }

    private static class RestClientLogFilter implements ClientRequestFilter {
        @Override
        public void filter(ClientRequestContext requestContext) {
            Logger.debug(requestContext.getEntity().toString());

            Logger.debug("METHOD: " + requestContext.getMethod());
            requestContext.getHeaders().forEach((h, list) -> {
                Logger.debug("HEADER: " + h + ": " + list.stream().reduce((o, acc) -> acc + o.toString()));
            });
            // Done
            // Лабораторная 3: разобраться как работает данный фильтр
            // и добавить логирование METHOD и HEADERS.
        }
    }
}

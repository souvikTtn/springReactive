package com.reactiveSpringPractice.reactiveSpringPractice.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@WebFluxTest
public class FluxControllerTest {

    @Autowired
    //got this bean using @WebFluxTest annotation
    private WebTestClient webTestClient;

    @Test
    public void testFluxControllerApproach1() {
        Flux<Integer> integerFlux = webTestClient.get().uri("/fluxstream")
                                                 //.accept(MediaType.APPLICATION_JSON_UTF8)
                                                 .exchange()
                                                 .expectStatus().isOk()
                                                 .returnResult(Integer.class)
                                                 .getResponseBody();
        StepVerifier.create(integerFlux)
                    .expectSubscription()
                    .expectNext(1, 2, 3, 4)
                    .verifyComplete();
    }

    @Test
    public void testFluxControllerApproach2() {
        webTestClient.get().uri("/fluxstream")
                     .exchange()
                     .expectStatus().isOk()
                     .expectBodyList(Integer.class)
                     .hasSize(4);
    }

    @Test
    public void testFluxControllerApproach3() {
        List<Integer> expectedResponse = Arrays.asList(1, 2, 3, 4);
        EntityExchangeResult<List<Integer>> realtimeResponse = webTestClient.get().uri("/fluxstream")
                                                                            .exchange()
                                                                            .expectStatus().isOk()
                                                                            .expectBodyList(Integer.class)
                                                                            .returnResult();
        realtimeResponse.getResponseBody();
        Assert.assertEquals(expectedResponse, realtimeResponse.getResponseBody());
    }

    @Test
    public void testFluxControllerApproach4() {
        List<Integer> expectedResponse = Arrays.asList(1, 2, 3, 4);
        webTestClient.get().uri("/fluxstream")
                     .exchange()
                     .expectStatus().isOk()
                     .expectBodyList(Integer.class)
                     .consumeWith(response -> Assert.assertEquals(expectedResponse,
                                                                  response.getResponseBody()));
    }

    @Test
    public void testFluxControllerStream() {
        Flux<Long> longFlux = webTestClient.get().uri("/fluxstream2")
                                           .accept(MediaType.APPLICATION_STREAM_JSON)
                                           .exchange()
                                           .expectStatus().isOk()
                                           .returnResult(Long.class)
                                           .getResponseBody();
        StepVerifier.create(longFlux.log())
                    .expectSubscription()
                    .expectNext(0L, 1L, 2L)
                    .thenCancel()
                    .verify();


    }

}

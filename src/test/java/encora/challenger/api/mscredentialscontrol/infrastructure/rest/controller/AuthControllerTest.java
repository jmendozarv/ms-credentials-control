package encora.challenger.api.mscredentialscontrol.infrastructure.rest.controller;

import static org.junit.jupiter.api.Assertions.*;

import encora.challenger.api.mscredentialscontrol.application.services.TokenManagerService;
import encora.challenger.api.mscredentialscontrol.domain.model.dto.CredentialRequest;
import encora.challenger.api.mscredentialscontrol.domain.model.dto.TokenRequest;
import encora.challenger.api.mscredentialscontrol.domain.model.dto.TokenResponse;
import encora.challenger.api.mscredentialscontrol.infrastructure.security.SecurityConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(AuthController.class)
@ContextConfiguration(classes = {AuthController.class})
@Import(SecurityConfig.class)
public class AuthControllerTest {

  @Autowired
  private WebTestClient webTestClient;

  @MockBean
  private TokenManagerService tokenManagerService;

  @Test
  public void testAuthTokenPost_Success() {
    // Arrange
    TokenRequest tokenRequest = new TokenRequest();
    tokenRequest.setClusterKey("clusterKey");
    tokenRequest.setUser("pepito");
    tokenRequest.setPassword("micontraseña");

    String expectedToken = "expectedToken";
    Mockito.when(tokenManagerService.generateTokenString(
        Mockito.anyString(), Mockito.anyString(), Mockito.anyString()
    )).thenReturn(Mono.just(expectedToken));

    // Act
    WebTestClient.ResponseSpec response = webTestClient.post()
        .uri("/auth/token")
        .body(Mono.just(tokenRequest), TokenRequest.class)
        .exchange();

    // Assert
    response
        .expectStatus().isOk()
        .expectBody(TokenResponse.class)
        .value(tokenResponse -> {
          Assertions.assertEquals(expectedToken, tokenResponse.getToken());
        });
  }

  @Test
  public void testAuthTokenPost_BadRequest() {
    // Arrange
    TokenRequest tokenRequest = new TokenRequest();
    tokenRequest.setClusterKey("invalidClusterKey");
    tokenRequest.setUser("user");
    tokenRequest.setPassword("wrongPassword");

    Mockito.when(tokenManagerService.generateTokenString(
        Mockito.anyString(), Mockito.anyString(), Mockito.anyString()
    )).thenReturn(Mono.empty());

    // Act
    WebTestClient.ResponseSpec response = webTestClient.post()
        .uri("/auth/token")
        .body(Mono.just(tokenRequest), TokenRequest.class)
        .exchange();

    // Assert
    response
        .expectStatus().isBadRequest();
  }

  @Test
  public void testAuthRegisterPost_BadRequest() {
    // Arrange
    CredentialRequest credentialRequest = new CredentialRequest();
    credentialRequest.setUser(null);  // Datos faltantes o incorrectos
    credentialRequest.setPassword("newPassword");

    Mockito.when(tokenManagerService.registerCredential(
        Mockito.any(CredentialRequest.class)
    )).thenReturn(Mono.empty());  // No devuelve datos

    // Act
    WebTestClient.ResponseSpec response = webTestClient.post()
        .uri("/auth/register")
        .body(Mono.just(credentialRequest), CredentialRequest.class)
        .exchange();

    // Assert
    response
        .expectStatus().isBadRequest();  // Esperamos un estado 400 Bad Request
  }


}
package encora.challenger.api.mscredentialscontrol.application.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import encora.challenger.api.mscredentialscontrol.domain.Credential;
import encora.challenger.api.mscredentialscontrol.domain.model.dto.CredentialRequest;
import encora.challenger.api.mscredentialscontrol.domain.model.dto.CredentialResponse;
import encora.challenger.api.mscredentialscontrol.domain.port.TokenServicePort;
import encora.challenger.api.mscredentialscontrol.infrastructure.adapters.properties.JwttProperties;
import encora.challenger.api.mscredentialscontrol.infrastructure.security.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Mono;

@Import(SecurityConfig.class)
public class TokenManagerServiceTest {

  @Mock
  private TokenServicePort tokenServicePort;

  @Mock
  private JwttProperties jwttProperties;

  @InjectMocks
  private TokenManagerService tokenManagerService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGenerateTokenString_ValidCredential() {
    String clusterKey = "cluster1";
    String user = "testUser";
    String password = "testPassword";

    Credential validCredential = new Credential(clusterKey, user, password);
    when(tokenServicePort.findByUser(user)).thenReturn(Mono.just(validCredential));
    when(jwttProperties.getExpiration()).thenReturn(3600000); // 1 hora
    when(jwttProperties.getSecret()).thenReturn(
        "0ddf5597e02d981f8803c4cc11f015a4e52679d706edb29b595d9e466def5bcf95273a3053ab5d97ee893c23e4023b912daefaade316406a33b7685d4d223dfa");

    Mono<String> result = tokenManagerService.generateTokenString(clusterKey, user, password);

    assertNotNull(result.block());
  }

  @Test
  public void testGenerateTokenString_InvalidCredential() {
    String clusterKey = "cluster1";
    String user = "invalidUser";
    String password = "testPassword";

    when(tokenServicePort.findByUser(user)).thenReturn(Mono.empty());

    Mono<String> result = tokenManagerService.generateTokenString(clusterKey, user, password);

    assertThrows(RuntimeException.class, result::block);
    verify(tokenServicePort, times(1)).findByUser(user);
  }

  @Test
  public void testRegisterCredential_NewCredential() {
    CredentialRequest request = new CredentialRequest();
    request.setClusterKey("cluster1");
    request.setUser("newUser");
    request.setPassword("password");

    when(tokenServicePort.findByUser("newUser")).thenReturn(Mono.empty());
    when(tokenServicePort.save(any(Credential.class))).thenReturn(
        Mono.just(Credential.builder().user("user").build()));

    Mono<CredentialResponse> result = tokenManagerService.registerCredential(request);

    assertNotNull(result.block());
    assertEquals("Credential registered successfully", result.block().getMessage());
  }

  @Test
  public void testRegisterCredential_ExistingCredential() {
    Credential existingCredential = new Credential("cluster1", "existingUser", "password");

    CredentialRequest request = new CredentialRequest();
    request.setClusterKey("cluster1");
    request.setUser("existingUser");
    request.setPassword("password");

    when(tokenServicePort.findByUser("existingUser")).thenReturn(Mono.just(existingCredential));

    Mono<CredentialResponse> result = tokenManagerService.registerCredential(request);

    assertThrows(RuntimeException.class, result::block);
    verify(tokenServicePort, times(1)).findByUser("existingUser");
  }

}
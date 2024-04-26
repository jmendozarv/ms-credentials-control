package encora.challenger.api.mscredentialscontrol.application.services;

import encora.challenger.api.mscredentialscontrol.application.usecases.TokenService;
import encora.challenger.api.mscredentialscontrol.domain.Credential;
import encora.challenger.api.mscredentialscontrol.domain.model.dto.CredentialRequest;
import encora.challenger.api.mscredentialscontrol.domain.model.dto.CredentialResponse;
import encora.challenger.api.mscredentialscontrol.domain.port.TokenServicePort;
import encora.challenger.api.mscredentialscontrol.infrastructure.adapters.properties.JwttProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TokenManagerService implements TokenService {

  private final TokenServicePort tokenServicePort;

  private final JwttProperties jwttProperties;


  @Override
  public Mono<String> generateTokenString(String clusterKey, String user, String password) {
    return tokenServicePort.findByUser(user)
        .filter(credential -> credential.getPassword().equals(password))
        .map(credential -> Jwts.builder()
            .setSubject(clusterKey)
            .claim("user", user)
            .setIssuedAt(new Date())
            .setExpiration(
                new Date(System.currentTimeMillis() + jwttProperties.getExpiration()))
            .signWith(SignatureAlgorithm.HS512, jwttProperties.getSecret())
            .compact())
        .switchIfEmpty(Mono.error(new RuntimeException("Invalid credentials")));
  }

  @Override
  public Mono<CredentialResponse> registerCredential(CredentialRequest credentialRequest) {
    return tokenServicePort.findByUser(
            credentialRequest.getUser())
        .flatMap(existingCredential -> Mono.error(
            new RuntimeException("Credential already exists"))
        )
        .switchIfEmpty(Mono.defer(() -> tokenServicePort.save(
            Credential
                .builder()
                .clusterKey(credentialRequest.getClusterKey())
                .user(credentialRequest.getUser())
                .password(credentialRequest.getPassword())
                .build())))
        .map(savedCredential -> {
          CredentialResponse response = new CredentialResponse();
          response.setMessage("Credential registered successfully");
          return response;
        });
  }
}

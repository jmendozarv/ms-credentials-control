package encora.challenger.api.mscredentialscontrol.application.services;

import encora.challenger.api.mscredentialscontrol.application.usecases.TokenService;
import encora.challenger.api.mscredentialscontrol.domain.Credential;
import encora.challenger.api.mscredentialscontrol.domain.model.dto.CredentialRequest;
import encora.challenger.api.mscredentialscontrol.domain.model.dto.CredentialResponse;
import encora.challenger.api.mscredentialscontrol.domain.port.TokenServicePort;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TokenManagerService implements TokenService {

  private final TokenServicePort tokenServicePort;

  @Value(value = "${jwt.secret}")
  private String secret;
  @Value(value = "${jwt.expiration}")
  private Integer expiration;


  @Override
  public Mono<String> generateTokenString(String clusterKey, String user, String password) {
    return tokenServicePort.findByClusterKey(clusterKey)
        .filter(credential -> credential.getUser().equals(user) && credential.getPassword()
            .equals(password))
        .map(credential -> {
          // Generar token JWT
          return Jwts.builder()
              .setSubject(clusterKey)
              .claim("user", user)
              .setIssuedAt(new Date())
              .setExpiration(
                  new Date(System.currentTimeMillis() + expiration))
              .signWith(SignatureAlgorithm.HS512, secret)
              .compact();
        })
        .switchIfEmpty(Mono.error(new RuntimeException(
            "Invalid credentials")));
  }

  @Override
  public Mono<CredentialResponse> registerCredential(CredentialRequest credentialRequest) {
    return tokenServicePort.findByUser(
            credentialRequest.getUser())
        .filter(existingCredential -> existingCredential.getUser()
            .equals(credentialRequest.getUser()))
        .flatMap(existingCredential -> Mono.error(
            new RuntimeException("Credential already exists")))
        .switchIfEmpty(Mono.defer(() -> {
          // Si no existe, registrar la nueva credencial
          return tokenServicePort.save(
              Credential
                  .builder()
                  .clusterKey(credentialRequest.getClusterKey())
                  .user(credentialRequest.getUser())
                  .password(credentialRequest.getPassword())
                  .build());  // Guardar la nueva credencial
        }))
        .map(savedCredential -> {
          // Crear y devolver CredentialResponse
          CredentialResponse response = new CredentialResponse();
          response.setMessage("Credential registered successfully");
          return response;
        });
  }
}

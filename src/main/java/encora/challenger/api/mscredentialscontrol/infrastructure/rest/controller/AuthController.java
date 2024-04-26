package encora.challenger.api.mscredentialscontrol.infrastructure.rest.controller;

import encora.challenger.api.mscredentialscontrol.application.services.TokenManagerService;
import encora.challenger.api.mscredentialscontrol.domain.model.dto.CredentialRequest;
import encora.challenger.api.mscredentialscontrol.domain.model.dto.CredentialResponse;
import encora.challenger.api.mscredentialscontrol.domain.model.dto.TokenRequest;
import encora.challenger.api.mscredentialscontrol.domain.model.dto.TokenResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.openapitools.api.AuthApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@Log4j2
public class AuthController implements AuthApi {

  private final TokenManagerService tokenManagerService;

  @Override
  public Mono<ResponseEntity<TokenResponse>> authTokenPost(Mono<TokenRequest> tokenRequest,
      ServerWebExchange exchange) {

    return tokenRequest
        .flatMap(request ->
            tokenManagerService.generateTokenString(
                request.getClusterKey(),
                request.getUser(),
                request.getPassword())
        )
        .map(token -> {
          TokenResponse tokenResponse = new TokenResponse();
          tokenResponse.setToken(token);
          return ResponseEntity.ok(tokenResponse);
        })
        .switchIfEmpty(Mono.just(
            ResponseEntity.badRequest().build()
        ));
  }

  @Override
  public Mono<ResponseEntity<CredentialResponse>> authRegisterPost(
      Mono<CredentialRequest> credentialRequest, ServerWebExchange exchange) {
    return credentialRequest
        .flatMap(tokenManagerService::registerCredential
        )
        .map(credential -> {
          CredentialResponse response = new CredentialResponse();
          response.setMessage("Credential registered successfully");
          return ResponseEntity.status(HttpStatus.CREATED)
              .body(response);
        })
        .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()
        ))
        .onErrorResume(ex -> Mono.just(ResponseEntity.badRequest().build()
        ));
  }

}

package encora.challenger.api.mscredentialscontrol.application.usecases;

import encora.challenger.api.mscredentialscontrol.domain.model.dto.CredentialRequest;
import encora.challenger.api.mscredentialscontrol.domain.model.dto.CredentialResponse;
import reactor.core.publisher.Mono;

public interface TokenService {

  Mono<String> generateTokenString(String clusterKey, String user, String password);

  Mono<CredentialResponse> registerCredential(CredentialRequest credentialRequest);
}

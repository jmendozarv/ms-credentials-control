package encora.challenger.api.mscredentialscontrol.domain.port;

import encora.challenger.api.mscredentialscontrol.domain.Credential;
import reactor.core.publisher.Mono;

public interface TokenServicePort {

  Mono<Credential> findByClusterKey(String clusterKey);

  Mono<Credential> findByUser(String user);

  public Mono<Credential> save(Credential credential);
}

package encora.challenger.api.mscredentialscontrol.infrastructure.adapters.repository;

import encora.challenger.api.mscredentialscontrol.infrastructure.adapters.entities.CredentialEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CredentialRepository extends ReactiveMongoRepository<CredentialEntity, String> {

  Mono<CredentialEntity> findByClusterKey(String clusterKey);

  Mono<CredentialEntity> findByUser(String user);


}

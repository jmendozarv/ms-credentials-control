package encora.challenger.api.mscredentialscontrol.infrastructure.adapters.repository;

import encora.challenger.api.mscredentialscontrol.domain.Credential;
import encora.challenger.api.mscredentialscontrol.domain.port.TokenServicePort;
import encora.challenger.api.mscredentialscontrol.infrastructure.adapters.entities.CredentialEntity;
import encora.challenger.api.mscredentialscontrol.infrastructure.adapters.mapper.CredentialDboMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class CredentialRepositoryAdapter implements TokenServicePort {

  private final CredentialRepository credentialRepository;
  private final CredentialDboMapper credentialDboMapper;

  @Override
  public Mono<Credential> findByClusterKey(String clusterKey) {
    return credentialRepository.findByClusterKey(clusterKey).map(credentialDboMapper::toDomain);
  }

  @Override
  public Mono<Credential> findByUser(String user) {
    return credentialRepository.findByUser(user).map(credentialDboMapper::toDomain);
  }

  @Override
  public Mono<Credential> save(Credential credential) {
    CredentialEntity credentialEntity = credentialDboMapper.toDbo(credential);
    return credentialRepository.save(credentialEntity).map(credentialDboMapper::toDomain);
  }
}

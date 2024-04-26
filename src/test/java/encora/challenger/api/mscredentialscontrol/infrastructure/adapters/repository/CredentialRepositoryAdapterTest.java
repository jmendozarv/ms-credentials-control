package encora.challenger.api.mscredentialscontrol.infrastructure.adapters.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import encora.challenger.api.mscredentialscontrol.domain.Credential;
import encora.challenger.api.mscredentialscontrol.infrastructure.adapters.entities.CredentialEntity;
import encora.challenger.api.mscredentialscontrol.infrastructure.adapters.mapper.CredentialDboMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

class CredentialRepositoryAdapterTest {
  @Mock
  private CredentialRepository credentialRepository;

  @Mock
  private CredentialDboMapper credentialDboMapper;

  @InjectMocks
  private CredentialRepositoryAdapter credentialRepositoryAdapter;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testFindByClusterKey() {
    String clusterKey = "cluster1";
    CredentialEntity entity = new CredentialEntity();
    entity.setClusterKey(clusterKey);

    when(credentialRepository.findByClusterKey(clusterKey)).thenReturn(Mono.just(entity));
    when(credentialDboMapper.toDomain(entity)).thenReturn(new Credential(clusterKey, "user", "password"));

    Mono<Credential> result = credentialRepositoryAdapter.findByClusterKey(clusterKey);

    assertNotNull(result.block());
    assertEquals(clusterKey, result.block().getClusterKey());
    verify(credentialRepository, times(1)).findByClusterKey(clusterKey);
  }

  @Test
  public void testFindByUser() {
    String user = "testUser";
    CredentialEntity entity = new CredentialEntity();
    entity.setUser(user);

    when(credentialRepository.findByUser(user)).thenReturn(Mono.just(entity));
    when(credentialDboMapper.toDomain(entity)).thenReturn(new Credential("cluster1", user, "password"));

    Mono<Credential> result = credentialRepositoryAdapter.findByUser(user);

    assertNotNull(result.block());
    assertEquals(user, result.block().getUser());
    verify(credentialRepository, times(1)).findByUser(user);
  }

  @Test
  public void testSave() {
    Credential credential = new Credential("cluster1", "user", "password");
    CredentialEntity entity = new CredentialEntity();
    entity.setClusterKey("cluster1");
    entity.setUser("user");
    entity.setPassword("password");

    when(credentialDboMapper.toDbo(credential)).thenReturn(entity);
    when(credentialRepository.save(entity)).thenReturn(Mono.just(entity));
    when(credentialDboMapper.toDomain(entity)).thenReturn(credential);

    Mono<Credential> result = credentialRepositoryAdapter.save(credential);

    assertNotNull(result.block());
    assertEquals("cluster1", result.block().getClusterKey());
    verify(credentialRepository, times(1)).save(entity);
  }
}
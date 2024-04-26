package encora.challenger.api.mscredentialscontrol.infrastructure.adapters.mapper;

import encora.challenger.api.mscredentialscontrol.domain.Credential;
import encora.challenger.api.mscredentialscontrol.infrastructure.adapters.entities.CredentialEntity;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CredentialDboMapper {
  @Mapping(source = "user", target = "user")
  @Mapping(source = "clusterKey", target = "clusterKey")
  @Mapping(source = "password", target = "password")
  CredentialEntity toDbo(Credential credential);

  @Mapping(source = "user", target = "user")
  @Mapping(source = "clusterKey", target = "clusterKey")
  Credential toDomain(CredentialEntity credentialEntity);
}

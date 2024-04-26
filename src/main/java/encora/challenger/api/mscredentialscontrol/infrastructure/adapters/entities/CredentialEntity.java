package encora.challenger.api.mscredentialscontrol.infrastructure.adapters.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "credential-storage")
public class CredentialEntity {

  @Field("cluster_key")
  private String clusterKey;
  private String user;
  private String password;
}

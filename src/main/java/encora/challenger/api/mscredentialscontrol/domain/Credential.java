package encora.challenger.api.mscredentialscontrol.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Credential {
  private String clusterKey;
  private String user;
  private String password;

}

package encora.challenger.api.mscredentialscontrol.infrastructure.adapters.mongo;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

@Configuration
public class MongoMappingConfig {
  @Autowired
  private MappingMongoConverter mappingMongoConverter;

  @PostConstruct
  public void setUpMongoEscapeCharacterConversion() {
    mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
  }
}

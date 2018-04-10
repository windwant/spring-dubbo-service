package org.windwant.spring.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.windwant.spring.model.Person;

import java.util.List;

/**
 * Created by Administrator on 18-3-23.
 */
//@RepositoryRestResource(collectionResourceRel = "people", path = "people")
public interface MongoPersonRepository extends MongoRepository<Person, String> {

    List<Person> findByLastName(@Param("name") String name);
}

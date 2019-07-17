package org.haycco.tanlan.user.repository;

import org.haycco.tanlan.user.model.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 * @author haycco
 */
public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Mono<User> findByPhoneAndStatusNot(String phone, Integer status);

    Mono<User> findByNickname(String nickname);

    Mono<User> findByIdAndStatusNot(String id, Integer status);

    @Query("{'$or':[{'_id':?0},{'nickname':?0}]}")
    Mono<User> findByIdOrNickname(String idOrNickname);
}

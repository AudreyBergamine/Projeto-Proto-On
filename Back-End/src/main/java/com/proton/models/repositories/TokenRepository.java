package com.proton.models.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.proton.models.entities.token.Token;

public interface TokenRepository extends JpaRepository<Token, Integer> {

  /*@Query("""
      select t from Token t
      where t.user.id = :id and (t.expired = false or t.revoked = false)
      """)*/

  // @Query(value = """
  //     select t from Token t inner join Municipe u\s
  //     on t.user.id = u.id\s
  //     where u.id = :id and (t.expired = false or t.revoked = false)\s
  //     """)
      @Query(value = """
      select t from Token t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
  List<Token> findAllValidTokenByUser(Integer id);

  Optional<Token> findByToken(String token);

}

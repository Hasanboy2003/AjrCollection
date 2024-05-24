package com.yurakamri.ajrcollection.repository;

import com.yurakamri.ajrcollection.entity.UserSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserSearchRepo extends JpaRepository<UserSearch, UUID> {

    @Query(
            nativeQuery = true,
            value = "select us.*\n" +
                    "from user_search us\n" +
                    "where us.user_id = :userId\n" +
                    "  and us.created_at = (select min(us2.created_at) from user_search us2 where us2.user_id = :userId)"
    )
    UserSearch getLastUserSearch(@Param("userId") UUID userId);

    boolean existsByUserIdAndSearchText(UUID user_id, String searchText);

    List<UserSearch> findAllByUserIdOrderByCreatedAtDesc(UUID user_id);
}

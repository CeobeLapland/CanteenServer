package com.canteen.repository;

import com.canteen.model.entity.Seasoning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// SeasoningRepository
@Repository
public interface SeasoningRepository extends JpaRepository<Seasoning, Long> {

}

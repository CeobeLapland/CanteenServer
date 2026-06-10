package com.canteen.repository;

import com.canteen.model.entity.mid.FoodTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// FoodTagRepository
@Repository
public interface FoodTagRepository extends JpaRepository<FoodTag, Long> {
}

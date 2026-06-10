package com.canteen.repository;

import com.canteen.model.entity.mid.FoodPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// FoodPostRepository
@Repository
public interface FoodPostRepository extends JpaRepository<FoodPost, Long> {

}

package com.canteen.repository;

import com.canteen.model.entity.mid.PostType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// PostTypeRepository
@Repository
public interface PostTypeRepository extends JpaRepository<PostType, Long> {
}

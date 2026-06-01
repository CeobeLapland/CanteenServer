package com.canteen.repository;

import com.canteen.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// TagRepository
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

}
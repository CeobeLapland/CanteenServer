package com.canteen.repository;

import com.canteen.model.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// TypeRepository
@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {

}

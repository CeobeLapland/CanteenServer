package com.canteen.repository;

import com.canteen.model.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 数据访问层（Repository）
 * <p>所有 Repository 集中在此文件，后续可拆分为独立接口文件。
 * Spring Data JPA 会自动实现接口方法，无需手写 SQL（基础 CRUD）。
 * 复杂查询使用 {@code @Query} 编写 JPQL 或原生 SQL。
 */
public class Repositories {





    // CampusRepository
    /*@Repository
    public interface CampusRepository extends JpaRepository<Campus, Long> {

    }

    // CampusRepository
    @Repository
    public interface CanteenRepository extends JpaRepository<Canteen, Long> {

    }

    // CampusRepository
    @Repository
    public interface FloorRepository extends JpaRepository<Floor, Long> {

    }*/


}

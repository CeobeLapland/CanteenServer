package com.canteen.repository;

import com.canteen.model.entity.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    /** 按菜名模糊搜索（分页） */
    Page<Food> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    /** 按菜名精确查找 */
    Optional<Food> findByName(String name);

    /** 根据FilterFoodRequest中的条件动态查询菜品列表（分页） */
    @Query("SELECT f FROM Food f " +
            "WHERE (:name IS NULL OR LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:campus IS NULL OR f.campus = :campus) " +
            "AND (:canteen IS NULL OR f.canteen = :canteen) " +
            "AND (:floor IS NULL OR f.floor = :floor) " +
            "AND (:window IS NULL OR f.window = :window)" +
            "AND (:minPrice IS NULL OR f.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR f.price <= :maxPrice)"
    )
    Page<Food> filterFoods(@Param("name") String name,
                           @Param("campus") String campus,
                           @Param("canteen") String canteen,
                           @Param("floor") String floor,
                           @Param("window") String window,
                           int minPrice, int maxPrice,
                           Pageable pageable);

    /** 增加标签的详细查询方法，使用JPQL语句连接Food和Tag表 */
    @Query("SELECT f FROM Food f LEFT JOIN f.tags t " +
            "WHERE (:tags IS NULL OR t.name IN :tags) " +
            "AND (:name IS NULL OR LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:campus IS NULL OR f.campus = :campus) " +
            "AND (:canteen IS NULL OR f.canteen = :canteen) " +
            "AND (:floor IS NULL OR f.floor = :floor) " +
            "AND (:window IS NULL OR f.window = :window)" +
            "AND (:minPrice IS NULL OR f.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR f.price <= :maxPrice)" +
            "GROUP BY f.id " +
            "HAVING (:tags IS NULL OR COUNT(DISTINCT t.id) >= 1)")
    Page<Food> filterFoodsWithTags(@Param("name") String name,
                                 @Param("campus") String campus,
                                 @Param("canteen") String canteen,
                                 @Param("floor") String floor,
                                 @Param("window") String window,
                                 int minPrice, int maxPrice,
                                 @Param("tags") java.util.List<String> tags,
                                 Pageable pageable);

    // 在FoodRepository里查找tag表可以嘛？不行，FoodRepository只能操作Food实体，Tag是一个独立的实体，应该有自己的TagRepository来操作。虽然Food和Tag之间有多对多关系，但我们不能直接在FoodRepository里写查询Tag的JPQL语句。正确的做法是创建一个TagRepository接口，继承JpaRepository<Tag, Long>，然后在TagRepository里写查询Tag的方法，比如根据名字查找Tag或者根据ID查找Tag等。这样才能保持代码的清晰和职责分离。
}
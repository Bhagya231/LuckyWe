package org.ecommerce.repository;

import org.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>
{
    @Query("""
           SELECT o
           FROM Order o
           WHERE o.user.id = :id
           AND o.isDeleted = false
           """)
    List<Order> findCustomerOrders(@Param("id") UUID id);


    @Query("""
           SELECT o
           FROM Order o
           WHERE o.id = :id
           AND o.user.id = :userId
           AND o.isDeleted = false
           """)
    Optional<Order> findCustomerOrder(@Param("id") UUID id, @Param("userId") UUID userId);


    @Query("""
       SELECT o
       FROM Order o
       WHERE o.orderNumber = :orderNumber
       AND o.isDeleted = false
       """)
    Optional<Order> getOrderByOrderNumber(@Param("orderNumber") String orderNumber);


    @Query("""
           SELECT o
           FROM Order o
           WHERE o.isDeleted = false
           """)
    List<Order> findAllActiveOrders();


}

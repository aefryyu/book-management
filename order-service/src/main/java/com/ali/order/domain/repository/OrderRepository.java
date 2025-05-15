package com.ali.order.domain.repository;

import com.ali.order.domain.dto.OrderSummary;
import com.ali.order.domain.entity.OrderEntity;
import com.ali.order.domain.model.OrderStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByStatus(OrderStatus status);

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    default void updateOrderStatus(String orderNumber, OrderStatus status) {
        OrderEntity order = this.findByOrderNumber(orderNumber).orElseThrow();
        order.setStatus(status);
        this.save(order);
    }

    @Query(
            """
               select new com.ali.order.domain.dto.OrderSummary(o.orderNumber, o.status)
               from OrderEntity o
               where o.userName = :userName
            """)
    List<OrderSummary> findByUsername(@Param("userName") String username);

    @Query(
            """
            select distinct o
            from OrderEntity o left join fetch o.items
            where o.userName = :userName and o.orderNumber = :orderNumber
            """)
    Optional<OrderEntity> findByUserNameAndOrderNumber(
            @Param("userName") String userName, @Param("orderNumber") String orderNumber);
}

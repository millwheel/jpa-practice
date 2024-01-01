package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.dto.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {
    private final EntityManager em;
    public List<OrderSimpleQueryDto> findOrderDtos() {
        // delivery의 address는 값 타입으로 동작하기 때문에 그대로 넣어줘도 됨
        // 기본적으로 fetch join 쓸 때처럼 join 까지는 똑같이 한다.
        // 하지만 column을 더 적게 가져오므로 네트워크를 더 적게 사용. 하지만 요즘 네트워크 성능이 좋아서 크게 차이 나지 않음
        // 원하는 DTO를 즉시 반환
        return em.createQuery(
                        "select new jpabook.jpashop.dto.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" + " join o.member m" + " join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();
    }
}

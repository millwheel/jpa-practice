package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class, id);
    }

    public List<Order> findAllByString(OrderSearch orderSearch) {
        //language=JPAQL
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }
        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000); //􀭭􀘀 1000􀑤
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }

    public List<Order> findAllWithMemberDelivery() {
        // 성능 최적화
        // fetch join <- jpa에만 있는 문법
        // 이것을 사용하면 proxy 객체도 아니고 실제 Java 객체에 값을 다 채워서 가져 온다.
        // fetch를 사용하므로 지연로딩 자체가 일어나지 않고 바로 가져온다.
        return em.createQuery(
                        "select o from Order o" +
                                " join fetch o.member m" +
                                " join fetch o.delivery d", Order.class)
                .getResultList();
    }

    public List<Order> findAllWithItem() {
        // distinct 써주면 JPA가 order를 확인하고 orderid를 기준으로 중복을 제거하준다.
        return em.createQuery(
                        "select distinct o from Order o" +
                                " join fetch o.member m" +
                                " join fetch o.delivery d" +
                                " join fetch o.orderItems oi" +
                                " join fetch oi.item i", Order.class)
                .setFirstResult(0) // out of memory 뜰 수도 있음.
                .setMaxResults(100) // 이렇게 작성해도 DB에서 10000개 가져온 후, 메모리에서 100개 페이징 처리하게 된다.
                .getResultList(); // fetch join을 하는 순간 order의 기준이 모두 틀어지게 됨. 1대다 fetch join 할 때는 페이징 못 씀
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        // ToOne에만 fetch join을 사용한다. 이건 하나 씩만 연결하기 때문에 성능에 영향 안 준다.
        // 컬렉션에는 fetch join을 사용하지 않는다. 컬렉션은 지연 로딩으로 조회한다.
        // 대신 지연 로딩 성능 최적화를 위해 @BatchSize를 적용한다.
        return em.createQuery(
                        "select o from Order o" +
                                " join fetch o.member m" +
                                " join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}

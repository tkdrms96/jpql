package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx= em.getTransaction();

        tx.begin();

        /* 예외처리 */
        try {
            //반환타입이 명확할때 사용 TypeQuery
            Member member =  new Member();
            member.setUserName("member1");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();
            //case 1 : entity 프로젝션
            List<Member> result = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            //case2 : entity 프로젝션 (연관 mapping) 쿼리문이 join해서 날라감 <- 이경우 명시적으로 createQuery에 join을 명시해주는게 좋다.
            List<Team> result2 = em.createQuery("select t from Member m join Team t", Team.class)
                    .getResultList();

            //case3 : embedded 프로젝션 Order에서 embedded 타입인 Address를 가지고올때
            List<Address> result3 = em.createQuery("select o.address from Order o", Address.class)
                            .getResultList();

            //case4 : 스칼라 타입 return Object type casting이 필요함
            List<Address> result4 = em.createQuery("select distinct m.username, m.age from Member m", Address.class)
                    .getResultList();

            //type casting
            Object o = result4.get(0);
            Object[] resultObject = (Object[]) o;

            // none type casting
            List<Object[]> result5 = em.createQuery("select distinct m.username, m.age from Member m")
                    .getResultList();

            Object[] resultObject2 = result5.get(0);

            // new 명령어로 조회 DTO로 바로 조회하는 것
            List<MemberDTO> result6 = em.createQuery("select new jpql.MemberDTO distinct(m.username, m.age) from Member m", MemberDTO.class)
                            .getResultList();


            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }

        emf.close();
    }
}

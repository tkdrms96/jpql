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

            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
            //반환타입이 명확하지 않을 때 사용
            Query query2 = em.createQuery("select m from Member m");

            //결과조회 API case 1 결과값이 List일때 : query.getResultList()
            List<Member> resultList = query1.getResultList();

            for(Member member1 : resultList){
                System.out.println("member1 = "+ member1);
            }
            //결과조회 API case 2 결과값이 List일때 : query.getResultList()
            Object singleResult = query2.getSingleResult();

            System.out.println(singleResult);


            //getResultList <- 결과가 없을 때 Exception을 조심 하지 않아도 됨 빈 리스트 반환

            //getSingleResult <- 결과가 없을때 Exception을 반환

            //파라미터 바인딩
            em.createQuery("select m from Member m where m.username =: username").setParameter("username", "usernameParam");
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }

        emf.close();
    }
}

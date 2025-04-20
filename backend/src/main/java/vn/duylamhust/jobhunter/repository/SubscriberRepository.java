package vn.duylamhust.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.duylamhust.jobhunter.domain.Subscriber;

public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {
    Subscriber findByEmail(String email);
}

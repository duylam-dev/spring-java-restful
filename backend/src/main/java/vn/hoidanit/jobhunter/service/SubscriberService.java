package vn.hoidanit.jobhunter.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.repository.SubscriberRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@Service
@RequiredArgsConstructor
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;

    public Subscriber handleCreate(Subscriber subscriber) throws IdInvalidException {
        var sub = subscriberRepository.findByEmail(subscriber.getEmail());
        if (sub != null)
            throw new IdInvalidException("subscriber existed!");
        // check skill
        List<Long> skill_id = subscriber.getSkills().stream().map(item -> item.getId()).toList();
        subscriber.setSkills(skillRepository.findAllById(skill_id));
        return subscriberRepository.save(subscriber);
    }

    public Subscriber handleUpdate(Subscriber subscriber) throws IdInvalidException {
        var sub = subscriberRepository.findById(subscriber.getId())
                .orElseThrow(() -> new IdInvalidException("subscriber not exist!"));
        List<Long> skill_id = subscriber.getSkills().stream().map(item -> item.getId()).toList();
        sub.setSkills(skillRepository.findAllById(skill_id));

        return subscriberRepository.save(sub);
    }
}

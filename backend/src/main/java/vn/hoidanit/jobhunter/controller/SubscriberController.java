package vn.hoidanit.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.service.SubscriberService;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequiredArgsConstructor
public class SubscriberController extends BaseController {
    private final SubscriberService subscriberService;

    @PostMapping("/subscribers")
    public ResponseEntity<Subscriber> create(@RequestBody Subscriber body) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriberService.handleCreate(body));
    }

    @PutMapping("/subscribers")
    public ResponseEntity<Subscriber> update(@RequestBody Subscriber body) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriberService.handleUpdate(body));
    }
}

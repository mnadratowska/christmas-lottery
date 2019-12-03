package com.javagda24.christmaslottery.service;

import com.javagda24.christmaslottery.model.Gift;
import com.javagda24.christmaslottery.repository.GiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GiftService {

    @Autowired
    private GiftRepository giftRepository;


    public Optional<Gift> findById(Long giftId) {
        return giftRepository.findById(giftId);
    }

    public Optional<Gift> findByIdAndUsername(Long giftId, String name) {
        return giftRepository.findByIdAndAccount_Username(giftId, name);
    }

    public void save(Gift gift, String name) {
        if (gift.getAccount().getUsername().equals(name)) {
            giftRepository.save(gift);
        }
    }

    public List<Gift> giftsByUsername(String name) {
        return giftRepository.findAllByAccount_UsernameOrderByAdditionDateDesc(name);
    }

    public void remove(Long giftId, String name) {
        if (giftRepository.existsByIdAndAccount_Username(giftId, name)) {
            giftRepository.deleteById(giftId);
        }
    }
}

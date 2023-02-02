package com.samsamoo.zzalu.TitleHakwon.service;

import com.samsamoo.zzalu.TitleHakwon.entity.TitleHakwon;
import com.samsamoo.zzalu.TitleHakwon.repository.TitleHackwonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@RequiredArgsConstructor  //얘가 자동으로 생성자 주입해줌
@Repository
public class TitleHakwonService {
    private final TitleHackwonRepository titleHackwonRepository;


    public TitleHakwon getTitleHakwonInfo(String openDate){
        if(titleHackwonRepository.findTitleHakwonByOpenDate(openDate)==null){

        }
       return titleHackwonRepository.findTitleHakwonByOpenDate(openDate);

    }

}

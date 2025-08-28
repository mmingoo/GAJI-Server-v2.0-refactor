package gaji.service.domain.common.service;

import gaji.service.domain.common.converter.HashtagConverter;
import gaji.service.domain.common.entity.Hashtag;
import gaji.service.domain.common.entity.SelectHashtag;
import gaji.service.domain.common.repository.HashtagRepository;
import gaji.service.domain.common.repository.SelectHashtagRepository;
import gaji.service.domain.enums.PostTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;
    private final SelectHashtagRepository selectHashtagRepository;

    @Override
    public boolean ExistHashtagByName(String name) {
        return hashtagRepository.existsByName(name);
    }

    // 이미 존재하는 해시태그는 조회, 존재하지 않는 해시태그는 생성해서 List로 반환하는 메서드
    @Override
    @Transactional
    public List<Hashtag> createHashtagEntityList(List<String> hashtagStringList) {
        return hashtagStringList.stream()
                .map(hashtag -> {
                    if (hashtagRepository.existsByName(hashtag)) {
                        return hashtagRepository.findByName(hashtag);
                    } else {
                        return hashtagRepository.save(HashtagConverter.toHashtag(hashtag));
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveAllSelectHashtag(List<SelectHashtag> selectHashtagList) {
        selectHashtagRepository.saveAll(selectHashtagList);
    }

    @Override
    public List<SelectHashtag> findAllFetchJoinWithHashtagByEntityIdAndPostType(Long entityId, PostTypeEnum postType) {
        return selectHashtagRepository.findAllFetchJoinWithHashtagByEntityIdAndPostType(entityId, postType);
    }

    @Override
    @Transactional
    public void deleteAllByEntityIdAndType(Long entityId, PostTypeEnum postType) {
        selectHashtagRepository.deleteAllByEntityIdAndType(entityId, postType);
    }

    // TODO: postId에 해당하는 hashtagList 반환해주는 메서드 만들고 HashtagConverter에 DTO로 변환하는 메서드 만들기
}

package gaji.service.domain.common.service;


import gaji.service.domain.common.entity.Hashtag;
import gaji.service.domain.common.entity.SelectHashtag;
import gaji.service.domain.enums.PostTypeEnum;

import java.util.List;

public interface HashtagService {

    boolean ExistHashtagByName(String name);
    List<SelectHashtag> findAllFetchJoinWithHashtagByEntityIdAndPostType(Long entityId, PostTypeEnum postType);
    List<Hashtag> createHashtagEntityList(List<String> hashtagStringList);
    void saveAllSelectHashtag(List<SelectHashtag> selectHashtagList);
    void deleteAllByEntityIdAndType(Long entityId, PostTypeEnum postType);
}

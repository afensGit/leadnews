package com.bin.model.mappers.wemedia;

import com.bin.model.media.dtos.WmMaterialListDto;
import com.bin.model.media.pojos.WmMaterial;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * @author huangbin
 * @version 1.0
 * @date 2021/2/26 10:46
 */
public interface WmMaterialMapper {

    int insertPicture(WmMaterial material);

    WmMaterial selectByPrimaryKey(Integer id);

    int deleteByPrimaryKey(Integer id);

    List<WmMaterial> findListByUidAndStatus(@Param("dto")WmMaterialListDto dto, @Param("userId")Long userId);

    int countListByUidAndStatus(@Param("dto")WmMaterialListDto dto, @Param("userId")Long userId);

    int updateStatusByUidAndId(@Param("id")Integer id, @Param("userId")Long userId, @Param("type")Short type);

    List<WmMaterial> findMaterialByUserIdAndImageUrl(@Param("userId") Long userId, @Param("imageUrls") Collection<Object> imageUrls);

}

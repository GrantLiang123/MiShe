package com.grant.MiShe.web.admin.mapper;

import com.grant.MiShe.model.entity.GraphInfo;
import com.grant.MiShe.model.enums.ItemType;
import com.grant.MiShe.web.admin.vo.graph.GraphVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author liubo
* @description 针对表【graph_info(图片信息表)】的数据库操作Mapper
* @createDate 2023-07-24 15:48:00
* @Entity com.grant.MiShe.model.GraphInfo
*/
public interface GraphInfoMapper extends BaseMapper<GraphInfo> {

    List<GraphVo> selectListByIdAndItem(ItemType itemType, Long id);

    List<GraphVo> selectListByItemTypeAndId(ItemType itemType, Long id);
}





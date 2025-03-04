package com.grant.MiShe.web.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.grant.MiShe.common.exception.LeaseException;
import com.grant.MiShe.common.result.ResultCodeEnum;
import com.grant.MiShe.model.entity.*;
import com.grant.MiShe.model.enums.ItemType;
import com.grant.MiShe.web.admin.mapper.*;
import com.grant.MiShe.web.admin.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.grant.MiShe.web.admin.vo.apartment.ApartmentDetailVo;
import com.grant.MiShe.web.admin.vo.apartment.ApartmentItemVo;
import com.grant.MiShe.web.admin.vo.apartment.ApartmentQueryVo;
import com.grant.MiShe.web.admin.vo.apartment.ApartmentSubmitVo;
import com.grant.MiShe.web.admin.vo.fee.FeeValueVo;
import com.grant.MiShe.web.admin.vo.graph.GraphVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class ApartmentInfoServiceImpl extends ServiceImpl<ApartmentInfoMapper, ApartmentInfo>
        implements ApartmentInfoService {

    @Autowired
    private GraphInfoService graphInfoService;

    @Autowired
    private ApartmentFacilityService apartmentFacilityService;

    @Autowired
    private ApartmentLabelService apartmentLabelService;

    @Autowired
    private ApartmentFeeValueService apartmentFeeValueService;

    @Autowired
    private ApartmentInfoMapper apartmentInfoMapper;

    @Autowired
    private GraphInfoMapper graphInfoMapper;

    @Autowired
    private LabelInfoMapper labelInfoMapper;

    @Autowired
    private FacilityInfoMapper facilityInfoMapper;

    @Autowired
    private FeeValueMapper feeValueMapper;

    @Autowired
    private RoomInfoMapper roomInfoMapper;

    @Override
    public void saveOrUpdateApartment(ApartmentSubmitVo apartmentSubmitVo) {
        boolean isUpdate=apartmentSubmitVo.getId()!=null;
        super.saveOrUpdate(apartmentSubmitVo);

        if(isUpdate){
            LambdaQueryWrapper<GraphInfo> graphInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            graphInfoLambdaQueryWrapper.eq(GraphInfo::getItemType, ItemType.APARTMENT);
            graphInfoLambdaQueryWrapper.eq(GraphInfo::getId,apartmentSubmitVo.getId());
            graphInfoService.remove(graphInfoLambdaQueryWrapper);


            LambdaQueryWrapper<ApartmentFacility> apartmentFacilityLambdaQueryWrapper = new LambdaQueryWrapper<>();
            apartmentFacilityLambdaQueryWrapper.eq(ApartmentFacility::getId,apartmentSubmitVo.getId());
            apartmentFacilityService.remove(apartmentFacilityLambdaQueryWrapper);


            LambdaQueryWrapper<ApartmentLabel> apartmentLabelLambdaQueryWrapper = new LambdaQueryWrapper<>();
            apartmentLabelLambdaQueryWrapper.eq(ApartmentLabel::getId,apartmentSubmitVo.getId());
            apartmentLabelService.remove(apartmentLabelLambdaQueryWrapper);

            LambdaQueryWrapper<ApartmentFeeValue> apartmentFeeValueLambdaQueryWrapper = new LambdaQueryWrapper<>();
            apartmentFeeValueLambdaQueryWrapper.eq(ApartmentFeeValue::getId,apartmentSubmitVo.getId());
            apartmentFeeValueService.remove(apartmentFeeValueLambdaQueryWrapper);

        }

        List<GraphVo> graphVoList = apartmentSubmitVo.getGraphVoList();
        List<GraphInfo> graphInfoList=new ArrayList<>();
        if(!CollectionUtils.isEmpty(graphVoList)){
            for (GraphVo graphVo : graphVoList) {
                GraphInfo graphInfo = new GraphInfo();
                graphInfo.setItemId(apartmentSubmitVo.getId());
                graphInfo.setItemType(ItemType.APARTMENT);
                graphInfo.setName(graphVo.getName());
                graphInfo.setUrl(graphVo.getUrl());

                graphInfoList.add(graphInfo);
            }
        }
        graphInfoService.saveBatch(graphInfoList);

        List<Long> facilityInfoIds = apartmentSubmitVo.getFacilityInfoIds();
        if(!CollectionUtils.isEmpty(facilityInfoIds)){
            List<ApartmentFacility> apartmentFacilityList=new ArrayList<>();
            for (Long facilityInfoId : facilityInfoIds) {
                ApartmentFacility apartmentFacility = ApartmentFacility.builder().build();
                apartmentFacility.setApartmentId(apartmentSubmitVo.getId());
                apartmentFacility.setFacilityId(facilityInfoId);
                apartmentFacilityList.add(apartmentFacility);
            }
            apartmentFacilityService.saveBatch(apartmentFacilityList);
        }

        List<Long> labelIds = apartmentSubmitVo.getLabelIds();
        if (!CollectionUtils.isEmpty(labelIds)) {
            List<ApartmentLabel> apartmentLabelList = new ArrayList<>();
            for (Long labelId : labelIds) {
                ApartmentLabel apartmentLabel = ApartmentLabel.builder().build();
                apartmentLabel.setApartmentId(apartmentSubmitVo.getId());
                apartmentLabel.setLabelId(labelId);
                apartmentLabelList.add(apartmentLabel);
            }
            apartmentLabelService.saveBatch(apartmentLabelList);
        }


        List<Long> feeValueIds = apartmentSubmitVo.getFeeValueIds();
        if (!CollectionUtils.isEmpty(feeValueIds)) {
            ArrayList<ApartmentFeeValue> apartmentFeeValueList = new ArrayList<>();
            for (Long feeValueId : feeValueIds) {
                ApartmentFeeValue apartmentFeeValue = ApartmentFeeValue.builder().build();
                apartmentFeeValue.setApartmentId(apartmentSubmitVo.getId());
                apartmentFeeValue.setFeeValueId(feeValueId);
                apartmentFeeValueList.add(apartmentFeeValue);
            }
            apartmentFeeValueService.saveBatch(apartmentFeeValueList);
        }




    }

    @Override
    public IPage<ApartmentItemVo> pageItem(Page<ApartmentItemVo> page, ApartmentQueryVo queryVo) {

        return apartmentInfoMapper.pageItem(page,queryVo);
    }

    @Override
    public ApartmentDetailVo getDetailById(Long id) {
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(id);

        List<GraphVo> graphVoList=graphInfoMapper.selectListByIdAndItem(ItemType.APARTMENT,id);

        List<LabelInfo> labelInfoList=labelInfoMapper.selectListByApartmentId(id);


        List<FacilityInfo> facilityInfoList=facilityInfoMapper.selectListByApartmentId(id);

        List<FeeValueVo> feeValueVoList=feeValueMapper.selectListByApartmentId(id);

        ApartmentDetailVo apartmentDetailVo = new ApartmentDetailVo();
        BeanUtils.copyProperties(apartmentInfo,apartmentDetailVo);
        apartmentDetailVo.setGraphVoList(graphVoList);
        apartmentDetailVo.setLabelInfoList(labelInfoList);
        apartmentDetailVo.setFacilityInfoList(facilityInfoList);
        apartmentDetailVo.setFeeValueVoList(feeValueVoList);
        return apartmentDetailVo;
    }

    @Override
    public void removeApartmentById(Long id) {
        LambdaQueryWrapper<RoomInfo> roomInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roomInfoLambdaQueryWrapper.eq(RoomInfo::getApartmentId,id);
        Long l = roomInfoMapper.selectCount(roomInfoLambdaQueryWrapper);
        if(l>0){
            throw new LeaseException(ResultCodeEnum.ADMIN_APARTMENT_DELETE_ERROR);
        }


        super.removeById(id);

        LambdaQueryWrapper<GraphInfo> graphInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        graphInfoLambdaQueryWrapper.eq(GraphInfo::getItemType, ItemType.APARTMENT);
        graphInfoLambdaQueryWrapper.eq(GraphInfo::getId,id);
        graphInfoService.remove(graphInfoLambdaQueryWrapper);


        LambdaQueryWrapper<ApartmentFacility> apartmentFacilityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        apartmentFacilityLambdaQueryWrapper.eq(ApartmentFacility::getId,id);
        apartmentFacilityService.remove(apartmentFacilityLambdaQueryWrapper);


        LambdaQueryWrapper<ApartmentLabel> apartmentLabelLambdaQueryWrapper = new LambdaQueryWrapper<>();
        apartmentLabelLambdaQueryWrapper.eq(ApartmentLabel::getId,id);
        apartmentLabelService.remove(apartmentLabelLambdaQueryWrapper);

        LambdaQueryWrapper<ApartmentFeeValue> apartmentFeeValueLambdaQueryWrapper = new LambdaQueryWrapper<>();
        apartmentFeeValueLambdaQueryWrapper.eq(ApartmentFeeValue::getId,id);
        apartmentFeeValueService.remove(apartmentFeeValueLambdaQueryWrapper);


    }
}





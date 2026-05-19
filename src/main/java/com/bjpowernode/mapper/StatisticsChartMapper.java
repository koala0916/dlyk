package com.bjpowernode.mapper;

import com.bjpowernode.entity.TClue;
import com.bjpowernode.entity.TCustomer;
import com.bjpowernode.vo.NameValueData;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface StatisticsChartMapper {

  List<NameValueData> selectMonthlyTranByUser(@Param("startTime") Date startTime);

  List<NameValueData> selectMonthlyTranTotal(@Param("startTime") Date startTime);

  List<NameValueData> selectPerformancePieByMonth(@Param("year") int year, @Param("month") Integer month);

  List<NameValueData> selectPerformancePieByYear(@Param("year") int year);

  Integer countClues(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
      @Param("userId") Integer userId);

  Integer countCluesContactedOrConverted(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
      @Param("userId") Integer userId);

  Integer countCluesConverted(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
      @Param("userId") Integer userId);

  Integer countRenewalTrans(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
      @Param("userId") Integer userId);

  List<TCustomer> selectCustomersForAnalysis(@Param("sources") List<String> sources,
      @Param("courseTypes") List<String> courseTypes,
      @Param("studyingList") List<Integer> studyingList,
      @Param("createByList") List<Integer> createByList,
      @Param("ageRanges") List<String> ageRanges);

  List<TClue> selectCluesForAnalysis(@Param("sources") List<String> sources,
      @Param("courses") List<String> courses,
      @Param("statusList") List<String> statusList,
      @Param("strengthList") List<Integer> strengthList,
      @Param("createByList") List<Integer> createByList,
      @Param("ageRanges") List<String> ageRanges);

  List<String> selectDistinctCustomerSource();

  List<String> selectDistinctCustomerCourseType();

  List<String> selectDistinctClueSource();

  List<String> selectDistinctClueCourse();
}

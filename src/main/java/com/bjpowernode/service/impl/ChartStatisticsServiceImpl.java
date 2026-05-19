package com.bjpowernode.service.impl;

import com.bjpowernode.entity.TClue;
import com.bjpowernode.entity.TCustomer;
import com.bjpowernode.entity.TUser;
import com.bjpowernode.mapper.StatisticsChartMapper;
import com.bjpowernode.mapper.TUserMapper;
import com.bjpowernode.query.AnalysisPieQuery;
import com.bjpowernode.query.ChartFunnelQuery;
import com.bjpowernode.service.ChartStatisticsService;
import com.bjpowernode.service.UserService;
import com.bjpowernode.vo.AnalysisFilterOptionsVO;
import com.bjpowernode.vo.LineChartVO;
import com.bjpowernode.vo.LineSeriesVO;
import com.bjpowernode.vo.NameValueData;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ChartStatisticsServiceImpl implements ChartStatisticsService {

  private static final DateTimeFormatter MONTH_FMT = DateTimeFormatter.ofPattern("yyyy-MM");

  @Resource
  private StatisticsChartMapper statisticsChartMapper;

  @Resource
  private UserService userService;

  @Resource
  private TUserMapper tUserMapper;

  @Override
  public LineChartVO getPerformanceLineChart() {
    LocalDate start = LocalDate.now().minusMonths(11).withDayOfMonth(1);
    Date startTime = Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant());

    List<String> months = new ArrayList<>();
    for (int i = 0; i < 12; i++) {
      months.add(start.plusMonths(i).format(MONTH_FMT));
    }

    Map<String, Map<String, BigDecimal>> userMonthMap = new LinkedHashMap<>();
    for (NameValueData row : statisticsChartMapper.selectMonthlyTranByUser(startTime)) {
      String key = String.valueOf(row.getName());
      int idx = key.indexOf('|');
      if (idx <= 0) {
        continue;
      }
      String month = key.substring(0, idx);
      String userName = key.substring(idx + 1);
      userMonthMap.computeIfAbsent(userName, k -> new HashMap<>())
          .put(month, toBigDecimal(row.getValue()));
    }

    Map<String, BigDecimal> totalMap = new HashMap<>();
    for (NameValueData row : statisticsChartMapper.selectMonthlyTranTotal(startTime)) {
      totalMap.put(String.valueOf(row.getName()), toBigDecimal(row.getValue()));
    }

    List<LineSeriesVO> series = new ArrayList<>();
    for (Map.Entry<String, Map<String, BigDecimal>> e : userMonthMap.entrySet()) {
      LineSeriesVO s = new LineSeriesVO();
      s.setName(e.getKey());
      List<Double> data = new ArrayList<>();
      for (String m : months) {
        data.add(toBigDecimal(e.getValue().getOrDefault(m, BigDecimal.ZERO)).doubleValue());
      }
      s.setData(data);
      series.add(s);
    }

    LineSeriesVO total = new LineSeriesVO();
    total.setName("金额总量");
    List<Double> totalData = new ArrayList<>();
    for (String m : months) {
      totalData.add(toBigDecimal(totalMap.getOrDefault(m, BigDecimal.ZERO)).doubleValue());
    }
    total.setData(totalData);
    series.add(0, total);

    LineChartVO vo = new LineChartVO();
    vo.setMonths(months);
    vo.setSeries(series);
    return vo;
  }

  @Override
  public List<NameValueData> getPerformancePie(String periodType, Integer year, Integer month) {
    if (year == null) {
      year = LocalDate.now().getYear();
    }
    if ("year".equalsIgnoreCase(periodType)) {
      return statisticsChartMapper.selectPerformancePieByYear(year);
    }
    int m = month != null ? month : LocalDate.now().getMonthValue();
    return statisticsChartMapper.selectPerformancePieByMonth(year, m);
  }

  @Override
  public List<NameValueData> getPerformanceFunnel(ChartFunnelQuery query) {
    Date start = query.getStartTime();
    Date end = query.getEndTime();
    Integer userId = query.getUserId();
    if (userId != null && userId <= 0) {
      userId = null;
    }

    List<NameValueData> list = new ArrayList<>();
    list.add(NameValueData.builder().name("所有线索数")
        .value(statisticsChartMapper.countClues(start, end, userId)).build());
    list.add(NameValueData.builder().name("已联系及已转线索数")
        .value(statisticsChartMapper.countCluesContactedOrConverted(start, end, userId)).build());
    list.add(NameValueData.builder().name("已转客户线索数")
        .value(statisticsChartMapper.countCluesConverted(start, end, userId)).build());
    list.add(NameValueData.builder().name("客户续费交易数")
        .value(statisticsChartMapper.countRenewalTrans(start, end, userId)).build());
    return list;
  }

  @Override
  public AnalysisFilterOptionsVO getCustomerFilterOptions() {
    AnalysisFilterOptionsVO vo = new AnalysisFilterOptionsVO();
    vo.setDimensions(Arrays.asList("createBy", "source", "age", "courseType", "studying"));
    Map<String, List<NameValueData>> options = new LinkedHashMap<>();
    options.put("createBy", toUserOptions());
    options.put("source", toTextOptions(statisticsChartMapper.selectDistinctCustomerSource()));
    options.put("courseType", toTextOptions(statisticsChartMapper.selectDistinctCustomerCourseType()));
    options.put("studying", Arrays.asList(
        NameValueData.builder().name("是").value("1").build(),
        NameValueData.builder().name("否").value("0").build()));
    options.put("age", buildAgeRangeOptions());
    vo.setOptions(options);
    return vo;
  }

  @Override
  public AnalysisFilterOptionsVO getClueFilterOptions() {
    AnalysisFilterOptionsVO vo = new AnalysisFilterOptionsVO();
    vo.setDimensions(Arrays.asList("age", "intentionCourse", "intentionStrength", "source", "clueStatus", "createBy"));
    Map<String, List<NameValueData>> options = new LinkedHashMap<>();
    options.put("createBy", toUserOptions());
    options.put("source", toTextOptions(statisticsChartMapper.selectDistinctClueSource()));
    options.put("intentionCourse", toTextOptions(statisticsChartMapper.selectDistinctClueCourse()));
    options.put("clueStatus", Arrays.asList(
        NameValueData.builder().name("未联系").value("未联系").build(),
        NameValueData.builder().name("已联系").value("已联系").build(),
        NameValueData.builder().name("已转客户").value("已转客户").build()));
    options.put("intentionStrength", IntStream.rangeClosed(1, 10)
        .mapToObj(i -> NameValueData.builder().name(String.valueOf(i)).value(String.valueOf(i)).build())
        .collect(Collectors.toList()));
    options.put("age", buildAgeRangeOptions());
    vo.setOptions(options);
    return vo;
  }

  @Override
  public List<NameValueData> getAnalysisPie(AnalysisPieQuery query) {
    if (query == null || !StringUtils.hasText(query.getDimension())) {
      return Collections.emptyList();
    }
    Map<String, List<String>> scopes = query.getScopes() != null ? query.getScopes() : Collections.emptyMap();
    String dimension = query.getDimension();

    if ("clue".equalsIgnoreCase(query.getEntityType())) {
      List<TClue> clues = statisticsChartMapper.selectCluesForAnalysis(
          scopes.get("source"), scopes.get("intentionCourse"), scopes.get("clueStatus"),
          parseIntList(scopes.get("intentionStrength")), parseIntList(scopes.get("createBy")),
          scopes.get("age"));
      return groupClues(clues, dimension);
    }

    List<TCustomer> customers = statisticsChartMapper.selectCustomersForAnalysis(
        scopes.get("source"), scopes.get("courseType"), parseIntList(scopes.get("studying")),
        parseIntList(scopes.get("createBy")), scopes.get("age"));
    return groupCustomers(customers, dimension);
  }

  private List<NameValueData> groupCustomers(List<TCustomer> list, String dimension) {
    Map<String, Long> map = new LinkedHashMap<>();
    for (TCustomer c : list) {
      String key = customerDimensionKey(c, dimension);
      map.merge(key, 1L, Long::sum);
    }
    return toPieList(map);
  }

  private List<NameValueData> groupClues(List<TClue> list, String dimension) {
    Map<String, Long> map = new LinkedHashMap<>();
    for (TClue c : list) {
      String key = clueDimensionKey(c, dimension);
      map.merge(key, 1L, Long::sum);
    }
    return toPieList(map);
  }

  private String customerDimensionKey(TCustomer c, String dimension) {
    return switch (dimension) {
      case "createBy" -> userName(c.getCreateBy());
      case "source" -> nullToLabel(c.getSource());
      case "courseType" -> nullToLabel(c.getCourseType());
      case "studying" -> c.getStudying() != null && c.getStudying() == 1 ? "正在学习" : "未学习";
      case "age" -> ageBucket(c.getAge());
      default -> "其他";
    };
  }

  private String clueDimensionKey(TClue c, String dimension) {
    return switch (dimension) {
      case "createBy" -> userName(c.getCreateBy());
      case "source" -> nullToLabel(c.getSource());
      case "intentionCourse" -> nullToLabel(c.getIntentionCourse());
      case "clueStatus" -> nullToLabel(c.getClueStatus());
      case "intentionStrength" -> c.getIntentionStrength() != null ? c.getIntentionStrength() + " 分" : "未知";
      case "age" -> ageBucket(c.getAge());
      default -> "其他";
    };
  }

  private String ageBucket(Integer age) {
    if (age == null) {
      return "年龄未知";
    }
    int start = (age / 5) * 5;
    int end = start + 5;
    return start + "-" + end + "岁";
  }

  private String userName(Integer userId) {
    if (userId == null) {
      return "未知";
    }
    TUser u = tUserMapper.selectByPrimaryKey(userId);
    return u != null && StringUtils.hasText(u.getName()) ? u.getName() : ("用户" + userId);
  }

  private List<NameValueData> toPieList(Map<String, Long> map) {
    long total = map.values().stream().mapToLong(Long::longValue).sum();
    if (total == 0) {
      return Collections.singletonList(NameValueData.builder().name("暂无数据").value(0).build());
    }
    return map.entrySet().stream()
        .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
        .map(e -> NameValueData.builder().name(e.getKey()).value(e.getValue()).build())
        .collect(Collectors.toList());
  }

  private List<NameValueData> toUserOptions() {
    List<TUser> users = userService.getOwners();
    if (users == null) {
      return Collections.emptyList();
    }
    return users.stream()
        .map(u -> NameValueData.builder().name(u.getName()).value(String.valueOf(u.getId())).build())
        .collect(Collectors.toList());
  }

  private List<NameValueData> toTextOptions(List<String> texts) {
    if (texts == null) {
      return Collections.emptyList();
    }
    return texts.stream()
        .map(t -> NameValueData.builder().name(t).value(t).build())
        .collect(Collectors.toList());
  }

  private List<NameValueData> buildAgeRangeOptions() {
    List<NameValueData> list = new ArrayList<>();
    for (int start = 5; start <= 60; start += 5) {
      String label = start + "-" + (start + 5) + "岁";
      list.add(NameValueData.builder().name(label).value(start + "-" + (start + 5)).build());
    }
    return list;
  }

  private List<Integer> parseIntList(List<String> raw) {
    if (CollectionUtils.isEmpty(raw)) {
      return null;
    }
    List<Integer> list = new ArrayList<>();
    for (String s : raw) {
      try {
        list.add(Integer.parseInt(s));
      } catch (NumberFormatException ignored) {
      }
    }
    return list.isEmpty() ? null : list;
  }

  private BigDecimal toBigDecimal(Object v) {
    if (v == null) {
      return BigDecimal.ZERO;
    }
    if (v instanceof BigDecimal bd) {
      return bd;
    }
    return new BigDecimal(String.valueOf(v)).setScale(2, RoundingMode.HALF_UP);
  }

  private String nullToLabel(String s) {
    return StringUtils.hasText(s) ? s : "未填写";
  }
}

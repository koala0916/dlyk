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

  /**
   * 业绩分析页 - 折线图：近 12 个月每月交易金额趋势。
   * <p>
   * 返回给前端 {@link LineChartVO}，结构示意：
   * <pre>
   * months: ["2025-06","2025-07", ... , "2026-05"]   // X 轴 12 个月份
   * series: [
   *   { name:"金额总量", data:[12000, 15000, ...] },  // 全公司每月汇总（插在第一条）
   *   { name:"Monica",  data:[3000, 4000, ...] },     // 各员工作为 create_by 的成交汇总
   *   { name:"Chandler", data:[...] },
   *   ...
   * ]
   * </pre>
   * 前端 PerformanceAnalysisView.vue 用 buildLineOption(months, series) 画 ECharts 折线。
   * 「按员工 / 全公司」切换在前端过滤 series，本方法始终返回全部数据。
   * </p>
   */
  @Override
  public LineChartVO getPerformanceLineChart() {
    // ---------- 1. 计算统计起点：从今天往前推 11 个月，再取该月 1 号 0 点 ----------
    // 例：今天是 2026-05-19 → start = 2025-06-01，加上后面循环 12 个月，正好覆盖近 12 个自然月
    LocalDate start = LocalDate.now().minusMonths(11).withDayOfMonth(1);
    // LocalDate 转 java.util.Date，供 MyBatis 传给 SQL 的 #{startTime}
    Date startTime = Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant());

    // ---------- 2. 生成 X 轴月份列表（固定 12 个，格式 yyyy-MM） ----------
    List<String> months = new ArrayList<>();
    for (int i = 0; i < 12; i++) {
      months.add(start.plusMonths(i).format(MONTH_FMT));
    }

    // ---------- 3. 查「每个员工 × 每个月」的成交金额，整理成二维 Map ----------
    // SQL（selectMonthlyTranByUser）按 月份 + create_by 分组 sum(money)，
    // 并把 name 拼成 "2025-06|Monica" 这种字符串（月份|员工名），value 为金额
    // 外层 Map：key=员工名，内层 Map：key=月份，value=该月该员工成交总额
    Map<String, Map<String, BigDecimal>> userMonthMap = new LinkedHashMap<>();
    for (NameValueData row : statisticsChartMapper.selectMonthlyTranByUser(startTime)) {
      String key = String.valueOf(row.getName()); // 例如 "2025-08|Chandler"
      int idx = key.indexOf('|');
      if (idx <= 0) {
        // 格式不对则跳过，防止 substring 越界或脏数据
        continue;
      }
      String month = key.substring(0, idx);       // "2025-08"
      String userName = key.substring(idx + 1); // "Chandler"
      // computeIfAbsent：该员工第一次出现时建内层 HashMap，再把该月金额 put 进去
      userMonthMap.computeIfAbsent(userName, k -> new HashMap<>())
          .put(month, toBigDecimal(row.getValue()));
    }

    // ---------- 4. 查「全公司每月」成交总额（不区分员工） ----------
    // SQL（selectMonthlyTranTotal）按月份分组；name=月份，value=当月所有交易金额之和
    Map<String, BigDecimal> totalMap = new HashMap<>();
    for (NameValueData row : statisticsChartMapper.selectMonthlyTranTotal(startTime)) {
      totalMap.put(String.valueOf(row.getName()), toBigDecimal(row.getValue()));
    }

    // ---------- 5. 为每个员工组装一条折线：data 长度必须等于 months，缺月补 0 ----------
    List<LineSeriesVO> series = new ArrayList<>();
    for (Map.Entry<String, Map<String, BigDecimal>> e : userMonthMap.entrySet()) {
      LineSeriesVO s = new LineSeriesVO();
      s.setName(e.getKey()); // 图例名 = 员工姓名
      List<Double> data = new ArrayList<>();
      for (String m : months) {
        // 该员工在月份 m 无成交时 getOrDefault 为 0，保证折线不断点错位
        data.add(toBigDecimal(e.getValue().getOrDefault(m, BigDecimal.ZERO)).doubleValue());
      }
      s.setData(data);
      series.add(s);
    }

    // ---------- 6. 组装「金额总量」折线，并插到 series 第一条（前端「全公司」模式用这条） ----------
    LineSeriesVO total = new LineSeriesVO();
    total.setName("金额总量");
    List<Double> totalData = new ArrayList<>();
    for (String m : months) {
      totalData.add(toBigDecimal(totalMap.getOrDefault(m, BigDecimal.ZERO)).doubleValue());
    }
    total.setData(totalData);
    series.add(0, total); // 插入索引 0，作为第一条 series

    // ---------- 7. 封装 VO 返回 ----------
    LineChartVO vo = new LineChartVO();
    vo.setMonths(months);   // X 轴
    vo.setSeries(series);   // 多条 Y 轴折线
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

  /**
   * 对客户列表按指定维度分组计数，供数据分析页「客户饼图」使用。
   * <p>
   * 调用链：getAnalysisPie → selectCustomersForAnalysis（SQL 已按 scopes 筛好样本）
   * → 本方法在内存里 groupBy → toPieList 转成 [{name, value}, ...]。
   * </p>
   *
   * @param list      筛选后的客户集合，每条对应 t_customer 一行
   * @param dimension 分组字段名，与前端 dimension 一致，如 source、age、createBy
   * @return 饼图数据：name=分组标签（如「抖音团购」），value=该组客户条数
   */
  private List<NameValueData> groupCustomers(List<TCustomer> list, String dimension) {
    // LinkedHashMap：保持分组键的插入顺序；key=分组名，value=该组累计条数
    Map<String, Long> map = new LinkedHashMap<>();
    for (TCustomer c : list) {
      // 根据 dimension 从客户实体取展示用标签，见 customerDimensionKey
      String key = customerDimensionKey(c, dimension);
      // merge：同一 key 第一次 put 1，之后每次 +1（等价于分组计数）
      map.merge(key, 1L, Long::sum);
    }
    // 转成前端 ECharts 需要的列表，并按数量降序；无数据时返回「暂无数据」
    return toPieList(map);
  }

  /**
   * 对线索列表按指定维度分组计数，供数据分析页「线索饼图」使用。
   * <p>
   * 逻辑与 {@link #groupCustomers} 相同，只是实体换成 {@link TClue}，
   * 分组标签由 {@link #clueDimensionKey} 生成（如线索状态、意向强度等）。
   * </p>
   *
   * @param list      筛选后的线索集合
   * @param dimension 分组字段名，如 source、clueStatus、intentionStrength
   * @return 饼图数据列表
   */
  private List<NameValueData> groupClues(List<TClue> list, String dimension) {
    Map<String, Long> map = new LinkedHashMap<>();
    for (TClue c : list) {
      String key = clueDimensionKey(c, dimension);
      map.merge(key, 1L, Long::sum);
    }
    return toPieList(map);
  }

  /**
   * 从一条客户记录中，取出「用于饼图分组」的中文/展示标签。
   * <p>
   * dimension 与前端 DataAnalysisView 里单选的字段名一致（如 source、age）。
   * groupCustomers 会对返回的字符串做 merge 计数，相同标签累加。
   * </p>
   */
  private String customerDimensionKey(TCustomer c, String dimension) {
    return switch (dimension) {
      case "createBy" -> resolveCustomerCreatorName(c);           // 创建人姓名
      case "source" -> nullToLabel(c.getSource());                // 来源，空则「未填写」
      case "courseType" -> nullToLabel(c.getCourseType());        // 课程类型
      case "studying" -> c.getStudying() != null && c.getStudying() == 1 ? "正在学习" : "未学习";
      case "age" -> ageBucket(c.getAge());                        // 年龄归入 5 岁一档
      default -> "其他";                                          // 未知 dimension 兜底
    };
  }

  /**
   * 从一条线索记录中取出饼图分组标签，规则与客户侧类似，字段为线索特有项。
   */
  private String clueDimensionKey(TClue c, String dimension) {
    return switch (dimension) {
      case "createBy" -> resolveClueCreatorName(c);
      case "source" -> nullToLabel(c.getSource());
      case "intentionCourse" -> nullToLabel(c.getIntentionCourse());   // 意向课程
      case "clueStatus" -> nullToLabel(c.getClueStatus());             // 未联系/已联系/已转客户
      case "intentionStrength" -> c.getIntentionStrength() != null
          ? c.getIntentionStrength() + " 分" : "未知";                  // 1~10 分
      case "age" -> ageBucket(c.getAge());
      default -> "其他";
    };
  }

  /**
   * 把具体年龄归入年龄段，与前端筛选下拉、SQL 年龄条件使用的区间一致。
   * <p>
   * 算法：每 5 岁一档，下限为 (age/5)*5。例如 age=12 → 10-15岁，age=7 → 5-10岁。
   * </p>
   */
  private String ageBucket(Integer age) {
    if (age == null) {
      return "年龄未知";
    }
    int start = (age / 5) * 5;  // 整除再乘 5，得到区间起点
    int end = start + 5;
    return start + "-" + end + "岁";
  }

  /**
   * 客户创建人展示名：分析查询已 left join t_user，优先用 createByDO.name。
   */
  private String resolveCustomerCreatorName(TCustomer c) {
    if (c.getCreateByDO() != null && StringUtils.hasText(c.getCreateByDO().getName())) {
      return c.getCreateByDO().getName();
    }
    // 联表未带出姓名时，用 create_by 主键再查用户表
    return userName(c.getCreateBy());
  }

  /** 线索创建人展示名，逻辑同 {@link #resolveCustomerCreatorName} */
  private String resolveClueCreatorName(TClue c) {
    if (c.getCreateByDO() != null && StringUtils.hasText(c.getCreateByDO().getName())) {
      return c.getCreateByDO().getName();
    }
    return userName(c.getCreateBy());
  }

  /**
   * 根据用户 id 查姓名；用于 createBy 维度或联表失败时的回退。
   *
   * @param userId t_user.id，可为 null
   * @return 姓名；null 或查不到时返回「未知」或「用户{id}」
   */
  private String userName(Integer userId) {
    if (userId == null) {
      return "未知";
    }
    TUser u = tUserMapper.selectByPrimaryKey(userId);
    return u != null && StringUtils.hasText(u.getName()) ? u.getName() : ("用户" + userId);
  }

  /**
   * 将分组计数 Map 转为饼图接口列表，并按数量从大到小排序。
   *
   * @param map key=分组标签，value=该组条数（来自 groupCustomers / groupClues 的 merge）
   */
  private List<NameValueData> toPieList(Map<String, Long> map) {
    long total = map.values().stream().mapToLong(Long::longValue).sum();
    if (total == 0) {
      // 筛选后无样本时，前端仍收到一条占位，避免饼图空白无提示
      return Collections.singletonList(NameValueData.builder().name("暂无数据").value(0).build());
    }
    return map.entrySet().stream()
        .sorted((a, b) -> Long.compare(b.getValue(), a.getValue())) // 数量多的扇区排前面
        .map(e -> NameValueData.builder().name(e.getKey()).value(e.getValue()).build())
        .collect(Collectors.toList());
  }

  /**
   * 构建「创建人」筛选项：下拉显示姓名，提交值为用户 id 字符串。
   * 用于 getCustomerFilterOptions / getClueFilterOptions 的 createBy 维度。
   */
  private List<NameValueData> toUserOptions() {
    List<TUser> users = userService.getOwners();
    if (users == null) {
      return Collections.emptyList();
    }
    return users.stream()
        .map(u -> NameValueData.builder().name(u.getName()).value(String.valueOf(u.getId())).build())
        .collect(Collectors.toList());
  }

  /**
   * 把数据库 distinct 出来的字符串列表，转成前端下拉选项（name 与 value 相同）。
   * 用于来源、课程类型、意向课程等文本维度。
   */
  private List<NameValueData> toTextOptions(List<String> texts) {
    if (texts == null) {
      return Collections.emptyList();
    }
    return texts.stream()
        .map(t -> NameValueData.builder().name(t).value(t).build())
        .collect(Collectors.toList());
  }

  /**
   * 生成年龄区间筛选项：5-10、10-15 … 55-60（共 12 档）。
   * value 为 "5-10" 这种格式，Mapper XML 里用 substring_index 解析成 SQL 条件。
   */
  private List<NameValueData> buildAgeRangeOptions() {
    List<NameValueData> list = new ArrayList<>();
    for (int start = 5; start <= 60; start += 5) {
      String label = start + "-" + (start + 5) + "岁";
      list.add(NameValueData.builder().name(label).value(start + "-" + (start + 5)).build());
    }
    return list;
  }

  /**
   * 前端 scopes 里多为字符串数组，创建人/是否学习/意向强度等需转为 Integer 再传给 Mapper。
   * <p>
   * 返回 null 表示未选或全部解析失败，Mapper 的 &lt;if test="createByList != null"&gt; 不会加该条件。
   * </p>
   */
  private List<Integer> parseIntList(List<String> raw) {
    if (CollectionUtils.isEmpty(raw)) {
      return null;
    }
    List<Integer> list = new ArrayList<>();
    for (String s : raw) {
      try {
        list.add(Integer.parseInt(s));
      } catch (NumberFormatException ignored) {
        // 非法数字跳过，不中断整个筛选
      }
    }
    return list.isEmpty() ? null : list;
  }

  /**
   * 折线图等场景：把 SQL 汇总金额（可能是 BigDecimal、Long、String）统一为 BigDecimal。
   * 用于 getPerformanceLineChart 里组装每月金额。
   */
  private BigDecimal toBigDecimal(Object v) {
    if (v == null) {
      return BigDecimal.ZERO;
    }
    if (v instanceof BigDecimal bd) {
      return bd;
    }
    return new BigDecimal(String.valueOf(v)).setScale(2, RoundingMode.HALF_UP);
  }

  /**
   * 文本字段为空或纯空白时，饼图分组显示「未填写」，避免 null 导致统计键不一致。
   */
  private String nullToLabel(String s) {
    return StringUtils.hasText(s) ? s : "未填写";
  }
}

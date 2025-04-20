Easyquery
=========

简单快速构建 Java Web 服务查询。

<h4 align="right"><strong>简体中文</strong> | <a href="./README.md">English</a></h4>

<p align="center">
   <img src="https://img.shields.io/badge/license-MIT-blue.svg" alt="license">
   <img src="https://img.shields.io/badge/JDK-17+-green.svg" alt="jdk">
</p> 

# 简介

Easyquery 可以用来简单快速地构建查询请求，例如筛选、搜索和排序，可通过定义查询对象，自动构建查询 SQL，免去了手动拼接 SQL 或组装查询代码。

例如，假设我们使用 MybatisPlus，我们的查询请求接口查询了表 job，实体对象如下所示，现在我们要增加字段 name，type 的筛选，以及 created_time 的排序。一般我们可以这样写：

```java
// 实体对象 job.java
@Data
@TableName("job")
public class Job {

    @TableId(type = IdType.AUTO)
    private String id;
    private OffsetDateTime createdTime;
    private OffsetDateTime updatedTime;
    private String name;
    private String type;
    private String remark;
}

// 构建查询 JobServiceImpl.java，省略了 JobMapper 和 IJobService 的代码
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements IJobService {
    public List<Job> listJob(String name, String type, boolean createdTimeAsc) {
        QueryWrapper<Job> wrapper = new QueryWrapper<>();
        if (Objects.nonNull(name)) {
            wrapper.eq("name", name);
        }
        if (Objects.nonNull(type)) {
            wrapper.eq("type", type);
        }
        wrapper.orderBy(true, createdTimeAsc, "created_time");
        return list(wrapper);
    }
}
```

而 Easyquery 可以简化上述步骤，将一系列的查询构建语句简化为一个 DTO 对象配置，通过配置对象直接即可构建 QueryWrapper，service 直接调用即可。

```java
// DTO 配置对象
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobDTO {
    @QueryFilter
    private String name;
    @QueryFilter
    private String type;
    @QuerySorter
    private Boolean createdTime;
}
// 在 controller 类中调用（也可包装在 service 中）
@PostMapping("/listJob")
public ResponseEntity<?> listJob(@RequestBody JobDTO dto) {
    MyBatisPlusQueryWrapper<Job> wrapper = new MyBatisPlusQueryWrapper<>(extractorHolder);
    QueryWrapper<Job> queryWrapper = wrapper.build(dto);
    List<Job> data = jobService.list(queryWrapper);
    return ResponseEntity.ok(data);
}
```

Easyquery 可根据配置的 DTO 对象及传入的值自动构建查询 QueryWrapper，免去了手动构建的繁琐代码。

Easyquery 还支持多种筛选运算符（大于、小于、包含、非空等），多字段搜索，组合排序等功能。

Easyquery 核心库基于 JDK 17+，无任何三方依赖，基于用户自己使用的 Mybatis 查询框架（目前支持 MybatisPlus 和 MybatisFlex）。

# 安装

# 使用

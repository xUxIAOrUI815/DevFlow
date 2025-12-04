# 后端接口设计文档

## 总体设计架构

Controller-Service-Mapper 三层架构

- Controller 负责接收前端请求，参数校验，返回统一的JSON结果
- Service 负责业务逻辑
- Mapper(DAO) 负责和数据库交互

## 统一数据交互格式

返回JSON格式
```
{
    "code": 200,          // 200表示成功，500表示服务器错误
    "msg": "操作成功",     // 提示信息
    "data": { ... }       // 具体数据，可能是对象，也可能是数组
}
```

## 核心接口

### 模块A 项目管理

*用于侧边栏展示项目列表，以及切换项目*

1. **获取所有项目列表**
- 请求路径 GET /project/list
- 作用 前端侧边栏展示所有Project
- 参数 无
- 返回数据
```
[
  { "id": 1, "name": "DevFlow研发平台", "code": "DF" },
  { "id": 2, "name": "电商小程序", "code": "SHOP" }
]
```
2. **创建新项目**
- 请求路径 POST /project/add
- 作用 用户点击“新建项目”按钮。
- 参数
```
{
  "name": "新项目",
  "code": "NEW",
  "description": "这是描述",
  "ownerId": 1
}
```
### 模块B 任务管理

*用于看板展示、拖拽状态、新建任务。*

3. **查询某项目下的任务列表**
- 请求路径 GET /issue/list
- 作用 进入项目详情页时，加载所有任务放入看板。
- 参数 projectId=1
- 返回数据
```
[
  { "id": 101, "title": "设计数据库", "status": 0, "priority": 2, "assigneeId": 1 },
  { "id": 102, "title": "编写接口", "status": 1, "priority": 1, "assigneeId": 1 }
]
```
4. **创建新任务**
- 请求路径 POST /issue/add
- 作用 往看板里增加一张卡片。
- 参数
```
{
  "projectId": 1,
  "title": "修复登录Bug",
  "type": 2,          // 1:Feature, 2:Bug
  "priority": 3,      // 0:Low, 3:Urgent
  "assigneeId": 1
}
```
5. **更新任务**
- 请求路径 PUT /issue/update
- 作用 在前端把卡片从“待办”拖到“进行中”时，调用此接口更新数据库。
- 参数 
```
{
  "id": 101,
  "status": 1   // 只要传 id 和需要修改的字段即可
}
```
### 模块C 用户管理

*用于下拉菜单选择任务指派。*
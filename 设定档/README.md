# 料理工艺2设定档

## 技术性信息

### 协议

保留所有权利。为避免法律上的麻烦，暂时不考虑自定义协议。所有贡献者签署协议。

### 目标版本

1.15.2 / 1.16

### 前置

 - Kiwi
 - Patchouli
 - Mixin ~~(可能，考虑到经营玩法的复杂性)~~

## 基础设施

三种基本元素：食材、调料、菜品。这三种元素均可以由用户通过数据包定义。当然，模组预置的数据也通过数据包定义，并且可被其他数据包覆盖或清除。

### 食材（Material）

#### 定义

 - 映射到的 Item 或 Tag\<Item>
 - 解锁获得的 buff

#### 获取

目前通过采集、生产、村民交易、雇员寻访和从其他模组中获取。
然而这并不是最理想的方式，玩家开局一次接触过多食材可能导致难以适应，后面可能改进流程。

#### 研究与星级

通过烹饪食材可以增加其研究进度。研究至一定程度可解锁手册中关于该食材的相关信息（提示与其他食材可能组成的菜品）或直接解锁菜品的配方。研究进度达到100%后，提升一星级，今后烹饪含有该食材的菜品会获得buff。食材的最大星级可能时食材不同而变。

#### 变种

为了解决需要获取食材过多的问题，鼓励玩家提升食材的星级，将近似食材合并在一起，变种食材通过提升星级获取。

### 调料（Spice）

主要为了配合食材组合成菜品，增加难度与丰富性。目前没有过多计划。

### 厨具（Cookware）

### 菜品（CuisineFood）

#### 定义

 - 映射到的 Item
 - （是否应当允许玩家自定义菜品的饥饿度与饱食度？这将不得不在玩家进入游戏时进行修改，并在退出游戏时恢复数据）
 - 菜系（可自行添加删除菜系）
 - 售价

#### 获取

烹饪（主要），与其他玩家交易（次要）。

#### 研究与星级

见**经营玩法**部分。

#### 可放置菜品

计划移植 Custom Player Model，令菜品以方块（实体？多个置于相同方块的菜品可合并为同一实体，缩放）形式放置在世界中。。右击食用，左击取下。

### 配方（CuisineRecipe）

#### 定义

 - 匹配条件列表
 - 厨具及其条件（时间、温度等）
 - 所制成的菜品
 - 匹配优先级

#### 获取

玩家持有的配方以物品形式被玩家交互，可以储存在手册中，可与纸合成来复制。
配方可通过烹饪、抽卡、交易、特殊事件/任务获取。
默认玩家解锁配方时会在聊天栏中全服广播（同达成进度），可通过 GameRule 关闭。

#### 配方物品

菜品通过某种方式可获得其菜谱（并非最优菜谱）（若如此，是否应当给每道菜分配一个ID，并在存档中记录烹饪流程，待物品消失后将数据删除？）
玩家可将菜谱贴在屏幕上，每完成一步自动打勾，方便玩家跟随步骤。

## 烹饪玩法

玩家通过厨具随机组合食材与调料，系统从所有配方中~~选取一个最接近的配方~~进行匹配。
计划优先实现的厨具：炒锅、炸锅、蒸笼、烤箱、高压锅、凉拌盆、罐子（腌制）。

## 经营玩法

玩家雇佣村民或其他模组中的生物（如车万女仆）经营餐饮店，从中赚取货币。
一名玩家可经营多家餐饮店，一家餐饮店售卖的菜品主要为同一菜系。
售卖菜品，并使顾客满意可以增加其研究进度。研究达到100%提升星级，菜品的售价更高。
玩家可将菜品设置为特色菜（有上限），增加其销量。

### 雇员职业

 - 厨师：操作单个厨具
 - 寻访员：外出采集食材，招揽顾客，获取灵感

### 雇员养成

待续

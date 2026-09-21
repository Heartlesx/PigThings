# 维护说明

## 本地依赖

- 根目录 `libs/Pigthings-1.4.5.jar`：历史源文件缺失时保留下来的本模组类和资源。构建会合并该 JAR，并让恢复的源码/新资源优先；移除它会丢失功能。
- 根目录 Curios 5.9.1 API：Forge 1.20.1 编译依赖。
- `neoforge/libs` Curios 9.5.1 API 和运行库：NeoForge 1.21.1 开发依赖。
- `forge-1.12.2/libs` Baubles 1.5.2：Forge 1.12.2 编译依赖，游戏运行也需要 Baubles。

这些 JAR 已是仓库构建输入，区别于 build/libs 与 dist 内的成品。未更改依赖版本或玩法。

## 美术资源

`assets/reference` 保留用户提供并实际采用的四张参考图，`assets/models` 保存草帽和唐刀的 Blockbench 源模型。游戏读取各版本 src/main/resources 中的导出资源，而不是直接读取 bbmodel。

草帽运行时装备网格额外修正了 Y 轴 180° 朝向，并使用头部显示 Y=7.25、缩放 1.1。原始草帽 bbmodel 作为原稿保存，重新导出时须保留这些修正。

唐刀为兼容 Java 版方块模型制作的简化立体模型，含 200 个方块和 256×256 贴图。原稿细纹未逐像素复刻。

## 发布检查

1. 构建对应版本，Forge 成品必须完成 reobfJar。
2. 确认 JAR 包含模型、纹理、语言和物品注册；不要用开发类目录替代成品。
3. 游戏内检查左右手、GUI、装备位置、附魔光效、物品功能及服务器加载。

构建缓存、日志、测试运行目录和历史成品不进入 Git。旧本地工程及缓存未删除，整理后的 Git 工程独立保留。

Forge 1.20.1 的唐刀当前使用本地最新 OBJ/MTL 导出及 carrot_saber_obj.png；另外两版沿用方块模型。assets/models 中的唐刀为早期可编辑方块稿，不覆盖 OBJ 导出。

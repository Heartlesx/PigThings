# PigThings

Minecraft 辅助模组，提供多功能头盔、戒指、挖掘工具和胡萝卜唐刀。模型与贴图随模组内置，无需额外资源包。

## 支持版本

| 工程 | Minecraft / 加载器 | Java | 模组版本 |
| --- | --- | --- | --- |
| 根目录 | Forge 1.20.1 / 47.4.3 | 17 | 1.6.3 |
| `neoforge/` | NeoForge 1.21.1 / 21.1.250 | 21 | 1.6.4-neoforge |
| `forge-1.12.2/` | Forge 1.12.2 / 14.23.5.2860 | 8 | 1.7.4 |

不同版本分别安装对应 JAR，不要同时安装多份 PigThings。戒指使用对应版本的 Curios；Forge 1.12.2 使用 Baubles。

## 物品

- **多功能头盔** `pigthings:nice_helmet`：创造飞行、速度调节、无惯性、夜视、饱和、磁铁等，佩戴后按 G 设置。外观为草帽小猪，已修正朝向和佩戴高度。
- **多功能戒指** `pigthings:nice_ring`：提供主要辅助能力，设置保存在戒指自身；与头盔同时装备时头盔优先。使用紫色小猪戒指图标。
- **障碍破坏者** `pigthings:nice_pickaxe`：高速挖掘、无限耐久、右键单格破坏，按 G 设置精准采集；不破坏基岩等不可破坏方块。
- **矿脉挖掘镐** `pigthings:vein_mining_pickaxe`：Forge 1.20.1 提供的连锁挖掘工具。
- **猪咪锭** `pigthings:pig_ingot`：材料物品，使用新版小猪锭图标。
- **胡萝卜唐刀** `pigthings:carrot_saber`：立体模型，钻石剑属性（7 伤害、1.6 攻速、1561 耐久），支持附魔与钻石修复。位于创造模式战斗栏，暂未添加合成配方。

获取唐刀：`/give @p pigthings:carrot_saber 1`。

## 目录

```text
src/                  Forge 1.20.1 源码与游戏资源
neoforge/             NeoForge 1.21.1 独立工程
forge-1.12.2/          Forge 1.12.2 独立工程
assets/models/        可编辑的 Blockbench 模型
assets/reference/     本轮使用的原始参考图
libs/                 根工程必需的本地依赖
docs/                维护说明与演示素材
build-all.ps1         Windows 三版本构建入口
dist/                 本地构建结果汇总（不提交）
```

## 构建

安装 JDK 8、17、21。在 Windows 仓库根目录运行：

```powershell
powershell -ExecutionPolicy Bypass -File .\build-all.ps1
# 只构建一个版本
powershell -ExecutionPolicy Bypass -File .\build-all.ps1 -Targets 1.20.1
```

脚本自动检测常见安装目录中的 JDK，结果收集到 `dist/`。也可为目标工程设置合适的 `JAVA_HOME`，进入其目录执行 `./gradlew build --no-daemon`。各工程直接构建的成品在各自 `build/libs/`。首次构建需联网下载 Gradle 和 Minecraft 依赖；现代版本用 Gradle 8.8，1.12.2 用 Gradle 4.9。

根工程仍依赖 `libs/Pigthings-1.4.5.jar` 中尚未恢复的旧类和资源，不能删除。详见 [维护说明](docs/maintenance.md)。

## 验证与许可

三个版本已完成构建与资源核验。唐刀经过 Blockbench 握持预览，近期外观更新尚未完成全版本游戏内实测。工程没有自动化测试用例。

自有源码使用 [MIT License](LICENSE.txt)。Minecraft、Forge、NeoForge、Curios、Baubles 等第三方内容遵循各自许可。

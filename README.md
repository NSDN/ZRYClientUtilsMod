# ZRYClientUtilsMod

## zh-CN

ZRY制作的客户端辅助Mod，仅需客户端安装。

目前仅支持forge 1.19.2版本的MC。本Mod许可协议为MIT。

提供以下功能：

* 类似WorldEdit CUI的WorldEdit选区高亮功能
* 一个GUI面板 WE_Panel，默认按V键调出，可设定快捷命令

### 功能介绍

#### WorldEdit选区高亮

可对WorldEdit的选区渲染一个外框，
以便指示WorldEdit选区。
目前为止仅支持长方体/正方体选区。

仅在Forge版本的WorldEdit测试过。

实现原理为hook从服务端发往客户端的`worldedit:cui`报文，
由于不知道什么原因，
只有方形选区情况下报文内才具有正确的选区信息数据，
所以目前只支持方形选区。

#### WE_Panel

该功能的主要制作动机，是使用WorldEdit的时候，
时常会需要重复执行一些命令，例如`//desel`。
该功能提供一个可通过配置文件配置的面板，
面板上可设定一系列按钮来绑定到命令，
点击按钮或按键盘上对应快捷键即可执行。

可通过编辑配置文件设定该面板内的快捷命令。
配置文件位于`.minecraft/config/zry_client_utils.toml`。
可以参考配置文件内的注释进行配置文件编写。

在游戏中，可以通过命令`/reload-zry-client-utils-config`
重新载入配置文件。

默认通过V键调出WE_Panel，该快捷键可在MC的按键设置中修改，
在面板弹出后，按相同的按键可关闭面板。

## en-US

TODO

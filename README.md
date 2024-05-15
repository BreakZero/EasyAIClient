[![Github Release](https://github.com/BreakZero/EasyAIClient/actions/workflows/Release.yml/badge.svg?branch=main)](https://github.com/BreakZero/EasyAIClient/actions/workflows/Release.yml)
## 关于 EasyAIClient

> EasyAI通过接入AI平台Rest API实现AI使用工具，只需你提供正确的API Key即可使用其中的功能，目前仅支持Google的Gemini。<br/>
> 由于没有拿到Chat GPT的API Key，暂时没有验证，无法提供正确的功能测试和使用。

### 技术和架构
EasyAI选用Jetpack Compose实现UI，目前仅支持Android平台，计划稳定后KMM实现。
EasyAI使用Clear架构设计(参考[nowinandroid](https://github.com/android/nowinandroid)), MVI的架构模式开发。
在设计上，使用插件整合的方式实现快速支持AI插件功能实现。

### 目前支持功能

- [x] Gemini聊天功能
- [x] 聊天的保存和删除功能
- [x] 设置管理AI Platform API Key功能
- [x] Gemini Multi Modal分析功能
- [x] 选择默认聊天AI Model
- [ ] 支持ChatGPT（WIP）

### 界面UI展示
界面参考ChatGPT iOS客户端

|                                                                             |                                                                             |                                                                             |
|-----------------------------------------------------------------------------|-----------------------------------------------------------------------------|-----------------------------------------------------------------------------|
| ![Screenshot_20240515_105141.png](screens%2FScreenshot_20240515_105141.png) | ![Screenshot_20240515_105207.png](screens%2FScreenshot_20240515_105207.png) | ![Screenshot_20240515_105628.png](screens%2FScreenshot_20240515_105628.png) |
| ![Screenshot_20240515_105709.png](screens%2FScreenshot_20240515_105709.png) | ![Screenshot_20240515_110301.png](screens%2FScreenshot_20240515_110301.png) | ![Screenshot_20240515_110325.png](screens%2FScreenshot_20240515_110325.png) |
| ![Screenshot_20240515_110350.png](screens%2FScreenshot_20240515_110350.png) | ![Screenshot_20240515_110413.png](screens%2FScreenshot_20240515_110413.png) |                                                                             |
# cordova-plugin-hmpush
> 集成华为推送、小米推送的 Cordova 插件。

  
## Usage & Install

#### 安装说明
* 安装插件时需要填写小米推送对应的 APPKEY 与 APPID，参考 plugin.xml 中 `$MI_PUSH_APPID` 与 `$MI_PUSH_APPKEY`
* 华为需要填写APP_ID，初次使用时需要上传 HUAWEI TOKEN 至服务端
* 具体支持的功能参见: www/NXTPlugin.js

#### 安装命令
```bash
cordova plugin add Your_Plugin_Path --nofetch --variable HW_APPKEY=YOUR_HW_APPKEY --variable MI_PUSH_APPID=YOUR_MI_PUSH_APPID --variable MI_PUSH_APPKEY=
=YOUR_MI_PUSH_APPKEY
```


## Example
例子基于 `ionic` ，当然你也可以不基于 `ionic` 。
可以使用 ```ionic start myApp tabs```安装 ionic 初始模板，
将 `www/` 下的代码替换为 `example/` 里的代码即可。


## FAQ



## Special Thanks



## License
MIT@yipeng.info

var HMPlugin = function () {}

// private plugin function

HMPlugin.prototype.receiveMessage = {}
HMPlugin.prototype.openNotification = {}
HMPlugin.prototype.receiveNotification = {}

HMPlugin.prototype.isPlatformIOS = function () {
  var isPlatformIOS = (device.platform == 'iPhone' ||
    device.platform == 'iPad' ||
    device.platform == 'iPod touch' ||
    device.platform == 'iOS')
  return isPlatformIOS
}

HMPlugin.prototype.errorCallback = function (msg) {
  console.log('Javascript Callback Error: ' + msg)
}

HMPlugin.prototype.callNative = function (name, args, successCallback) {
  if (device.platform === 'Android') {
    // Android执行新逻辑
     cordova.exec(successCallback, this.errorCallback, 'HMPushPlugin', name, args)
  }
}

// Common methods
HMPlugin.prototype.init = function () {
  if (device.platform === 'Android') {
    this.callNative('init', [], null)
  }
}

HMPlugin.prototype.setDebugMode = function (mode) {
  if (device.platform === 'Android') {
    this.callNative('setDebugMode', [mode], null)
  }
}

HMPlugin.prototype.getRegistrationID = function (successCallback) {
  this.callNative('getRegistrationID', [], successCallback)
}

HMPlugin.prototype.stopPush = function () {
  this.callNative('stopPush', [], null)
}

HMPlugin.prototype.resumePush = function () {
  this.callNative('resumePush', [], null)
}

HMPlugin.prototype.isPushStopped = function (successCallback) {
  this.callNative('isPushStopped', [], successCallback)
}

HMPlugin.prototype.clearLocalNotifications = function () {
  if (device.platform === 'Android') {
    this.callNative('clearLocalNotifications', [], null)
  } else {
    this.clearAllLocalNotifications()
  }
}

HMPlugin.prototype.setTagsWithAlias = function (tags, alias) {
  if (tags == null) {
    this.setAlias(alias)
    return
  }
  if (alias == null) {
    this.setTags(tags)
    return
  }
  var arrayTagWithAlias = [tags]
  arrayTagWithAlias.unshift(alias)
  this.callNative('setTagsWithAlias', arrayTagWithAlias, null)
}

HMPlugin.prototype.setTags = function (tags) {
  this.callNative('setTags', tags, null)
}

HMPlugin.prototype.setAlias = function (alias) {
  this.callNative('setAlias', [alias], null)
}

// 判断系统设置中是否对本应用启用通知。
// iOS: 返回值如果大于 0，代表通知开启；0: 通知关闭。
//		UIRemoteNotificationTypeNone    = 0,
//    	UIRemoteNotificationTypeBadge   = 1 << 0,
//    	UIRemoteNotificationTypeSound   = 1 << 1,
//    	UIRemoteNotificationTypeAlert   = 1 << 2,
//    	UIRemoteNotificationTypeNewsstandContentAvailability = 1 << 3,
// Android: 返回值 1 代表通知启用、0: 通知关闭。
HMPlugin.prototype.getUserNotificationSettings = function (successCallback) {
  if (this.isPlatformIOS()) {
    this.callNative('getUserNotificationSettings', [], successCallback)
  } else if (device.platform == 'Android') {
    this.callNative('areNotificationEnabled', [], successCallback)
  }
}

// iOS methods

HMPlugin.prototype.startJPushSDK = function () {
  this.callNative('startJPushSDK', [] , null)
}

HMPlugin.prototype.setBadge = function (value) {
  if (this.isPlatformIOS()) {
    this.callNative('setBadge', [value], null)
  }
}

HMPlugin.prototype.resetBadge = function () {
  if (this.isPlatformIOS()) {
    this.callNative('resetBadge', [], null)
  }
}

HMPlugin.prototype.setDebugModeFromIos = function () {
  if (this.isPlatformIOS()) {
    this.callNative('setDebugModeFromIos', [], null)
  }
}

HMPlugin.prototype.setLogOFF = function () {
  if (this.isPlatformIOS()) {
    this.callNative('setLogOFF', [], null)
  }
}

HMPlugin.prototype.setCrashLogON = function () {
  if (this.isPlatformIOS()) {
    this.callNative('crashLogON', [], null)
  }
}

HMPlugin.prototype.addLocalNotificationForIOS = function (delayTime, content,
  badge, notificationID, extras) {
  if (this.isPlatformIOS()) {
    this.callNative('setLocalNotification', [delayTime, content, badge, notificationID, extras], null)
  }
}

HMPlugin.prototype.deleteLocalNotificationWithIdentifierKeyInIOS = function (identifierKey) {
  if (this.isPlatformIOS()) {
    this.callNative('deleteLocalNotificationWithIdentifierKey', [identifierKey], null)
  }
}

HMPlugin.prototype.clearAllLocalNotifications = function () {
  if (this.isPlatformIOS()) {
    this.callNative('clearAllLocalNotifications', [], null)
  }
}

HMPlugin.prototype.setLocation = function (latitude, longitude) {
  if (this.isPlatformIOS()) {
    this.callNative('setLocation', [latitude, longitude], null)
  }
}

HMPlugin.prototype.startLogPageView = function (pageName) {
  if (this.isPlatformIOS()) {
    this.callNative('startLogPageView', [pageName], null)
  }
}

HMPlugin.prototype.stopLogPageView = function (pageName) {
  if (this.isPlatformIOS()) {
    this.callNative('stopLogPageView', [pageName], null)
  }
}

HMPlugin.prototype.beginLogPageView = function (pageName, duration) {
  if (this.isPlatformIOS()) {
    this.callNative('beginLogPageView', [pageName, duration], null)
  }
}

HMPlugin.prototype.setApplicationIconBadgeNumber = function (badge) {
  if (this.isPlatformIOS()) {
    this.callNative('setApplicationIconBadgeNumber', [badge], null)
  }
}

HMPlugin.prototype.getApplicationIconBadgeNumber = function (callback) {
  if (this.isPlatformIOS()) {
    this.callNative('getApplicationIconBadgeNumber', [], callback)
  }
}

HMPlugin.prototype.addDismissActions = function (actions, categoryId) {
  this.callNative('addDismissActions', [actions, categoryId])
}

HMPlugin.prototype.addNotificationActions = function (actions, categoryId) {
  this.callNative('addNotificationActions', [actions, categoryId])
}

// Android methods
HMPlugin.prototype.setBasicPushNotificationBuilder = function () {
  if (device.platform == 'Android') {
    this.callNative('setBasicPushNotificationBuilder', [], null)
  }
}

HMPlugin.prototype.setCustomPushNotificationBuilder = function () {
  if (device.platform == 'Android') {
    this.callNative('setCustomPushNotificationBuilder', [], null)
  }
}

HMPlugin.prototype.receiveRegistrationIdInAndroidCallback = function (data) {
   if (device.platform === 'Android') {
     data = JSON.stringify(data)
     var event = JSON.parse(data)
     cordova.fireDocumentEvent('jpush.receiveRegistrationId', event);
   }
 }

HMPlugin.prototype.receiveMessageInAndroidCallback = function (data) {
  data = JSON.stringify(data)
  console.log('HMPlugin:receiveMessageInAndroidCallback: ' + data)
  this.receiveMessage = JSON.parse(data)
  cordova.fireDocumentEvent('jpush.receiveMessage', this.receiveMessage)
}

HMPlugin.prototype.openNotificationInAndroidCallback = function (data) {
  data = JSON.stringify(data)
  console.log('HMPlugin:openNotificationInAndroidCallback: ' + data)
  this.openNotification = JSON.parse(data)
  cordova.fireDocumentEvent('jpush.openNotification', this.openNotification)
}

HMPlugin.prototype.receiveNotificationInAndroidCallback = function (data) {
  data = JSON.stringify(data)
  console.log('HMPlugin:receiveNotificationInAndroidCallback: ' + data)
  this.receiveNotification = JSON.parse(data)
  cordova.fireDocumentEvent('jpush.receiveNotification', this.receiveNotification)
}

HMPlugin.prototype.clearAllNotification = function () {
  if (device.platform === 'Android') {
    this.callNative('clearAllNotification', [], null)
  }
}

HMPlugin.prototype.clearNotificationById = function (id) {
  if (device.platform === 'Android') {
    this.callNative('clearNotificationById', [id], null)
  }
}

HMPlugin.prototype.setLatestNotificationNum = function (num) {
  if (device.platform == 'Android') {
    this.callNative('setLatestNotificationNum', [num], null)
  }
}

HMPlugin.prototype.addLocalNotification = function (builderId, content, title,
  notificationID, broadcastTime, extras) {
  if (device.platform == 'Android') {
    this.callNative('addLocalNotification',
      [builderId, content, title, notificationID, broadcastTime, extras], null)
  }
}

HMPlugin.prototype.removeLocalNotification = function (notificationID) {
  if (device.platform === 'Android') {
    this.callNative('removeLocalNotification', [notificationID], null)
  }
}

HMPlugin.prototype.reportNotificationOpened = function (msgID) {
  if (device.platform === 'Android') {
    this.callNative('reportNotificationOpened', [msgID], null)
  }
}

/**
 *是否开启统计分析功能，用于“用户使用时长”，“活跃用户”，“用户打开次数”的统计，并上报到服务器上，
 *在 Portal 上展示给开发者。
 */
HMPlugin.prototype.setStatisticsOpen = function (mode) {
  if (device.platform == 'Android') {
    this.callNative('setStatisticsOpen', [mode], null)
  }
}

/**
 * 用于在 Android 6.0 及以上系统，申请一些权限
 * 具体可看：http://docs.jpush.io/client/android_api/#android-60
 */
HMPlugin.prototype.requestPermission = function () {
  if (device.platform == 'Android') {
    this.callNative('requestPermission', [], null)
  }
}

HMPlugin.prototype.setSilenceTime = function (startHour, startMinute, endHour, endMinute) {
  if (device.platform == 'Android') {
    this.callNative('setSilenceTime', [startHour, startMinute, endHour, endMinute], null)
  }
}

HMPlugin.prototype.setPushTime = function (weekdays, startHour, endHour) {
  if (device.platform == 'Android') {
    this.callNative('setPushTime', [weekdays, startHour, endHour], null)
  }
}


// 收到华为token
HMPlugin.prototype.onReceiveHuaWeiToken = function (token) {
  if (device.platform == 'Android') {
      token = JSON.stringify(token)
      console.log('HMPlugin:onReceiveHuaWeiTokenCallBack: ' + token)
      cordova.fireDocumentEvent('jpush.onReceiveHuaWeiToken', token)
  }
}

if (!window.plugins) {
  window.plugins = {}
}

if (!window.plugins.HMPlugin) {
  window.plugins.HMPlugin = new HMPlugin()
}

// module.exports = new HMPlugin()

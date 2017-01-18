# MaterialSetupWizard
Beautiful, highly Google-like setting up experience, for your Android device only.
Support API 11+.
[![](https://jitpack.io/v/TaRGroup/MaterialSetupWizard.svg)](https://jitpack.io/#TaRGroup/MaterialSetupWizard)

喜欢的话别忘了 **Star** 和 **Watch**，有问题随意**Issues**~
![Screenshot1](https://raw.githubusercontent.com/TaRGroup/MaterialSetupWizard/master/image/screenshot-1-device-2017-01-18-131830.png)![Screenshot2](https://raw.githubusercontent.com/TaRGroup/MaterialSetupWizard/master/image/screenshot-2-photo6339210114027857836.jpg)

# 用法
## 添加依赖
1. 在项目级build.gradle找到相应位置添加（若已有就跳过）：
```grooxy
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
2. 添加依赖：
```grooxy
compile 'com.github.TaRGroup:MaterialSetupWizard:alpha1'
```
## 创建WizardActivity
你需要创建一个新的类，将其继承 `WizardActivity`:
[例子:Act.java](https://github.com/TaRGroup/MaterialSetupWizard/blob/master/app/src/main/java/kh/android/materialsetupwizard/Act.java)
```java
public class Act extends WizardActivity {}
```
随后重写 `onCreate(Bundle)` 方法:
```Java
public class Act extends WizardActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
```
**记住！必须执行`super.onCreate` 且不能执行 `setContentView`!
因为执行`setContentView`会覆盖已经配置好的向导视图！

这时候运行这个Activity，就可以看到一个空白的设置向导页面了。

![Screenshot-EmptyWizard](https://raw.githubusercontent.com/TaRGroup/MaterialSetupWizard/master/image/screenshot-1-device-2017-01-18-131830.png)

## 创建页面
页面是以Fragment形式放置的。现在创建一个类，继承 `PageFragment` 并
**实现抽象方法：`onCreateView (LayoutInflater, ViewGroup, Bundle)` 返回视图和
`getTitle(Context)` 返回标题。**
[例子:Page2.java](https://github.com/TaRGroup/MaterialSetupWizard/blob/master/app/src/main/java/kh/android/materialsetupwizard/Page2.java)

```Java
public class Page2 extends PageFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        return null;
    }

    @Override
    public String getTitle (Context context) {
        return "Page2";
    }
}
```

_（我们也提供了一个便捷的[ProgressFragment](https://github.com/TaRGroup/MaterialSetupWizard/blob/master/materialwizard/src/main/java/kh/android/materialwizard/ProgressFragment.java)，可以显示一个进度条。同时建议将进度条作为临时页面显示。有关 **临时页面** 的介绍将在后面解释）_
此时，页面已经准备好了，但仍无法显示。因此我们需要将其添加到WizardActivity。

## 添加页面
添加页面很简单，只需要在WizardActivity的`onCreate(Bundle)`方法中调用`addPage(PageFragment)`即可：
[例子:Act.java](https://github.com/TaRGroup/MaterialSetupWizard/blob/master/app/src/main/java/kh/android/materialsetupwizard/Act.java)

```Java
public class Act extends WizardActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPage(new Page2());
    }
}
```

这时，运行应用，Page2已被添加了。同时，底部按钮也随之变化。由于Page2即是第一个页面，也是最后一个，所以**返回**按钮自动隐藏，**下一步**按钮改为**完成**。

_由于Page2返回了空的视图，所以不会显示任何部件。但是您可以从标题看到已经变成了Page2所设定的标题_

![Screenshot-Page2](https://raw.githubusercontent.com/TaRGroup/MaterialSetupWizard/master/image/page-2-device-2017-01-18-133204.png)

## 临时页面
通常在设置向导都会有一些耗时操作，比如注册账户。需要向用户提供一个进度条界面。
但若先添加这类页面，显示，随后移除，会显得很笨拙。
**临时页面**就很好的解决了这一问题。它拥有以下特性：

* 一个WizardActivity一次仅能显示一个临时页面。在显示过程中再次添加会导致先前临时页面消失
* 在添加临时页面后，用户的 **返回键** 将被禁用（关于禁用，请参考下面章节）；同时，**前进**和**后退**按钮也会被隐藏（关于控制按钮，请参考后续**API**部分）。
* 临时页面不同于传统页面，不会出现在**页面队列** 中，所以也不受**前进**和**后退**操作影响。

若要添加临时页面，只需要调用`turnTempPage(PageFragment)`即可。
若要隐藏临时页面，则使用`dismissTempPage`方法。
[例子:Act.java](https://github.com/TaRGroup/MaterialSetupWizard/blob/master/app/src/main/java/kh/android/materialsetupwizard/Act.java)

这里以 **[ProgressFragment](https://github.com/TaRGroup/MaterialSetupWizard/blob/master/materialwizard/src/main/java/kh/android/materialwizard/ProgressFragment.java)**
作为临时页面，这是一个带有横向进度条的`PageFragment`，您可以直接使用它。

```Java
public class Act extends WizardActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        turnTempPage(new ProgressFragment());
    }
}
```
运行程序，只有进度条的临时页面被显示了。同时用户无法回退，前进。
![Screenshot-Temp-Page](https://raw.githubusercontent.com/TaRGroup/MaterialSetupWizard/master/image/tenp-pagedevice-2017-01-18-135052.png)

## 禁用返回键规则
在符合以下逻辑时，WizardActivity会阻止用户点击返回键。您也可以重写`onBackPressed` 来执行您的操作。
* 当有临时页面在显示时
* 当返回按钮被隐藏或禁用时
随后，若当前页面不是第一页，WizardActivity会执行**上一页**操作，直到到达第一页后，再次点击返回键来退出程序。

**至此，已完成了基本操作。后续将介绍大多数实用API。**

# API
### `WizardActivity`

添加页面 `addPage(PageFragment)`

按索引移除页面 `removePage (int pos)`

按PageFragment移除页面 `removePage (PageFragment fragment)`

跳转页面 `turnPage (int pos)`

设置标题栏折叠 `setAppBarExpanded (boolean expanded)`

显示临时页面 `turnTempPage (PageFragment fragment)`

隐藏临时页面 `dismissTempPage`

获取当前页面索引 `getCurrentPage ()`

获取总页面数 `getPagesCount ()`

更新标题 `updateTitle ()`

_以下方法均有**getter**方法_

启用/禁用返回按钮 `setForwardEnable (boolean enable)`

设置返回按钮显示状态 `setForwardVisibility (int visibility)`

启用/禁用前进按钮 `setNextEnable (boolean enable)`

设置前进按钮显示状态 `setNextVisibility (int visibility)`

设置是否在页面变化后（如前进）自动更新按钮状态（默认true）`setAutoUpdateButton(boolean mAutoUpdateButton)`

设置后退按钮文本 `setForwardText(CharSequence text)`

设置前进按钮文本 `setNextText(CharSequence text)`

设置按钮栏显示状态 `setButtonBarVisibility (int visibility)`

设置是否允许用户通过左右滑动控制页面（默认false） `setAppBarExpanded (boolean expanded)`

### `PageFragment`

返回标题 `getTitle (Context context)`
**注意！若需要从资源读取标题，会触发异常。由于读取标题时Fragment未添加到Activity。请使用getTitle的Context参数获取资源。并且更新标题和顶部背景图片后需要调用`updateTitle()`**

返回视图 `onCreateView (LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)`

获取WizardActivity `getWizardActivity ()`

设置顶部图片 `setTitleBackgroundImage (Drawable drawable)`


喜欢的话别忘了 **Star** 和 **Watch**，有问题随意**Issues**~
## License

    Copyright 2016 TaRGroup

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
